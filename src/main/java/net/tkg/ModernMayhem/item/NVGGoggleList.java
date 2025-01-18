package net.tkg.ModernMayhem.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.registry.SoundRegistryMM;
import net.tkg.ModernMayhem.util.NVGConfigs;

public enum NVGGoggleList {

    BLACK_GPNVG(
            NVGConfigs.WHITE_PHOSPHOR_GPVNG,
            2,
            SoundRegistryMM.SOUND_NVG_ON,
            SoundRegistryMM.SOUND_NVG_OFF,
            0,
            0
    ),
    TAN_GPNVG(
            NVGConfigs.GREEN_PHOSPHOR_GPVNG,
            3,
            SoundRegistryMM.SOUND_NVG_ON,
            SoundRegistryMM.SOUND_NVG_OFF,
            0,
            1
    ),
    GAMER_GPNVG(
            NVGConfigs.WHITE_PHOSPHOR_GPVNG,
            2,
            SoundRegistryMM.SOUND_NVG_ON,
            SoundRegistryMM.SOUND_NVG_OFF,
            0,
            99
    ),
    BLACK_NVG21(
            NVGConfigs.WHITE_PHOSPHOR_NVG21,
            1,
            SoundRegistryMM.SOUND_NVG_ON,
            SoundRegistryMM.SOUND_NVG_OFF,
            1,
            0
    ),
    TAN_NVG21(
            NVGConfigs.GREEN_PHOSPHOR_NVG21,
            1,
            SoundRegistryMM.SOUND_NVG_ON,
            SoundRegistryMM.SOUND_NVG_OFF,
            1,
            1
    ),
    GREEN_NVG21(
            NVGConfigs.GREEN_PHOSPHOR_NVG21,
            1,
            SoundRegistryMM.SOUND_NVG_ON,
            SoundRegistryMM.SOUND_NVG_OFF,
            1,
            2
    );

    private final GenericNVGGogglesItem.NVGConfig[] configs;
    private final int configIndex;
    private final RegistryObject<SoundEvent> activationSound;
    private final RegistryObject<SoundEvent> deactivationSound;
    private final int Type;
    private final int Variant;

    NVGGoggleList(GenericNVGGogglesItem.NVGConfig[] pConfigs, int pConfigIndex, RegistryObject<SoundEvent> pActivationSound, RegistryObject<SoundEvent> pDeactivationSound, int Type , int Variant) {
        this.configs = pConfigs;
        this.configIndex = pConfigIndex;
        this.activationSound = pActivationSound;
        this.deactivationSound = pDeactivationSound;
        this.Type = Type;
        this.Variant = Variant;
    }

    public GenericNVGGogglesItem.NVGConfig[] getConfigs() {
        return this.configs;
    }

    public int getConfigIndex() {
        return this.configIndex;
    }

    public RegistryObject<SoundEvent> getActivationSound() {
        return this.activationSound;
    }

    public RegistryObject<SoundEvent> getDeactivationSound() {
        return this.deactivationSound;
    }

    public int getType() {
        return this.Type;
    }

    public int getVariant() {
        return this.Variant;
    }
}
