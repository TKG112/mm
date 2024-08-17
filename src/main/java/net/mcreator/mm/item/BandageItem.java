
package net.mcreator.mm.item;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;

import net.mcreator.mm.procedures.BandageRightclickedProcedure;

public class BandageItem extends Item {
	public BandageItem() {
		super(new Item.Properties().stacksTo(8).rarity(Rarity.COMMON));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		BandageRightclickedProcedure.execute(entity, ar.getObject());
		return ar;
	}
}
