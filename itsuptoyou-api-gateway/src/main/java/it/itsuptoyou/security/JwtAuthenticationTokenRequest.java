package it.itsuptoyou.security;

import java.io.Serializable;

public class JwtAuthenticationTokenRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String password;

    public JwtAuthenticationTokenRequest() {
        super();
    }

    public JwtAuthenticationTokenRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}