package net.tkg.ModernMayhem.server.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.tkg.ModernMayhem.server.registry.BlockEntityRegistryMM;

public class DuffelBagBlockEntity extends BlockEntity {
    private ItemStack duffelBag = ItemStack.EMPTY;

    public DuffelBagBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegistryMM.DUFFEL_BAG.get(), pos, state);
    }

    public void setDuffelBag(ItemStack stack) {
        this.duffelBag = stack.copy();
    }

    public ItemStack getDuffelBag() {
        return duffelBag;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (!duffelBag.isEmpty()) {
            tag.put("Duffel", duffelBag.save(new CompoundTag()));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Duffel")) {
            duffelBag = ItemStack.of(tag.getCompound("Duffel"));
        }
    }
}

