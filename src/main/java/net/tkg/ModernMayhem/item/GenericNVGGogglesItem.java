package net.tkg.ModernMayhem.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class GenericNVGGogglesItem extends Item implements GeoItem, ICurioItem {
    private AnimatableInstanceCache cache = null;
    private NVGConfig config;
    private String animationprocedure = "Idle";
    public ResourceLocation ACTIVATION_SOUND = null;
    public ResourceLocation DEACTIVATION_SOUND = null;

    // Animations
    private static final RawAnimation ANIM_IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation ANIM_OPEN = RawAnimation.begin().thenPlayAndHold("opening").thenLoop("opened");
    private static final RawAnimation ANIM_CLOSE = RawAnimation.begin().thenPlayAndHold("closing").thenLoop("closed");


    public GenericNVGGogglesItem(NVGConfig pConfig) {
        super(new Item.Properties().stacksTo(1).durability(0));
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.config = pConfig;
    }

    public GenericNVGGogglesItem(NVGConfig pConfig, ResourceLocation pActivationSound, ResourceLocation pDeactivationSound) {
        super(new Item.Properties().stacksTo(1).durability(0));
        this.cache = GeckoLibUtil.createInstanceCache(this);
        this.config = pConfig;
        this.ACTIVATION_SOUND = pActivationSound;
        this.DEACTIVATION_SOUND = pDeactivationSound;
    }

    public NVGConfig getConfig() {
        return this.config;
    }

    private CompoundTag checkAndGetTag(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        return stack.getTag();
    }

    private void updateNVGMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("NvgCheck")) {
            boolean nvgCheck = tag.getBoolean("NvgCheck");
            tag.putInt("nvg_mode", nvgCheck ? 1 : 0);
        } else {
            tag.putInt("nvg_mode", 0);
        }
        stack.setTag(tag);
    }

    public static int getNVGMode(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("nvg_mode")) {
            return tag.getInt("nvg_mode");
        }
        return 0;
    }

    public static void switchNVGMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("NvgCheck")) {
            boolean nvgCheck = tag.getBoolean("NvgCheck");
            tag.putBoolean("NvgCheck", !nvgCheck);
        } else {
            tag.putBoolean("NvgCheck", true);
        }
        stack.setTag(tag);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        int mode = getNVGMode(stack);
        tooltip.add(Component.literal("NVG Mode: " + (mode == 1 ? "ON" : "OFF")));
        if (stack.getOrCreateTag().contains("NvgCheck")) {
            boolean nvgCheck = stack.getOrCreateTag().getBoolean("NvgCheck");
            tooltip.add(Component.literal("NvgCheck: " + (nvgCheck ? "TRUE" : "FALSE")));
        } else {
            tooltip.add(Component.literal("NvgCheck: NOT SET"));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "procedureController", 5, state -> {
            if (!this.animationprocedure.equals("Idle")) {
                if (this.animationprocedure.equals("Close")) {
                    // If the animation is not the current animation, set the animation to close
                    if (!state.isCurrentAnimation(ANIM_CLOSE)) {state.getController().setAnimation(ANIM_CLOSE);}
                } else if (this.animationprocedure.equals("Open")) {
                    // If the animation is not the current animation, set the animation to open
                    if (!state.isCurrentAnimation(ANIM_OPEN)) {state.getController().setAnimation(ANIM_OPEN);}
                } else {
                    // If the animation is neither close nor open, set the animation to idle
                    state.getController().setAnimation(ANIM_IDLE);

                }
            }
            return PlayState.CONTINUE;
        } ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() { return this.cache; }

    // NVGConfig class, used to store the configuration of the NVG goggles
    public static class NVGConfig {
        private Float brightness = 1.0f;
        private Float redValue = 1.0f;
        private Float greenValue = 1.0f;
        private Float blueValue = 1.0f;

        public NVGConfig(float pBrightness) {
            brightness = pBrightness;
        }

        public NVGConfig(float pBrightness, float pRed, float pGreen, float pBlue) {
            brightness = pBrightness;
            redValue = pRed;
            greenValue = pGreen;
            blueValue = pBlue;
        }

        public float getBrightness() { return brightness; }
        public float getRedValue() { return redValue; }
        public float getGreenValue() { return greenValue; }
        public float getBlueValue() { return blueValue; }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (entity instanceof LivingEntity livingEntity) {
            if (stack.hasTag() && stack.getTag().contains("NvgCheck")) {
                boolean isNvgCheck = stack.getTag().getBoolean("NvgCheck");
                if (isNvgCheck && !this.animationprocedure.equals("Close")) {
                    this.animationprocedure = "Close";
                } else if (!isNvgCheck && !this.animationprocedure.equals("Open")) {
                    this.animationprocedure = "Open";
                }
            }
            if (entity.level().isClientSide()) {
                updateNVGMode(stack);
            }
        } else {
            this.animationprocedure = "Idle";
        }
    }
}
