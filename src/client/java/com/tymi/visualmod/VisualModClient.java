package com.tymi.visualmod;

import com.tymi.visualmod.gui.ClickGuiScreen;
import com.tymi.visualmod.gui.Draw;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class VisualModClient implements ClientModInitializer {
	public static KeyBinding openGui;

	@Override
	public void onInitializeClient() {
		openGui = KeyBindingHelper.registerKeyBinding(new KeyBinding(
			"key.visualmod.open_gui",
			InputUtil.Type.KEYSYM,
			GLFW.GLFW_KEY_RIGHT_SHIFT,
			KeyBinding.Category.create(Identifier.of("visualmod", "main"))
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (openGui.wasPressed()) {
				if (client.currentScreen == null) client.setScreen(new ClickGuiScreen());
			}
		});

		HudElementRegistry.attachElementAfter(
			VanillaHudElements.CHAT,
			Identifier.of("visualmod", "hud"),
			(ctx, tick) -> {
				MinecraftClient mc = MinecraftClient.getInstance();
				if (mc.options.hudHidden) return;
				int y = 6;
				for (Module m : ModuleManager.MODULES) {
					if (!m.enabled) continue;
					String s = m.text.get();
					if (s.isEmpty()) continue;
					int w = mc.textRenderer.getWidth(s) + 14;
					Draw.rrect(ctx, 6, y, w, 16, 4, 0xB0101014);
					ctx.fill(6, y + 4, 8, y + 12, ModuleManager.accent());
					ctx.drawText(mc.textRenderer, s, 13, y + 4, 0xFFFFFFFF, false);
					y += 20;
				}
			}
		);
	}
}
