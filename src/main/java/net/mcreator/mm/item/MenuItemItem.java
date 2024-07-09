
package net.mcreator.mm.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class MenuItemItem extends Item {
	public MenuItemItem() {
		super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
	}
}
