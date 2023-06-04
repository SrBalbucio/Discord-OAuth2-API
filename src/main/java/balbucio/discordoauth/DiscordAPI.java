package balbucio.discordoauth;

import balbucio.discordoauth.model.Connection;
import balbucio.discordoauth.model.GroupChannel;
import balbucio.discordoauth.model.Guild;
import balbucio.discordoauth.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DiscordAPI
{
    public static final String BASE_URI = "https://discord.com/api";
    private static final Gson gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
    private final String accessToken;

    private static <T> T toObject(String str, Class<T> clazz)
    {
        return gson.fromJson(str, clazz);
    }

    private String getVersion() throws IOException
    {
        InputStream resourceAsStream = this.getClass().getResourceAsStream("/version.properties");
        java.util.Properties prop = new java.util.Properties();
        prop.load(resourceAsStream);
        return prop.getProperty("version");
    }

    private void setHeaders(org.jsoup.Connection request) throws IOException
    {
        request.header("Authorization", "Bearer " + accessToken);
        request.header("User-Agent",
            String.format("Discord-OAuth2-Java, version %s, platform %s %s", getVersion(), System.getProperty("os.name"),
                System.getProperty("os.version")));
    }

    private String handleGet(String path) throws IOException
    {
        org.jsoup.Connection request = Jsoup.connect(BASE_URI + path).ignoreContentType(true);
        setHeaders(request);

        return request.get().body().text();
    }

    public User fetchUser() throws IOException
    {
        return toObject(handleGet("/users/@me"), User.class);
    }

    public List<Guild> fetchGuilds() throws IOException
    {
        return Arrays.asList(toObject(handleGet("/users/@me/guilds"), Guild[].class));
    }

    public List<Connection> fetchConnections() throws IOException
    {
        return Arrays.asList(toObject(handleGet("/users/@me/connections"), Connection[].class));
    }

    public boolean joinGuild(Guild guild, User user, String botToken) throws IOException{
        org.jsoup.Connection request = Jsoup.connect(BASE_URI+"/guilds/{guild.id}/members/{user.id}"
                .replace("{guild.id}", guild.getId())
                .replace("{user.id}", user.getId()))
                .requestBody(new JSONObject().put("access_token", accessToken).toString())
                .ignoreContentType(true);
        request.header("Authorization", "Bot " + botToken);
        request.method(org.jsoup.Connection.Method.POST);
        int s = request.execute().statusCode();
        return s == 201 || s == 204;
    }

    public boolean joinGroup(GroupChannel channel, User user) throws IOException{
        org.jsoup.Connection request = Jsoup.connect(BASE_URI+"/channels/{channel.id}/recipients/{user.id}"
                        .replace("{channel.id}", channel.getID())
                        .replace("{user.id}", user.getId()))
                .requestBody(new JSONObject().put("access_token", accessToken).toString())
                .ignoreContentType(true);
        setHeaders(request);
        request.method(org.jsoup.Connection.Method.POST);
        int s = request.execute().statusCode();
        return s == 201 || s == 204;
    }
}
