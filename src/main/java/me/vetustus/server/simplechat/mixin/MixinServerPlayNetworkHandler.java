package me.vetustus.server.simplechat.mixin;

import me.vetustus.server.simplechat.api.event.PlayerChatCallback;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.function.Consumer;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class MixinServerPlayNetworkHandler implements ServerPlayPacketListener {

    @Shadow public ServerPlayerEntity player;

    @Shadow
    protected abstract void method_31286(String message);

    @Shadow
    protected abstract void filterText(String text, Consumer<String> consumer);

    /**
     * @author vetustus
     */
    @Overwrite
    public void onGameMessage(ChatMessageC2SPacket packet) {
        String string = StringUtils.normalizeSpace(packet.getChatMessage());
        if (string.startsWith("/")) {
            NetworkThreadUtils.forceMainThread(packet, this, this.player.getServerWorld());
            this.method_31286(string);
        } else {
            PlayerChatCallback.ChatMessage message = PlayerChatCallback.EVENT.invoker().result(player, packet.getChatMessage());
            if (!message.isCancelled())
                this.filterText(message.getMessage(), this::method_31286);
        }
    }
}
