package com.rocku.auth.domain;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final RSAKey signingKey;
    private final String issuer;
    private final String audience;
    private final Duration accessTokenTtl;

    public TokenService(
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.audience}") String audience,
            @Value("${app.jwt.key-id:}") String keyId,
            @Value("${app.jwt.private-key-path:}") String privateKeyPath,
            @Value("${app.jwt.public-key-path:}") String publicKeyPath,
            @Value("${app.jwt.access-token-ttl:PT15M}") Duration accessTokenTtl,
            @Value("${app.jwt.allow-ephemeral-key:false}") boolean allowEphemeralKey) throws Exception {
        this.issuer = issuer;
        this.audience = audience;
        this.accessTokenTtl = accessTokenTtl;
        var kid = keyId.isBlank() ? UUID.randomUUID().toString() : keyId;
        if (privateKeyPath.isBlank() && publicKeyPath.isBlank() && allowEphemeralKey) {
            signingKey = new RSAKeyGenerator(2048).keyID(kid).generate();
        } else if (!privateKeyPath.isBlank() && !publicKeyPath.isBlank() && !keyId.isBlank()) {
            signingKey = loadKey(privateKeyPath, publicKeyPath, keyId);
        } else {
            throw new IllegalStateException("Configure JWT key paths and kid, or enable an ephemeral dev key");
        }
    }

    public TokenResponse issue(User user) throws JOSEException {
        var now = Instant.now();
        var claims = new JWTClaimsSet.Builder()
                .issuer(issuer)
                .subject(user.getUsername())
                .audience(audience)
                .issueTime(Date.from(now))
                .expirationTime(Date.from(now.plus(accessTokenTtl)))
                .jwtID(UUID.randomUUID().toString())
                .claim("uid", user.getId().toString())
                .claim("roles", List.of(user.getRole()))
                .claim("scope", "notes:read notes:write contact:write")
                .build();
        var jwt = new SignedJWT(new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(JOSEObjectType.JWT).keyID(signingKey.getKeyID()).build(), claims);
        jwt.sign(new RSASSASigner(signingKey.toPrivateKey()));
        return new TokenResponse(jwt.serialize(), "Bearer", accessTokenTtl.toSeconds(),
                user.getId(), user.getUsername(), List.of(user.getRole()));
    }

    public JWKSet jwkSet() {
        return new JWKSet(signingKey.toPublicJWK());
    }

    private static RSAKey loadKey(String privatePath, String publicPath, String keyId) throws Exception {
        var keyFactory = KeyFactory.getInstance("RSA");
        var privateBytes = decodePem(Files.readString(Path.of(privatePath)), "PRIVATE KEY");
        var publicBytes = decodePem(Files.readString(Path.of(publicPath)), "PUBLIC KEY");
        var privateKey = (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
        var publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));
        return new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(keyId).build();
    }

    private static byte[] decodePem(String pem, String label) {
        var content = pem.replace("-----BEGIN " + label + "-----", "")
                .replace("-----END " + label + "-----", "");
        return Base64.getMimeDecoder().decode(content);
    }

    public record TokenResponse(String accessToken, String tokenType, long expiresIn,
            UUID userId, String username, List<String> roles) {}
}