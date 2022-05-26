/*
 * Copyright 2016, 2017, 2018, 2019 FabricMC
 * Copyright 2022 QuiltMC
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

package net.fabricmc.fabric.impl.client.screen;

import java.util.Set;

import org.jetbrains.annotations.ApiStatus;
import org.quiltmc.qsl.base.api.util.TriState;
import it.unimi.dsi.fastutil.objects.ReferenceArraySet;

import net.minecraft.client.gui.screen.Screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenKeyboardEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

/**
 * Factory methods for creating event instances used in {@link ScreenExtensions}.
 */
@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public final class ScreenEventFactory {
	private static final Set<Screen> ACTIVE_SCREENS = new ReferenceArraySet<>(2);

	public static Event<ScreenEvents.Remove> createRemoveEvent() {
		var event = EventFactory.createArrayBacked(ScreenEvents.Remove.class, callbacks -> screen -> {
			for (var callback : callbacks) {
				callback.onRemove(screen);
			}
		});
		return event;
	}

	public static Event<ScreenEvents.BeforeRender> createBeforeRenderEvent() {
		var event = EventFactory.createArrayBacked(ScreenEvents.BeforeRender.class, callbacks -> (screen, matrices, mouseX, mouseY, tickDelta) -> {
			for (var callback : callbacks) {
				callback.beforeRender(screen, matrices, mouseX, mouseY, tickDelta);
			}
		});
		return event;
	}

	public static Event<ScreenEvents.AfterRender> createAfterRenderEvent() {
		var event = EventFactory.createArrayBacked(ScreenEvents.AfterRender.class, callbacks -> (screen, matrices, mouseX, mouseY, tickDelta) -> {
			for (var callback : callbacks) {
				callback.afterRender(screen, matrices, mouseX, mouseY, tickDelta);
			}
		});
		return event;
	}

	public static Event<ScreenEvents.BeforeTick> createBeforeTickEvent() {
		var event = EventFactory.createArrayBacked(ScreenEvents.BeforeTick.class, callbacks -> screen -> {
			for (var callback : callbacks) {
				callback.beforeTick(screen);
			}
		});
		return event;
	}

	public static Event<ScreenEvents.AfterTick> createAfterTickEvent() {
		var event = EventFactory.createArrayBacked(ScreenEvents.AfterTick.class, callbacks -> screen -> {
			for (var callback : callbacks) {
				callback.afterTick(screen);
			}
		});
		return event;
	}

	// Keyboard events

	public static Event<ScreenKeyboardEvents.AllowKeyPress> createAllowKeyPressEvent() {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.AllowKeyPress.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				if (!callback.allowKeyPress(screen, key, scancode, modifiers)) {
					return false;
				}
			}

			return true;
		});
		return event;
	}

	public static Event<ScreenKeyboardEvents.BeforeKeyPress> createBeforeKeyPressEvent() {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.BeforeKeyPress.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				callback.beforeKeyPress(screen, key, scancode, modifiers);
			}
		});
		return event;
	}

	public static Event<ScreenKeyboardEvents.AfterKeyPress> createAfterKeyPressEvent() {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.AfterKeyPress.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				callback.afterKeyPress(screen, key, scancode, modifiers);
			}
		});
		return event;
	}

	public static Event<ScreenKeyboardEvents.AllowKeyRelease> createAllowKeyReleaseEvent() {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.AllowKeyRelease.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				if (!callback.allowKeyRelease(screen, key, scancode, modifiers)) {
					return false;
				}
			}

			return true;
		});
		return event;
	}

	public static Event<ScreenKeyboardEvents.BeforeKeyRelease> createBeforeKeyReleaseEvent() {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.BeforeKeyRelease.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				callback.beforeKeyRelease(screen, key, scancode, modifiers);
			}
		});
		return event;
	}

	public static Event<ScreenKeyboardEvents.AfterKeyRelease> createAfterKeyReleaseEvent() {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.AfterKeyRelease.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				callback.afterKeyRelease(screen, key, scancode, modifiers);
			}
		});
		return event;
	}

	// Mouse Events

	public static Event<ScreenMouseEvents.AllowMouseClick> createAllowMouseClickEvent() {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseClick.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				if (!callback.allowMouseClick(screen, mouseX, mouseY, button)) {
					return false;
				}
			}

			return true;
		});
		return event;
	}

	public static Event<ScreenMouseEvents.BeforeMouseClick> createBeforeMouseClickEvent() {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseClick.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				callback.beforeMouseClick(screen, mouseX, mouseY, button);
			}
		});
		return event;
	}

	public static Event<ScreenMouseEvents.AfterMouseClick> createAfterMouseClickEvent() {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseClick.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				callback.afterMouseClick(screen, mouseX, mouseY, button);
			}
		});
		return event;
	}

	public static Event<ScreenMouseEvents.AllowMouseRelease> createAllowMouseReleaseEvent() {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseRelease.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				if (!callback.allowMouseRelease(screen, mouseX, mouseY, button)) {
					return false;
				}
			}

			return true;
		});
		return event;
	}

	public static Event<ScreenMouseEvents.BeforeMouseRelease> createBeforeMouseReleaseEvent() {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseRelease.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				callback.beforeMouseRelease(screen, mouseX, mouseY, button);
			}
		});
		return event;
	}

	public static Event<ScreenMouseEvents.AfterMouseRelease> createAfterMouseReleaseEvent() {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseRelease.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				callback.afterMouseRelease(screen, mouseX, mouseY, button);
			}
		});
		return event;
	}

	public static Event<ScreenMouseEvents.AllowMouseScroll> createAllowMouseScrollEvent() {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseScroll.class, callbacks -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (var callback : callbacks) {
				if (!callback.allowMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount)) {
					return false;
				}
			}

			return true;
		});
		return event;
	}

	public static Event<ScreenMouseEvents.BeforeMouseScroll> createBeforeMouseScrollEvent() {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseScroll.class, callbacks -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (var callback : callbacks) {
				callback.beforeMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
			}
		});
		return event;
	}

	public static Event<ScreenMouseEvents.AfterMouseScroll> createAfterMouseScrollEvent() {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseScroll.class, callbacks -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (var callback : callbacks) {
				callback.afterMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
			}
		});
		return event;
	}

	public static void activateScreen(Screen screen) {
		ACTIVE_SCREENS.add(screen);
	}

	protected static void initializeCompatEvents() {
		org.quiltmc.qsl.screen.api.client.ScreenEvents.REMOVE.register(screen -> {
			if (ACTIVE_SCREENS.remove(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getRemoveEvent().invoker().onRemove(screen);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenEvents.BEFORE_RENDER.register((screen, matrices, mouseX, mouseY, tickDelta) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getBeforeRenderEvent().invoker().beforeRender(screen, matrices, mouseX, mouseY, tickDelta);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenEvents.AFTER_RENDER.register((screen, matrices, mouseX, mouseY, tickDelta) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getAfterRenderEvent().invoker().afterRender(screen, matrices, mouseX, mouseY, tickDelta);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenEvents.BEFORE_TICK.register(screen -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getBeforeTickEvent().invoker().beforeTick(screen);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenEvents.AFTER_TICK.register(screen -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getAfterTickEvent().invoker().afterTick(screen);
			}
		});

		// Keyboard Events

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.ALLOW_KEY_PRESS.register((screen, key, scancode, modifiers) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				return TriState.fromBoolean(
					ScreenExtensions.getExtensions(screen).fabric_getAllowKeyPressEvent().invoker().allowKeyPress(screen, key, scancode, modifiers)
				);
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.BEFORE_KEY_PRESS.register((screen, key, scancode, modifiers) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getBeforeKeyPressEvent().invoker().beforeKeyPress(screen, key, scancode, modifiers);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.AFTER_KEY_PRESS.register((screen, key, scancode, modifiers) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getAfterKeyPressEvent().invoker().afterKeyPress(screen, key, scancode, modifiers);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.ALLOW_KEY_RELEASE.register((screen, key, scancode, modifiers) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				return TriState.fromBoolean(
					ScreenExtensions.getExtensions(screen).fabric_getAllowKeyReleaseEvent().invoker().allowKeyRelease(screen, key, scancode, modifiers)
				);
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.BEFORE_KEY_RELEASE.register((screen, key, scancode, modifiers) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getBeforeKeyReleaseEvent().invoker().beforeKeyRelease(screen, key, scancode, modifiers);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.AFTER_KEY_RELEASE.register((screen, key, scancode, modifiers) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getAfterKeyReleaseEvent().invoker().afterKeyRelease(screen, key, scancode, modifiers);
			}
		});

		// Mouse Events

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.ALLOW_MOUSE_CLICK.register((screen, mouseX, mouseY, button) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				return TriState.fromBoolean(
					ScreenExtensions.getExtensions(screen).fabric_getAllowMouseClickEvent().invoker().allowMouseClick(screen, mouseX, mouseY, button)
				);
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.BEFORE_MOUSE_CLICK.register((screen, mouseX, mouseY, button) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getBeforeMouseClickEvent().invoker().beforeMouseClick(screen, mouseX, mouseY, button);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.AFTER_MOUSE_CLICK.register((screen, mouseX, mouseY, button) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getAfterMouseClickEvent().invoker().afterMouseClick(screen, mouseX, mouseY, button);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.ALLOW_MOUSE_RELEASE.register((screen, mouseX, mouseY, button) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				return TriState.fromBoolean(
					ScreenExtensions.getExtensions(screen).fabric_getAllowMouseReleaseEvent().invoker().allowMouseRelease(screen, mouseX, mouseY, button)
				);
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.BEFORE_MOUSE_RELEASE.register((screen, mouseX, mouseY, button) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getBeforeMouseReleaseEvent().invoker().beforeMouseRelease(screen, mouseX, mouseY, button);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.AFTER_MOUSE_RELEASE.register((screen, mouseX, mouseY, button) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getAfterMouseReleaseEvent().invoker().afterMouseRelease(screen, mouseX, mouseY, button);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.ALLOW_MOUSE_SCROLL.register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				return TriState.fromBoolean(
					ScreenExtensions.getExtensions(screen).fabric_getAllowMouseScrollEvent().invoker().allowMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount)
				);
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.BEFORE_MOUSE_SCROLL.register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getBeforeMouseScrollEvent().invoker().beforeMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.AFTER_MOUSE_SCROLL.register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			if (ACTIVE_SCREENS.contains(screen)) {
				ScreenExtensions.getExtensions(screen).fabric_getAfterMouseScrollEvent().invoker().afterMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
			}
		});
	}

	private ScreenEventFactory() { }
}
