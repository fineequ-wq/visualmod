package com.tymi.visualmod.gui;

import net.minecraft.client.gui.DrawContext;

public class Draw {
	/** Скруглённый прямоугольник (радиус r) без текстур — построчно. */
	public static void rrect(DrawContext c, int x, int y, int w, int h, int r, int color) {
		r = Math.min(r, Math.min(w, h) / 2);
		for (int i = 0; i < r; i++) {
			double dy = r - i - 0.5;
			int inset = (int) Math.round(r - Math.sqrt(Math.max(0, r * r - dy * dy)));
			c.fill(x + inset, y + i, x + w - inset, y + i + 1, color);
			c.fill(x + inset, y + h - i - 1, x + w - inset, y + h - i, color);
		}
		c.fill(x, y + r, x + w, y + h - r, color);
	}

	public static int lerpColor(int a, int b, float t) {
		int aa = (a >>> 24), ar = (a >> 16) & 255, ag = (a >> 8) & 255, ab = a & 255;
		int ba = (b >>> 24), br = (b >> 16) & 255, bg = (b >> 8) & 255, bb = b & 255;
		return ((int) (aa + (ba - aa) * t) << 24) | ((int) (ar + (br - ar) * t) << 16)
			| ((int) (ag + (bg - ag) * t) << 8) | (int) (ab + (bb - ab) * t);
	}
}
