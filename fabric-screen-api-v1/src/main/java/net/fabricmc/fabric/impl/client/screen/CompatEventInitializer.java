package net.fabricmc.fabric.impl.client.screen;

import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;

public class CompatEventInitializer implements ClientModInitializer {
	@Override
	public void onInitializeClient(ModContainer mod) {
		ScreenEventFactory.initializeCompatEvents();
	}
}
