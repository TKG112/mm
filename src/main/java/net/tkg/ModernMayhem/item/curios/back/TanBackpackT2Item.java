package net.tkg.ModernMayhem.item.curios.back;

import io.netty.buffer.Unpooled;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.util.LazyOptional;
import net.tkg.ModernMayhem.GUI.BackpackT2GUIMenu;
import net.tkg.ModernMayhem.GUI.TestBackpackGUIMenu;
import net.tkg.ModernMayhem.client.renderer.TanBackpackT2Renderer;
import net.tkg.ModernMayhem.item.generic.GenericBackpackItem;
import net.tkg.ModernMayhem.util.CuriosUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class TanBackpackT2Item extends GenericBackpackItem implements GeoItem, ICurioItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public TanBackpackT2Item() {
        super(3, 6);
    }

    @Override
    public void OpenGUIFromPlayerInventory(Player pPlayer, ItemStack pStack) {
        pPlayer.openMenu(new MenuProvider() {
            @Override
            public @NotNull Component getDisplayName() {
                return Component.literal("Test Backpack");
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
                CompoundTag tag = pStack.getOrCreateTag();
                if (tag.contains("inventory")) {
                    System.out.println("Found inventory when opening the backpack gui");
                    data.writeNbt(tag.getCompound("inventory"));
                    data.writeBoolean(false);
                    data.writeItemStack(pStack, false);
                }
                return new BackpackT2GUIMenu(pContainerId, pPlayerInventory, data);
            }
        });
    }

    @Override
    public void OpenGUIFromCuriosInventory(Player pPlayer, ItemStack pStack) {
        pPlayer.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Test Backpack");
            }

            @Override
            public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
                CompoundTag tag = pStack.getOrCreateTag();
                boolean resetStackInInv = false;
                if (!tag.contains("inventory")) {
                    resetStackInInv = true;
                }
                InitInventory(pStack, 3*6);
                data.writeNbt(tag.getCompound("inventory"));
                data.writeBoolean(true);
                data.writeItemStack(pStack, false);
                int backpackSlotID = CuriosUtil.getBackpackSlotID(pPlayer);
                data.writeInt(backpackSlotID);
                ICuriosItemHandler playerCuriosInventory = CuriosApi.getCuriosInventory(pPlayer).resolve().get();
                if (resetStackInInv) {
                    playerCuriosInventory.getStacksHandler("back").ifPresent(iCurioStacksHandler -> {
                        iCurioStacksHandler.getStacks().setStackInSlot(backpackSlotID, pStack);
                    });
                    playerCuriosInventory = CuriosApi.getCuriosInventory(pPlayer).resolve().get();
                }
                return new BackpackT2GUIMenu(pContainerId, pPlayerInventory, playerCuriosInventory, data);
            }
        });
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private GeoArmorRenderer<?> lRenderer;

            @Override
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                if (this.lRenderer == null)
                    this.lRenderer = new TanBackpackT2Renderer();
                this.lRenderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
                return this.lRenderer;
            }

        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, (state) -> PlayState.CONTINUE));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
