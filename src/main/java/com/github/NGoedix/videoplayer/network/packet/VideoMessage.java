package com.github.NGoedix.videoplayer.network.packet;

import com.github.NGoedix.videoplayer.Reference;
import com.github.NGoedix.videoplayer.client.ClientHandler;
import com.github.NGoedix.videoplayer.network.PacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record VideoMessage(int messageType, String url, int volume, boolean isControlBlocked, boolean canSkip) implements CustomPacketPayload {
    public static final Type<VideoMessage> ID = new Type<>(Reference.resourceLocation("send_video"));
    public static final StreamCodec<FriendlyByteBuf, VideoMessage> CODEC = PacketHandler.newCodec(input -> {
        int type = input.readInt();
        if (type != MessageType.START) {
            return new VideoMessage();
        }
        return new VideoMessage(MessageType.START, input.readUTF(), input.readInt(), input.readBoolean(), input.readBoolean());
    });

    public VideoMessage() {
        this(MessageType.STOP, null, 0, false, false);
    }

    public static void receive(VideoMessage message, ClientPlayNetworking.Context context) {
        Reference.LOGGER.info("received video message");
        if (message.messageType == MessageType.START) {
            Reference.LOGGER.info("starting video");
            ClientHandler.openVideo(context.client(), message.url, message.volume, message.isControlBlocked, message.canSkip);
        } else if (message.messageType == MessageType.STOP) {
            Reference.LOGGER.info("stopping video");
            ClientHandler.stopVideoIfExists(context.client());
        }
    }

    @Override
    public @NotNull Type<VideoMessage> type() {
        return ID;
    }
}
