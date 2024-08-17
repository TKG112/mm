package net.mcreator.mm.item;

import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.InteractionResult;

import net.mcreator.mm.procedures.DuffelbagRightclickedOnBlockProcedure;

public class DuffelbagItem extends BundleItem {
	public DuffelbagItem() {
		super(new BundleItem.Properties().stacksTo(1).rarity(Rarity.COMMON));
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		super.useOn(context);
		DuffelbagRightclickedOnBlockProcedure.execute(context.getLevel(), context.getClickedPos().getX(), context.getClickedPos().getY(), context.getClickedPos().getZ(), context.getPlayer(), context.getItemInHand());
		return InteractionResult.SUCCESS;
	}
}
