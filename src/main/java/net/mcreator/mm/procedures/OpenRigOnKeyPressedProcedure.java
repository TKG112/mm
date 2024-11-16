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

import net.mcreator.mm.world.inventory.RigGUI2Menu;
import net.mcreator.mm.world.inventory.ReconGUI2Menu;
import net.mcreator.mm.world.inventory.BandoleerGUI2Menu;
import net.mcreator.mm.world.inventory.AmmoGUI2Menu;
import net.mcreator.mm.init.MmModItems;
import net.mcreator.mm.MmMod;

import java.util.function.Supplier;
import java.util.concurrent.atomic.AtomicReference;
import java.util.Map;

import io.netty.buffer.Unpooled;

public class OpenRigOnKeyPressedProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_PLATE_CARRIER_AMMO.get(), lv).isPresent() : false) {
			if (entity instanceof ServerPlayer _ent) {
				BlockPos _bpos = BlockPos.containing(x, y, z);
				NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("AmmoGUI2");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
						return new AmmoGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
					}
				}, _bpos);
			}
			MmMod.queueServerWork(1, () -> {
				if (entity instanceof LivingEntity lv) {
					CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_PLATE_CARRIER_AMMO.get()).forEach(item -> {
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
					});
				}
			});
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_PLATE_CARRIER_POUCHES.get(), lv).isPresent() : false) {
			if (entity instanceof ServerPlayer _ent) {
				BlockPos _bpos = BlockPos.containing(x, y, z);
				NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("RigGUI2");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
						return new RigGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
					}
				}, _bpos);
			}
			MmMod.queueServerWork(1, () -> {
				if (entity instanceof LivingEntity lv) {
					CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_PLATE_CARRIER_POUCHES.get()).forEach(item -> {
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
					});
				}
			});
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BANDOLEER.get(), lv).isPresent() : false) {
			if (entity instanceof ServerPlayer _ent) {
				BlockPos _bpos = BlockPos.containing(x, y, z);
				NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("BandoleerGUI2");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
						return new BandoleerGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
					}
				}, _bpos);
			}
			MmMod.queueServerWork(1, () -> {
				if (entity instanceof LivingEntity lv) {
					CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BANDOLEER.get()).forEach(item -> {
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
					});
				}
			});
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.RECON.get(), lv).isPresent() : false) {
			if (entity instanceof ServerPlayer _ent) {
				BlockPos _bpos = BlockPos.containing(x, y, z);
				NetworkHooks.openScreen((ServerPlayer) _ent, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("ReconGUI2");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
						return new ReconGUI2Menu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
					}
				}, _bpos);
			}
			MmMod.queueServerWork(1, () -> {
				if (entity instanceof LivingEntity lv) {
					CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.RECON.get()).forEach(item -> {
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
					});
				}
			});
		}
	}
}
