package net.tkg.ModernMayhem.server.item.curios.head;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.renderer.curios.head.HeadGearRenderer;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.function.Consumer;

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public class HeadGearItems extends Item implements GeoItem, ICurioItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final int type;
    private final int variant;
    private final boolean hasFaceWearCapability;

    private static final TagKey<Item> GAS_MASK_INCOMPATIBLE = ItemTags.create(fromNamespaceAndPath(ModernMayhemMod.ID, "gas_mask_incompatible"));

    public HeadGearItems(int type, int variant) {
        this(type, variant, false);
    }

    public HeadGearItems(int type, int variant, boolean pHasFaceWearCapability) {
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
        this.type = type;
        this.variant = variant;
        this.hasFaceWearCapability = pHasFaceWearCapability;
        /* In order, the types are :
         *   - 0 : balaclava
         *   - 1 : glasses
         *   - 2 : goggles
         *   - 3 : headset
         *   - 4 : military balaclava
         *   - 5 : gas mask
         */
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> lRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new HeadGearRenderer();
                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }

        });
        consumer.accept(new IClientItemExtensions() {
            private HeadGearRenderer.HeadGearItemRenderer renderer = null;
            // Don't instantiate until ready. This prevents race conditions breaking things
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new HeadGearRenderer.HeadGearItemRenderer();

                return renderer;
            }
        });
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        Entity entity = slotContext.entity();
        if (!(entity instanceof LivingEntity)) return;

        // Only check for gas mask (type 5) incompatibility
        if (this.type != 5) return;

        if (entity instanceof Player player && !player.level().isClientSide()) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);

            // Check if wearing helmet from incompatible tag
            if (!helmet.isEmpty() && helmet.is(GAS_MASK_INCOMPATIBLE)) {
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

        // Only check for gas mask (type 5) incompatibility
        if (this.type != 5) return true;

        ItemStack headItem = player.getItemBySlot(EquipmentSlot.HEAD);
        // Can't equip gas mask if wearing helmet from incompatible tag
        return headItem.isEmpty() || !headItem.is(GAS_MASK_INCOMPATIBLE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public int getType() {
        return type;
    }

    public int getVariant() {
        return variant;
    }

    public boolean hasFaceWearCapability() { return this.hasFaceWearCapability; }
}