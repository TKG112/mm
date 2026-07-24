package net.tkg.ModernMayhem.content.def;

import net.minecraft.ChatFormatting;

/**
 * One line of descriptive text shown under an item's name in its tooltip.
 * <p>
 * The text itself is a translation key rather than a literal, so a pack can be translated the same
 * way item names are -- see the naming section of the content pack docs.
 */
public record TooltipLine(String key, ChatFormatting color) {
}
