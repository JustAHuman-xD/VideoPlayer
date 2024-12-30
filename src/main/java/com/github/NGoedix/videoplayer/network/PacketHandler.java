package com.github.NGoedix.videoplayer.network;

import com.github.NGoedix.videoplayer.network.packet.*;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.function.Function;

public class PacketHandler {

    public static void registerS2CPackets() {
        PayloadTypeRegistry.playS2C().register(VideoMessage.ID, VideoMessage.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(VideoMessage.ID, VideoMessage::receive);
        PayloadTypeRegistry.playS2C().register(MusicMessage.ID, MusicMessage.CODEC);
        ClientPlayNetworking.registerGlobalReceiver(MusicMessage.ID, MusicMessage::receive);
    }

    public static <P extends CustomPacketPayload> StreamCodec<FriendlyByteBuf, P> newCodec(Function<ByteArrayDataInput, P> decoder) {
        return StreamCodec.of((value, buf) -> {}, buf -> {
            byte[] bytes = new byte[buf.readableBytes()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = buf.readByte();
            }
            return decoder.apply(ByteStreams.newDataInput(bytes));
        });
    }
}
