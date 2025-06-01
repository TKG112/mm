package net.tkg.ModernMayhem.client.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.custom.NVGFirstPersonRenderer;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.registry.SoundRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationProcessor;
import software.bernie.geckolib.core.keyframe.event.data.CustomInstructionKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.SoundKeyframeData;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

public class NVGFirstPersonFakeItem extends Item implements GeoAnimatable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NVGFirstPersonFakeItem() {
        super(new Properties());
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private final NVGFirstPersonRenderer renderer = new NVGFirstPersonRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<NVGFirstPersonFakeItem> controller = new AnimationController<>(this, 0, state -> {
            LocalPlayer player = Minecraft.getInstance().player;
            assert player != null;
            // We limit the animation to only run when the player nvg goggles are fully open or closed
            // This add a cooldown to the animation so it doesn't spam
            AnimationProcessor.QueuedAnimation currentAnim = state.getController().getCurrentAnimation();
            if (currentAnim == null) {
                state.setAnimation(GenericNVGGogglesItem.ANIM_IDLE);
                return PlayState.CONTINUE;
            } else {
                System.out.println(currentAnim.animation().name());
            }
            if (state.isCurrentAnimationStage("opened") || state.isCurrentAnimationStage("closed") || state.isCurrentAnimationStage("idle") || state.isCurrentAnimationStage("")) {
                ItemStack stack = CuriosUtil.getFaceWearItem(player);
                if (stack.getItem() instanceof GenericNVGGogglesItem) {
                    if (GenericNVGGogglesItem.isNVGOnFace(stack)) {
                        state.setAnimation(GenericNVGGogglesItem.ANIM_CLOSE);
                    } else {
                        state.setAnimation(GenericNVGGogglesItem.ANIM_OPEN);
                    }
                }
            }
            if (!(state.isCurrentAnimation(GenericNVGGogglesItem.ANIM_CLOSE) || state.isCurrentAnimation(GenericNVGGogglesItem.ANIM_OPEN) || state.isCurrentAnimation(GenericNVGGogglesItem.ANIM_IDLE))) {
                state.setAnimation(GenericNVGGogglesItem.ANIM_IDLE);
            }
            return PlayState.CONTINUE;
        });

        // Add a custom instruction keyframe handler to handle the NVG effect enabling/disabling
        controller.setCustomInstructionKeyframeHandler(event -> {
            // Check if it's the open animation or the close animation
            CustomInstructionKeyframeData keyframeData = event.getKeyframeData();
            String key = keyframeData.getInstructions();
            LocalPlayer player = Minecraft.getInstance().player;
            System.out.println("Keyframe instruction: \"" + key + "\"");
            ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
            if (player != null) {
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                    if (key.equals("enableNVGEffect;")) {
                        System.out.println("Enabling NVG effect");
                        player.displayClientMessage(Component.literal("NVG Effect Enabled"), true);
                        GenericNVGGogglesItem.switchOnNVGMode(facewearItem);
                    } else if (key.equals("disableNVGEffect;")) {
                        System.out.println("Disabling NVG effect");
                        player.displayClientMessage(Component.literal("NVG Effect Disabled"), true);
                        GenericNVGGogglesItem.switchOffNVGMode(facewearItem);
                    }
                }
            }
        });

        // Add the sounds to the controller
        controller.setSoundKeyframeHandler(event -> {
            SoundKeyframeData soundData = event.getKeyframeData();
            String soundKey = soundData.getSound();
            LocalPlayer player = Minecraft.getInstance().player;
            Level world = Minecraft.getInstance().level;

            if (player != null && world != null) {
                ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem genericNVGGogglesItem) {
                    switch (soundKey) {
                        case "nvg_on" -> {
                            System.out.println("Playing sound on");
                            player.displayClientMessage(Component.literal("NVG Activated"), true);
                            world.playSeededSound(
                                    player,
                                    player.getX(),
                                    player.getY(),
                                    player.getZ(),
                                    genericNVGGogglesItem.ACTIVATION_SOUND.get(),
                                    SoundSource.NEUTRAL,
                                    1,
                                    1,
                                    0
                            );
                        }
                        case "nvg_off" -> {
                            System.out.println("Playing sound off");
                            player.displayClientMessage(Component.literal("NVG Deactivated"), true);
                            world.playSeededSound(
                                    player,
                                    player.getX(),
                                    player.getY(),
                                    player.getZ(),
                                    genericNVGGogglesItem.DEACTIVATION_SOUND.get(),
                                    SoundSource.NEUTRAL,
                                    1,
                                    1,
                                    0
                            );
                        }
                        case "nvg_equip" -> {
                            System.out.println("Playing equip sound");
                            world.playSeededSound(
                                    player,
                                    player.getX(),
                                    player.getY(),
                                    player.getZ(),
                                    SoundRegistryMM.SOUND_NVG_PUT_ON.get(),
                                    SoundSource.NEUTRAL,
                                    1,
                                    1,
                                    0
                            );
                        }
                        case "nvg_unequip" -> {
                            System.out.println("Playing unequip sound");
                            world.playSeededSound(
                                    player,
                                    player.getX(),
                                    player.getY(),
                                    player.getZ(),
                                    SoundRegistryMM.SOUND_NVG_PUT_OFF.get(),
                                    SoundSource.NEUTRAL,
                                    1,
                                    1,
                                    0
                            );
                        }
                        default -> System.err.println("Unknown sound key: " + soundKey);
                    }
                }
            } else System.err.println("Player or world is null, cannot play sound: " + soundKey);
        });

        controllers.add(controller);

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object object) {
        return Minecraft.getInstance().level != null ? Minecraft.getInstance().level.getGameTime() : 0;
    }


}
