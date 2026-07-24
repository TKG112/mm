package net.tkg.ModernMayhem.content.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.content.client.DataArmorIconRenderer;
import net.tkg.ModernMayhem.content.client.DataArmorRenderer;
import net.tkg.ModernMayhem.content.def.ArmorDefinition;
import net.tkg.ModernMayhem.server.config.CommonConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * A single armor piece built entirely from a data-driven {@link ArmorDefinition}.
 * <p>
 * Stats come from the definition's JSON (authoritative in phase 1). Protection / toughness /
 * knockback are applied via attribute modifiers (double precision) rather than the int-only
 * {@link ArmorMaterial#getDefenseForType}, mirroring the existing hardcoded armor.
 * <p>
 * Rendering is delegated to the shared {@link DataArmorRenderer} (worn) and
 * {@link DataArmorIconRenderer} (inventory/hand); both read their model, texture and animation
 * straight from this item's definition, so one renderer serves every data-driven armor piece.
 */
public class DataArmorItem extends ArmorItem implements GeoItem {
    private static final UUID[] SLOT_UUIDS = {
            UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"), // feet
            UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"), // legs
            UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"), // chest
            UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150")  // head
    };

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final ArmorDefinition definition;

    public DataArmorItem(ArmorDefinition def) {
        super(buildMaterial(def), def.slot(), new Properties().stacksTo(1).rarity(Rarity.COMMON));
        this.definition = def;
    }

    public ArmorDefinition definition() {
        return definition;
    }

    private static ArmorMaterial buildMaterial(ArmorDefinition def) {
        return new ArmorMaterial() {
            @Override public int getDurabilityForType(@NotNull Type type) { return Math.max(1, def.stats().durability()); }
            @Override public int getDefenseForType(@NotNull Type type) { return 0; }
            @Override public int getEnchantmentValue() { return 0; }
            @Override public @NotNull SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_GENERIC; }
            @Override public @NotNull Ingredient getRepairIngredient() { return Ingredient.of(); }
            @Override public @NotNull String getName() { return def.id().getNamespace() + "_" + def.id().getPath(); }
            @Override public float getToughness() { return 0f; }
            @Override public float getKnockbackResistance() { return 0f; }
        };
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if (slot != this.getType().getSlot()) {
            return super.getAttributeModifiers(slot, stack);
        }
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        UUID uuid = SLOT_UUIDS[slot.getIndex()];

        double protection = definition.stats().protection();
        double toughness = definition.stats().toughness();
        double knockback = definition.stats().knockback();

        if (CommonConfig.ENABLE_DYNAMIC_ARMOR_STATS.get()) {
            int maxDamage = stack.getMaxDamage();
            if (maxDamage > 0) {
                double durabilityPercent = 1.0 - ((double) stack.getDamageValue() / maxDamage);
                if (durabilityPercent < 0) durabilityPercent = 0;
                double factor = 0.1 + (0.9 * durabilityPercent);

                protection *= factor;
                toughness *= factor;
                knockback *= factor;
            }
        }

        if (protection > 0) modifiers.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor modifier", protection, AttributeModifier.Operation.ADDITION));
        if (toughness > 0) modifiers.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", toughness, AttributeModifier.Operation.ADDITION));
        if (knockback > 0) modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", knockback, AttributeModifier.Operation.ADDITION));

        return modifiers;
    }

    /**
     * Vanilla derives an armor layer texture from the material name
     * ({@code minecraft:textures/models/armor/<material>_layer_1.png}) and binds it before our
     * GeckoLib renderer draws. That file never exists for data-driven gear, which logs a failed
     * texture load for every pack. Point it at the definition's real texture instead -- GeckoLib still
     * renders with its own, but nothing fails to load.
     */
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return this.definition.texture().toString();
    }

    /**
     * With dynamic stats enabled the piece stops at 0 durability instead of breaking, matching
     * ModernMayhem's hardcoded armor -- it keeps protecting at 10% rather than vanishing.
     */
    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        if (CommonConfig.ENABLE_DYNAMIC_ARMOR_STATS.get()) {
            int newDamage = stack.getDamageValue() + amount;
            if (newDamage >= stack.getMaxDamage()) {
                stack.setDamageValue(stack.getMaxDamage());
                return 0;
            }
        }
        return super.damageItem(stack, amount, entity, onBroken);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> armorRenderer;
            private DataArmorIconRenderer iconRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                if (this.armorRenderer == null) this.armorRenderer = new DataArmorRenderer();
                this.armorRenderer.prepForRender(entity, stack, slot, original);
                return this.armorRenderer;
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.iconRenderer == null) this.iconRenderer = new DataArmorIconRenderer();
                return this.iconRenderer;
            }
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable net.minecraft.world.level.Level level,
                                @NotNull java.util.List<net.minecraft.network.chat.Component> tooltip,
                                @NotNull net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        DataTooltips.append(definition.tooltip(), tooltip);
    }
}
