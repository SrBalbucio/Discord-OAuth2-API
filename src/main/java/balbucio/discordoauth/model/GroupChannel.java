package balbucio.discordoauth.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.PRIVATE)
public class GroupChannel {

    private String ID;

    public GroupChannel(String ID) {
        this.ID = ID;
    }
}
