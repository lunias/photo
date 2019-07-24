package com.ethanaa.photo.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "photo.auth")
public class AuthProperties {

    private Server server = new Server();
    private Resource resource = new Resource();

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public static class Server {

        private String url = "http://localhost:8080";
        private String tokenEndpoint = "/api/auth/login";
        private String refreshTokenEndpoint = "/api/auth/token";
        private String privateKeyFile = "private_key.der";
        private String publicKeyFile = "public_key.der";
        private Integer tokenValidMinutes = 5;
        private Integer refreshTokenValidMinutes = 60;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTokenEndpoint() {
            return tokenEndpoint;
        }

        public void setTokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
        }

        public String getRefreshTokenEndpoint() {
            return refreshTokenEndpoint;
        }

        public void setRefreshTokenEndpoint(String refreshTokenEndpoint) {
            this.refreshTokenEndpoint = refreshTokenEndpoint;
        }

        public String getPrivateKeyFile() {
            return privateKeyFile;
        }

        public void setPrivateKeyFile(String privateKeyFile) {
            this.privateKeyFile = privateKeyFile;
        }

        public String getPublicKeyFile() {
            return publicKeyFile;
        }

        public void setPublicKeyFile(String publicKeyFile) {
            this.publicKeyFile = publicKeyFile;
        }

        public Integer getTokenValidMinutes() {
            return tokenValidMinutes;
        }

        public void setTokenValidMinutes(Integer tokenValidMinutes) {
            this.tokenValidMinutes = tokenValidMinutes;
        }

        public Integer getRefreshTokenValidMinutes() {
            return refreshTokenValidMinutes;
        }

        public void setRefreshTokenValidMinutes(Integer refreshTokenValidMinutes) {
            this.refreshTokenValidMinutes = refreshTokenValidMinutes;
        }
    }

    public static class Resource {

        private String applicationName = "resource-server";
        private String tokenEndpoint = "/api/auth/login";
        private String refreshTokenEndpoint = "/api/auth/token";
        private String userContextEndpoint = "/api/auth/whoami";
        private String publicKeyFile = "public_key.der";
        private Boolean logInvalidTokens = false;

        public String getApplicationName() {
            return applicationName;
        }

        public void setApplicationName(String applicationName) {
            this.applicationName = applicationName;
        }

        public String getTokenEndpoint() {
            return tokenEndpoint;
        }

        public void setTokenEndpoint(String tokenEndpoint) {
            this.tokenEndpoint = tokenEndpoint;
        }

        public String getRefreshTokenEndpoint() {
            return refreshTokenEndpoint;
        }

        public void setRefreshTokenEndpoint(String refreshTokenEndpoint) {
            this.refreshTokenEndpoint = refreshTokenEndpoint;
        }

        public String getUserContextEndpoint() {
            return userContextEndpoint;
        }

        public void setUserContextEndpoint(String userContextEndpoint) {
            this.userContextEndpoint = userContextEndpoint;
        }

        public String getPublicKeyFile() {
            return publicKeyFile;
        }

        public void setPublicKeyFile(String publicKeyFile) {
            this.publicKeyFile = publicKeyFile;
        }

        public Boolean getLogInvalidTokens() {
            return logInvalidTokens;
        }

        public void setLogInvalidTokens(Boolean logInvalidTokens) {
            this.logInvalidTokens = logInvalidTokens;
        }
    }
}
