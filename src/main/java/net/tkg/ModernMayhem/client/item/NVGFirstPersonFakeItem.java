package net.tkg.ModernMayhem.client.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.event.RenderNVGFirstPerson;
import net.tkg.ModernMayhem.client.renderer.custom.NVGFirstPersonRenderer;
import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;
import net.tkg.ModernMayhem.server.network.NVGSyncSwitchOffPacket;
import net.tkg.ModernMayhem.server.network.NVGSyncSwitchOnPacket;
import net.tkg.ModernMayhem.server.registry.PacketsRegistryMM;
import net.tkg.ModernMayhem.server.registry.SoundRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationProcessor;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.data.CustomInstructionKeyframeData;
import software.bernie.geckolib.core.keyframe.event.data.SoundKeyframeData;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.function.Consumer;

/**
 * This item is used to render the NVG goggles in first person view.
 * It is a fake item that is not actually used in the game, but it is used to render the NVG goggles in first person view.
 * It uses the {@link NVGFirstPersonRenderer} to render the goggles in first person view.
 * Combined with the event {@link net.tkg.ModernMayhem.client.event.RenderNVGFirstPerson} to handle the rendering of the goggles in first person view.
 */
public class NVGFirstPersonFakeItem extends Item implements GeoAnimatable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public static final NVGFirstPersonRenderer NVG_FIRST_PERSON_RENDERER = new NVGFirstPersonRenderer();

    // Complementary animations for the NVG goggles
    public static final RawAnimation ANIM_OPENED = RawAnimation.begin().thenLoop("opened");
    public static final RawAnimation ANIM_CLOSED = RawAnimation.begin().thenLoop("closed");

    public NVGFirstPersonFakeItem() {
        super(new Properties());
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private NVGFirstPersonRenderer renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null) {
                    this.renderer = new NVGFirstPersonRenderer();
                }
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
                // if the current animation is null
                // we check if the player has NVG goggles equipped and set the animation accordingly
                if (CuriosUtil.hasNVGEquipped(player)) {
                    ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
                    if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                        if (GenericNVGGogglesItem.isNVGOnFace(facewearItem)) {
                            state.setAnimation(ANIM_CLOSED);
                        } else {
                            state.setAnimation(ANIM_OPENED);
                        }
                    } else {
                        state.setAnimation(GenericNVGGogglesItem.ANIM_IDLE);
                    }
                } else {
                    state.setAnimation(GenericNVGGogglesItem.ANIM_IDLE);
                }
                return PlayState.CONTINUE;
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
            RenderNVGFirstPerson.shouldRenderLeftArm = !(state.isCurrentAnimationStage("opening") || state.isCurrentAnimationStage("closing")); // Prevent the player from rendering a third arm when the NVG animation shows the left arm
            return PlayState.CONTINUE;
        });

        // Add a custom instruction keyframe handler to handle the NVG effect enabling/disabling
        controller.setCustomInstructionKeyframeHandler(event -> {
            // Check if it's the open animation or the close animation
            CustomInstructionKeyframeData keyframeData = event.getKeyframeData();
            String key = keyframeData.getInstructions();
            LocalPlayer player = Minecraft.getInstance().player;
            ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
            if (player != null) {
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                    // We switch on the NVG mode on the client side and then on the server side.
                    // This is done to avoid the delay of the server response.
                    // Since the server will synchronize the state later.
                    if (key.equals("enableNVGEffect;")) {
                        GenericNVGGogglesItem.switchOnNVGMode(facewearItem);
                        PacketsRegistryMM.getChannel().sendToServer(new NVGSyncSwitchOnPacket());
                    } else if (key.equals("disableNVGEffect;")) {
                        GenericNVGGogglesItem.switchOffNVGMode(facewearItem);
                        PacketsRegistryMM.getChannel().sendToServer(new NVGSyncSwitchOffPacket());
                    }
                }
            }
        });

        // Add a custom instruction keyframe handler to handle the NVG effect enabling/disabling
        controller.setCustomInstructionKeyframeHandler(event -> {
            // Check if it's the open animation or the close animation
            CustomInstructionKeyframeData keyframeData = event.getKeyframeData();
            String key = keyframeData.getInstructions();
            LocalPlayer player = Minecraft.getInstance().player;
            ItemStack facewearItem = CuriosUtil.getFaceWearItem(player);
            if (player != null) {
                if (facewearItem.getItem() instanceof GenericNVGGogglesItem) {
                    // We switch on the NVG mode on the client side and then on the server side.
                    // This is done to avoid the delay of the server response.
                    // Since the server will synchronize the state later.
                    if (key.equals("enableNVGEffect;")) {
                        GenericNVGGogglesItem.switchOnNVGMode(facewearItem);
                        PacketsRegistryMM.getChannel().sendToServer(new NVGSyncSwitchOnPacket());
                    } else if (key.equals("disableNVGEffect;")) {
                        GenericNVGGogglesItem.switchOffNVGMode(facewearItem);
                        PacketsRegistryMM.getChannel().sendToServer(new NVGSyncSwitchOffPacket());
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
                            //player.displayClientMessage(Component.literal("NVG Activated"), true);
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
                            //player.displayClientMessage(Component.literal("NVG Deactivated"), true);
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
