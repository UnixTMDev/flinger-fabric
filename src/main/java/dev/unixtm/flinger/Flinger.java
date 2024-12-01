package dev.unixtm.flinger;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import net.minecraft.entity.player.PlayerEntity;
import org.slf4j.LoggerFactory;

public class Flinger implements ModInitializer {
	public static final String MOD_ID = "flinger";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("What up yo");
		LOGGER.info("Its flingin time");

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
				ClientCommandManager.literal("fling")
						.then(ClientCommandManager.argument("force", IntegerArgumentType.integer()).executes(Flinger::flingRelative)
		)));
		//thems synonyms homie --UnixTMDev
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
				ClientCommandManager.literal("flingup")
						.then(ClientCommandManager.argument("force", IntegerArgumentType.integer()).executes(Flinger::flingVert)
						)));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
				ClientCommandManager.literal("flingdown")
						.then(ClientCommandManager.argument("force", IntegerArgumentType.integer()).executes(Flinger::flingDown)
						)));
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
				ClientCommandManager.literal("flingvert")
						.then(ClientCommandManager.argument("force", IntegerArgumentType.integer()).executes(Flinger::flingVert)
						)));
	}
	//TODO: Make this one actually work
	/*
	public static int flingAbs(CommandContext<FabricClientCommandSource> context){
		// For versions below 1.19, replace "Text.literal" with "new LiteralText".
		// For versions below 1.20, remode "() ->" directly.
		double x = IntegerArgumentType.getInteger(context, "x");
		double y = IntegerArgumentType.getInteger(context, "y");
		double z = IntegerArgumentType.getInteger(context, "z");
		//double y = 0;
		//double z = 0;


		//PlayerEntity player = MinecraftClient.getInstance().player;
		PlayerEntity player = context.getSource().getPlayer();
		assert player != null;
		player.addVelocity(x,y,z);
		context.getSource().sendFeedback(Text.literal("flinging"));

		return 1;
	}
	*/
	public static int flingVert(CommandContext<FabricClientCommandSource> context){
		double forceArg = IntegerArgumentType.getInteger(context, "force");
		PlayerEntity player;
		player = context.getSource().getPlayer();
		assert player != null;
		//Vec3d force = getForceVector(player.getYaw(),player.getPitch(),forceArg);
		player.addVelocity(0,forceArg, 0);
		context.getSource().sendFeedback(Text.literal("flinging vertically"));

		return 1;
	}
	public static int flingDown(CommandContext<FabricClientCommandSource> context){
		double forceArg = -IntegerArgumentType.getInteger(context, "force");
		PlayerEntity player;
		player = context.getSource().getPlayer();
		assert player != null;
		//Vec3d force = getForceVector(player.getYaw(),player.getPitch(),forceArg);
		player.addVelocity(0,forceArg, 0);
		context.getSource().sendFeedback(Text.literal("flinging vertically"));

		return 1;
	}
	public static int flingRelative(CommandContext<FabricClientCommandSource> context){
		double forceArg = IntegerArgumentType.getInteger(context, "force");
		PlayerEntity player;
        player = context.getSource().getPlayer();
        assert player != null;
		Vec3d force = getForceVector(player.getYaw(),player.getPitch(),forceArg);
		player.addVelocity(force);
		context.getSource().sendFeedback(Text.literal("flinging"));

		return 1;
	}
	//Courtesy of ChatGPT lol
	public static Vec3d getForceVector(float yaw, float pitch, double forceMagnitude) {
		// Convert yaw and pitch to radians
		double yawRad = Math.toRadians(yaw);
		double pitchRad = Math.toRadians(-pitch);

		// Compute unit vector components
		double x = -Math.sin(yawRad) * -Math.cos(pitchRad);
		double y = Math.sin(pitchRad);
		double z = Math.cos(yawRad) * -Math.cos(pitchRad);

		// Scale by force magnitude
		return new Vec3d(-x, y, -z).multiply(forceMagnitude);
	}
}