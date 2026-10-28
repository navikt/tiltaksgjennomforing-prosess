package no.nav.tag.tiltaksgjennomforingprosess.configuration;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.TilskuddsPeriode;
import no.nav.tag.tiltaksgjennomforingprosess.integrasjon.PdfgenAdapter;
import no.nav.tag.tiltaksgjennomforingprosess.properties.PdfgenProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.restclient.test.autoconfigure.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Sikrer at JSON-formatet på HTTP-grensa er uendret etter Jackson 3-migreringen.
 * Jackson 3 skriver datoer annerledes som standard, og pdfgen og avtale-API-et
 * forventer "yyyy-MM-dd".
 */
@RestClientTest(components = {PdfgenAdapter.class, PdfgenProperties.class})
@Import(RestTemplatePlain.class)
@TestPropertySource(properties = "prosess.integrasjon.pdfgen.uri=http://pdfgen/api/v1/genpdf")
class RestTemplateJsonFormatTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PdfgenAdapter pdfgenAdapter;

    @Autowired
    private JsonMapper jsonMapper;

    private MockRestServiceServer server;

    @BeforeEach
    void setUp() {
        server = MockRestServiceServer.bindTo(restTemplate).build();
    }

    @Test
    void pdfgen_mottar_datoer_som_iso_dato_strenger() {
        Avtale avtale = TestData.opprettLonnstilskuddsAvtale();
        avtale.setOpprettet(LocalDate.of(2020, 2, 3));
        avtale.setGodkjentAvDeltaker(LocalDate.of(2020, 2, 4));
        avtale.setGodkjentAvArbeidsgiver(LocalDate.of(2020, 2, 5));
        avtale.setGodkjentAvVeileder(LocalDate.of(2020, 2, 6));
        avtale.setGodkjentTaushetserklæringAvMentor(null);
        TilskuddsPeriode periode = new TilskuddsPeriode();
        periode.setStartDato(LocalDate.of(2020, 3, 1));
        periode.setSluttDato(LocalDate.of(2020, 3, 31));
        periode.setBeløp(1000);
        avtale.setTilskuddsPerioder(List.of(periode));

        server.expect(requestTo("http://pdfgen/api/v1/genpdf/tiltak-avtale/midlertidig-lonnstilskudd"))
                .andExpect(method(HttpMethod.POST))
                .andExpect(jsonPath("$.opprettet").value("2020-02-03"))
                .andExpect(jsonPath("$.startDato").value("2019-09-16"))
                .andExpect(jsonPath("$.sluttDato").value("2019-11-16"))
                .andExpect(jsonPath("$.godkjentAvDeltaker").value("2020-02-04"))
                .andExpect(jsonPath("$.godkjentAvArbeidsgiver").value("2020-02-05"))
                .andExpect(jsonPath("$.godkjentAvVeileder").value("2020-02-06"))
                .andExpect(jsonPath("$.godkjentTaushetserklæringAvMentor").doesNotExist())
                .andExpect(jsonPath("$.tilskuddsPerioder[0].startDato").value("2020-03-01"))
                .andExpect(jsonPath("$.tilskuddsPerioder[0].sluttDato").value("2020-03-31"))
                .andExpect(jsonPath("$.tilskuddsPerioder[0].beløp").value(1000))
                .andExpect(jsonPath("$.tiltakstype").value("MIDLERTIDIG_LONNSTILSKUDD"))
                .andRespond(withSuccess(new byte[]{1}, MediaType.APPLICATION_PDF));

        pdfgenAdapter.genererPdf(avtale);

        server.verify();
    }

    @Test
    void leser_avtaler_med_iso_datoer_og_ukjente_felter() throws Exception {
        JsonNode mapping = jsonMapper.readTree(new ClassPathResource("mappings/hentAvtalerTilJournalfoering.json").getInputStream());
        String body = mapping.get("mappings").get(0).get("response").get("jsonBody").toString();

        server.expect(requestTo("http://avtale-api/avtaler"))
                .andRespond(withSuccess(body, MediaType.APPLICATION_JSON));

        List<Avtale> avtaler = restTemplate.exchange("http://avtale-api/avtaler", HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Avtale>>() {}).getBody();

        assertThat(avtaler).isNotEmpty();
        Avtale mentor = avtaler.getFirst();
        assertThat(mentor.getOpprettet()).isEqualTo(LocalDate.of(2020, 2, 11));
        assertThat(mentor.getStartDato()).isEqualTo(LocalDate.of(2020, 2, 11));
        assertThat(mentor.getSluttDato()).isEqualTo(LocalDate.of(2020, 2, 29));
        assertThat(mentor.getGodkjentAvVeileder()).isEqualTo(LocalDate.of(2020, 2, 11));
        assertThat(avtaler).anySatisfy(avtale ->
                assertThat(avtale.getTilskuddsPerioder()).anySatisfy(periode ->
                        assertThat(periode.getStartDato()).isEqualTo(LocalDate.of(2022, 5, 14))));
    }
}
