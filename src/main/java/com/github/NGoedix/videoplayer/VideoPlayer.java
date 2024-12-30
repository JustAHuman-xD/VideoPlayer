package com.github.NGoedix.videoplayer;

import com.github.NGoedix.videoplayer.commands.*;
import com.github.NGoedix.videoplayer.commands.arguments.SymbolStringArgumentSerializer;
import com.github.NGoedix.videoplayer.commands.arguments.SymbolStringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.ArgumentTypeRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.ResourceLocation;

public class VideoPlayer implements ModInitializer {

    @Override
    public void onInitialize() {
        Reference.LOGGER.info("Initializing VideoPlayer");

        ArgumentTypeRegistry.registerArgumentType(new ResourceLocation(Reference.MOD_ID, "symbol_string"), SymbolStringArgumentType.class, new SymbolStringArgumentSerializer());

        CommandRegistrationCallback.EVENT.register(PlayVideoCommand::register);
        CommandRegistrationCallback.EVENT.register(PlayCustomVideoCommand::register);
        CommandRegistrationCallback.EVENT.register(StopVideoCommand::register);
        CommandRegistrationCallback.EVENT.register(PlayMusicCommand::register);
        CommandRegistrationCallback.EVENT.register(StopMusicCommand::register);
    }
}
