package net.tkg.ModernMayhem.server.item.generic;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.server.item.NVGGoggleList;
import net.tkg.ModernMayhem.server.registry.ItemRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public abstract class GenericSpecialGogglesItem extends Item implements GeoItem, ICurioItem {
    private NVGConfig[] configs;
    private int configIndex = 0;
    private static int defaultConfigIndex = 0;
    public RegistryObject<SoundEvent> ACTIVATION_SOUND = null;
    public RegistryObject<SoundEvent> DEACTIVATION_SOUND = null;
    private static final NVGConfig FALLBACK_CONFIG = new NVGConfig(1.0f, 1.0f, 1.0f, 1.0f);

    public static final RawAnimation ANIM_IDLE = RawAnimation.begin().thenLoop("opened");
    public static final RawAnimation ANIM_OPEN = RawAnimation.begin().thenPlay("opening").thenLoop("opened");
    public static final RawAnimation ANIM_CLOSE = RawAnimation.begin().thenPlay("closing").thenLoop("closed");

    private static final Map<UUID, Integer> lastConfigIndexMap = new HashMap<>();

    private static final TagKey<Item> HAS_HEAD_MOUNT_TAG = ItemTags.create(fromNamespaceAndPath(ModernMayhemMod.ID, "has_head_mount"));
    private static final String COTI_CONTENTS_TAG = "CotiContents";

    public enum GoggleType {
        NIGHT_VISION,
        THERMAL
    }

    private final GoggleType goggleType;
    private final boolean canHoldCoti;
    private final boolean hasAutoGain;
    private final boolean hasAutoGating;

    public abstract NVGGoggleList getConfig();

    public GenericSpecialGogglesItem(NVGConfig pConfig) {
        super(new Item.Properties().stacksTo(1).durability(0));
        defaultConfigIndex = 0;
        this.goggleType = GoggleType.NIGHT_VISION;
        this.canHoldCoti = false;
        this.hasAutoGain = false;
        this.hasAutoGating = false;
    }

    public GenericSpecialGogglesItem(NVGConfig pConfig, RegistryObject<SoundEvent> pActivationSound, RegistryObject<SoundEvent> pDeactivationSound) {
        super(new Item.Properties().stacksTo(1).durability(0));
        defaultConfigIndex = 0;
        this.ACTIVATION_SOUND = pActivationSound;
        this.DEACTIVATION_SOUND = pDeactivationSound;
        this.goggleType = GoggleType.NIGHT_VISION;
        this.canHoldCoti = false;
        this.hasAutoGain = false;
        this.hasAutoGating = false;
    }

    public GenericSpecialGogglesItem(NVGConfig[] pConfigs, int startConfigIndex, RegistryObject<SoundEvent> pActivationSound, RegistryObject<SoundEvent> pDeactivationSound) {
        super(new Item.Properties().stacksTo(1).durability(0));
        this.configs = pConfigs;
        defaultConfigIndex = startConfigIndex;
        this.ACTIVATION_SOUND = pActivationSound;
        this.DEACTIVATION_SOUND = pDeactivationSound;
        this.goggleType = GoggleType.NIGHT_VISION;
        this.canHoldCoti = false;
        this.hasAutoGain = false;
        this.hasAutoGating = false;

        calculateBrightnessRange();
    }

    public GenericSpecialGogglesItem(NVGConfig[] pConfigs, int startConfigIndex, RegistryObject<SoundEvent> pActivationSound, RegistryObject<SoundEvent> pDeactivationSound, GoggleType pGoggleType, boolean canHoldCoti, boolean pHasAutoGain, boolean pHasAutoGating) {
        super(new Item.Properties().stacksTo(1).durability(0));
        this.configs = pConfigs;
        defaultConfigIndex = startConfigIndex;
        this.ACTIVATION_SOUND = pActivationSound;
        this.DEACTIVATION_SOUND = pDeactivationSound;
        this.goggleType = pGoggleType;
        this.canHoldCoti = canHoldCoti;
        this.hasAutoGain = pHasAutoGain;
        this.hasAutoGating = pHasAutoGating;

        calculateBrightnessRange();
    }

    public GenericSpecialGogglesItem(NVGConfig[] pConfigs, int startConfigIndex, RegistryObject<SoundEvent> pActivationSound, RegistryObject<SoundEvent> pDeactivationSound, GoggleType pGoggleType) {
        super(new Item.Properties().stacksTo(1).durability(0));
        this.configs = pConfigs;
        defaultConfigIndex = startConfigIndex;
        this.ACTIVATION_SOUND = pActivationSound;
        this.DEACTIVATION_SOUND = pDeactivationSound;
        this.goggleType = pGoggleType;
        this.canHoldCoti = false;
        this.hasAutoGain = false;
        this.hasAutoGating = false;

        calculateBrightnessRange();
    }

    public boolean shouldRenderShader() {
        return true;
    }

    public boolean canHoldCoti() {
        return canHoldCoti;
    }

    public boolean hasAutoGain() {
        return hasAutoGain;
    }

    public boolean hasAutoGating() {
        return hasAutoGating;
    }

    @Override
    public boolean overrideStackedOnOther(@NotNull ItemStack nvgStack, @NotNull Slot slot, @NotNull ClickAction clickAction, @NotNull Player player) {
        if (!canHoldCoti || nvgStack.getCount() != 1 || clickAction != ClickAction.SECONDARY) {
            return false;
        }

        ItemStack slotStack = slot.getItem();

        if (slotStack.isEmpty()) {
            if (hasCoti(nvgStack)) {
                ItemStack cotiStack = removeCoti(nvgStack);
                if (!cotiStack.isEmpty()) {
                    playRemoveSound(player);
                    ItemStack remaining = slot.safeInsert(cotiStack);
                    if (!remaining.isEmpty()) {
                        insertCoti(nvgStack, remaining);
                    }
                    return true;
                }
            }
        }
        else {
            if (isCotiItem(slotStack) && !hasCoti(nvgStack)) {
                ItemStack takenStack = slot.safeTake(slotStack.getCount(), 1, player);
                if (!takenStack.isEmpty()) {
                    insertCoti(nvgStack, takenStack);
                    playInsertSound(player);
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NotNull ItemStack nvgStack, @NotNull ItemStack otherStack, @NotNull Slot slot, @NotNull ClickAction clickAction, @NotNull Player player, @NotNull SlotAccess slotAccess) {
        if (!canHoldCoti || nvgStack.getCount() != 1) {
            return false;
        }

        if (clickAction == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (otherStack.isEmpty()) {
                if (hasCoti(nvgStack)) {
                    ItemStack cotiStack = removeCoti(nvgStack);
                    if (!cotiStack.isEmpty()) {
                        playRemoveSound(player);
                        slotAccess.set(cotiStack);
                        return true;
                    }
                }
            }
            else if (isCotiItem(otherStack) && !hasCoti(nvgStack)) {
                ItemStack toInsert = otherStack.split(1);
                if (insertCoti(nvgStack, toInsert)) {
                    playInsertSound(player);
                    return true;
                } else {
                    otherStack.grow(1);
                }
            }
        }

        return false;
    }

    @Override
    public Optional<TooltipComponent> getTooltipImage(@NotNull ItemStack stack) {
        if (!canHoldCoti) {
            return Optional.empty();
        }

        if (hasCoti(stack)) {
            ItemStack cotiStack = getCoti(stack);
            if (!cotiStack.isEmpty()) {
                NonNullList<ItemStack> contents = NonNullList.create();
                contents.add(cotiStack);
                return Optional.of(new BundleTooltip(contents, 0));
            }
        }

        return Optional.empty();
    }

    public boolean insertCoti(ItemStack nvgStack, ItemStack cotiStack) {
        if (!canHoldCoti || hasCoti(nvgStack) || !isCotiItem(cotiStack)) {
            return false;
        }

        CompoundTag tag = nvgStack.getOrCreateTag();
        CompoundTag cotiTag = new CompoundTag();
        cotiStack.save(cotiTag);
        tag.put(COTI_CONTENTS_TAG, cotiTag);
        nvgStack.setTag(tag);

        return true;
    }

    public ItemStack removeCoti(ItemStack nvgStack) {
        if (!hasCoti(nvgStack)) {
            return ItemStack.EMPTY;
        }

        CompoundTag tag = nvgStack.getTag();
        if (tag == null) {
            return ItemStack.EMPTY;
        }

        CompoundTag cotiTag = tag.getCompound(COTI_CONTENTS_TAG);
        ItemStack cotiStack = ItemStack.of(cotiTag);

        tag.remove(COTI_CONTENTS_TAG);
        nvgStack.setTag(tag);

        return cotiStack;
    }

    public static boolean hasCoti(ItemStack nvgStack) {
        if (!(nvgStack.getItem() instanceof GenericSpecialGogglesItem nvgItem)) {
            return false;
        }

        if (!nvgItem.canHoldCoti()) {
            return false;
        }

        CompoundTag tag = nvgStack.getTag();
        return tag != null && tag.contains(COTI_CONTENTS_TAG);
    }

    public static ItemStack getCoti(ItemStack nvgStack) {
        if (!hasCoti(nvgStack)) {
            return ItemStack.EMPTY;
        }

        CompoundTag tag = nvgStack.getTag();
        if (tag == null) {
            return ItemStack.EMPTY;
        }

        CompoundTag cotiTag = tag.getCompound(COTI_CONTENTS_TAG);
        return ItemStack.of(cotiTag);
    }


    private boolean isCotiItem(ItemStack stack) {
        return stack.is(ItemRegistryMM.COTI.get());
    }


    private void playRemoveSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (canHoldCoti) {
            if (hasCoti(stack)) {
                tooltip.add(Component.translatable("description.mm.nvg.coti_installed").withStyle(ChatFormatting.YELLOW));
            }
        }
    }

    public static NVGConfig getCurrentConfig(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        GenericSpecialGogglesItem itemInstance = (GenericSpecialGogglesItem) item.getItem();

        if (itemInstance.configs == null || itemInstance.configs.length == 0) {
            if (!tag.contains("configIndex")) tag.putInt("configIndex", 0);
            return FALLBACK_CONFIG;
        }

        int safeDefaultIndex = Math.min(defaultConfigIndex, itemInstance.configs.length - 1);

        if (tag.contains("configIndex")) {
            int idx = tag.getInt("configIndex");
            if (idx < 0) {
                idx = 0;
            } else if (idx >= itemInstance.configs.length) {
                idx = itemInstance.configs.length - 1;
            }
            tag.putInt("configIndex", idx);
            item.setTag(tag);
            return itemInstance.configs[idx];
        }
        tag.putInt("configIndex", safeDefaultIndex);
        item.setTag(tag);
        return itemInstance.configs[safeDefaultIndex];
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
        System.out.println("[ModernMayhem] Switching on NVG mode for item: " + item);
        CompoundTag tag = item.getOrCreateTag();
        tag.putBoolean("NvgCheck", true);
        item.setTag(tag);
    }

    public static void switchOffNVGMode(ItemStack item) {
        System.out.println("[ModernMayhem] Switching off NVG mode for item: " + item);
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
        GenericSpecialGogglesItem itemInstance = (GenericSpecialGogglesItem) item.getItem();

        if (itemInstance.configs == null || itemInstance.configs.length == 0) {
            tag.putInt("configIndex", 0);
            item.setTag(tag);
            return;
        }

        if (tag.contains("configIndex")) {
            int configIndex = tag.getInt("configIndex");
            configIndex = Math.max(0, Math.min(configIndex, itemInstance.configs.length - 1));
            if (configIndex < itemInstance.configs.length - 1) {
                tag.putInt("configIndex", configIndex + 1);
            }
            item.setTag(tag);
            return;
        }

        int safeDefaultIndex = Math.min(defaultConfigIndex, itemInstance.configs.length - 1);
        tag.putInt("configIndex", safeDefaultIndex);
        item.setTag(tag);
    }

    public static void switchConfigDown(ItemStack item) {
        CompoundTag tag = item.getOrCreateTag();
        GenericSpecialGogglesItem itemInstance = (GenericSpecialGogglesItem) item.getItem();

        if (itemInstance.configs == null || itemInstance.configs.length == 0) {
            tag.putInt("configIndex", 0);
            item.setTag(tag);
            return;
        }

        if (tag.contains("configIndex")) {
            int configIndex = tag.getInt("configIndex");
            configIndex = Math.max(0, Math.min(configIndex, itemInstance.configs.length - 1));
            if (configIndex > 0) {
                tag.putInt("configIndex", configIndex - 1);
            }
            item.setTag(tag);
            return;
        }

        int safeDefaultIndex = Math.min(defaultConfigIndex, itemInstance.configs.length - 1);
        tag.putInt("configIndex", safeDefaultIndex);
        item.setTag(tag);
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
                    if (stack.getItem() instanceof GenericSpecialGogglesItem) {
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

    private void calculateBrightnessRange() {
        if (configs == null || configs.length == 0) return;

        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;

        for (NVGConfig config : configs) {
            float brightness = config.getBrightness();
            if (brightness < min) min = brightness;
            if (brightness > max) max = brightness;
        }

        for (NVGConfig config : configs) {
            config.setMinGain(min);
            config.setMaxGain(max);
        }
    }

    public static class NVGConfig {
        private Float brightness = 1.0f;
        private Float redValue = 1.0f;
        private Float greenValue = 1.0f;
        private Float blueValue = 1.0f;
        private ResourceLocation overlay = null;
        private Float minGain = 0.1f;
        private Float maxGain = 1.0f;
        private Float noiseMultiplier = 1.0f;
        private Float autoGainSpeed = 0.05f;
        private Float autoGainOffset = 0f;
        private Float autoGatingOffset = 0.1f;
        private Float autoGatingSpeed = 0.1f;

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

        public NVGConfig(float pBrightness, float pRed, float pGreen, float pBlue, String pOverlay, float pNoiseMultiplier) {
            brightness = pBrightness;
            redValue = pRed;
            greenValue = pGreen;
            blueValue = pBlue;
            overlay = fromNamespaceAndPath(ModernMayhemMod.ID, pOverlay);
            noiseMultiplier = pNoiseMultiplier;
        }

        public NVGConfig(float pBrightness, float pRed, float pGreen, float pBlue, String pOverlay, float pNoiseMultiplier, float pAutoGainSpeed) {
            brightness = pBrightness;
            redValue = pRed;
            greenValue = pGreen;
            blueValue = pBlue;
            overlay = fromNamespaceAndPath(ModernMayhemMod.ID, pOverlay);
            noiseMultiplier = pNoiseMultiplier;
            autoGainSpeed = pAutoGainSpeed;
        }

        public NVGConfig(float pBrightness, float pRed, float pGreen, float pBlue, String pOverlay, float pNoiseMultiplier, float pAutoGainSpeed, float pAutoGatingOffset, float pAutoGatingSpeed) {
            brightness = pBrightness;
            redValue = pRed;
            greenValue = pGreen;
            blueValue = pBlue;
            overlay = fromNamespaceAndPath(ModernMayhemMod.ID, pOverlay);
            noiseMultiplier = pNoiseMultiplier;
            autoGainSpeed = pAutoGainSpeed;
            autoGatingOffset = pAutoGatingOffset;
            autoGatingSpeed = pAutoGatingSpeed;

        }

        public NVGConfig(float pBrightness, float pRed, float pGreen, float pBlue, String pOverlay, float pNoiseMultiplier, float pAutoGainSpeed, float pAutoGainOffset, float pAutoGatingOffset, float pAutoGatingSpeed) {
            brightness = pBrightness;
            redValue = pRed;
            greenValue = pGreen;
            blueValue = pBlue;
            overlay = fromNamespaceAndPath(ModernMayhemMod.ID, pOverlay);
            noiseMultiplier = pNoiseMultiplier;
            autoGainSpeed = pAutoGainSpeed;
            autoGainOffset = pAutoGainOffset;
            autoGatingOffset = pAutoGatingOffset;
            autoGatingSpeed = pAutoGatingSpeed;

        }

        public NVGConfig(String pOverlay) {
            overlay = fromNamespaceAndPath(ModernMayhemMod.ID, pOverlay);
        }

        public float getBrightness() { return brightness; }
        public float getRedValue() { return redValue; }
        public float getGreenValue() { return greenValue; }
        public float getBlueValue() { return blueValue; }
        public ResourceLocation getOverlay() { return overlay; }
        public void setMinGain(float min) { this.minGain = min; }
        public void setMaxGain(float max) { this.maxGain = max; }
        public float getMinGain() { return minGain - autoGainOffset; }
        public float getMaxGain() { return maxGain + autoGatingOffset; }
        public float getNoiseMultiplier() { return noiseMultiplier; }
        public float getAutoGainSpeed() { return autoGainSpeed; }
        public float getAutoGatingOffset() { return autoGatingOffset; }
        public float getAutoGatingSpeed() { return autoGatingSpeed; }
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

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        LivingEntity entity = slotContext.entity();
        if (!(entity instanceof Player player)) return false;

        if (!player.isAddedToWorld()) return true;

        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        return !headItem.isEmpty() && headItem.is(HAS_HEAD_MOUNT_TAG);
    }

    public static boolean hasConfigIndexChanged(Player player, ItemStack stack) {
        if (!(stack.getItem() instanceof GenericSpecialGogglesItem)) return false;
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

    public static void switchAutoGain(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("AutoGainEnabled")) {
            boolean currentStatus = tag.getBoolean("AutoGainEnabled");
            tag.putBoolean("AutoGainEnabled", !currentStatus);
        } else {
            tag.putBoolean("AutoGainEnabled", true);
        }
        stack.setTag(tag);
    }

    public static boolean isAutoGainEnabled(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("AutoGainEnabled")) {
            return tag.getBoolean("AutoGainEnabled");
        }
        return false;
    }

    public static void switchCotiMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("Coti")) {
            boolean current = tag.getBoolean("Coti");
            tag.putBoolean("Coti", !current);
        } else {
            tag.putBoolean("Coti", true);
        }
        stack.setTag(tag);
    }

    public static boolean isCotiEnabled(ItemStack stack) {
        return hasCoti(stack) && stack.getTag() != null && stack.getTag().getBoolean("Coti");
    }
}