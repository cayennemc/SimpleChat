package me.vetustus.server.simplechat;

import com.google.gson.Gson;
import me.vetustus.server.simplechat.api.event.PlayerChatCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class SimpleChat implements ModInitializer {
    private ChatConfig config;

    @Override
    public void onInitialize() {

        try {
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PlayerChatCallback.EVENT.register((player, message) -> {
            PlayerChatCallback.ChatMessage chatMessage = new PlayerChatCallback.ChatMessage(player, message);

            chatMessage.setCancelled(true);

            boolean isGlobalMessage = false;
            String chatFormat = config.getLocalChatFormat();
            if (config.isGlobalChatEnabled()) {
                if (message.startsWith("!")) {
                    isGlobalMessage = true;
                    chatFormat = config.getGlobalChatFormat();
                    message = message.substring(1);
                }
            }
            chatFormat = translateChatColors('&', chatFormat);
            String stringMessage = chatFormat
                    .replaceAll("%player%", player.getDisplayName().asString())
            .replaceAll("%message%", message);
            if (config.isChatColorsEnabled())
                stringMessage = translateChatColors('&', stringMessage);

            Text resultMessage = Text.of(stringMessage);

            List<ServerPlayerEntity> players = player.getServer().getPlayerManager().getPlayerList();
            for (ServerPlayerEntity p : players) {
                if (config.isGlobalChatEnabled()) {
                    if (isGlobalMessage) {
                        p.sendMessage(resultMessage, false);
                    } else {
                        if (p.squaredDistanceTo(player) <= config.getChatRange()) {
                            p.sendMessage(resultMessage, false);
                        }
                    }
                } else {
                    p.sendMessage(resultMessage, false);
                }
            }
            return chatMessage;
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("simplechat").executes(context -> {
                if (context.getSource().hasPermissionLevel(1)) {
                    try {
                        loadConfig();
                        context.getSource().sendFeedback(Text.of("Settings are reloaded!"), false);
                    } catch (IOException e) {
                        context.getSource().sendFeedback(Text.of("An error occurred while reloading the settings (see the console)!"), false);
                        e.printStackTrace();
                    }
                } else {
                    context.getSource().sendFeedback(Text.of("You don't have the right to do this! If you think this is an error, contact your server administrator.")
                            .copy().formatted(Formatting.RED), false);
                }
                return 1;
            }));
        });
    }

    private void loadConfig() throws IOException {
        File configFile = new File(ChatConfig.CONFIG_PATH);
        if (!configFile.exists()) {
            Files.copy(this.getClass().getClassLoader().getResourceAsStream("simplechat.json"), configFile.toPath());
        }
        try {
            config = new Gson().fromJson(new FileReader(ChatConfig.CONFIG_PATH), ChatConfig.class);
        } catch (FileNotFoundException e) {
            config = new ChatConfig();
            e.printStackTrace();
        }
    }

    // almost like in bukkit
    public static String translateChatColors(char chatCode, String message) {
        if (message == null)
            return "";
        char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length - 1; i++) {
            if (chars[i] == chatCode && "AaBbCcDdEeFfKkLlMmNnOoRrXx0123456789".indexOf(chars[i + 1]) > -1) {
                chars[i] = '\u00a7';          // paragraph symbol
                chars[i + 1] = Character.toLowerCase(chars[i + 1]);
            }
        }
        return new String(chars);
    }
}
