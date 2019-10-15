package no.nav.tag.tiltaksgjennomforingprosess.token;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader.Builder;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JwtTokenGenerator {

    public static final long EXPIRY_MILLISEC = 1000L;
    public static final String SUBJECT = "mySubject";
    public static final String ISSUER = "myIssuer";
    public static final String AUDIENCE = "myAudience";

    private JwtTokenGenerator() {
    }

    public static String signedJWTAsString() {
        return signedJWTAsString(EXPIRY_MILLISEC);
    }

    public static String signedJWTAsString(long expiry) {
        JWTClaimsSet claimsSet = buildClaimSet(TimeUnit.MILLISECONDS.toMillis(expiry));
        return createSignedJWT(JwkGenerator.getDefaultRSAKey(), claimsSet).serialize();
    }

    public static JWTClaimsSet buildClaimSet(long expiry) {
        Map<String, Object> additionalClaims = new HashMap<>();
        Date now = new Date();
        JWTClaimsSet.Builder claimSetBuilder = new JWTClaimsSet.Builder()
                .subject(SUBJECT)
                .issuer(ISSUER)
                .audience(AUDIENCE)
                .jwtID(UUID.randomUUID().toString())
                .claim("ver", "1.0")
                .claim("nonce", "myNonce")
                .claim("auth_time", now)
                .notBeforeTime(now)
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + expiry));
        additionalClaims.keySet().forEach(key -> claimSetBuilder.claim(key, additionalClaims.get(key)));
        return claimSetBuilder.build();
    }

    protected static SignedJWT createSignedJWT(RSAKey rsaJwk, JWTClaimsSet claimsSet) {
        try {
            Builder header = new Builder(JWSAlgorithm.RS256)
                    .keyID(rsaJwk.getKeyID())
                    .type(JOSEObjectType.JWT);

            SignedJWT signedJWT = new SignedJWT(header.build(), claimsSet);
            JWSSigner signer = new RSASSASigner(rsaJwk.toPrivateKey());
            signedJWT.sign(signer);

            return signedJWT;
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }
}
