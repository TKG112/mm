package net.tkg.ModernMayhem.server.item.curios.facewear;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.client.renderer.curios.facewear.GenericNVGGogglesRenderer;
import net.tkg.ModernMayhem.server.item.NVGGoggleList;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import net.tkg.ModernMayhem.server.util.CuriosFacewearProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.SlotContext;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class VisorItem extends GenericSpecialGogglesItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final NVGGoggleList config;

    public VisorItem(NVGGoggleList nvgGoggleList) {
        super(
                nvgGoggleList.getConfigs(),
                nvgGoggleList.getConfigIndex(),
                nvgGoggleList.getActivationSound(),
                nvgGoggleList.getDeactivationSound(),
                GoggleType.VISOR
        );
        this.config = nvgGoggleList;
    }

    @Override
    public boolean shouldRenderShader() {
        return false;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> multimap = HashMultimap.create();

        if (isNVGOnFace(stack)) {
            double protection = CuriosFacewearProperties.VISOR.getProtection();
            double toughness = CuriosFacewearProperties.VISOR.getToughness();
            double knockback = CuriosFacewearProperties.VISOR.getKnockback();

            if (protection > 0) {
                multimap.put(Attributes.ARMOR, new AttributeModifier(uuid, "Visor armor bonus", protection, AttributeModifier.Operation.ADDITION));
            }
            if (toughness > 0) {
                multimap.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Visor toughness", toughness, AttributeModifier.Operation.ADDITION));
            }
            if (knockback > 0) {
                multimap.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Visor knockback resistance", knockback, AttributeModifier.Operation.ADDITION));
            }
        }
        return multimap;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            // Use the generic renderer
            private GenericNVGGogglesRenderer<VisorItem> lRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new GenericNVGGogglesRenderer<>(); // No custom model needed

                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }
        });

        consumer.accept(new IClientItemExtensions() {
            // Use the generic slot renderer
            private GenericNVGGogglesRenderer.GenericNVGGogglesSlotRenderer<VisorItem> renderer = null;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new GenericNVGGogglesRenderer.GenericNVGGogglesSlotRenderer<>();
                return renderer;
            }
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        tooltip.add(Component.translatable("description.mm.nvgs").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public NVGGoggleList getConfig() {
        return config;
    }
}