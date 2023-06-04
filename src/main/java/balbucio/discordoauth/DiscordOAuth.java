package balbucio.discordoauth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.mokulu.discord.oauth.model.TokensResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static io.mokulu.discord.oauth.DiscordAPI.BASE_URI;

@Slf4j
@RequiredArgsConstructor
public class DiscordOAuth
{
    private static final Gson gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
    private static final String GRANT_TYPE_AUTHORIZATION = "authorization_code";
    private static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
    private final String clientID;
    private final String clientSecret;
    private final String redirectUri;
    private final List<String> scope;

    public DiscordOAuth(String clientID, String clientSecret, String redirectUri, String[] scope){
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = Arrays.asList(scope);
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

        // URI builder turns spaces into +, but Discord API doesn't support that in scope
        return builder.toString() + "&scope=" + String.join("%20", scope);
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
