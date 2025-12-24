package net.tkg.ModernMayhem.server.item.generic;

import io.netty.buffer.Unpooled;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.tkg.ModernMayhem.server.GUI.GenericBackpackGUI;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.util.List;

public abstract class GenericBackpackItem extends Item implements ICurioItem {
    private final byte curiosSlotType;

    public GenericBackpackItem(byte pCuriosSlotType) {
        super(new Properties().stacksTo(1));
        this.curiosSlotType = pCuriosSlotType;
    }

    public abstract int getInventoryLines();
    public abstract int getInventoryColumns();

    public abstract boolean canSupplyAmmo();

    public int getInventorySize() {
        return getInventoryLines() * getInventoryColumns();
    }

    private void validateAndResizeInventory(ItemStack stack, Player player) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("inventory")) return;

        int configSize = getInventorySize();
        ItemStackHandler currentHandler = new ItemStackHandler();
        currentHandler.deserializeNBT(tag.getCompound("inventory"));

        if (currentHandler.getSlots() != configSize) {
            ItemStackHandler newHandler = new ItemStackHandler(configSize);

            for (int i = 0; i < currentHandler.getSlots(); i++) {
                ItemStack s = currentHandler.getStackInSlot(i);

                if (i < configSize) {
                    newHandler.setStackInSlot(i, s);
                }
                else if (!s.isEmpty()) {
                    if (!player.getInventory().add(s)) {
                        player.drop(s, false);
                    }
                }
            }
            tag.put("inventory", newHandler.serializeNBT());
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if (this.getInventorySize() > 0) {
            ItemStack stack = pPlayer.getItemInHand(pUsedHand);

            if (!pLevel.isClientSide) {
                validateAndResizeInventory(stack, pPlayer);
            }

            InitInventory(stack, this.getInventorySize());
            if (pPlayer instanceof ServerPlayer && pUsedHand == InteractionHand.MAIN_HAND) {
                OpenGUIFromPlayerInventory(pPlayer, stack);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    @Override
    public boolean overrideStackedOnOther(ItemStack pStack, Slot pSlot, ClickAction pAction, Player pPlayer) {
        if (this.getInventorySize() > 0) {
            ItemStack slotStack = pSlot.getItem();
            CompoundTag tag = InitInventory(pStack, this.getInventorySize());

            if (pAction == ClickAction.SECONDARY) {
                ItemStackHandler inventory = new ItemStackHandler(getInventorySize());
                inventory.deserializeNBT(tag.getCompound("inventory"));

                if (slotStack.isEmpty()) {
                    for (int i = inventory.getSlots() - 1; i >= 0; i--) {
                        ItemStack stack = inventory.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            pSlot.set(inventory.extractItem(i, stack.getCount(), false));
                            tag.put("inventory", inventory.serializeNBT());
                            return true;
                        }
                    }
                } else {
                    for (int i = 0; i < inventory.getSlots(); i++) {
                        ItemStack stack = inventory.getStackInSlot(i);
                        if (ItemStack.isSameItemSameTags(slotStack, inventory.getStackInSlot(i))) {
                            ItemStack remaining = inventory.insertItem(i, slotStack, false);
                            pSlot.set(remaining);
                            tag.put("inventory", inventory.serializeNBT());
                            if (remaining.getCount() <= 0) return true;
                        }
                        if (stack.isEmpty()) {
                            inventory.insertItem(i, slotStack, false);
                            pSlot.set(ItemStack.EMPTY);
                            tag.put("inventory", inventory.serializeNBT());
                            return true;
                        }
                    }
                }
            }
        }
        return super.overrideStackedOnOther(pStack, pSlot, pAction, pPlayer);
    }

    @Override
    public boolean overrideOtherStackedOnMe(ItemStack pStack, ItemStack pOther, Slot pSlot, ClickAction pAction, Player pPlayer, SlotAccess pAccess) {
        return super.overrideOtherStackedOnMe(pStack, pOther, pSlot, pAction, pPlayer, pAccess);
    }

    protected static CompoundTag InitInventory(ItemStack pStack, int pInventorySize) {
        CompoundTag tag = pStack.getOrCreateTag();
        if (!tag.contains("inventory")) {
            tag.put("inventory", new ItemStackHandler(pInventorySize).serializeNBT());
        }
        return tag;
    }

    public void OpenGUIFromPlayerInventory(Player pPlayer, ItemStack pStack) {
        if (this.getInventorySize() > 0) {
            ServerPlayer player = (ServerPlayer) pPlayer;
            FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
            CompoundTag tag = pStack.getOrCreateTag();

            validateAndResizeInventory(pStack, pPlayer);

            if (tag.contains("inventory")) {
                data.writeByte(this.getInventoryLines());
                data.writeByte(this.getInventoryColumns());
                data.writeNbt(tag.getCompound("inventory"));
                data.writeBoolean(false);
                data.writeItemStack(pStack, false);
            }
            NetworkHooks.openScreen(player, new SimpleMenuProvider(((pContainerId, pPlayerInventory, pPlayer1) -> new GenericBackpackGUI(pContainerId, pPlayerInventory, data)), pStack.getDisplayName()), friendlyByteBuf -> {
                friendlyByteBuf.writeByte(getInventoryLines());
                friendlyByteBuf.writeByte(getInventoryColumns());
                friendlyByteBuf.writeNbt(tag.getCompound("inventory"));
                friendlyByteBuf.writeBoolean(false);
                friendlyByteBuf.writeItemStack(pStack, false);
            });
        }
    }

    public void OpenGUIFromCuriosInventory(Player pPlayer, ItemStack pStack) {
        if (this.getInventorySize() > 0) {
            ServerPlayer player = (ServerPlayer) pPlayer;
            FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
            CompoundTag tag = pStack.getOrCreateTag();
            boolean resetStackInInv = !tag.contains("inventory");
            String curiosSlotTypeIdentifer = switch (this.curiosSlotType) {
                case 0 -> "back";
                case 1 -> "body";
                default -> "";
            };
            int backpackSlotID = switch (this.curiosSlotType) {
                case 0 -> CuriosUtil.getBackpackSlotID(pPlayer);
                case 1 -> CuriosUtil.getRigSlotID(pPlayer);
                default -> -1;
            };

            validateAndResizeInventory(pStack, pPlayer);

            InitInventory(pStack, getInventorySize());
            ICuriosItemHandler playerCuriosInventory = CuriosApi.getCuriosInventory(pPlayer).resolve().get();
            if (resetStackInInv) {
                playerCuriosInventory.getStacksHandler(curiosSlotTypeIdentifer).ifPresent(iCurioStacksHandler -> {
                    iCurioStacksHandler.getStacks().setStackInSlot(backpackSlotID, pStack);
                });
                playerCuriosInventory = CuriosApi.getCuriosInventory(pPlayer).resolve().get();
            }
            data.writeByte(this.getInventoryLines());
            data.writeByte(this.getInventoryColumns());
            data.writeNbt(tag.getCompound("inventory"));
            data.writeBoolean(true);
            data.writeByte(backpackSlotID);
            data.writeByte(this.curiosSlotType);

            ICuriosItemHandler finalPlayerCuriosInventory = playerCuriosInventory;
            NetworkHooks.openScreen(player, new SimpleMenuProvider(((pContainerId, pPlayerInventory, pPlayer1) -> new GenericBackpackGUI(pContainerId, pPlayerInventory, data, finalPlayerCuriosInventory)), pStack.getDisplayName()), friendlyByteBuf -> {
                friendlyByteBuf.writeByte(getInventoryLines());
                friendlyByteBuf.writeByte(getInventoryColumns());
                friendlyByteBuf.writeNbt(tag.getCompound("inventory"));
                friendlyByteBuf.writeBoolean(true);
                friendlyByteBuf.writeByte(backpackSlotID);
                friendlyByteBuf.writeByte(this.curiosSlotType);
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("inventory")) {
            ItemStackHandler inventory = new ItemStackHandler();
            inventory.deserializeNBT(tag.getCompound("inventory"));

            int shownItems = 0;
            for (int i = 0; i < inventory.getSlots(); i++) {
                ItemStack item = inventory.getStackInSlot(i);
                if (!item.isEmpty()) {
                    MutableComponent line = Component.literal("• ").withStyle(ChatFormatting.GRAY);
                    line.append(item.getHoverName());
                    line.append(Component.literal(" x" + item.getCount()).withStyle(ChatFormatting.GRAY));
                    tooltip.add(line);
                    shownItems++;
                    if (shownItems >= 5) break;
                }
            }

            int totalItems = (int) java.util.stream.IntStream.range(0, inventory.getSlots())
                    .mapToObj(inventory::getStackInSlot)
                    .filter(s -> !s.isEmpty())
                    .count();

            if (shownItems < totalItems) {
                tooltip.add(Component.literal("...and " + (totalItems - shownItems) + " more")
                        .withStyle(ChatFormatting.DARK_GRAY));
            }
        }
    }
}