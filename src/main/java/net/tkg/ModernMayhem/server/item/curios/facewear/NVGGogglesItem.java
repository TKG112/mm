package net.tkg.ModernMayhem.server.item.curios.facewear;

import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.curios.facewear.GenericNVGGogglesRenderer;
import net.tkg.ModernMayhem.server.item.NVGGoggleList;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class NVGGogglesItem extends GenericSpecialGogglesItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final boolean isGamerNVG;
    private final NVGGoggleList config;

    public NVGGogglesItem(NVGGoggleList nvgGoggleList) {
        super(
                nvgGoggleList.getConfigs(),
                nvgGoggleList.getConfigIndex(),
                nvgGoggleList.getActivationSound(),
                nvgGoggleList.getDeactivationSound(),
                GoggleType.NIGHT_VISION,
                canHoldCoti(nvgGoggleList),
                hasAutoGain(nvgGoggleList),
                hasAutoGating(nvgGoggleList)
        );
        this.isGamerNVG = nvgGoggleList == NVGGoggleList.GAMER_GPNVG;
        this.config = nvgGoggleList;
    }

    private static boolean hasAutoGating(NVGGoggleList config) {
        return config == NVGGoggleList.BLACK_PVS14 ||
                config == NVGGoggleList.TAN_PVS14 ||
                config == NVGGoggleList.GREEN_PVS14 ||
                config == NVGGoggleList.BLACK_GPNVG ||
                config == NVGGoggleList.TAN_GPNVG;
    }

    private static boolean hasAutoGain(NVGGoggleList config) {
        return config == NVGGoggleList.BLACK_PVS14 ||
                config == NVGGoggleList.TAN_PVS14 ||
                config == NVGGoggleList.GREEN_PVS14 ||
                config == NVGGoggleList.BLACK_GPNVG ||
                config == NVGGoggleList.TAN_GPNVG;
    }

    private static boolean canHoldCoti(NVGGoggleList config) {
        return config == NVGGoggleList.BLACK_PVS14 ||
                config == NVGGoggleList.TAN_PVS14 ||
                config == NVGGoggleList.GREEN_PVS14 ||
                config == NVGGoggleList.BLACK_PVS7;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GenericNVGGogglesRenderer<NVGGogglesItem> lRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new GenericNVGGogglesRenderer<>();

                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }
        });

        consumer.accept(new IClientItemExtensions() {
            private GenericNVGGogglesRenderer.GenericNVGGogglesSlotRenderer<NVGGogglesItem> renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GenericNVGGogglesRenderer.GenericNVGGogglesSlotRenderer<>();
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

        if (isGamerNVG) {
            tooltip.add(Component.translatable("description.mm.ultra_gamer_gpnvg").withStyle(ChatFormatting.RED));
        }

        tooltip.add(Component.translatable("description.mm.nvgs").withStyle(ChatFormatting.GRAY));
    }

    public boolean isGamerNVG() {
        return isGamerNVG;
    }

    public NVGGoggleList getConfig() {
        return config;
    }
}