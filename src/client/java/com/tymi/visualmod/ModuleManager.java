package com.tymi.visualmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.MathHelper;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ModuleManager {
	public static float hue = 0.62f; // акцентный цвет (0..1)

	public static final List<Module> MODULES = List.of(
		new Module("FPS", true, () -> MinecraftClient.getInstance().getCurrentFps() + " fps"),
		new Module("Coordinates", true, () -> {
			var p = MinecraftClient.getInstance().player;
			return p == null ? "" : String.format("%.0f  %.0f  %.0f", p.getX(), p.getY(), p.getZ());
		}),
		new Module("Direction", false, () -> {
			var p = MinecraftClient.getInstance().player;
			if (p == null) return "";
			String[] d = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};
			int i = Math.floorMod(Math.round(MathHelper.wrapDegrees(p.getYaw()) / 45f), 8);
			return d[i];
		}),
		new Module("Clock", false, () -> LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
	);

	public static int accent() {
		return java.awt.Color.HSBtoRGB(hue, 0.55f, 1f) | 0xFF000000;
	}
}
