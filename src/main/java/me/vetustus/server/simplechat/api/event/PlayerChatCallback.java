package me.vetustus.server.simplechat.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

public interface PlayerChatCallback {

    Event<PlayerChatCallback> EVENT = EventFactory.createArrayBacked(PlayerChatCallback.class,
            listeners -> (player, message) -> {
                for (PlayerChatCallback listener : listeners) {
                    return listener.result(player, message);
                }
                return new ChatMessage(player, message);
            });

    ChatMessage result(ServerPlayerEntity player, String message);

    class ChatMessage {
        private final ServerPlayerEntity sender;
        private String message;
        private boolean isCancelled = false;

        public ChatMessage(ServerPlayerEntity sender, String message) {
            this.sender = sender;
            this.message = message;
        }

        public ServerPlayerEntity getPlayer() {
            return this.sender;
        }

        public void setMessage(String message) {
            this.message = message;
            if (this.message == null)
                this.message = "";
        }

        public String getMessage() {
            return this.message;
        }

        public void setCancelled(boolean isCancelled) {
            this.isCancelled = isCancelled;
        }

        public boolean isCancelled() {
            return this.isCancelled;
        }
    }
}
