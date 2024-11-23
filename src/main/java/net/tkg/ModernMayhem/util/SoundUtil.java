package net.tkg.ModernMayhem.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.tkg.ModernMayhem.item.GenericNVGGogglesItem;

import java.util.Objects;


public class SoundUtil {

    // This method is used to play a sound at a specific location in the world.
    // It will play the sound on the server side if the world is not client side.
    // Otherwise, it will play the sound locally on the client side.
    public static void playLocationAwareSound(
        Level world,
        double x,
        double y,
        double z,
        ResourceLocation sound,
        SoundSource soundSource,
        float volume,
        float pitch
    ) {
        if (!world.isClientSide()) {
            world.playSound(
                    null,
                    BlockPos.containing(x, y, z),
                    Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(sound)),
                    SoundSource.NEUTRAL,
                    1,
                    1
            );
        } else {
            world.playLocalSound(
                    x,
                    y,
                    z,
                    Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(sound)),
                    SoundSource.NEUTRAL,
                    1,
                    1,
                    false
            );
        }
    }
}
