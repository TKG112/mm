package net.tkg.ModernMayhem.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.config.ClientConfig;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.network.NVGSyncSwitchOffPacket;
import net.tkg.ModernMayhem.server.network.NVGSyncSwitchOnPacket;
import net.tkg.ModernMayhem.server.registry.PacketsRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import software.bernie.geckolib.cache.GeckoLibCache;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.loading.object.BakedAnimations;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class FacewearShaderSync {

    private static final int HIDDEN_DELAY_TICKS = 10;

    private static final int FALLBACK_GRACE_TICKS = 40;

    private static final int GRACE_BUFFER_TICKS = 5;

    private static int mismatchTicks;

    private static Item lastItem;
    private static boolean lastFlippedDown;
    private static int soundCountdown = -1;
    private static boolean soundIsSwitchOn;

    private FacewearShaderSync() {}

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        LocalPlayer player = Minecraft.getInstance().player;
        ItemStack facewear = player == null ? null : CuriosUtil.getFaceWearItem(player);

        if (player == null || facewear == null || facewear.isEmpty()
                || !(facewear.getItem() instanceof GenericSpecialGogglesItem goggles)) {
            reset();
            return;
        }

        boolean flippedDown = GenericSpecialGogglesItem.isNVGOnFace(facewear);
        boolean modelHidden = ClientConfig.HIDE_FIRST_PERSON_GOGGLES.get();

        tickSwitchSound(player, goggles, facewear, flippedDown, modelHidden);

        if (goggles.shouldRenderShader()) {
            tickEffectState(goggles, facewear, flippedDown, modelHidden);
        }
    }

    private static void tickSwitchSound(LocalPlayer player, GenericSpecialGogglesItem goggles,
                                        ItemStack facewear, boolean flippedDown, boolean modelHidden) {
        if (facewear.getItem() != lastItem) {
            lastItem = facewear.getItem();
            lastFlippedDown = flippedDown;
            soundCountdown = -1;
            return;
        }

        if (flippedDown != lastFlippedDown) {
            lastFlippedDown = flippedDown;
            if (modelHidden) {
                soundCountdown = HIDDEN_DELAY_TICKS;
                soundIsSwitchOn = flippedDown;
            }
        }

        if (soundCountdown >= 0 && --soundCountdown < 0) {
            playSwitchSound(player, goggles, soundIsSwitchOn);
        }
    }

    private static void tickEffectState(GenericSpecialGogglesItem goggles, ItemStack facewear,
                                        boolean flippedDown, boolean modelHidden) {
        if (flippedDown == GenericSpecialGogglesItem.getNVGCheck(facewear)) {
            mismatchTicks = 0;
            return;
        }

        int delay = modelHidden
                ? HIDDEN_DELAY_TICKS
                : animationTicks(goggles, facewear, flippedDown) + GRACE_BUFFER_TICKS;

        if (++mismatchTicks < delay) return;
        mismatchTicks = 0;

        if (flippedDown) {
            GenericSpecialGogglesItem.switchOnNVGMode(facewear);
            PacketsRegistryMM.getChannel().sendToServer(new NVGSyncSwitchOnPacket());
        } else {
            GenericSpecialGogglesItem.switchOffNVGMode(facewear);
            PacketsRegistryMM.getChannel().sendToServer(new NVGSyncSwitchOffPacket());
        }
    }

    private static int animationTicks(GenericSpecialGogglesItem goggles, ItemStack facewear, boolean flippingDown) {
        ResourceLocation animationFile =
                goggles.getFirstPersonAnimation(GenericSpecialGogglesItem.hasCoti(facewear));
        if (animationFile == null) return FALLBACK_GRACE_TICKS;

        BakedAnimations baked = GeckoLibCache.getBakedAnimations().get(animationFile);
        if (baked == null) return FALLBACK_GRACE_TICKS;

        Animation animation = baked.getAnimation(flippingDown ? "closing" : "opening");
        if (animation == null) return FALLBACK_GRACE_TICKS;

        return (int) Math.ceil(animation.length());
    }

    private static void playSwitchSound(LocalPlayer player, GenericSpecialGogglesItem goggles, boolean on) {
        Level level = player.level();
        RegistryObject<SoundEvent> sound = on ? goggles.ACTIVATION_SOUND : goggles.DEACTIVATION_SOUND;
        if (sound == null || !sound.isPresent()) return;

        level.playSeededSound(player, player.getX(), player.getY(), player.getZ(),
                sound.get(), SoundSource.NEUTRAL, 1, 1, 0);
    }

    private static void reset() {
        mismatchTicks = 0;
        lastItem = null;
        soundCountdown = -1;
    }
}
