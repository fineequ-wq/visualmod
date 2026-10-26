package com.tymi.visualmod;

import java.util.function.Supplier;

public class Module {
	public final String name;
	public final Supplier<String> text; // что показывать на HUD
	public boolean enabled;
	public float anim;  // анимация переключателя 0..1
	public float hover; // анимация наведения 0..1

	public Module(String name, boolean enabled, Supplier<String> text) {
		this.name = name;
		this.enabled = enabled;
		this.text = text;
		this.anim = enabled ? 1f : 0f;
	}
}
