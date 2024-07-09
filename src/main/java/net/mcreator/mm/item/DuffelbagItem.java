
package net.mcreator.mm.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.BundleItem;

public class DuffelbagItem extends BundleItem {
    public DuffelbagItem() {
        super(new BundleItem.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }
}