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

import java.util.List;

import org.jetbrains.annotations.ApiStatus;
import org.quiltmc.qsl.base.api.util.TriState;
import org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.AfterKeyPress;
import org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.BeforeKeyPress;
import org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.BeforeKeyRelease;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;

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
	// These lists will always have keep holding events, preventing them from being GC'd! This is bad!
	// FIXME - Find a solution for this
	private static final List<FabricScreenEventHolder<ScreenEvents.Remove>> REMOVE_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenEvents.BeforeRender>> BEFORE_RENDER_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenEvents.AfterRender>> AFTER_RENDER_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenEvents.BeforeTick>> BEFORE_TICK_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenEvents.AfterTick>> AFTER_TICK_EVENTS = new ReferenceArrayList<>();
	// Keyboard Events
	private static final List<FabricScreenEventHolder<ScreenKeyboardEvents.AllowKeyPress>> ALLOW_KEY_PRESS_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenKeyboardEvents.BeforeKeyPress>> BEFORE_KEY_PRESS_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenKeyboardEvents.AfterKeyPress>> AFTER_KEY_PRESS_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenKeyboardEvents.AllowKeyRelease>> ALLOW_KEY_RELEASE_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenKeyboardEvents.BeforeKeyRelease>> BEFORE_KEY_RELEASE_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenKeyboardEvents.AfterKeyRelease>> AFTER_KEY_RELEASE_EVENTS = new ReferenceArrayList<>();
	// Mouse Events
	private static final List<FabricScreenEventHolder<ScreenMouseEvents.AllowMouseClick>> ALLOW_MOUSE_CLICK_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenMouseEvents.BeforeMouseClick>> BEFORE_MOUSE_CLICK_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenMouseEvents.AfterMouseClick>> AFTER_MOUSE_CLICK_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenMouseEvents.AllowMouseRelease>> ALLOW_MOUSE_RELEASE_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenMouseEvents.BeforeMouseRelease>> BEFORE_MOUSE_RELEASE_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenMouseEvents.AfterMouseRelease>> AFTER_MOUSE_RELEASE_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenMouseEvents.AllowMouseScroll>> ALLOW_MOUSE_SCROLL_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenMouseEvents.BeforeMouseScroll>> BEFORE_MOUSE_SCROLL_EVENTS = new ReferenceArrayList<>();
	private static final List<FabricScreenEventHolder<ScreenMouseEvents.AfterMouseScroll>> AFTER_MOUSE_SCROLL_EVENTS = new ReferenceArrayList<>();

	public static Event<ScreenEvents.Remove> createRemoveEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenEvents.Remove.class, callbacks -> screen -> {
			for (var callback : callbacks) {
				callback.onRemove(screen);
			}
		});
		REMOVE_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenEvents.BeforeRender> createBeforeRenderEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenEvents.BeforeRender.class, callbacks -> (screen, matrices, mouseX, mouseY, tickDelta) -> {
			for (var callback : callbacks) {
				callback.beforeRender(screen, matrices, mouseX, mouseY, tickDelta);
			}
		});
		BEFORE_RENDER_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenEvents.AfterRender> createAfterRenderEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenEvents.AfterRender.class, callbacks -> (screen, matrices, mouseX, mouseY, tickDelta) -> {
			for (var callback : callbacks) {
				callback.afterRender(screen, matrices, mouseX, mouseY, tickDelta);
			}
		});
		AFTER_RENDER_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenEvents.BeforeTick> createBeforeTickEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenEvents.BeforeTick.class, callbacks -> screen -> {
			for (var callback : callbacks) {
				callback.beforeTick(screen);
			}
		});
		BEFORE_TICK_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenEvents.AfterTick> createAfterTickEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenEvents.AfterTick.class, callbacks -> screen -> {
			for (var callback : callbacks) {
				callback.afterTick(screen);
			}
		});
		AFTER_TICK_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	// Keyboard events

	public static Event<ScreenKeyboardEvents.AllowKeyPress> createAllowKeyPressEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.AllowKeyPress.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				if (!callback.allowKeyPress(screen, key, scancode, modifiers)) {
					return false;
				}
			}

			return true;
		});
		ALLOW_KEY_PRESS_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenKeyboardEvents.BeforeKeyPress> createBeforeKeyPressEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.BeforeKeyPress.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				((BeforeKeyPress) callback).beforeKeyPress(screen, key, scancode, modifiers);
			}
		});
		BEFORE_KEY_PRESS_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenKeyboardEvents.AfterKeyPress> createAfterKeyPressEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.AfterKeyPress.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				((AfterKeyPress) callback).afterKeyPress(screen, key, scancode, modifiers);
			}
		});
		AFTER_KEY_PRESS_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenKeyboardEvents.AllowKeyRelease> createAllowKeyReleaseEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.AllowKeyRelease.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				if (!callback.allowKeyRelease(screen, key, scancode, modifiers)) {
					return false;
				}
			}

			return true;
		});
		ALLOW_KEY_RELEASE_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenKeyboardEvents.BeforeKeyRelease> createBeforeKeyReleaseEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.BeforeKeyRelease.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				((BeforeKeyRelease) callback).beforeKeyRelease(screen, key, scancode, modifiers);
			}
		});
		BEFORE_KEY_RELEASE_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenKeyboardEvents.AfterKeyRelease> createAfterKeyReleaseEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenKeyboardEvents.AfterKeyRelease.class, callbacks -> (screen, key, scancode, modifiers) -> {
			for (var callback : callbacks) {
				callback.afterKeyRelease(screen, key, scancode, modifiers);
			}
		});
		AFTER_KEY_RELEASE_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	// Mouse Events

	public static Event<ScreenMouseEvents.AllowMouseClick> createAllowMouseClickEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseClick.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				if (!callback.allowMouseClick(screen, mouseX, mouseY, button)) {
					return false;
				}
			}

			return true;
		});
		ALLOW_MOUSE_CLICK_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenMouseEvents.BeforeMouseClick> createBeforeMouseClickEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseClick.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				callback.beforeMouseClick(screen, mouseX, mouseY, button);
			}
		});
		BEFORE_MOUSE_CLICK_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenMouseEvents.AfterMouseClick> createAfterMouseClickEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseClick.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				callback.afterMouseClick(screen, mouseX, mouseY, button);
			}
		});
		AFTER_MOUSE_CLICK_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenMouseEvents.AllowMouseRelease> createAllowMouseReleaseEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseRelease.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				if (!callback.allowMouseRelease(screen, mouseX, mouseY, button)) {
					return false;
				}
			}

			return true;
		});
		ALLOW_MOUSE_RELEASE_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenMouseEvents.BeforeMouseRelease> createBeforeMouseReleaseEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseRelease.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				callback.beforeMouseRelease(screen, mouseX, mouseY, button);
			}
		});
		BEFORE_MOUSE_RELEASE_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenMouseEvents.AfterMouseRelease> createAfterMouseReleaseEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseRelease.class, callbacks -> (screen, mouseX, mouseY, button) -> {
			for (var callback : callbacks) {
				callback.afterMouseRelease(screen, mouseX, mouseY, button);
			}
		});
		AFTER_MOUSE_RELEASE_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenMouseEvents.AllowMouseScroll> createAllowMouseScrollEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AllowMouseScroll.class, callbacks -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (var callback : callbacks) {
				if (!callback.allowMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount)) {
					return false;
				}
			}

			return true;
		});
		ALLOW_MOUSE_SCROLL_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenMouseEvents.BeforeMouseScroll> createBeforeMouseScrollEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.BeforeMouseScroll.class, callbacks -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (var callback : callbacks) {
				callback.beforeMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
			}
		});
		BEFORE_MOUSE_SCROLL_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	public static Event<ScreenMouseEvents.AfterMouseScroll> createAfterMouseScrollEvent(Class<? extends Screen> screenClass) {
		var event = EventFactory.createArrayBacked(ScreenMouseEvents.AfterMouseScroll.class, callbacks -> (screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (var callback : callbacks) {
				callback.afterMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
			}
		});
		AFTER_MOUSE_SCROLL_EVENTS.add(new FabricScreenEventHolder<>(screenClass, event));
		return event;
	}

	private ScreenEventFactory() {
		org.quiltmc.qsl.screen.api.client.ScreenEvents.REMOVE.register(screen -> {
			for (FabricScreenEventHolder<ScreenEvents.Remove> holder : REMOVE_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().onRemove(screen);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenEvents.BEFORE_RENDER.register((screen, matrices, mouseX, mouseY, tickDelta) -> {
			for (FabricScreenEventHolder<ScreenEvents.BeforeRender> holder : BEFORE_RENDER_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().beforeRender(screen, matrices, mouseX, mouseY, tickDelta);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenEvents.AFTER_RENDER.register((screen, matrices, mouseX, mouseY, tickDelta) -> {
			for (FabricScreenEventHolder<ScreenEvents.AfterRender> holder : AFTER_RENDER_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().afterRender(screen, matrices, mouseX, mouseY, tickDelta);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenEvents.BEFORE_TICK.register(screen -> {
			for (FabricScreenEventHolder<ScreenEvents.BeforeTick> holder : BEFORE_TICK_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().beforeTick(screen);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenEvents.AFTER_TICK.register(screen -> {
			for (FabricScreenEventHolder<ScreenEvents.AfterTick> holder : AFTER_TICK_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().afterTick(screen);
				}
			}
		});

		// Keyboard Events

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.ALLOW_KEY_PRESS.register((screen, key, scancode, modifiers) -> {
			for (FabricScreenEventHolder<ScreenKeyboardEvents.AllowKeyPress> holder : ALLOW_KEY_PRESS_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					return TriState.fromBoolean(holder.event.invoker().allowKeyPress(screen, key, scancode, modifiers));
				}
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.BEFORE_KEY_PRESS.register((screen, key, scancode, modifiers) -> {
			for (FabricScreenEventHolder<ScreenKeyboardEvents.BeforeKeyPress> holder : BEFORE_KEY_PRESS_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().beforeKeyPress(screen, key, scancode, modifiers);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.AFTER_KEY_PRESS.register((screen, key, scancode, modifiers) -> {
			for (FabricScreenEventHolder<ScreenKeyboardEvents.AfterKeyPress> holder : AFTER_KEY_PRESS_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().afterKeyPress(screen, key, scancode, modifiers);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.ALLOW_KEY_RELEASE.register((screen, key, scancode, modifiers) -> {
			for (FabricScreenEventHolder<ScreenKeyboardEvents.AllowKeyRelease> holder : ALLOW_KEY_RELEASE_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					return TriState.fromBoolean(holder.event.invoker().allowKeyRelease(screen, key, scancode, modifiers));
				}
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.BEFORE_KEY_RELEASE.register((screen, key, scancode, modifiers) -> {
			for (FabricScreenEventHolder<ScreenKeyboardEvents.BeforeKeyRelease> holder : BEFORE_KEY_RELEASE_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().beforeKeyRelease(screen, key, scancode, modifiers);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenKeyboardEvents.AFTER_KEY_RELEASE.register((screen, key, scancode, modifiers) -> {
			for (FabricScreenEventHolder<ScreenKeyboardEvents.AfterKeyRelease> holder : AFTER_KEY_RELEASE_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().afterKeyRelease(screen, key, scancode, modifiers);
				}
			}
		});

		// Mouse Events

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.ALLOW_MOUSE_CLICK.register((screen, mouseX, mouseY, button) -> {
			for (FabricScreenEventHolder<ScreenMouseEvents.AllowMouseClick> holder : ALLOW_MOUSE_CLICK_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					return TriState.fromBoolean(holder.event.invoker().allowMouseClick(screen, mouseX, mouseY, button));
				}
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.BEFORE_MOUSE_CLICK.register((screen, mouseX, mouseY, button) -> {
			for (FabricScreenEventHolder<ScreenMouseEvents.BeforeMouseClick> holder : BEFORE_MOUSE_CLICK_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().beforeMouseClick(screen, mouseX, mouseY, button);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.AFTER_MOUSE_CLICK.register((screen, mouseX, mouseY, button) -> {
			for (FabricScreenEventHolder<ScreenMouseEvents.AfterMouseClick> holder : AFTER_MOUSE_CLICK_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().afterMouseClick(screen, mouseX, mouseY, button);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.ALLOW_MOUSE_RELEASE.register((screen, mouseX, mouseY, button) -> {
			for (FabricScreenEventHolder<ScreenMouseEvents.AllowMouseRelease> holder : ALLOW_MOUSE_RELEASE_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					return TriState.fromBoolean(holder.event.invoker().allowMouseRelease(screen, mouseX, mouseY, button));
				}
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.BEFORE_MOUSE_RELEASE.register((screen, mouseX, mouseY, button) -> {
			for (FabricScreenEventHolder<ScreenMouseEvents.BeforeMouseRelease> holder : BEFORE_MOUSE_RELEASE_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().beforeMouseRelease(screen, mouseX, mouseY, button);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.AFTER_MOUSE_RELEASE.register((screen, mouseX, mouseY, button) -> {
			for (FabricScreenEventHolder<ScreenMouseEvents.AfterMouseRelease> holder : AFTER_MOUSE_RELEASE_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().afterMouseRelease(screen, mouseX, mouseY, button);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.ALLOW_MOUSE_SCROLL.register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (FabricScreenEventHolder<ScreenMouseEvents.AllowMouseScroll> holder : ALLOW_MOUSE_SCROLL_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					return TriState.fromBoolean(holder.event.invoker().allowMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount));
				}
			}

			return TriState.DEFAULT;
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.BEFORE_MOUSE_SCROLL.register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (FabricScreenEventHolder<ScreenMouseEvents.BeforeMouseScroll> holder : BEFORE_MOUSE_SCROLL_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().beforeMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
				}
			}
		});

		org.quiltmc.qsl.screen.api.client.ScreenMouseEvents.AFTER_MOUSE_SCROLL.register((screen, mouseX, mouseY, horizontalAmount, verticalAmount) -> {
			for (FabricScreenEventHolder<ScreenMouseEvents.AfterMouseScroll> holder : AFTER_MOUSE_SCROLL_EVENTS) {
				if (holder.isTargetScreen(screen)) {
					holder.event.invoker().afterMouseScroll(screen, mouseX, mouseY, horizontalAmount, verticalAmount);
				}
			}
		});
	}

	public record FabricScreenEventHolder<T>(Class<? extends Screen> screenClass, Event<T> event) {
		public boolean isTargetScreen(Screen screen) {
			return screen.getClass() == screenClass;
		}
	}
}
