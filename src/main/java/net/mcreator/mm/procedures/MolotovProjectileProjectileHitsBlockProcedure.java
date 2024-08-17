package net.mcreator.mm.procedures;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.BlockPos;

import net.mcreator.mm.init.MmModItems;

import java.util.Objects;
import java.util.Map;
import java.util.Iterator;
import java.util.HashMap;

public class MolotovProjectileProjectileHitsBlockProcedure {
	@Mod.EventBusSubscriber
	private static class FireExpiryHandler {
		@SubscribeEvent
		public static void onServerTick(TickEvent.ServerTickEvent event) {
			if (!event.phase.equals(TickEvent.Phase.END))
				return;
			Iterator<Map.Entry<BlockPosWrapper, Long>> iterator = FireBlockExpirations.entrySet().iterator();
			long currentTime = System.currentTimeMillis();
			while (iterator.hasNext()) {
				Map.Entry<BlockPosWrapper, Long> entry = iterator.next();
				if (currentTime >= entry.getValue()) {
					Level level = entry.getKey().getLevel();
					BlockPos pos = entry.getKey().getPos();
					if (level instanceof ServerLevel) {
						level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
					}
					iterator.remove();
				}
			}
		}
	}

	private static final Map<BlockPosWrapper, Long> FireBlockExpirations = new HashMap<>();

	public static void execute(Level world, BlockPos pos, Entity entity) {
		if (world instanceof ServerLevel) {
			int radius = 4;
			// Adicionar blocos de fogo em um raio de 5 blocos ao redor da posição
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					for (int z = -radius; z <= radius; z++) {
						if (Math.sqrt(x * x + y * y + z * z) <= radius) {
							BlockPos firePos = pos.offset(x, y, z);
							if (world.getBlockState(firePos).isAir()) {
								world.setBlock(firePos, Blocks.FIRE.defaultBlockState(), 3);
							}
						}
					}
				}
			}
			world.playSound(null, pos, SoundEvents.SPLASH_POTION_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);
		}
	}

	private static void removeItemFromPlayer(Player player, ItemStack itemStack) {
		for (ItemStack stack : player.getInventory().items) {
			if (stack.getItem() == itemStack.getItem() && stack.getCount() > 0) {
				stack.shrink(1);
				break;
			}
		}
	}

	private static class BlockPosWrapper {
		private final Level level;
		private final BlockPos pos;

		public BlockPosWrapper(Level level, BlockPos pos) {
			this.level = level;
			this.pos = pos;
		}

		public Level getLevel() {
			return level;
		}

		public BlockPos getPos() {
			return pos;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;
			BlockPosWrapper that = (BlockPosWrapper) o;
			return level.equals(that.level) && pos.equals(that.pos);
		}

		@Override
		public int hashCode() {
			return Objects.hash(level, pos);
		}
	}

	static {
		MinecraftForge.EVENT_BUS.register(FireExpiryHandler.class);
	}
}
