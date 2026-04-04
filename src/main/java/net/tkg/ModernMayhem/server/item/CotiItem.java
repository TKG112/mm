package net.tkg.ModernMayhem.server.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CotiItem extends Item {
    public CotiItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
                tooltip.add(Component.translatable("description.mm.coti_instructions").withStyle(ChatFormatting.GRAY));

                // TODO : Remove this disclaimer once a proper fix for people not understanding how to use the item is implemented
                tooltip.add(Component.literal("⚠").withStyle(ChatFormatting.RED).append(Component.translatable("description.mm.coti_instructions_temporary_disclaimer").withStyle(ChatFormatting.YELLOW)));
    }
}
