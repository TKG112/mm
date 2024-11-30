package net.tkg.ModernMayhem.item.generic;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.tkg.ModernMayhem.item.type.InventoryCapableItem;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public abstract class GenericBackapackItem extends Item implements ICurioItem, InventoryCapableItem {
    private final int inventorySize;
    private final int inventoryLines;
    private final int inventoryColumns;

    public GenericBackapackItem(int pInventoryLines, int pInventoryColumns) {
        super(new Properties().stacksTo(1));
        this.inventorySize = pInventoryLines * pInventoryColumns;
        this.inventoryLines = pInventoryLines;
        this.inventoryColumns = pInventoryColumns;
    }

    @Override
    public int GetInventorySize() {
        return this.inventorySize;
    }

    public static int add(ItemStack pInventoryCapableItemStack, ItemStack pInsertedStack) {
        if (!pInsertedStack.isEmpty() && pInsertedStack.getItem().canFitInsideContainerItems()) {
            CompoundTag compoundtag = pInventoryCapableItemStack.getOrCreateTag();
            if (!compoundtag.contains("Items")) {
                compoundtag.put("Items", new ListTag());
            }
            ListTag listtag = compoundtag.getList("Items", 10);
            ItemStack itemstack1 = pInsertedStack.copy();
            CompoundTag compoundtag2 = new CompoundTag();
            itemstack1.save(compoundtag2);
            listtag.add(0, (Tag)compoundtag2);

            return 1;
        }
        return 0;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        if (pPlayer.isShiftKeyDown() && pPlayer instanceof ServerPlayer serverPlayer) {
            openGUI(serverPlayer, pPlayer.getInventory());
        }
        else if (dropContents(itemstack, pPlayer)) {
            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, @NotNull Slot pSlot, @NotNull ClickAction pAction, @NotNull Player pPlayer) {
        if (pStack.getCount() != 1 || pAction != ClickAction.SECONDARY) {
            return false;
        } else {
            ItemStack itemstack = pSlot.getItem();
            if (itemstack.isEmpty()) {
                InventoryCapableItem.removeOne(pStack).ifPresent((p_150740_) -> {
                    add(pStack, pSlot.safeInsert(p_150740_));
                });
            } else if (itemstack.getItem().canFitInsideContainerItems()) {
                int j = add(pStack, pSlot.safeTake(itemstack.getCount(), 1, pPlayer));
            }

            return true;
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("item.minecraft.bundle.fullness", null, 64).withStyle(ChatFormatting.GRAY));
    }

    @Override
    public boolean canFitInsideContainerItems() {
        return false;
    }

    @Override
    public boolean isBarVisible(ItemStack pStack) {
        return ShowCapacityBarVisible() && pStack.hasTag() && pStack.getTag().contains("Items");
    }
}
