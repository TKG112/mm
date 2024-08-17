package net.mcreator.mm.init;

import top.theillusivec4.curios.api.SlotTypeMessage;

import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.resources.ResourceLocation;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MmModCustomCuriosSlots {
	@SubscribeEvent
	public static void enqueueIMC(final InterModEnqueueEvent event) {
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("facewear").icon(new ResourceLocation("curios:slot/empty_armor_slot_goggles")).size(1).build());
		InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("earwear").icon(new ResourceLocation("curios:slot/empty_armor_slot_headset")).size(1).build());
	}
}
