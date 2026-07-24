package net.tkg.ModernMayhem.content.item;

import net.minecraft.network.chat.Component;
import net.tkg.ModernMayhem.content.def.TooltipLine;

import java.util.List;

/** Shared rendering of a definition's {@code tooltip} lines, used by every data-driven item type. */
public final class DataTooltips {

    private DataTooltips() {}

    public static void append(List<TooltipLine> lines, List<Component> tooltip) {
        for (TooltipLine line : lines) {
            tooltip.add(Component.translatable(line.key()).withStyle(line.color()));
        }
    }
}
