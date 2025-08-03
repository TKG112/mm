package net.tkg.ModernMayhem.server.item.curios.facewear;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.renderer.curios.facewear.VisorRenderer;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class VisorItems extends Item implements GeoItem, ICurioItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int variant;

    private static final TagKey<Item> HAS_HEAD_MOUNT_TAG = ItemTags.create(fromNamespaceAndPath(ModernMayhemMod.ID, "has_head_mount"));

    public static final RawAnimation ANIM_IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation ANIM_OPEN = RawAnimation.begin().thenPlay("opening").thenLoop("opened");
    public static final RawAnimation ANIM_CLOSE = RawAnimation.begin().thenPlay("closing").thenLoop("closed");

    public VisorItems(int variant) {
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
        this.variant = variant;
    }

    public static void switchEquipState(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("VisorOnFace")) {
            boolean visorCheck = tag.getBoolean("VisorOnFace");
            tag.putBoolean("VisorOnFace", !visorCheck);
        } else {
            tag.putBoolean("VisorOnFace", true);
        }
        stack.setTag(tag);
    }

    public static boolean isVisorDown(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("VisorOnFace")) {
            return tag.getBoolean("VisorOnFace");
        }
        return false;
    }

    private void updateVisorMode(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("VisorCheck")) {
            boolean nvgCheck = tag.getBoolean("VisorCheck");
            tag.putInt("visor_mode", nvgCheck ? 1 : 0);
        } else {
            tag.putInt("visor_mode", 0);
        }
        stack.setTag(tag);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (!(entity instanceof LivingEntity)) return;

        if (entity.level().isClientSide()) {
            updateVisorMode(stack);
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

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack) {
        if (isVisorDown(stack)) {
            return ImmutableMultimap.of(
                    Attributes.ARMOR,
                    new AttributeModifier(uuid, "Visor armor bonus", 4.0, AttributeModifier.Operation.ADDITION)
            );
        }
        return ImmutableMultimap.of();
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> lRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new VisorRenderer();
                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }

        });
        consumer.accept(new IClientItemExtensions() {
            private VisorRenderer.VisorItemRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new VisorRenderer.VisorItemRenderer();

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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, 1, state -> {
            Entity entity = state.getData(DataTickets.ENTITY);
            if (entity == null || !entity.level().isClientSide()) {
                return PlayState.STOP;
            }
            if (entity instanceof Player player) {
                if (CuriosUtil.hasVisorEquipped(player)) {
                    ItemStack stack = CuriosUtil.getFaceWearItem(player);
                    if (stack.getItem() instanceof VisorItems) {
                        CompoundTag tag = stack.getOrCreateTag();
                        if (tag.contains("VisorOnFace")) {
                            if (tag.getBoolean("VisorOnFace")) {
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

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public int getVariant() {
        return variant;
    }
}
