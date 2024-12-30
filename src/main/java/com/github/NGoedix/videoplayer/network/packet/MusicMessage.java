package com.github.NGoedix.videoplayer.network.packet;

import com.github.NGoedix.videoplayer.Reference;
import com.github.NGoedix.videoplayer.client.ClientHandler;
import com.github.NGoedix.videoplayer.network.PacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

public record MusicMessage(int messageType, String url, int volume) implements CustomPacketPayload {
    public static final Type<MusicMessage> ID = new Type<>(Reference.resourceLocation("send_music"));
    public static final StreamCodec<FriendlyByteBuf, MusicMessage> CODEC = PacketHandler.newCodec(input -> {
        int type = input.readInt();
        if (type != MessageType.START) {
            return new MusicMessage();
        }
        return new MusicMessage(MessageType.START, input.readUTF(), input.readInt());
    });

    public MusicMessage() {
        this(MessageType.STOP, null, 0);
    }

    public static void receive(MusicMessage message, ClientPlayNetworking.Context context) {
        Reference.LOGGER.info("received music message");
        if (message.messageType == MessageType.START) {
            Reference.LOGGER.info("starting music");
            ClientHandler.playMusic(context.client(), message.url, message.volume);
        } else if (message.messageType == MessageType.STOP) {
            Reference.LOGGER.info("stop music");
            ClientHandler.stopMusicIfPlaying(context.client());
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
