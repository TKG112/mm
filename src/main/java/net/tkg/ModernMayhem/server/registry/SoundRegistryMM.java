package net.tkg.ModernMayhem.server.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class SoundRegistryMM {

    public static final DeferredRegister<SoundEvent> MOD_SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ModernMayhemMod.ID);

    public static RegistryObject<SoundEvent> SOUND_NVG_ON = registerSoundsEvent("sound_nvg_on");
    public static RegistryObject<SoundEvent> SOUND_NVG_OFF = registerSoundsEvent("sound_nvg_off");
    public static RegistryObject<SoundEvent> SMALL_CLICK = registerSoundsEvent("small_click");

    public static void init(IEventBus eventBus) {
        MOD_SOUNDS.register(eventBus);
    }

    private static RegistryObject<SoundEvent> registerSoundsEvent(String name) {
        return MOD_SOUNDS.register(name, () -> SoundEvent.createVariableRangeEvent((fromNamespaceAndPath(ModernMayhemMod.ID, name))));

    }
}
