package com.fahadali.intranet.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
public class Credentials {

    private String grantType;
    private String username;
    private String password;

    public Credentials() {
        this.grantType = "password";
    }

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
