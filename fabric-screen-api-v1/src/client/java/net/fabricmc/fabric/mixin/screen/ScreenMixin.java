/*
 * Copyright 2016, 2017, 2018, 2019 FabricMC
 * Copyright 2022 The Quilt Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.fabricmc.fabric.mixin.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.impl.client.screen.ScreenEventFactory;
import net.fabricmc.fabric.impl.client.screen.ScreenExtensions;

@Mixin(value = Screen.class, priority = 999)
abstract class ScreenMixin implements ScreenExtensions {
	@Unique
	private Event<ScreenEvents.Remove> qfapi$removeEvent;
	@Unique
	private Event<ScreenEvents.BeforeTick> qfapi$beforeTickEvent;
	@Unique
	private Event<ScreenEvents.AfterTick> qfapi$afterTickEvent;
	@Unique
	private Event<ScreenEvents.BeforeRender> qfapi$beforeRenderEvent;
	@Unique
	private Event<ScreenEvents.AfterRender> qfapi$afterRenderEvent;

	// Keyboard
	@Unique
	private Event<ScreenKeyboardEvents.AllowKeyPress> qfapi$allowKeyPressEvent;
	@Unique
	private Event<ScreenKeyboardEvents.BeforeKeyPress> qfapi$beforeKeyPressEvent;
	@Unique
	private Event<ScreenKeyboardEvents.AfterKeyPress> qfapi$afterKeyPressEvent;
	@Unique
	private Event<ScreenKeyboardEvents.AllowKeyRelease> qfapi$allowKeyReleaseEvent;
	@Unique
	private Event<ScreenKeyboardEvents.BeforeKeyRelease> qfapi$beforeKeyReleaseEvent;
	@Unique
	private Event<ScreenKeyboardEvents.AfterKeyRelease> qfapi$afterKeyReleaseEvent;

	// Mouse
	@Unique
	private Event<ScreenMouseEvents.AllowMouseClick> qfapi$allowMouseClickEvent;
	@Unique
	private Event<ScreenMouseEvents.BeforeMouseClick> qfapi$beforeMouseClickEvent;
	@Unique
	private Event<ScreenMouseEvents.AfterMouseClick> qfapi$afterMouseClickEvent;
	@Unique
	private Event<ScreenMouseEvents.AllowMouseRelease> qfapi$allowMouseReleaseEvent;
	@Unique
	private Event<ScreenMouseEvents.BeforeMouseRelease> qfapi$beforeMouseReleaseEvent;
	@Unique
	private Event<ScreenMouseEvents.AfterMouseRelease> qfapi$afterMouseReleaseEvent;
	@Unique
	private Event<ScreenMouseEvents.AllowMouseScroll> qfapi$allowMouseScrollEvent;
	@Unique
	private Event<ScreenMouseEvents.BeforeMouseScroll> qfapi$beforeMouseScrollEvent;
	@Unique
	private Event<ScreenMouseEvents.AfterMouseScroll> qfapi$afterMouseScrollEvent;

	@Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("HEAD"))
	private void beforeInitScreen(MinecraftClient client, int width, int height, CallbackInfo ci) {
		this.beforeInit(client, width, height);
	}

	@Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("TAIL"))
	private void afterInitScreen(MinecraftClient client, int width, int height, CallbackInfo ci) {
		this.afterInit(client, width, height);
	}

	@Inject(method = "resize", at = @At("HEAD"))
	private void beforeResizeScreen(MinecraftClient client, int width, int height, CallbackInfo ci) {
		this.beforeInit(client, width, height);
	}

	@Inject(method = "resize", at = @At("TAIL"))
	private void afterResizeScreen(MinecraftClient client, int width, int height, CallbackInfo ci) {
		this.afterInit(client, width, height);
	}

	@Unique
	private void beforeInit(MinecraftClient client, int width, int height) {
		this.qfapi$removeEvent = ScreenEventFactory.createRemoveEvent();
		this.qfapi$beforeRenderEvent = ScreenEventFactory.createBeforeRenderEvent();
		this.qfapi$afterRenderEvent = ScreenEventFactory.createAfterRenderEvent();
		this.qfapi$beforeTickEvent = ScreenEventFactory.createBeforeTickEvent();
		this.qfapi$afterTickEvent = ScreenEventFactory.createAfterTickEvent();

		// Keyboard
		this.qfapi$allowKeyPressEvent = ScreenEventFactory.createAllowKeyPressEvent();
		this.qfapi$beforeKeyPressEvent = ScreenEventFactory.createBeforeKeyPressEvent();
		this.qfapi$afterKeyPressEvent = ScreenEventFactory.createAfterKeyPressEvent();
		this.qfapi$allowKeyReleaseEvent = ScreenEventFactory.createAllowKeyReleaseEvent();
		this.qfapi$beforeKeyReleaseEvent = ScreenEventFactory.createBeforeKeyReleaseEvent();
		this.qfapi$afterKeyReleaseEvent = ScreenEventFactory.createAfterKeyReleaseEvent();

		// Mouse
		this.qfapi$allowMouseClickEvent = ScreenEventFactory.createAllowMouseClickEvent();
		this.qfapi$beforeMouseClickEvent = ScreenEventFactory.createBeforeMouseClickEvent();
		this.qfapi$afterMouseClickEvent = ScreenEventFactory.createAfterMouseClickEvent();
		this.qfapi$allowMouseReleaseEvent = ScreenEventFactory.createAllowMouseReleaseEvent();
		this.qfapi$beforeMouseReleaseEvent = ScreenEventFactory.createBeforeMouseReleaseEvent();
		this.qfapi$afterMouseReleaseEvent = ScreenEventFactory.createAfterMouseReleaseEvent();
		this.qfapi$allowMouseScrollEvent = ScreenEventFactory.createAllowMouseScrollEvent();
		this.qfapi$beforeMouseScrollEvent = ScreenEventFactory.createBeforeMouseScrollEvent();
		this.qfapi$afterMouseScrollEvent = ScreenEventFactory.createAfterMouseScrollEvent();

		// Activate our Fabric events
		ScreenEventFactory.activateScreen((Screen) (Object) this);

		// Invoke Fabric's one as well since we diverged here
		ScreenEvents.BEFORE_INIT.invoker().beforeInit(client, (Screen) (Object) this, width, height);
	}

	@Unique
	private void afterInit(MinecraftClient client, int width, int height) {
		ScreenEvents.AFTER_INIT.invoker().afterInit(client, (Screen) (Object) this, width, height);
	}

	@Unique
	private <T> Event<T> ensureEventsAreInitialized(Event<T> event) {
		if (event == null) {
			throw new IllegalStateException(String.format("[quilted_fabric_screen_api_v1] The current screen (%s) has not been correctly initialised, please send this crash log to the mod author. This is usually caused by calling setScreen on the wrong thread.", this.getClass().getName()));
		}

		return event;
	}

	@Override
	public Event<ScreenEvents.Remove> fabric_getRemoveEvent() {
		return ensureEventsAreInitialized(this.qfapi$removeEvent);
	}

	@Override
	public Event<ScreenEvents.BeforeTick> fabric_getBeforeTickEvent() {
		return ensureEventsAreInitialized(this.qfapi$beforeTickEvent);
	}

	@Override
	public Event<ScreenEvents.AfterTick> fabric_getAfterTickEvent() {
		return ensureEventsAreInitialized(this.qfapi$afterTickEvent);
	}

	@Override
	public Event<ScreenEvents.BeforeRender> fabric_getBeforeRenderEvent() {
		return ensureEventsAreInitialized(this.qfapi$beforeRenderEvent);
	}

	@Override
	public Event<ScreenEvents.AfterRender> fabric_getAfterRenderEvent() {
		return ensureEventsAreInitialized(this.qfapi$afterRenderEvent);
	}

	// Keyboard

	@Override
	public Event<ScreenKeyboardEvents.AllowKeyPress> fabric_getAllowKeyPressEvent() {
		return ensureEventsAreInitialized(this.qfapi$allowKeyPressEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.BeforeKeyPress> fabric_getBeforeKeyPressEvent() {
		return ensureEventsAreInitialized(this.qfapi$beforeKeyPressEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.AfterKeyPress> fabric_getAfterKeyPressEvent() {
		return ensureEventsAreInitialized(this.qfapi$afterKeyPressEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.AllowKeyRelease> fabric_getAllowKeyReleaseEvent() {
		return ensureEventsAreInitialized(this.qfapi$allowKeyReleaseEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.BeforeKeyRelease> fabric_getBeforeKeyReleaseEvent() {
		return ensureEventsAreInitialized(this.qfapi$beforeKeyReleaseEvent);
	}

	@Override
	public Event<ScreenKeyboardEvents.AfterKeyRelease> fabric_getAfterKeyReleaseEvent() {
		return ensureEventsAreInitialized(this.qfapi$afterKeyReleaseEvent);
	}

	// Mouse

	@Override
	public Event<ScreenMouseEvents.AllowMouseClick> fabric_getAllowMouseClickEvent() {
		return ensureEventsAreInitialized(this.qfapi$allowMouseClickEvent);
	}

	@Override
	public Event<ScreenMouseEvents.BeforeMouseClick> fabric_getBeforeMouseClickEvent() {
		return ensureEventsAreInitialized(this.qfapi$beforeMouseClickEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AfterMouseClick> fabric_getAfterMouseClickEvent() {
		return ensureEventsAreInitialized(this.qfapi$afterMouseClickEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AllowMouseRelease> fabric_getAllowMouseReleaseEvent() {
		return ensureEventsAreInitialized(this.qfapi$allowMouseReleaseEvent);
	}

	@Override
	public Event<ScreenMouseEvents.BeforeMouseRelease> fabric_getBeforeMouseReleaseEvent() {
		return ensureEventsAreInitialized(this.qfapi$beforeMouseReleaseEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AfterMouseRelease> fabric_getAfterMouseReleaseEvent() {
		return ensureEventsAreInitialized(this.qfapi$afterMouseReleaseEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AllowMouseScroll> fabric_getAllowMouseScrollEvent() {
		return ensureEventsAreInitialized(this.qfapi$allowMouseScrollEvent);
	}

	@Override
	public Event<ScreenMouseEvents.BeforeMouseScroll> fabric_getBeforeMouseScrollEvent() {
		return ensureEventsAreInitialized(this.qfapi$beforeMouseScrollEvent);
	}

	@Override
	public Event<ScreenMouseEvents.AfterMouseScroll> fabric_getAfterMouseScrollEvent() {
		return ensureEventsAreInitialized(this.qfapi$afterMouseScrollEvent);
	}
}
