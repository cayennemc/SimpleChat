# SimpleChat
![modrinth_downloads](https://img.shields.io/badge/dynamic/json?color=5da545&label=modrinth&prefix=downloads%20&query=downloads&url=https://api.modrinth.com/api/v1/mod/2KNt6S40&style=flat&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHZpZXdCb3g9IjAgMCAxMSAxMSIgd2lkdGg9IjE0LjY2NyIgaGVpZ2h0PSIxNC42NjciICB4bWxuczp2PSJodHRwczovL3ZlY3RhLmlvL25hbm8iPjxkZWZzPjxjbGlwUGF0aCBpZD0iQSI+PHBhdGggZD0iTTAgMGgxMXYxMUgweiIvPjwvY2xpcFBhdGg+PC9kZWZzPjxnIGNsaXAtcGF0aD0idXJsKCNBKSI+PHBhdGggZD0iTTEuMzA5IDcuODU3YTQuNjQgNC42NCAwIDAgMS0uNDYxLTEuMDYzSDBDLjU5MSA5LjIwNiAyLjc5NiAxMSA1LjQyMiAxMWMxLjk4MSAwIDMuNzIyLTEuMDIgNC43MTEtMi41NTZoMGwtLjc1LS4zNDVjLS44NTQgMS4yNjEtMi4zMSAyLjA5Mi0zLjk2MSAyLjA5MmE0Ljc4IDQuNzggMCAwIDEtMy4wMDUtMS4wNTVsMS44MDktMS40NzQuOTg0Ljg0NyAxLjkwNS0xLjAwM0w4LjE3NCA1LjgybC0uMzg0LS43ODYtMS4xMTYuNjM1LS41MTYuNjk0LS42MjYuMjM2LS44NzMtLjM4N2gwbC0uMjEzLS45MS4zNTUtLjU2Ljc4Ny0uMzcuODQ1LS45NTktLjcwMi0uNTEtMS44NzQuNzEzLTEuMzYyIDEuNjUxLjY0NSAxLjA5OC0xLjgzMSAxLjQ5MnptOS42MTQtMS40NEE1LjQ0IDUuNDQgMCAwIDAgMTEgNS41QzExIDIuNDY0IDguNTAxIDAgNS40MjIgMCAyLjc5NiAwIC41OTEgMS43OTQgMCA0LjIwNmguODQ4QzEuNDE5IDIuMjQ1IDMuMjUyLjgwOSA1LjQyMi44MDljMi42MjYgMCA0Ljc1OCAyLjEwMiA0Ljc1OCA0LjY5MSAwIC4xOS0uMDEyLjM3Ni0uMDM0LjU2bC43NzcuMzU3aDB6IiBmaWxsLXJ1bGU9ImV2ZW5vZGQiIGZpbGw9IiM1ZGE0MjYiLz48L2c+PC9zdmc+)
![java_16](https://img.shields.io/badge/java-16+-orange?logo=java)

_A simple chat mod for your server._

Works even in a single player game.

![Imgur](https://i.imgur.com/orpafbR.jpg)
## Features
- Global and local chat (you can turn it off)
- Color chat (you can turn it off)
- Reloading the configuration with the command
- For developers: Player chat event

## Configuration
The configuration is located in `<game or server directory>/config/simplechat.json`.
| Name | Description | Type |
|-|-|-|
| enable_chat_mod | Enables (true) or disables (false) chat handling by the mod. | boolean |
| enable_global_chat | Enables (true) or disables (false) the global chat. | boolean |
| enable_chat_colors | Enables (true) or disables (false) the use of color codes in the chat. | boolean |
| local_chat_format | Defines the appearance of the local chat. | String |
| global_chat_format | Defines the appearance of the global chat. | String |
| chat_range | Specifies the distance after which local chat messages will not be visible (if global chat is enabled). | int |

```json
{
  "enable_chat_mod": true,
  "enable_global_chat": false,
  "enable_chat_colors": false,
  "local_chat_format": "%player% > &7%message%",
  "global_chat_format": "&8[&bG&8] &r%player% > &e%message%",
  "chat_range": 100
}
```
You can use the placeholder `%player%` to specify the player's nickname and the placeholder `%message%` to specify their message in the chat.

You can reload the configuration without restarting the server or the game using the `/simplechat` command (requires [permission level](https://minecraft.fandom.com/wiki/Server.properties#op-permission-level) 1 or more).

## API
If you are a developer, you can use an event called when a player writes something to the chat.

Look [`me.vetustus.server.simplechat.api.event.PlayerChatCallback`](src/main/java/me/vetustus/server/simplechat/api/event/PlayerChatCallback.java). 
To control the behavior, use the [ChatMessage](src/main/java/me/vetustus/server/simplechat/api/event/PlayerChatCallback.java#L47) subclass, which can be used to cancel sending a message or change it.

*Example:*
```java
/**
 * Prohibits players from writing messages by canceling an event.
 */
PlayerChatCallback.EVENT.register((player, message) -> {
  PlayerChatCallback.ChatMessage chatMessage = new PlayerChatCallback.ChatMessage(player, message);
  chatMessage.setCancelled(true);
  return chatMessage;
});
```
## License
The MIT license is used.
