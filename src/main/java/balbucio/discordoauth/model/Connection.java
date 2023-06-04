package balbucio.discordoauth.model;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class Connection
{
    private String id;
    private String name;
    private String type;
    private boolean revoked;
    private boolean verified;
    @SerializedName("friend_sync")
    private boolean friendSync;
    @SerializedName("show_activity")
    private boolean showActivity;
    @SerializedName("two_way_link")
    private boolean twoWayLink;
    private int visibility;
}
