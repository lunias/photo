package com.ethanaa.photo.config;

import com.ethanaa.photo.security.AuthProperties;
import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class KeyConfig {

    private static final String ALG = "RSA";

    @Autowired private ResourceLoader resourceLoader;

    @Autowired private AuthProperties properties;

    @Bean
    public PublicKey publicKey() throws Exception {

        Resource resource = resourceLoader.getResource("classpath:" + properties.getServer().getPublicKeyFile());
        InputStream keyAsStream = resource.getInputStream();

        byte[] publicKeyBytes = ByteStreams.toByteArray(keyAsStream);

        X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(publicKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(ALG);

        return keyFactory.generatePublic(x509Spec);
    }

    @Bean
    public PrivateKey privateKey() throws Exception {

        Resource resource = resourceLoader.getResource("classpath:" + properties.getServer().getPrivateKeyFile());
        InputStream keyAsStream = resource.getInputStream();

        byte[] privateKeyBytes = ByteStreams.toByteArray(keyAsStream);

        PKCS8EncodedKeySpec pkcs8Spec = new PKCS8EncodedKeySpec(privateKeyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(ALG);

        return keyFactory.generatePrivate(pkcs8Spec);
    }
}
