package com.github.NGoedix.videoplayer.network;

import com.github.NGoedix.videoplayer.network.packet.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class PacketHandler {

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SendVideoMessage.ID, SendVideoMessage::receive);
        ClientPlayNetworking.registerGlobalReceiver(SendCustomVideoMessage.ID, SendCustomVideoMessage::receive);
        ClientPlayNetworking.registerGlobalReceiver(SendMusicMessage.ID, SendMusicMessage::receive);
    }

    // SEND MESSAGES S2C
    public static void sendS2CSendVideoStart(ServerPlayer player, String url, int volume, boolean controlBlocked, boolean canSkip) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeEnum(SendVideoMessage.VideoMessageType.START);
        buf.writeUtf(url);
        buf.writeInt(volume);
        buf.writeBoolean(controlBlocked);
        buf.writeBoolean(canSkip);
        ServerPlayNetworking.send(player, SendVideoMessage.ID, buf);
    }

    public static void sendS2CSendVideoStop(ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeEnum(SendVideoMessage.VideoMessageType.STOP);
        ServerPlayNetworking.send(player, SendVideoMessage.ID, buf);
    }

    public static void sendS2CSendMusicStart(ServerPlayer player, String url, int volume) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeEnum(SendMusicMessage.MusicMessageType.START);
        buf.writeUtf(url);
        buf.writeInt(volume);
        ServerPlayNetworking.send(player, SendMusicMessage.ID, buf);
    }

    public static void sendS2CSendMusicStop(ServerPlayer player) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeEnum(SendMusicMessage.MusicMessageType.STOP);
        ServerPlayNetworking.send(player, SendMusicMessage.ID, buf);
    }

    public static void sendS2CSendVideoStart(ServerPlayer player, String url, int volume, boolean controlBlocked, boolean canSkip, int mode, int position, int optionInMode, int optionInSecs, int optionOutMode, int optionOutSecs) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeEnum(SendCustomVideoMessage.VideoMessageType.START);
        buf.writeUtf(url);
        buf.writeInt(volume);
        buf.writeBoolean(controlBlocked);
        buf.writeBoolean(canSkip);
        buf.writeInt(mode);
        buf.writeInt(position);
        buf.writeInt(optionInMode);
        buf.writeInt(optionInSecs);
        buf.writeInt(optionOutMode);
        buf.writeInt(optionOutSecs);
        ServerPlayNetworking.send(player, SendCustomVideoMessage.ID, buf);
    }
}
