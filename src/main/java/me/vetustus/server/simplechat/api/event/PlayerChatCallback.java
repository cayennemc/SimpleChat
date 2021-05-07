package me.vetustus.server.simplechat.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Callback for when a player writes something to the chat.
 *
 * <p>To work with the chat, you need to register a listener and implement a callback.</p>
 *
 * <p>All actions are performed through a special {@link ChatMessage} object.</p>
 *
 * <pre><code>
 * // Prohibits players from writing messages by canceling an event.
 * PlayerChatCallback.EVENT.register((player, message) -> {
 *      PlayerChatCallback.ChatMessage chatMessage = new PlayerChatCallback.ChatMessage(player, message);
 *      chatMessage.setCancelled(true);
 *      return chatMessage;
 * });
 * </code></pre>
 */
public interface PlayerChatCallback {

    Event<PlayerChatCallback> EVENT = EventFactory.createArrayBacked(PlayerChatCallback.class,
            listeners -> (player, message) -> {
                for (PlayerChatCallback listener : listeners) {
                    return listener.result(player, message);
                }
                return new ChatMessage(player, message);
            });

    ChatMessage result(ServerPlayerEntity player, String message);

    /**
     * Object for determining the further behavior of the event.
     *
     * <p>With it, the message text can be changed or the message may not be sent at all.</p>
     *
     * <pre><code>
     * // Example showing how to change the message text
     * PlayerChatCallback.ChatMessage chatMessage = new PlayerChatCallback.ChatMessage (player, message);
     * if ("Hello!".equalsIgnoreCase(chatMessage.getMessage()))
     *      chatMessage.setMessage("Bye!");
     * </code></pre>
     */
    class ChatMessage {
        private final ServerPlayerEntity sender;
        private String message;
        private boolean isCancelled = false;

        public ChatMessage(ServerPlayerEntity sender, String message) {
            this.sender = sender;
            this.message = message;
        }

        /**
         * Get the player who sent the message.
         *
         * @return the sender of the message
         */
        public ServerPlayerEntity getPlayer() {
            return this.sender;
        }

        /**
         * Get the message text.
         * It should be understood that the method will return a new text if the message is changed via setMessage.
         *
         * @return the message text to send
         */
        public String getMessage() {
            return this.message;
        }

        /**
         * Set a new message text.
         * If the new text is <i>null</i>, the message text will be set as an empty string ("").
         *
         * @param message new message
         */
        public void setMessage(String message) {
            this.message = message;
            if (this.message == null)
                this.message = "";
        }

        /**
         * Whether the message was canceled.
         *
         * @return true if the event is canceled and the message is not sent, otherwise false
         */
        public boolean isCancelled() {
            return this.isCancelled;
        }

        /**
         * Whether to cancel sending the message.
         * If you cancel, the message will simply not be sent to the players.
         *
         * @param isCancelled true or false
         */
        public void setCancelled(boolean isCancelled) {
            this.isCancelled = isCancelled;
        }
    }
}
