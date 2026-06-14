package net.tkg.ModernMayhem.server.item.curios.facewear;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.curios.facewear.GenericSpecialGogglesRenderer;
import net.tkg.ModernMayhem.server.item.NVGGoggleList;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class TVGGogglesItem extends GenericSpecialGogglesItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final NVGGoggleList config;

    public TVGGogglesItem(NVGGoggleList nvgGoggleList) {
        super(
                nvgGoggleList.getConfigs(),
                nvgGoggleList.getConfigIndex(),
                nvgGoggleList.getActivationSound(),
                nvgGoggleList.getDeactivationSound(),
                GoggleType.THERMAL
        );
        this.config = nvgGoggleList;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GenericSpecialGogglesRenderer<TVGGogglesItem> lRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new GenericSpecialGogglesRenderer<>();

                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }
        });

        consumer.accept(new IClientItemExtensions() {
            private GenericSpecialGogglesRenderer.GenericNVGGogglesSlotRenderer<TVGGogglesItem> renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GenericSpecialGogglesRenderer.GenericNVGGogglesSlotRenderer<>();
                return renderer;
            }
        });
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("description.mm.nvgs").withStyle(ChatFormatting.GRAY));
    }

    public static final String PALETTE_TAG = "ThermalPalette";
    public static final int PALETTE_COUNT = 5; // WHITE_HOT, BLACK_HOT, RED_HOT, FUSION, IRONBOW

    public static int getThermalPalette(ItemStack stack) {
        if (stack == null) return 0;
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(PALETTE_TAG)) return 0;
        int p = tag.getInt(PALETTE_TAG);
        return ((p % PALETTE_COUNT) + PALETTE_COUNT) % PALETTE_COUNT;
    }

    public static void setThermalPalette(ItemStack stack, int palette) {
        if (stack == null) return;
        int wrapped = ((palette % PALETTE_COUNT) + PALETTE_COUNT) % PALETTE_COUNT;
        stack.getOrCreateTag().putInt(PALETTE_TAG, wrapped);
    }

    public static void cycleThermalPalette(ItemStack stack, boolean forward) {
        setThermalPalette(stack, getThermalPalette(stack) + (forward ? 1 : -1));
    }

    public NVGGoggleList getConfig() {
        return config;
    }
}