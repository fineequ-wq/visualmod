package com.tymi.visualmod.gui;

import com.tymi.visualmod.Module;
import com.tymi.visualmod.ModuleManager;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.glfw.GLFW;

public class ClickGuiScreen extends Screen {
	private static final int PW = 190, ROW = 24, PAD = 10, HEAD = 34, FOOT = 40;
	private float open = 0f;
	private boolean dragSlider = false;

	public ClickGuiScreen() { super(Text.literal("ClickGUI")); }

	@Override public boolean shouldPause() { return false; }

	private int panelH() { return HEAD + ModuleManager.MODULES.size() * ROW + FOOT; }
	private int px() { return (width - PW) / 2; }
	private int py() { return (height - panelH()) / 2 + (int) ((1 - open) * 14); }

	@Override
	public void render(DrawContext c, int mx, int my, float delta) {
		open = Math.min(1f, open + delta * 0.18f);
		int accent = ModuleManager.accent();
		int x = px(), y = py(), h = panelH();

		c.fill(0, 0, width, height, ((int) (110 * open) << 24));

		// панель
		Draw.rrect(c, x - 1, y - 1, PW + 2, h + 2, 8, ((int) (60 * open) << 24) | (accent & 0xFFFFFF));
		Draw.rrect(c, x, y, PW, h, 7, ((int) (235 * open) << 24) | 0x0F0F13);

		// заголовок
		c.fill(x + PAD, y + 14, x + PAD + 3, y + 22, accent);
		c.drawText(textRenderer, "VISUAL", x + PAD + 8, y + 13, 0xFFFFFFFF, false);
		c.drawText(textRenderer, "R-SHIFT", x + PW - PAD - textRenderer.getWidth("R-SHIFT"), y + 13, 0xFF5A5A66, false);

		// модули
		int ry = y + HEAD;
		for (Module m : ModuleManager.MODULES) {
			boolean hov = mx >= x + 4 && mx <= x + PW - 4 && my >= ry && my < ry + ROW;
			m.hover += ((hov ? 1f : 0f) - m.hover) * 0.25f;
			m.anim += ((m.enabled ? 1f : 0f) - m.anim) * 0.25f;

			if (m.hover > 0.01f)
				Draw.rrect(c, x + 4, ry, PW - 8, ROW - 2, 5, ((int) (28 * m.hover) << 24) | 0xFFFFFF);

			int textCol = Draw.lerpColor(0xFF7C7C88, 0xFFFFFFFF, Math.max(m.anim, m.hover * 0.6f));
			c.drawText(textRenderer, m.name, x + PAD + 4, ry + 8, textCol, false);

			// переключатель
			int sw = 24, sh = 12, sx = x + PW - PAD - sw - 2, sy = ry + 5;
			Draw.rrect(c, sx, sy, sw, sh, 6, Draw.lerpColor(0xFF26262E, accent, m.anim));
			int kx = sx + 2 + (int) ((sw - sh) * m.anim);
			Draw.rrect(c, kx, sy + 2, 8, 8, 4, 0xFFFFFFFF);
			ry += ROW;
		}

		// слайдер цвета
		int sx = x + PAD + 4, sw = PW - 2 * PAD - 8, sy = y + h - 22;
		c.drawText(textRenderer, "ACCENT", sx, sy - 12, 0xFF7C7C88, false);
		for (int i = 0; i < sw; i++) {
			int col = java.awt.Color.HSBtoRGB(i / (float) sw, 0.55f, 1f) | 0xFF000000;
			c.fill(sx + i, sy, sx + i + 1, sy + 4, col);
		}
		int kx = sx + (int) (ModuleManager.hue * sw);
		Draw.rrect(c, kx - 3, sy - 2, 6, 8, 3, 0xFFFFFFFF);
	}

	private void setHue(double mx) {
		int sx = px() + PAD + 4, sw = PW - 2 * PAD - 8;
		ModuleManager.hue = (float) MathHelper.clamp((mx - sx) / sw, 0, 1);
	}

	@Override
	public boolean mouseClicked(Click click, boolean doubled) {
		double mx = click.x(), my = click.y();
		int x = px(), y = py();
		if (click.button() == 0) {
			int ry = y + HEAD;
			for (Module m : ModuleManager.MODULES) {
				if (mx >= x + 4 && mx <= x + PW - 4 && my >= ry && my < ry + ROW) {
					m.enabled = !m.enabled;
					return true;
				}
				ry += ROW;
			}
			int sy = y + panelH() - 22;
			if (my >= sy - 4 && my <= sy + 10 && mx >= x + PAD && mx <= x + PW - PAD) {
				dragSlider = true;
				setHue(mx);
				return true;
			}
		}
		return super.mouseClicked(click, doubled);
	}

	@Override
	public boolean mouseDragged(Click click, double dx, double dy) {
		if (dragSlider) { setHue(click.x()); return true; }
		return super.mouseDragged(click, dx, dy);
	}

	@Override
	public boolean mouseReleased(Click click) {
		dragSlider = false;
		return super.mouseReleased(click);
	}

	@Override
	public boolean keyPressed(KeyInput input) {
		if (input.key() == GLFW.GLFW_KEY_RIGHT_SHIFT) { close(); return true; }
		return super.keyPressed(input);
	}
}
