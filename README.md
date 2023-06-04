# Discord OAuth2 API
A small Discord OAuth2 API wrapper for Java.

## Features
* Generates the authorization URL.
* Code authorization for access token and refresh token.
* Refresh the access token with refresh token.
* Get the user, guilds, and connection info of a user from access token.

## Importing To Your Project
[![](https://jitpack.io/v/SrBalbucio/discord-oauth2-api.svg)](https://jitpack.io/#SrBalbucio/discord-oauth2-api)

### Maven
```
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
```
```
	<dependency>
	    <groupId>com.github.SrBalbucio</groupId>
	    <artifactId>discord-oauth2-api</artifactId>
	    <version>v1.0.4</version>
	</dependency>
```

### Gradle
```
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
```
	dependencies {
	        implementation 'com.github.SrBalbucio:discord-oauth2-api:v1.0.4'
	}
```

## Using the API
### Creating the OAuth handler
```java
import balbucio.discordoauth.DiscordOAuth;

DiscordOAuth oauthHandler = new DiscordOAuth(clientID: String, clientSecret: String, redirectUri: String, scope: String[]);
DiscordOAuth oauthHandler = new DiscordOAuth(clientID: String, clientSecret: String, redirectUri: String, scope: List<String>);
DiscordOAuth oauthHandler = new DiscordOAuth(clientID: String, clientSecret: String, redirectUri: String, scope: SupportedScopes[]);
```

#### Generating the OAuth URL
```java
String authURL = oauthHandler.getAuthorizationURL(state: String);
```
`state` will be ingored by passing null.

#### Generating the OAuth URL with CustomRequest
```java
CustomRequest request = new CustomRequest(requestUri: String, state: String, scope: String[]);
CustomRequest request = new CustomRequest(requestUri: String, state: String, scope: List<String>);
CustomRequest request = new CustomRequest(requestUri: String, state: String, scope: SupportedScopes[]);
String authURL = oauthHandler.getAuthorizationURL(request: CustomRequest);
```
`state` will be ingored by passing null.

#### Authorizing the `code`
```java
import balbucio.discordoauth.model.TokensResponse;

TokensResponse tokens = oauthHandler.getTokens(code: String);
String accessToken = tokens.getAccessToken();
String refreshToken = tokens.getRefreshToken();
```

#### Refreshing the Access Token
```java
TokensResponse tokens = oauthHandler.refreshTokens(refresh_token: String);
```

### Creating the API handler
```java
import balbucio.discordoauth.DiscordAPI;

DiscordAPI api = new DiscordAPI(access_token: String);
```

The following API fetch calls will throw `IOException (HttpStatusException)` when access is denied due to invalid scope or expired token.

#### Fetching User
Scope `identity` is required.
Scope `email` is required for `email` and `verified` values.
```java
import balbucio.discordoauth.model.User;

User user = api.fetchUser();
```

#### Fetching Guilds
Scope `guilds` is required.
```java
import balbucio.discordoauth.model.Guild;

List<Guild> guilds = api.fetchGuilds();
```

#### Fetching Connections
Scope `connections` is required.
```java
import balbucio.discordoauth.model.Connection;

List<Connection> connections = api.fetchConnections();
```
#### Joining a guild
Scope `guilds.join` is required.<br>
Make sure you have a bot in your OAuth application and that this bot is in the guild you want to add members to, to learn more [click here.](https://discord.com/developers/docs/resources/guild#add-guild-member)
```java
api.joinGuild(guild: Guild, user: User, botToken: String);
// You can create a guild using just the ID
```
#### Joining a Group Channel
Scope `gdm.join` is required.<br>
This function has not yet been tested, so it is recommended not to add it to production. Feel free to test it out.
```java
GroupChannel channel = new GroupChannel(id: String);
api.joinGuild(channel: GroupChannel, user: User);
```
