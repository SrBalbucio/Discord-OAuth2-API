package balbucio.discordoauth.scope;

public enum SupportedScopes {

    IDENTIFY("identify"), IDENTIFY_EMAIL("email"), CONNECTIONS("connections"), GUILDS("guilds"), GUILDS_JOIN("guilds.join"), GROUP_JOIN("gdm.join");

    private String text;

    SupportedScopes(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
