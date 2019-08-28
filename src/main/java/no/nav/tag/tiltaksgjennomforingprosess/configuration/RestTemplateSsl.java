package no.nav.tag.tiltaksgjennomforingprosess.configuration;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

/**
 * For testing mot sts/joark i preprod
 */

@Configuration
@Profile("debug")
public class RestTemplateSsl {

    @Value("${server.ssl.trust-store}")
    private Resource truststore;

    @Value("${server.ssl.trust-store-password}")
    private String truststorePwd;

    @Bean(name = "restTemplate")
    public RestTemplate restTemplate() {
        SSLContext sslContext = null;

        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(truststore.getURL(), truststorePwd.toCharArray()).build();
        } catch (Exception e) {
            throw new RuntimeException( "Feil ved initiering av ssl context: ", e);
        }

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }
}
