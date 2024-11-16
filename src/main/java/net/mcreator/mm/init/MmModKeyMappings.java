
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.mm.init;

import org.lwjgl.glfw.GLFW;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;

import net.mcreator.mm.network.ToggleNVGMessage;
import net.mcreator.mm.network.OpenRigMessage;
import net.mcreator.mm.network.OpenBackpackMessage;
import net.mcreator.mm.network.IncreaseTubeGainMessage;
import net.mcreator.mm.network.DecreaseTubeGainMessage;
import net.mcreator.mm.MmMod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class MmModKeyMappings {
	public static final KeyMapping TOGGLE_NVG = new KeyMapping("key.mm.toggle_nvg", GLFW.GLFW_KEY_N, "key.categories.mm") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				MmMod.PACKET_HANDLER.sendToServer(new ToggleNVGMessage(0, 0));
				ToggleNVGMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping INCREASE_TUBE_GAIN = new KeyMapping("key.mm.increase_tube_gain", GLFW.GLFW_KEY_UP, "key.categories.mm") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				MmMod.PACKET_HANDLER.sendToServer(new IncreaseTubeGainMessage(0, 0));
				IncreaseTubeGainMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping DECREASE_TUBE_GAIN = new KeyMapping("key.mm.decrease_tube_gain", GLFW.GLFW_KEY_DOWN, "key.categories.mm") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				MmMod.PACKET_HANDLER.sendToServer(new DecreaseTubeGainMessage(0, 0));
				DecreaseTubeGainMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping OPEN_BACKPACK = new KeyMapping("key.mm.open_backpack", GLFW.GLFW_KEY_B, "key.categories.mm") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				MmMod.PACKET_HANDLER.sendToServer(new OpenBackpackMessage(0, 0));
				OpenBackpackMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};
	public static final KeyMapping OPEN_RIG = new KeyMapping("key.mm.open_rig", GLFW.GLFW_KEY_0, "key.categories.mm") {
		private boolean isDownOld = false;

		@Override
		public void setDown(boolean isDown) {
			super.setDown(isDown);
			if (isDownOld != isDown && isDown) {
				MmMod.PACKET_HANDLER.sendToServer(new OpenRigMessage(0, 0));
				OpenRigMessage.pressAction(Minecraft.getInstance().player, 0, 0);
			}
			isDownOld = isDown;
		}
	};

	@SubscribeEvent
	public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
		event.register(TOGGLE_NVG);
		event.register(INCREASE_TUBE_GAIN);
		event.register(DECREASE_TUBE_GAIN);
		event.register(OPEN_BACKPACK);
		event.register(OPEN_RIG);
	}

	@Mod.EventBusSubscriber({Dist.CLIENT})
	public static class KeyEventListener {
		@SubscribeEvent
		public static void onClientTick(TickEvent.ClientTickEvent event) {
			if (Minecraft.getInstance().screen == null) {
				TOGGLE_NVG.consumeClick();
				INCREASE_TUBE_GAIN.consumeClick();
				DECREASE_TUBE_GAIN.consumeClick();
				OPEN_BACKPACK.consumeClick();
				OPEN_RIG.consumeClick();
			}
		}
	}
}
