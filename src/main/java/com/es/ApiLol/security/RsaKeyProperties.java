package com.es.ApiLol.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rsa")
public record RsaKeyProperties(String publicKey, String privateKey) {

}
