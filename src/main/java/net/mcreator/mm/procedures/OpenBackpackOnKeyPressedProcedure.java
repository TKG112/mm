package net.mcreator.mm.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.MenuProvider;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.mcreator.mm.world.inventory.Tier3BackpackGUI2Menu;
import net.mcreator.mm.world.inventory.Tier2BackpackGUI2Menu;
import net.mcreator.mm.world.inventory.Tier1BackpackGUI2Menu;
import net.mcreator.mm.init.MmModItems;
import net.mcreator.mm.MmMod;

import java.util.function.Supplier;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;

import io.netty.buffer.Unpooled;

public class OpenBackpackOnKeyPressedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_BACKPACK_1.get(), lv).isPresent() : false) {
			if ((entity instanceof Player _plr1 && _plr1.containerMenu instanceof Tier1BackpackGUI2Menu) == false) {
				if (entity instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(x, y, z);
					NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("Tier1BackpackGUI2");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new Tier1BackpackGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				MmMod.queueServerWork(1, () -> {
					if (entity instanceof LivingEntity lv) {
						CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_BACKPACK_1.get()).forEach(item -> {
							ItemStack itemstackiterator = item.stack();
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).getCount());
								((Slot) _slots.get(0)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).getCount());
								((Slot) _slots.get(1)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).getCount());
								((Slot) _slots.get(2)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).getCount());
								((Slot) _slots.get(3)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).getCount());
								((Slot) _slots.get(4)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).getCount());
								((Slot) _slots.get(5)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).getCount());
								((Slot) _slots.get(6)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).getCount());
								((Slot) _slots.get(7)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).getCount());
								((Slot) _slots.get(8)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
						});
					}
				});
			} else {
				if (entity instanceof Player _player)
					_player.closeContainer();
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_BACKPACK_2.get(), lv).isPresent() : false) {
			if ((entity instanceof Player _plr61 && _plr61.containerMenu instanceof Tier2BackpackGUI2Menu) == false) {
				if (entity instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(x, y, z);
					NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("Tier2BackpackGUI2");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new Tier2BackpackGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				MmMod.queueServerWork(1, () -> {
					if (entity instanceof LivingEntity lv) {
						CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_BACKPACK_2.get()).forEach(item -> {
							ItemStack itemstackiterator = item.stack();
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).getCount());
								((Slot) _slots.get(0)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).getCount());
								((Slot) _slots.get(1)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).getCount());
								((Slot) _slots.get(2)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).getCount());
								((Slot) _slots.get(3)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).getCount());
								((Slot) _slots.get(4)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).getCount());
								((Slot) _slots.get(5)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).getCount());
								((Slot) _slots.get(6)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).getCount());
								((Slot) _slots.get(7)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).getCount());
								((Slot) _slots.get(8)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).getCount());
								((Slot) _slots.get(9)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).getCount());
								((Slot) _slots.get(10)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).getCount());
								((Slot) _slots.get(11)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).getCount());
								((Slot) _slots.get(12)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).getCount());
								((Slot) _slots.get(13)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).getCount());
								((Slot) _slots.get(14)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
						});
					}
				});
			} else {
				if (entity instanceof Player _player)
					_player.closeContainer();
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_BACKPACK_3.get(), lv).isPresent() : false) {
			if ((entity instanceof Player _plr157 && _plr157.containerMenu instanceof Tier3BackpackGUI2Menu) == false) {
				if (entity instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(x, y, z);
					NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("Tier3BackpackGUI2");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new Tier3BackpackGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				MmMod.queueServerWork(1, () -> {
					if (entity instanceof LivingEntity lv) {
						CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_BACKPACK_3.get()).forEach(item -> {
							ItemStack itemstackiterator = item.stack();
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).getCount());
								((Slot) _slots.get(0)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).getCount());
								((Slot) _slots.get(1)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).getCount());
								((Slot) _slots.get(2)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).getCount());
								((Slot) _slots.get(3)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).getCount());
								((Slot) _slots.get(4)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).getCount());
								((Slot) _slots.get(5)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).getCount());
								((Slot) _slots.get(6)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).getCount());
								((Slot) _slots.get(7)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).getCount());
								((Slot) _slots.get(8)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).getCount());
								((Slot) _slots.get(9)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).getCount());
								((Slot) _slots.get(10)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).getCount());
								((Slot) _slots.get(11)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).getCount());
								((Slot) _slots.get(12)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).getCount());
								((Slot) _slots.get(13)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).getCount());
								((Slot) _slots.get(14)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(15, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(15, itemstackiterator)).getCount());
								((Slot) _slots.get(15)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(16, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(16, itemstackiterator)).getCount());
								((Slot) _slots.get(16)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(17, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(17, itemstackiterator)).getCount());
								((Slot) _slots.get(17)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(18, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(18, itemstackiterator)).getCount());
								((Slot) _slots.get(18)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(19, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(19, itemstackiterator)).getCount());
								((Slot) _slots.get(19)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(20, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(20, itemstackiterator)).getCount());
								((Slot) _slots.get(20)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
						});
					}
				});
			} else {
				if (entity instanceof Player _player)
					_player.closeContainer();
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.GREEN_BACKPACK_1.get(), lv).isPresent() : false) {
			if ((entity instanceof Player _plr289 && _plr289.containerMenu instanceof Tier1BackpackGUI2Menu) == false) {
				if (entity instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(x, y, z);
					NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("Tier1BackpackGUI2");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new Tier1BackpackGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				MmMod.queueServerWork(1, () -> {
					if (entity instanceof LivingEntity lv) {
						CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.GREEN_BACKPACK_1.get()).forEach(item -> {
							ItemStack itemstackiterator = item.stack();
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).getCount());
								((Slot) _slots.get(0)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).getCount());
								((Slot) _slots.get(1)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).getCount());
								((Slot) _slots.get(2)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).getCount());
								((Slot) _slots.get(3)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).getCount());
								((Slot) _slots.get(4)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).getCount());
								((Slot) _slots.get(5)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).getCount());
								((Slot) _slots.get(6)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).getCount());
								((Slot) _slots.get(7)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).getCount());
								((Slot) _slots.get(8)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
						});
					}
				});
			} else {
				if (entity instanceof Player _player)
					_player.closeContainer();
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.GREEN_BACKPACK_2.get(), lv).isPresent() : false) {
			if ((entity instanceof Player _plr349 && _plr349.containerMenu instanceof Tier2BackpackGUI2Menu) == false) {
				if (entity instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(x, y, z);
					NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("Tier2BackpackGUI2");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new Tier2BackpackGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				MmMod.queueServerWork(1, () -> {
					if (entity instanceof LivingEntity lv) {
						CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.GREEN_BACKPACK_2.get()).forEach(item -> {
							ItemStack itemstackiterator = item.stack();
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).getCount());
								((Slot) _slots.get(0)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).getCount());
								((Slot) _slots.get(1)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).getCount());
								((Slot) _slots.get(2)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).getCount());
								((Slot) _slots.get(3)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).getCount());
								((Slot) _slots.get(4)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).getCount());
								((Slot) _slots.get(5)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).getCount());
								((Slot) _slots.get(6)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).getCount());
								((Slot) _slots.get(7)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).getCount());
								((Slot) _slots.get(8)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).getCount());
								((Slot) _slots.get(9)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).getCount());
								((Slot) _slots.get(10)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).getCount());
								((Slot) _slots.get(11)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).getCount());
								((Slot) _slots.get(12)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).getCount());
								((Slot) _slots.get(13)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).getCount());
								((Slot) _slots.get(14)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
						});
					}
				});
			} else {
				if (entity instanceof Player _player)
					_player.closeContainer();
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.GREEN_BACKPACK_3.get(), lv).isPresent() : false) {
			if ((entity instanceof Player _plr445 && _plr445.containerMenu instanceof Tier3BackpackGUI2Menu) == false) {
				if (entity instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(x, y, z);
					NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("Tier3BackpackGUI2");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new Tier3BackpackGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				MmMod.queueServerWork(1, () -> {
					if (entity instanceof LivingEntity lv) {
						CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.GREEN_BACKPACK_3.get()).forEach(item -> {
							ItemStack itemstackiterator = item.stack();
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).getCount());
								((Slot) _slots.get(0)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).getCount());
								((Slot) _slots.get(1)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).getCount());
								((Slot) _slots.get(2)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).getCount());
								((Slot) _slots.get(3)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).getCount());
								((Slot) _slots.get(4)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).getCount());
								((Slot) _slots.get(5)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).getCount());
								((Slot) _slots.get(6)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).getCount());
								((Slot) _slots.get(7)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).getCount());
								((Slot) _slots.get(8)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).getCount());
								((Slot) _slots.get(9)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).getCount());
								((Slot) _slots.get(10)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).getCount());
								((Slot) _slots.get(11)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).getCount());
								((Slot) _slots.get(12)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).getCount());
								((Slot) _slots.get(13)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).getCount());
								((Slot) _slots.get(14)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(15, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(15, itemstackiterator)).getCount());
								((Slot) _slots.get(15)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(16, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(16, itemstackiterator)).getCount());
								((Slot) _slots.get(16)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(17, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(17, itemstackiterator)).getCount());
								((Slot) _slots.get(17)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(18, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(18, itemstackiterator)).getCount());
								((Slot) _slots.get(18)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(19, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(19, itemstackiterator)).getCount());
								((Slot) _slots.get(19)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(20, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(20, itemstackiterator)).getCount());
								((Slot) _slots.get(20)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
						});
					}
				});
			} else {
				if (entity instanceof Player _player)
					_player.closeContainer();
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.TAN_BACKPACK_1.get(), lv).isPresent() : false) {
			if ((entity instanceof Player _plr577 && _plr577.containerMenu instanceof Tier1BackpackGUI2Menu) == false) {
				if (entity instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(x, y, z);
					NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("Tier1BackpackGUI2");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new Tier1BackpackGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				MmMod.queueServerWork(1, () -> {
					if (entity instanceof LivingEntity lv) {
						CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.TAN_BACKPACK_1.get()).forEach(item -> {
							ItemStack itemstackiterator = item.stack();
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).getCount());
								((Slot) _slots.get(0)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).getCount());
								((Slot) _slots.get(1)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).getCount());
								((Slot) _slots.get(2)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).getCount());
								((Slot) _slots.get(3)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).getCount());
								((Slot) _slots.get(4)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).getCount());
								((Slot) _slots.get(5)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).getCount());
								((Slot) _slots.get(6)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).getCount());
								((Slot) _slots.get(7)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).getCount());
								((Slot) _slots.get(8)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
						});
					}
				});
			} else {
				if (entity instanceof Player _player)
					_player.closeContainer();
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.TAN_BACKPACK_2.get(), lv).isPresent() : false) {
			if ((entity instanceof Player _plr637 && _plr637.containerMenu instanceof Tier2BackpackGUI2Menu) == false) {
				if (entity instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(x, y, z);
					NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("Tier2BackpackGUI2");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new Tier2BackpackGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				MmMod.queueServerWork(1, () -> {
					if (entity instanceof LivingEntity lv) {
						CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.TAN_BACKPACK_2.get()).forEach(item -> {
							ItemStack itemstackiterator = item.stack();
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).getCount());
								((Slot) _slots.get(0)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).getCount());
								((Slot) _slots.get(1)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).getCount());
								((Slot) _slots.get(2)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).getCount());
								((Slot) _slots.get(3)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).getCount());
								((Slot) _slots.get(4)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).getCount());
								((Slot) _slots.get(5)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).getCount());
								((Slot) _slots.get(6)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).getCount());
								((Slot) _slots.get(7)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).getCount());
								((Slot) _slots.get(8)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).getCount());
								((Slot) _slots.get(9)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).getCount());
								((Slot) _slots.get(10)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).getCount());
								((Slot) _slots.get(11)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).getCount());
								((Slot) _slots.get(12)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).getCount());
								((Slot) _slots.get(13)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).getCount());
								((Slot) _slots.get(14)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
						});
					}
				});
			} else {
				if (entity instanceof Player _player)
					_player.closeContainer();
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.TAN_BACKPACK_3.get(), lv).isPresent() : false) {
			if ((entity instanceof Player _plr733 && _plr733.containerMenu instanceof Tier3BackpackGUI2Menu) == false) {
				if (entity instanceof ServerPlayer _ent) {
					BlockPos _bpos = BlockPos.containing(x, y, z);
					NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
						@Override
						public Component getDisplayName() {
							return Component.literal("Tier3BackpackGUI2");
						}

						@Override
						public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
							return new Tier3BackpackGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
						}
					}, _bpos);
				}
				MmMod.queueServerWork(1, () -> {
					if (entity instanceof LivingEntity lv) {
						CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.TAN_BACKPACK_3.get()).forEach(item -> {
							ItemStack itemstackiterator = item.stack();
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(0, itemstackiterator)).getCount());
								((Slot) _slots.get(0)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(1, itemstackiterator)).getCount());
								((Slot) _slots.get(1)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(2, itemstackiterator)).getCount());
								((Slot) _slots.get(2)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(3, itemstackiterator)).getCount());
								((Slot) _slots.get(3)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(4, itemstackiterator)).getCount());
								((Slot) _slots.get(4)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(5, itemstackiterator)).getCount());
								((Slot) _slots.get(5)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(6, itemstackiterator)).getCount());
								((Slot) _slots.get(6)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(7, itemstackiterator)).getCount());
								((Slot) _slots.get(7)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(8, itemstackiterator)).getCount());
								((Slot) _slots.get(8)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(9, itemstackiterator)).getCount());
								((Slot) _slots.get(9)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(10, itemstackiterator)).getCount());
								((Slot) _slots.get(10)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(11, itemstackiterator)).getCount());
								((Slot) _slots.get(11)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(12, itemstackiterator)).getCount());
								((Slot) _slots.get(12)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(13, itemstackiterator)).getCount());
								((Slot) _slots.get(13)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(14, itemstackiterator)).getCount());
								((Slot) _slots.get(14)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(15, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(15, itemstackiterator)).getCount());
								((Slot) _slots.get(15)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(16, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(16, itemstackiterator)).getCount());
								((Slot) _slots.get(16)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(17, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(17, itemstackiterator)).getCount());
								((Slot) _slots.get(17)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(18, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(18, itemstackiterator)).getCount());
								((Slot) _slots.get(18)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(19, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(19, itemstackiterator)).getCount());
								((Slot) _slots.get(19)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
							if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
								ItemStack _setstack = (new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(20, itemstackiterator)).copy();
								_setstack.setCount((new Object() {
									public ItemStack getItemStack(int sltid, ItemStack _isc) {
										AtomicReference<ItemStack> _retval = new AtomicReference<>(ItemStack.EMPTY);
										_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
											_retval.set(capability.getStackInSlot(sltid).copy());
										});
										return _retval.get();
									}
								}.getItemStack(20, itemstackiterator)).getCount());
								((Slot) _slots.get(20)).set(_setstack);
								_player.containerMenu.broadcastChanges();
							}
						});
					}
				});
			} else {
				if (entity instanceof Player _player)
					_player.closeContainer();
			}
		}
	}
}
