package com.github.NGoedix.videoplayer.client;

import com.github.NGoedix.videoplayer.VideoPlayerUtils;
import com.github.NGoedix.videoplayer.client.gui.VideoScreen;
import com.github.NGoedix.videoplayer.network.PacketHandler;
import com.github.NGoedix.videoplayer.Reference;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.watermedia.api.image.ImageAPI;
import org.watermedia.api.image.ImageRenderer;
import org.watermedia.api.player.videolan.MusicPlayer;
import org.watermedia.core.tools.JarTool;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ClientHandler implements ClientModInitializer {

    @Environment(EnvType.CLIENT)
    private static ImageRenderer IMG_PAUSED;

    @Environment(EnvType.CLIENT)
    private static ImageRenderer IMG_STEP10;

    @Environment(EnvType.CLIENT)
    private static ImageRenderer IMG_STEP5;

    @Environment(EnvType.CLIENT)
    public static ImageRenderer pausedImage() { return IMG_PAUSED; }

    @Environment(EnvType.CLIENT)
    public static ImageRenderer step10Image() { return IMG_STEP10; }

    @Environment(EnvType.CLIENT)
    public static ImageRenderer step5Image() { return IMG_STEP5; }

    private static final List<MusicPlayer> musicPlayers = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        Reference.LOGGER.info("Initializing Client");

        if (VideoPlayerUtils.isInstalled("mr_stellarity", "stellarity")) {
            throw new VideoPlayerUtils.UnsupportedModException("mr_stellarity (Stellarity)", "breaks picture rendering, overwrites Minecraft core shaders and isn't possible work around that");
        }

        PacketHandler.registerS2CPackets();

        IMG_PAUSED = ImageAPI.renderer(JarTool.readImage("/pictures/paused.png"), true);
        IMG_STEP10 = ImageAPI.renderer(JarTool.readImage("/pictures/step10.png"), true);
        IMG_STEP5 = ImageAPI.renderer(JarTool.readImage("/pictures/step5.png"), true);
    }

    public static void openVideo(Minecraft client, String url, int volume, boolean isControlBlocked, boolean canSkip) {
        client.execute(() -> {
            Minecraft.getInstance().setScreen(new VideoScreen(url, volume, isControlBlocked, canSkip, false));
        });
    }

    public static void openVideo(Minecraft client, String url, int volume, boolean isControlBlocked, boolean canSkip, int optionInMode, int optionInSecs, int optionOutMode, int optionOutSecs) {
        client.execute(() -> {
            Minecraft.getInstance().setScreen(new VideoScreen(url, volume, isControlBlocked, canSkip, optionInMode, optionInSecs, optionOutMode, optionOutSecs));
        });
    }

    public static void stopVideoIfExists(Minecraft client) {
        client.execute(() -> {
            if (Minecraft.getInstance().screen instanceof VideoScreen screen) {
                screen.onClose();
            }
        });
    }

    public static void playMusic(Minecraft client, String url, int volume) {
        client.execute(() -> {
            // Until any callback in SyncMusicPlayer I will check if the music is playing when added other music player
            stopMusicIfPlaying();

            // Add the new player
            MusicPlayer musicPlayer = new MusicPlayer();
            musicPlayers.add(musicPlayer);
            musicPlayer.setVolume(volume);
            musicPlayer.start(URI.create(url));
        });
    }

    public static void stopMusicIfPlaying(Minecraft client) {
        client.execute(ClientHandler::stopMusicIfPlaying);
    }

    private static void stopMusicIfPlaying() {
        musicPlayers.removeIf(musicPlayer -> {
            if (musicPlayer.isPlaying()) {
                musicPlayer.stop();
                musicPlayer.release();
                return true;
            }
            return false;
        });
    }
}
