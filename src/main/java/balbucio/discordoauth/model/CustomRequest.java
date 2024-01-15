package balbucio.discordoauth.model;

import balbucio.discordoauth.scope.SupportedScopes;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Setter(AccessLevel.NONE)
public class CustomRequest {

    private String redirectUri;
    private String state;
    private List<String> scope = new ArrayList<>();

    public CustomRequest(String redirectUri, String state, List<String> scope) {
        this.redirectUri = redirectUri;
        this.state = state;
        this.scope = scope;
    }

    public CustomRequest(String redirectUri, String state, String[] scope) {
        this.redirectUri = redirectUri;
        this.state = state;
        this.scope = Arrays.asList(scope);
    }

    public CustomRequest(String requestUri, String state, SupportedScopes[] scopes){
        this.redirectUri = requestUri;
        this.state = state;
        Arrays.stream(scopes).forEach(s -> scope.add(s.getText()));
    }
}

