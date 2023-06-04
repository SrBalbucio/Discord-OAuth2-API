package balbucio.discordoauth;

import balbucio.discordoauth.model.CustomRequest;
import balbucio.discordoauth.scope.SupportedScopes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import balbucio.discordoauth.model.TokensResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static balbucio.discordoauth.DiscordAPI.BASE_URI;

@Slf4j
@RequiredArgsConstructor
public class DiscordOAuth
{
    private static final Gson gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
    private static final String GRANT_TYPE_AUTHORIZATION = "authorization_code";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private final String clientID;
    private final String clientSecret;
    private String redirectUri;
    private List<String> scope = new ArrayList<>();

    public DiscordOAuth(String clientID, String clientSecret, String redirectUri, String[] scope){
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = Arrays.asList(scope);
    }

    public DiscordOAuth(String clientID, String clientSecret, String redirectUri, SupportedScopes[] scopes){
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        Arrays.stream(scopes).forEach(s -> scope.add(s.getText()));
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public void setScope(List<String> scope) {
        this.scope = scope;
    }

    public void addScope(String s){
        scope.add(s);
    }

    public void addScope(SupportedScopes s){
        scope.add(s.getText());
    }

    private static TokensResponse toObject(String str)
    {
        return gson.fromJson(str, TokensResponse.class);
    }

    public String getAuthorizationURL(String state)
    {
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(BASE_URI + "/oauth2/authorize");
        }
        catch (URISyntaxException e)
        {
            log.error("Failed to initialize URIBuilder", e);
            return null;
        }
        builder.addParameter("response_type", "code");
        builder.addParameter("client_id", clientID);
        builder.addParameter("redirect_uri", redirectUri);
        if (state != null && state.length() > 0)
        {
            builder.addParameter("state", state);
        }
        return builder.toString() + "&scope=" + String.join("%20", scope);
    }

    public String getAuthorizationURL(CustomRequest request){
        URIBuilder builder;
        try
        {
            builder = new URIBuilder(BASE_URI + "/oauth2/authorize");
        }
        catch (URISyntaxException e)
        {
            log.error("Failed to initialize URIBuilder", e);
            return null;
        }
        builder.addParameter("response_type", "code");
        builder.addParameter("client_id", clientID);
        builder.addParameter("redirect_uri", request.getRedirectUri());
        if (request.getState() != null && request.getState().length() > 0) {
            builder.addParameter("state", request.getState());
        }
        return builder.toString() + "&scope=" + String.join("%20", request.getScope());
    }

    public TokensResponse getTokens(String code) throws IOException
    {
        Connection request = Jsoup.connect(BASE_URI + "/oauth2/token")
            .data("client_id", clientID)
            .data("client_secret", clientSecret)
            .data("grant_type", GRANT_TYPE_AUTHORIZATION)
            .data("code", code)
            .data("redirect_uri", redirectUri)
            .data("scope", String.join(" ", scope))
            .ignoreContentType(true);

        Document document = request.post();
        String response = document.body().text();

        return toObject(response);
    }

    public TokensResponse refreshTokens(String refresh_token) throws IOException
    {
        Connection request = Jsoup.connect(BASE_URI + "/oauth2/token")
            .data("client_id", clientID)
            .data("client_secret", clientSecret)
            .data("grant_type", GRANT_TYPE_REFRESH_TOKEN)
            .data("refresh_token", refresh_token)
            .ignoreContentType(true);

        Document document = request.post();
        String response = document.body().text();

        return toObject(response);
    }
}
