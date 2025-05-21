package net.tkg.ModernMayhem.server.item.generic;

import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.curios.facewear.NVGGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;
import java.util.Optional;

public abstract class GenericNVGGogglesItem extends Item implements GeoItem, ICurioItem {
    private NVGConfig[] configs;
    private int configIndex = 0;
    private static int defaultConfigIndex = 0;
    public RegistryObject<SoundEvent> ACTIVATION_SOUND = null;
    public RegistryObject<SoundEvent> DEACTIVATION_SOUND = null;

    // Animations
    public static final RawAnimation ANIM_IDLE = RawAnimation.begin().thenLoop("opened");
    public static final RawAnimation ANIM_OPEN = RawAnimation.begin().thenPlayAndHold("opening").thenLoop("opened");
    public static final RawAnimation ANIM_CLOSE = RawAnimation.begin().thenPlayAndHold("closing").thenLoop("closed");


    public GenericNVGGogglesItem(NVGConfig pConfig) {
        super(new Item.Properties().stacksTo(1).durability(0));
        defaultConfigIndex = 0;
    }

    public GenericNVGGogglesItem(NVGConfig pConfig, RegistryObject<SoundEvent> pActivationSound, RegistryObject<SoundEvent> pDeactivationSound) {
        super(new Item.Properties().stacksTo(1).durability(0));
        defaultConfigIndex = 0;
        this.ACTIVATION_SOUND = pActivationSound;
        this.DEACTIVATION_SOUND = pDeactivationSound;
    }

    public GenericNVGGogglesItem(NVGConfig[] pConfigs, int startConfigIndex, RegistryObject<SoundEvent> pActivationSound, RegistryObject<SoundEvent> pDeactivationSound) {
        super(new Item.Properties().stacksTo(1).durability(0));
        this.configs = pConfigs;
        defaultConfigIndex = startConfigIndex;
        this.ACTIVATION_SOUND = pActivationSound;
        this.DEACTIVATION_SOUND = pDeactivationSound;
    }

    public static NVGConfig getCurrentConfig(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        if (tag.contains("configIndex")) {
            return ((GenericNVGGogglesItem) item.getItem()).configs[tag.getInt("configIndex")];
        }
        tag.putInt("configIndex", defaultConfigIndex);
        return ((GenericNVGGogglesItem) item.getItem()).configs[defaultConfigIndex];

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

    public static boolean getNVGCheck(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("NvgCheck")) {
            return tag.getBoolean("NvgCheck");
        }
        return false;
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

    public static void switchConfigUp(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        if (tag.contains("configIndex")) {
            int configIndex = tag.getInt("configIndex");
            if (configIndex < ((GenericNVGGogglesItem) item.getItem()).configs.length - 1) {
                tag.putInt("configIndex", configIndex + 1);
            }
            return;
        }
        tag.putInt("configIndex", defaultConfigIndex);
    }

    public static void switchConfigDown(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        if (tag.contains("configIndex")) {
            int configIndex = tag.getInt("configIndex");
            if (configIndex > 0) {
                tag.putInt("configIndex", configIndex - 1);
            }
            return;
        }
        tag.putInt("configIndex", defaultConfigIndex);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 1, state -> {
            Entity entity = state.getData(DataTickets.ENTITY);
            if (entity instanceof Player player) {
                if (CuriosUtil.hasNVGEquipped(player)) {
                    ItemStack stack = CuriosUtil.getFaceWearItem(player);
                    if (stack.getItem() instanceof GenericNVGGogglesItem) {
                        CompoundTag tag = stack.getOrCreateTag();
                        if (tag.contains("nvg_mode")) {
                            int mode = tag.getInt("nvg_mode");
                            if (mode == 0) {
                                if (!state.isCurrentAnimation(ANIM_OPEN)) {state.setAnimation(ANIM_OPEN);}
                            } else {
                                if (!state.isCurrentAnimation(ANIM_CLOSE)) {state.setAnimation(ANIM_CLOSE);}
                            }
                        } else {
                            state.setAnimation(ANIM_IDLE);
                        }
                    }
                } else {
                    state.setAnimation(ANIM_IDLE);
                }}
            return PlayState.CONTINUE;
        }));
    }

    // NVGConfig class, used to store the configuration of the NVG goggles
    public static class NVGConfig {
        private Float brightness = 1.0f;
        private Float redValue = 1.0f;
        private Float greenValue = 1.0f;
        private Float blueValue = 1.0f;
        private ResourceLocation overlay = null;

        public NVGConfig(float pBrightness, float pRed, float pGreen, float pBlue) {
            brightness = pBrightness;
            redValue = pRed;
            greenValue = pGreen;
            blueValue = pBlue;
        }

        public NVGConfig(float pBrightness, float pRed, float pGreen, float pBlue, String pOverlay) {
            brightness = pBrightness;
            redValue = pRed;
            greenValue = pGreen;
            blueValue = pBlue;
            overlay = new ResourceLocation(ModernMayhemMod.ID, pOverlay);
        }

        public float getBrightness() { return brightness; }
        public float getRedValue() { return redValue; }
        public float getGreenValue() { return greenValue; }
        public float getBlueValue() { return blueValue; }
        public ResourceLocation getOverlay() { return overlay; }
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (entity instanceof LivingEntity livingEntity) {
            if (entity.level().isClientSide()) {
                updateNVGMode(stack);
            }
        }
    }

    // Used to check if it can be equipped (In this case, if the player has a helmet with the tag 'mm:hasHeadMount' equipped)
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        System.out.println("TESTING EQUIP HERE !");
        if (entity instanceof Player player) {
            Iterable<ItemStack> armorList = player.getArmorSlots();
            for(ItemStack itemStack : armorList) {
                if (itemStack.getItem() instanceof ArmorItem armorItem) {
                    if (armorItem.getEquipmentSlot() == EquipmentSlot.HEAD) {
                        return true;
                    }
                }
            }
            return false;
        }
        return true;
    }

    public static boolean isNVGEnabled(Player player) {
        if (player == null) return false;

        Optional<ICuriosItemHandler> optional = CuriosApi.getCuriosHelper().getCuriosHandler(player).resolve();
        if (optional.isEmpty()) return false;

        ICuriosItemHandler handler = optional.get();

        List<SlotResult> results = handler.findCurios(stack -> stack.getItem() instanceof NVGGogglesItem);

        for (SlotResult result : results) {
            ItemStack stack = result.stack();
            if (GenericNVGGogglesItem.getNVGCheck(stack) && Minecraft.getInstance().options.getCameraType() == CameraType.FIRST_PERSON) {
                return true;
            }
        }

        return false;
    }
}
