package net.tkg.ModernMayhem.content.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import net.tkg.ModernMayhem.content.client.DataCurioRenderer;
import net.tkg.ModernMayhem.content.def.AttributeBonus;
import net.tkg.ModernMayhem.content.def.CurioDefinition;
import net.tkg.ModernMayhem.server.config.CommonConfig;
import net.tkg.ModernMayhem.server.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * A single curio (backpack, chest rig, ...) built entirely from a data-driven {@link CurioDefinition}.
 * <p>
 * Inherits ModernMayhem's existing container behavior from {@link GenericBackpackItem} (GUI opening,
 * inventory resizing, quick-stacking, tooltips) and feeds it sizes from the definition's JSON instead
 * of a hardcoded enum. Armor attributes are applied the same way the hardcoded rigs do, including the
 * durability-scaling behavior behind {@code ENABLE_DYNAMIC_ARMOR_STATS}.
 */
public class DataCurioItem extends GenericBackpackItem implements GeoItem, ICurioItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final CurioDefinition definition;

    public DataCurioItem(CurioDefinition def) {
        super(slotTypeByte(def.slot()));
        this.definition = def;
    }

    public CurioDefinition definition() {
        return definition;
    }

    /** {@link GenericBackpackItem} identifies its Curios slot by a byte; map the identifier onto it. */
    private static byte slotTypeByte(String slot) {
        return switch (slot) {
            case "back" -> (byte) 0;
            case "body" -> (byte) 1;
            default -> (byte) -1;
        };
    }

    /** Data-driven curios know their slot by identifier, including slots invented by a content pack. */
    @Override
    protected String getCuriosSlotIdentifier() {
        return definition.slot();
    }

    @Override
    protected int getCuriosSlotID(Player player) {
        return CuriosUtil.getCurioSlotID(player, definition.slot());
    }

    @Override
    public int getInventoryLines() {
        return definition.storage().rows();
    }

    @Override
    public int getInventoryColumns() {
        return definition.storage().columns();
    }

    @Override
    public boolean canSupplyAmmo() {
        return definition.storage().suppliesAmmo();
    }

    @Override
    public boolean canBeDepleted() {
        return this.getMaxDamage(ItemStack.EMPTY) > 0;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return definition.stats().durability();
    }

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
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();

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

        if (protection > 0) modifiers.put(Attributes.ARMOR, new AttributeModifier(uuid, "Armor", protection, AttributeModifier.Operation.ADDITION));
        if (toughness > 0) modifiers.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", toughness, AttributeModifier.Operation.ADDITION));
        if (knockback > 0) modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Knockback resistance", knockback, AttributeModifier.Operation.ADDITION));

        for (AttributeBonus bonus : definition.attributes()) {
            Attribute attribute = ForgeRegistries.ATTRIBUTES.getValue(bonus.attribute());
            if (attribute == null) {
                continue;
            }
            modifiers.put(attribute, new AttributeModifier(uuid, bonus.name(), bonus.amount(), bonus.operation()));
        }

        return modifiers;
    }

    /** True when {@code helmet} is one this curio refuses to share the player's head with. */
    private boolean conflictsWith(ItemStack helmet) {
        ResourceLocation tagId = definition.conflictsWithHelmets();
        if (tagId == null || helmet.isEmpty()) return false;
        return helmet.is(ItemTags.create(tagId));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if (definition.conflictsWithHelmets() == null) return true;
        if (!(slotContext.entity() instanceof Player player)) return false;
        if (!player.isAddedToWorld()) return true;

        return !conflictsWith(player.getItemBySlot(EquipmentSlot.HEAD));
    }

    /**
     * {@link #canEquip} only guards putting the curio on -- a player can equip the curio first and
     * the conflicting helmet second. This unequips the curio back into the inventory if that happens.
     */
    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        if (definition.conflictsWithHelmets() == null) return;
        if (!(slotContext.entity() instanceof Player player) || player.level().isClientSide()) return;
        if (!conflictsWith(player.getItemBySlot(EquipmentSlot.HEAD))) return;

        CuriosApi.getCuriosInventory(player).ifPresent(curios ->
                curios.getStacksHandler(slotContext.identifier()).ifPresent(handler -> {
                    ItemStack removed = handler.getStacks().getStackInSlot(slotContext.index()).copy();
                    handler.getStacks().setStackInSlot(slotContext.index(), ItemStack.EMPTY);
                    if (!player.getInventory().add(removed)) {
                        player.drop(removed, false);
                    }
                }));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> wornRenderer;
            private DataCurioRenderer.Icon iconRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                if (this.wornRenderer == null) this.wornRenderer = new DataCurioRenderer();
                this.wornRenderer.prepForRender(entity, stack, slot, original);
                return this.wornRenderer;
            }

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.iconRenderer == null) this.iconRenderer = new DataCurioRenderer.Icon();
                return this.iconRenderer;
            }
        });
    }

    /** Adds the definition's lines after the inventory summary {@link GenericBackpackItem} draws. */
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @org.jetbrains.annotations.Nullable net.minecraft.world.level.Level level,
                                @NotNull java.util.List<net.minecraft.network.chat.Component> tooltip,
                                @NotNull net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        DataTooltips.append(definition.tooltip(), tooltip);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
