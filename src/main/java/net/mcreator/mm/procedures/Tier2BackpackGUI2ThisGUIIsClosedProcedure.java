package net.mcreator.mm.procedures;

import top.theillusivec4.curios.api.CuriosApi;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Entity;

import net.mcreator.mm.init.MmModItems;

import java.util.function.Supplier;
import java.util.Map;

public class Tier2BackpackGUI2ThisGUIIsClosedProcedure {
	public static void execute(LevelAccessor world, Entity entity) {
		if (entity == null)
			return;
		if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.BLACK_BACKPACK_2.get(), lv).isPresent() : false) {
			if (entity instanceof LivingEntity lv) {
				CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.BLACK_BACKPACK_2.get()).forEach(item -> {
					ItemStack itemstackiterator = item.stack();
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 0;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(0));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(1)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 1;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(1));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 2;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(2));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(3)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 3;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(3));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(4)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 4;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(4));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(5)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 5;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(5));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(6)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 6;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(6));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(7)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 7;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(7));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(8)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 8;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(8));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(9)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 9;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(9));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(10)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 10;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(10));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(11)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 11;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(11));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(12)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 12;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(12));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(13)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 13;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(13));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(14)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 14;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(14));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
				});
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.GREEN_BACKPACK_2.get(), lv).isPresent() : false) {
			if (entity instanceof LivingEntity lv) {
				CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.GREEN_BACKPACK_2.get()).forEach(item -> {
					ItemStack itemstackiterator = item.stack();
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 0;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(0));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(1)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 1;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(1));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 2;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(2));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(3)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 3;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(3));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(4)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 4;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(4));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(5)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 5;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(5));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(6)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 6;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(6));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(7)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 7;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(7));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(8)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 8;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(8));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(9)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 9;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(9));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(10)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 10;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(10));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(11)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 11;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(11));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(12)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 12;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(12));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(13)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 13;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(13));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(14)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 14;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(14));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
				});
			}
		} else if (entity instanceof LivingEntity lv ? CuriosApi.getCuriosHelper().findEquippedCurio(MmModItems.TAN_BACKPACK_2.get(), lv).isPresent() : false) {
			if (entity instanceof LivingEntity lv) {
				CuriosApi.getCuriosHelper().findCurios(lv, MmModItems.TAN_BACKPACK_2.get()).forEach(item -> {
					ItemStack itemstackiterator = item.stack();
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(0)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 0;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(0));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(1)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 1;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(1));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(2)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 2;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(2));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(3)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 3;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(3));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(4)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 4;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(4));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(5)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 5;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(5));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(6)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 6;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(6));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(7)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 7;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(7));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(8)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 8;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(8));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(9)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 9;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(9));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(10)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 10;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(10));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(11)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 11;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(11));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(12)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 12;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(12));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(13)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 13;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(13));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
					{
						ItemStack _isc = itemstackiterator;
						final ItemStack _setstack = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof Supplier _splr && _splr.get() instanceof Map _slt ? ((Slot) _slt.get(14)).getItem() : ItemStack.EMPTY).copy();
						final int _sltid = 14;
						_setstack.setCount(new Object() {
							public int getAmount(int sltid) {
								if (entity instanceof Player _player && _player.containerMenu instanceof Supplier _current && _current.get() instanceof Map _slots) {
									ItemStack stack = ((Slot) _slots.get(sltid)).getItem();
									if (stack != null)
										return stack.getCount();
								}
								return 0;
							}
						}.getAmount(14));
						_isc.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(capability -> {
							if (capability instanceof IItemHandlerModifiable itemHandlerModifiable) {
								itemHandlerModifiable.setStackInSlot(_sltid, _setstack);
							}
						});
					}
				});
			}
		}
	}
}
