package no.nav.tag.tiltaksgjennomforingprosess.integrasjon;

import no.nav.tag.tiltaksgjennomforingprosess.TestData;
import no.nav.tag.tiltaksgjennomforingprosess.domene.avtale.Avtale;
import no.nav.tag.tiltaksgjennomforingprosess.persondata.PersondataService;
import no.nav.tag.tiltaksgjennomforingprosess.properties.TiltakApiProperties;
import no.nav.team_tiltak.felles.persondata.pdl.domene.Diskresjonskode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;

import static no.nav.tag.tiltaksgjennomforingprosess.integrasjon.TiltaksgjennomfoeringApiService.PATH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TiltaksgjennomfoeringApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StsService stsService;

    @Mock
    private PersondataService persondataService;

    private final URI uri = URI.create("http://localhost:8090");
    private final URI expUri = UriComponentsBuilder.fromUri(uri).path(PATH).build().toUri();

    @InjectMocks
    private TiltaksgjennomfoeringApiService tiltaksgjennomfoeringApiService = new TiltaksgjennomfoeringApiService(new TiltakApiProperties(uri));


    @Test
    public void kall_mot_finn_avtaler_ok_skal_returnere_avtaler() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        List<Avtale> avtaleListe = Arrays.asList(avtale);
        when(restTemplate.exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.of(Optional.of(avtaleListe)));
        when(persondataService.hentDiskresjonskoder(anySet())).thenReturn(Map.of(
            avtale.getDeltakerFnr(), Optional.of(Diskresjonskode.UGRADERT)
        ));
        assertThat(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).isEqualTo(avtaleListe);
    }

    @Test
    public void kall_mot_finn_avtaler_feiler_skal_hente_nytt_sts_token_og_forsøke_på_nytt() {
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        List<Avtale> avtaleListe = Arrays.asList(avtale);
        when(persondataService.hentDiskresjonskoder(anySet())).thenReturn(Map.of(
            avtale.getDeltakerFnr(), Optional.of(Diskresjonskode.UGRADERT)
        ));
        when(restTemplate.exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenThrow(RuntimeException.class)
                .thenReturn(ResponseEntity.of(Optional.of(avtaleListe)));
        assertThat(tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering()).isEqualTo(avtaleListe);
        verify(stsService).evict();
        verify(stsService, times(2)).hentToken();
        verify(restTemplate, times(2)).exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    public void kall_mot_finn_avtaler_feiler_skal_gi_exception() {
        assertThrows(RuntimeException.class, () -> {
            when(restTemplate.exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenThrow(RuntimeException.class);
            tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering();
        });
    }

    @Test
    public void kall_mot_finn_avtaler_ok_skal_returnere_avtaler_OG_sladde_KUN_kode6og7_avtaler_med_unike_deltaker_fnr() {
        // GITT
        Avtale avtale = TestData.opprettArbeidstreningAvtale();
        Avtale avtaleKode6 = TestData.opprettArbeidstreningAvtale();
        avtaleKode6.setDeltakerFnr("01018099999");
        Avtale avtaleKode7 = TestData.opprettArbeidstreningAvtale();
        avtaleKode7.setDeltakerFnr("00018099999");

        Set<String> deltakerFnr = Set.of(avtale.getDeltakerFnr(), avtaleKode6.getDeltakerFnr(), avtaleKode7.getDeltakerFnr());
        when(restTemplate.exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.of(Optional.of(Arrays.asList(avtale, avtaleKode6, avtaleKode7))));
        when(persondataService.hentDiskresjonskoder(eq(deltakerFnr))).thenReturn(Map.of(
                avtale.getDeltakerFnr(), Optional.of(Diskresjonskode.UGRADERT),
                avtaleKode6.getDeltakerFnr(), Optional.of(Diskresjonskode.STRENGT_FORTROLIG),
                avtaleKode7.getDeltakerFnr(), Optional.of(Diskresjonskode.STRENGT_FORTROLIG_UTLAND)
        ));

        // NÅR
        List<Avtale> avtalerTilbake = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering();
        List<String> sladdedEllerIkke_FNR = avtalerTilbake.stream().map(Avtale::getDeltakerFnr).toList();
        List<String> sladdedEllerIkke_TLF = avtalerTilbake.stream().map(Avtale::getDeltakerTlf).toList();
        List<String> sladdedEllerIkke_FORNAVN = avtalerTilbake.stream().map(Avtale::getDeltakerFornavn).toList();
        List<String> sladdedEllerIkke_ETTERNAVN = avtalerTilbake.stream().map(Avtale::getDeltakerEtternavn).toList();

        // SÅ
        assertThat(sladdedEllerIkke_FNR).isEqualTo(Arrays.asList(avtale.getDeltakerFnr(), "***********", "***********"));
        assertThat(sladdedEllerIkke_TLF).isEqualTo(Arrays.asList(avtale.getDeltakerTlf(), "***********", "***********"));
        assertThat(sladdedEllerIkke_FORNAVN).isEqualTo(Arrays.asList(avtale.getDeltakerFornavn(), "***********", "***********"));
        assertThat(sladdedEllerIkke_ETTERNAVN).isEqualTo(Arrays.asList(avtale.getDeltakerEtternavn(), "***********", "***********"));
    }

    @Test
    public void kall_mot_finn_avtaler_ok_skal_returnere_avtaler_OG_sladde_KUN_kode6og7_avtaler_med_SAMME_deltaker_fnr() {
        // GITT
        Avtale avtaleKode67_1_for_samme_deltaker = TestData.opprettArbeidstreningAvtale();
        Avtale avtaleKode67_2_for_samme_deltaker = TestData.opprettLonnstilskuddsAvtale();
        Avtale avtaleKode67_3_for_samme_deltaker = TestData.opprettSommerjobbAvtale();

        Set<String> deltakerFnr = Set.of(TestData.opprettEnAvtale().getDeltakerFnr());
        when(restTemplate.exchange(eq(expUri), eq(HttpMethod.GET), any(HttpEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.of(Optional.of(Arrays.asList(avtaleKode67_1_for_samme_deltaker,
                        avtaleKode67_2_for_samme_deltaker,
                        avtaleKode67_3_for_samme_deltaker))));

        when(persondataService.hentDiskresjonskoder(eq(deltakerFnr))).thenReturn(Map.of(
                TestData.opprettEnAvtale().getDeltakerFnr(), Optional.of(Diskresjonskode.STRENGT_FORTROLIG)
        ));

        // NÅR
        List<Avtale> avtalerTilbake = tiltaksgjennomfoeringApiService.finnAvtalerTilJournalfoering();
        List<String> kode67Deltakere_FNR_Sladdet = avtalerTilbake.stream().map(Avtale::getDeltakerFnr).toList();
        List<String> kode67Deltakere_FORNAVN_Sladdet = avtalerTilbake.stream().map(Avtale::getDeltakerFornavn).toList();
        List<String> kode67Deltakere_ETTERNAVN_Sladdet = avtalerTilbake.stream().map(Avtale::getDeltakerEtternavn).toList();
        List<String> kode67Deltakere_TLF_Sladdet = avtalerTilbake.stream().map(Avtale::getDeltakerTlf).toList();

        // SÅ
        assertThat(kode67Deltakere_FNR_Sladdet).isEqualTo(Arrays.asList("***********", "***********", "***********"));
        assertThat(kode67Deltakere_FORNAVN_Sladdet).isEqualTo(Arrays.asList("***********", "***********", "***********"));
        assertThat(kode67Deltakere_ETTERNAVN_Sladdet).isEqualTo(Arrays.asList("***********", "***********", "***********"));
        assertThat(kode67Deltakere_TLF_Sladdet).isEqualTo(Arrays.asList("***********", "***********", "***********"));
    }
}
