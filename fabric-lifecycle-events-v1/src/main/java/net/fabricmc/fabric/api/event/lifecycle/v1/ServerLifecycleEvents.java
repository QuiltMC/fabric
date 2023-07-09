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

package net.fabricmc.fabric.api.event.lifecycle.v1;

import org.quiltmc.qsl.resource.loader.api.ResourceLoaderEvents;

import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.impl.base.event.QuiltCompatEvent;

@Deprecated
public final class ServerLifecycleEvents {
	private ServerLifecycleEvents() {
	}

	/**
	 * Called when a Minecraft server is starting.
	 *
	 * <p>This occurs before the {@link PlayerManager player manager} and any worlds are loaded.
	 */
	public static final Event<ServerStarting> SERVER_STARTING = QuiltCompatEvent.fromQuilt(
			org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents.STARTING,
			serverStarting -> serverStarting::onServerStarting,
			invokerGetter -> server -> invokerGetter.get().startingServer(server)
	);

	/**
	 * Called when a Minecraft server has started and is about to tick for the first time.
	 *
	 * <p>At this stage, all worlds are live.
	 */
	public static final Event<ServerStarted> SERVER_STARTED = QuiltCompatEvent.fromQuilt(
			org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents.READY,
			serverStarted -> serverStarted::onServerStarted,
			invokerGetter -> server -> invokerGetter.get().readyServer(server)
	);

	/**
	 * Called when a Minecraft server has started shutting down.
	 * This occurs before the server's network channel is closed and before any players are disconnected.
	 *
	 * <p>For example, an integrated server will begin stopping, but its client may continue to run.
	 *
	 * <p>All worlds are still present and can be modified.
	 */
	public static final Event<ServerStopping> SERVER_STOPPING = QuiltCompatEvent.fromQuilt(
			org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents.STOPPING,
			serverStopping -> serverStopping::onServerStopping,
			invokerGetter -> server -> invokerGetter.get().stoppingServer(server)
	);

	/**
	 * Called when a Minecraft server has stopped.
	 * All worlds have been closed and all (block)entities and players have been unloaded.
	 *
	 * <p>For example, an {@link net.fabricmc.api.EnvType#CLIENT integrated server} will begin stopping, but its client may continue to run.
	 * Meanwhile, for a {@link net.fabricmc.api.EnvType#SERVER dedicated server}, this will be the last event called.
	 */
	public static final Event<ServerStopped> SERVER_STOPPED = QuiltCompatEvent.fromQuilt(
			org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents.STOPPED,
			serverStopped -> serverStopped::onServerStopped,
			invokerGetter -> server -> invokerGetter.get().exitServer(server)
	);

	/**
	 * Called when a Minecraft server is about to send tag and recipe data to a player.
	 * @see SyncDataPackContents
	 */
	public static final Event<SyncDataPackContents> SYNC_DATA_PACK_CONTENTS = EventFactory.createArrayBacked(SyncDataPackContents.class, callbacks -> (player, joined) -> {
		for (SyncDataPackContents callback : callbacks) {
			callback.onSyncDataPackContents(player, joined);
		}
	});

	/**
	 * Called before a Minecraft server reloads data packs.
	 */
	public static final Event<StartDataPackReload> START_DATA_PACK_RELOAD = QuiltCompatEvent.fromQuilt(
			ResourceLoaderEvents.START_DATA_PACK_RELOAD,
			startDataPackReload -> (server, oldResourceManager) -> {
				if (server != null) {
					// Fabric only triggers it at reloads, not startup.
					startDataPackReload.startDataPackReload(server, null);
				}
			},
			invokerGetter -> (server, resourceManager) -> invokerGetter.get().onStartDataPackReload(server, resourceManager)
	);

	/**
	 * Called after a Minecraft server has reloaded data packs.
	 *
	 * <p>If reloading data packs was unsuccessful, the current data packs will be kept.
	 */
	public static final Event<EndDataPackReload> END_DATA_PACK_RELOAD = QuiltCompatEvent.fromQuilt(
			ResourceLoaderEvents.END_DATA_PACK_RELOAD,
			endDataPackReload -> (server, resourceManager, error) -> {
				if (server != null) {
					// Fabric only triggers it at reloads, not startup.
					// Also using an actual cast, unlike Fabric.
					endDataPackReload.endDataPackReload(server, (LifecycledResourceManager) resourceManager, error == null);
				}
			},
			invokerGetter -> (server, resourceManager, success) -> {
				invokerGetter.get().onEndDataPackReload(server, resourceManager,
						success ? null : new RuntimeException("Unknown error"));
			}
	);

	@FunctionalInterface
	public interface ServerStarting {
		void onServerStarting(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStarted {
		void onServerStarted(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStopping {
		void onServerStopping(MinecraftServer server);
	}

	@FunctionalInterface
	public interface ServerStopped {
		void onServerStopped(MinecraftServer server);
	}

	@FunctionalInterface
	public interface SyncDataPackContents {
		/**
		 * Called right before tags and recipes are sent to a player,
		 * either because the player joined, or because the server reloaded resources.
		 * The {@linkplain MinecraftServer#getResourceManager() server resource manager} is up-to-date when this is called.
		 *
		 * <p>For example, this event can be used to sync data loaded with custom resource reloaders.
		 *
		 * @param player Player to which the data is being sent.
		 * @param joined True if the player is joining the server, false if the server finished a successful resource reload.
		 */
		void onSyncDataPackContents(ServerPlayerEntity player, boolean joined);
	}

	@FunctionalInterface
	public interface StartDataPackReload {
		void startDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager);
	}

	@FunctionalInterface
	public interface EndDataPackReload {
		/**
		 * Called after data packs on a Minecraft server have been reloaded.
		 *
		 * <p>If the reload was not successful, the old data packs will be kept.
		 *
		 * @param server          the server
		 * @param resourceManager the resource manager
		 * @param success         if the reload was successful
		 */
		void endDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager, boolean success);
	}
}
