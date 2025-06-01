package net.tkg.ModernMayhem.server.item.generic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.*;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public abstract class GenericNVGGogglesItem extends Item implements GeoItem, ICurioItem {
    private NVGConfig[] configs;
    private int configIndex = 0;
    private static int defaultConfigIndex = 0;
    public RegistryObject<SoundEvent> ACTIVATION_SOUND = null;
    public RegistryObject<SoundEvent> DEACTIVATION_SOUND = null;

    // Animations
    public static final RawAnimation ANIM_IDLE = RawAnimation.begin().thenLoop("opened");
    public static final RawAnimation ANIM_OPEN = RawAnimation.begin().thenPlay("opening").thenLoop("opened");
    public static final RawAnimation ANIM_CLOSE = RawAnimation.begin().thenPlay("closing").thenLoop("closed");

    private static final Map<UUID, Integer> lastConfigIndexMap = new HashMap<>();

    private static final TagKey<Item> HAS_HEAD_MOUNT_TAG = ItemTags.create(fromNamespaceAndPath(ModernMayhemMod.ID, "has_head_mount"));


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

    public static void switchOnNVGMode(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        tag.putBoolean("NvgCheck", true);
        item.setTag(tag);
    }

    public static void switchOffNVGMode(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        tag.putBoolean("NvgCheck", false);
        item.setTag(tag);
    }

    public static void switchEquipState(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("NvgOnFace")) {
            boolean nvgCheck = tag.getBoolean("NvgOnFace");
            tag.putBoolean("NvgOnFace", !nvgCheck);
        } else {
            tag.putBoolean("NvgOnFace", true);
        }
        stack.setTag(tag);
    }

    public static boolean isNVGOnFace(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("NvgOnFace")) {
            return tag.getBoolean("NvgOnFace");
        }
        return false;
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
            if (entity == null || !entity.level().isClientSide()) {
                return PlayState.STOP;
            }
            if (entity instanceof Player player) {
                if (CuriosUtil.hasNVGEquipped(player)) {
                    ItemStack stack = CuriosUtil.getFaceWearItem(player);
                    if (stack.getItem() instanceof GenericNVGGogglesItem) {
                        CompoundTag tag = stack.getOrCreateTag();
                        if (tag.contains("NvgOnFace")) {
                            if (tag.getBoolean("NvgOnFace")) {
                                if (!state.isCurrentAnimation(ANIM_CLOSE)) {state.setAnimation(ANIM_CLOSE);}
                            } else {
                                if (!state.isCurrentAnimation(ANIM_OPEN)) {state.setAnimation(ANIM_OPEN);}
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
            overlay = fromNamespaceAndPath(ModernMayhemMod.ID, pOverlay);
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
        if (!(entity instanceof LivingEntity)) return;

        if (entity.level().isClientSide()) {
            updateNVGMode(stack);
        }

        if (entity instanceof Player player && !player.level().isClientSide()) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

            if (helmet.isEmpty() || !helmet.is(HAS_HEAD_MOUNT_TAG)) {
                CuriosApi.getCuriosInventory(player).ifPresent(curios -> {
                    curios.getStacksHandler(slotContext.identifier()).ifPresent(handler -> {
                        ItemStack removedStack = handler.getStacks().getStackInSlot(slotContext.index()).copy();
                        handler.getStacks().setStackInSlot(slotContext.index(), ItemStack.EMPTY);

                        boolean added = player.getInventory().add(removedStack);
                        if (!added) {
                            player.drop(removedStack, false);
                        }
                    });
                });
            }
        }
    }


    // Used to check if it can be equipped (In this case, if the player has a helmet with the tag 'mm:hasHeadMount' equipped)
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!(entity instanceof Player player)) return false;

        // If player data is not fully available yet (e.g., during login), allow equip
        if (!player.isAddedToWorld()) return true;

        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        return !headItem.isEmpty() && headItem.is(HAS_HEAD_MOUNT_TAG);
    }


    public static boolean hasConfigIndexChanged(Player player, ItemStack stack) {
        if (!(stack.getItem() instanceof GenericNVGGogglesItem)) return false;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains("configIndex")) return false;
        int currentIndex = tag.getInt("configIndex");
        UUID playerId = player.getUUID();
        int lastIndex = lastConfigIndexMap.getOrDefault(playerId, -1);
        if (currentIndex != lastIndex) {
            lastConfigIndexMap.put(playerId, currentIndex);
            return true;
        }
        return false;
    }
}