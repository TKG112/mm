package net.tkg.ModernMayhem.content.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.tkg.ModernMayhem.client.renderer.curios.facewear.GenericSpecialGogglesRenderer;
import net.tkg.ModernMayhem.content.def.GainStep;
import net.tkg.ModernMayhem.content.def.GogglesDefinition;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

/**
 * A goggle (night vision, thermal or visor) built entirely from a data-driven
 * {@link GogglesDefinition}.
 * <p>
 * The gain ladder from the JSON is converted into the {@code NVGConfig[]} the base class already
 * understands, so all of ModernMayhem's existing behaviour -- gain stepping, auto-gain, auto-gating,
 * COTI, toggling, shader driving -- works unchanged. Visuals come from the definition via the
 * resolution methods on the base class rather than the legacy type/variant switch.
 */
public class DataGogglesItem extends GenericSpecialGogglesItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GogglesDefinition definition;

    public DataGogglesItem(GogglesDefinition def) {
        super(
                toNvgConfigs(def.gainSteps()),
                clampDefaultGain(def),
                resolveSound(def.activationSound()),
                resolveSound(def.deactivationSound()),
                def.goggleType(),
                def.features().canHoldCoti(),
                def.features().autoGain(),
                def.features().autoGating()
        );
        this.definition = def;
    }

    public GogglesDefinition definition() {
        return definition;
    }

    /** Turns the JSON gain ladder into the config array the base class drives the shader from. */
    private static NVGConfig[] toNvgConfigs(List<GainStep> steps) {
        NVGConfig[] configs = new NVGConfig[steps.size()];
        for (int i = 0; i < steps.size(); i++) {
            GainStep step = steps.get(i);
            configs[i] = new NVGConfig(
                    step.brightness(), step.red(), step.green(), step.blue(),
                    step.overlay(), step.noise(),
                    step.autoGainSpeed(), step.autoGainOffset(),
                    step.autoGatingOffset(), step.autoGatingSpeed());
        }
        return configs;
    }

    /** Keeps a bad {@code default_gain} from indexing outside the ladder. */
    private static int clampDefaultGain(GogglesDefinition def) {
        int last = Math.max(0, def.gainSteps().size() - 1);
        return Math.max(0, Math.min(def.defaultGain(), last));
    }

    /**
     * Sounds are referenced by id and resolved lazily, so a definition can point at any registered
     * sound without ModernMayhem having to own it.
     */
    @Nullable
    private static RegistryObject<SoundEvent> resolveSound(@Nullable ResourceLocation id) {
        return id == null ? null : RegistryObject.create(id, ForgeRegistries.SOUND_EVENTS);
    }

    @Override
    public boolean hasRainbowPhosphor() {
        return definition.features().rainbowPhosphor();
    }

    /** A goggle that declares its own palettes cycles through those; otherwise the built-in set. */
    @Override
    public int getPaletteCount() {
        return definition.palettes().isEmpty()
                ? super.getPaletteCount()
                : definition.palettes().size();
    }

    @Nullable
    @Override
    public ResourceLocation getPostChain() {
        return definition.postChain();
    }

    /**
     * Armor granted while the goggle is actually down over the face -- matching how ModernMayhem's own
     * visor behaves, since a raised faceplate shouldn't protect anything. Goggles with no {@code stats}
     * block simply grant nothing.
     */
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        Multimap<Attribute, AttributeModifier> modifiers = HashMultimap.create();
        if (!isNVGOnFace(stack)) {
            return modifiers;
        }

        double protection = definition.stats().protection();
        double toughness = definition.stats().toughness();
        double knockback = definition.stats().knockback();

        if (protection > 0) modifiers.put(Attributes.ARMOR, new AttributeModifier(uuid, "Goggle armor", protection, AttributeModifier.Operation.ADDITION));
        if (toughness > 0) modifiers.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Goggle toughness", toughness, AttributeModifier.Operation.ADDITION));
        if (knockback > 0) modifiers.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Goggle knockback resistance", knockback, AttributeModifier.Operation.ADDITION));

        return modifiers;
    }

    @Override
    public ResourceLocation getGoggleModel(boolean hasCoti) {
        return definition.worn().model(hasCoti);
    }

    @Override
    public ResourceLocation getGoggleTexture(boolean hasCoti) {
        return definition.worn().texture(hasCoti);
    }

    @Override
    public ResourceLocation getGoggleAnimation(boolean hasCoti) {
        return definition.worn().animation(hasCoti);
    }

    @Override
    public ResourceLocation getFirstPersonModel(boolean hasCoti) {
        return definition.firstPerson().model(hasCoti);
    }

    @Override
    public ResourceLocation getFirstPersonTexture(boolean hasCoti) {
        return definition.firstPerson().texture(hasCoti);
    }

    @Override
    public ResourceLocation getFirstPersonAnimation(boolean hasCoti) {
        return definition.firstPerson().animation(hasCoti);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GenericSpecialGogglesRenderer<DataGogglesItem> wornRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity entity, ItemStack stack, EquipmentSlot slot, HumanoidModel<?> original) {
                if (this.wornRenderer == null) this.wornRenderer = new GenericSpecialGogglesRenderer<>();
                this.wornRenderer.prepForRender(entity, stack, slot, original);
                return this.wornRenderer;
            }
        });
        consumer.accept(new IClientItemExtensions() {
            private GenericSpecialGogglesRenderer.GenericNVGGogglesSlotRenderer<DataGogglesItem> iconRenderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.iconRenderer == null) {
                    this.iconRenderer = new GenericSpecialGogglesRenderer.GenericNVGGogglesSlotRenderer<>();
                }
                return this.iconRenderer;
            }
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable net.minecraft.world.level.Level level,
                                @NotNull List<net.minecraft.network.chat.Component> tooltip,
                                @NotNull net.minecraft.world.item.TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        DataTooltips.append(definition.tooltip(), tooltip);
    }
}
