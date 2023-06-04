package balbucio.discordoauth.model;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
@Setter(AccessLevel.PRIVATE)
public class Guild
{
    private String id;
    private String name;
    private String icon;
    @SerializedName("icon_hash")
    private String iconHash;
    private String splash;
    @SerializedName("discovery_splash")
    private String discoverySplash;
    private boolean owner;
    private String owner_id;
    @SerializedName("permissions")
    private String permissions;
    private String region;
    private String afk_channel_id;
    private Integer afk_timeout;
    private List<String> features;
    private List<String> emojis;
    private List<String> roles;

    public Guild(String id) {
        this.id = id;
    }
}
