package net.mcreator.mm.network;

import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.Capability;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.client.Minecraft;

import net.mcreator.mm.MmMod;

import java.util.function.Supplier;
import java.util.ArrayList;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MmModVariables {
	@SubscribeEvent
	public static void init(FMLCommonSetupEvent event) {
		MmMod.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
	}

	@SubscribeEvent
	public static void init(RegisterCapabilitiesEvent event) {
		event.register(PlayerVariables.class);
	}

	@Mod.EventBusSubscriber
	public static class EventBusVariableHandlers {
		@SubscribeEvent
		public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}

		@SubscribeEvent
		public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}

		@SubscribeEvent
		public static void clonePlayer(PlayerEvent.Clone event) {
			event.getOriginal().revive();
			PlayerVariables original = ((PlayerVariables) event.getOriginal().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			PlayerVariables clone = ((PlayerVariables) event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
			clone.bp1_save = original.bp1_save;
			clone.bp2_save = original.bp2_save;
			clone.bp3_save = original.bp3_save;
			clone.NGV_Black_Shader = original.NGV_Black_Shader;
			clone.NVG_Black_Check = original.NVG_Black_Check;
			clone.rig_save = original.rig_save;
			clone.bp3_slot3 = original.bp3_slot3;
			clone.NVG_Black_Tube_Gain = original.NVG_Black_Tube_Gain;
			clone.TVG_Check = original.TVG_Check;
			clone.TVG_Shader = original.TVG_Shader;
			if (!event.isWasDeath()) {
				clone.bp1_slot0 = original.bp1_slot0;
				clone.bp1_slot1 = original.bp1_slot1;
				clone.bp1_slot2 = original.bp1_slot2;
				clone.bp1_slot3 = original.bp1_slot3;
				clone.bp1_slot4 = original.bp1_slot4;
				clone.bp1_slot5 = original.bp1_slot5;
				clone.bp1_slot6 = original.bp1_slot6;
				clone.bp1_slot7 = original.bp1_slot7;
				clone.bp1_slot8 = original.bp1_slot8;
				clone.bp2_slot0 = original.bp2_slot0;
				clone.bp2_slot1 = original.bp2_slot1;
				clone.bp2_slot10 = original.bp2_slot10;
				clone.bp2_slot11 = original.bp2_slot11;
				clone.bp2_slot12 = original.bp2_slot12;
				clone.bp2_slot13 = original.bp2_slot13;
				clone.bp2_slot14 = original.bp2_slot14;
				clone.bp2_slot2 = original.bp2_slot2;
				clone.bp2_slot3 = original.bp2_slot3;
				clone.bp2_slot4 = original.bp2_slot4;
				clone.bp2_slot5 = original.bp2_slot5;
				clone.bp2_slot6 = original.bp2_slot6;
				clone.bp2_slot7 = original.bp2_slot7;
				clone.bp2_slot8 = original.bp2_slot8;
				clone.bp2_slot9 = original.bp2_slot9;
				clone.bp3_slot0 = original.bp3_slot0;
				clone.bp3_slot1 = original.bp3_slot1;
				clone.bp3_slot10 = original.bp3_slot10;
				clone.bp3_slot11 = original.bp3_slot11;
				clone.bp3_slot12 = original.bp3_slot12;
				clone.bp3_slot13 = original.bp3_slot13;
				clone.bp3_slot14 = original.bp3_slot14;
				clone.bp3_slot15 = original.bp3_slot15;
				clone.bp3_slot16 = original.bp3_slot16;
				clone.bp3_slot17 = original.bp3_slot17;
				clone.bp3_slot18 = original.bp3_slot18;
				clone.bp3_slot19 = original.bp3_slot19;
				clone.bp3_slot2 = original.bp3_slot2;
				clone.bp3_slot20 = original.bp3_slot20;
				clone.bp3_slot4 = original.bp3_slot4;
				clone.bp3_slot5 = original.bp3_slot5;
				clone.bp3_slot6 = original.bp3_slot6;
				clone.bp3_slot7 = original.bp3_slot7;
				clone.bp3_slot8 = original.bp3_slot8;
				clone.bp3_slot9 = original.bp3_slot9;
				clone.rig_slot0 = original.rig_slot0;
				clone.rig_slot1 = original.rig_slot1;
				clone.rig_slot2 = original.rig_slot2;
				clone.rig_slot3 = original.rig_slot3;
				clone.Helmet_Using = original.Helmet_Using;
			}
			if (!event.getEntity().level().isClientSide()) {
				for (Entity entityiterator : new ArrayList<>(event.getEntity().level().players())) {
					((PlayerVariables) entityiterator.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables())).syncPlayerVariables(entityiterator);
				}
			}
		}
	}

	public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<PlayerVariables>() {
	});

	@Mod.EventBusSubscriber
	private static class PlayerVariablesProvider implements ICapabilitySerializable<Tag> {
		@SubscribeEvent
		public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
				event.addCapability(new ResourceLocation("mm", "player_variables"), new PlayerVariablesProvider());
		}

		private final PlayerVariables playerVariables = new PlayerVariables();
		private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return cap == PLAYER_VARIABLES_CAPABILITY ? instance.cast() : LazyOptional.empty();
		}

		@Override
		public Tag serializeNBT() {
			return playerVariables.writeNBT();
		}

		@Override
		public void deserializeNBT(Tag nbt) {
			playerVariables.readNBT(nbt);
		}
	}

	public static class PlayerVariables {
		public boolean bp1_save = false;
		public ItemStack bp1_slot0 = ItemStack.EMPTY;
		public ItemStack bp1_slot1 = ItemStack.EMPTY;
		public ItemStack bp1_slot2 = ItemStack.EMPTY;
		public ItemStack bp1_slot3 = ItemStack.EMPTY;
		public ItemStack bp1_slot4 = ItemStack.EMPTY;
		public ItemStack bp1_slot5 = ItemStack.EMPTY;
		public ItemStack bp1_slot6 = ItemStack.EMPTY;
		public ItemStack bp1_slot7 = ItemStack.EMPTY;
		public ItemStack bp1_slot8 = ItemStack.EMPTY;
		public boolean bp2_save = true;
		public ItemStack bp2_slot0 = ItemStack.EMPTY;
		public ItemStack bp2_slot1 = ItemStack.EMPTY;
		public ItemStack bp2_slot10 = ItemStack.EMPTY;
		public ItemStack bp2_slot11 = ItemStack.EMPTY;
		public ItemStack bp2_slot12 = ItemStack.EMPTY;
		public ItemStack bp2_slot13 = ItemStack.EMPTY;
		public ItemStack bp2_slot14 = ItemStack.EMPTY;
		public ItemStack bp2_slot2 = ItemStack.EMPTY;
		public ItemStack bp2_slot3 = ItemStack.EMPTY;
		public ItemStack bp2_slot4 = ItemStack.EMPTY;
		public ItemStack bp2_slot5 = ItemStack.EMPTY;
		public ItemStack bp2_slot6 = ItemStack.EMPTY;
		public ItemStack bp2_slot7 = ItemStack.EMPTY;
		public ItemStack bp2_slot8 = ItemStack.EMPTY;
		public ItemStack bp2_slot9 = ItemStack.EMPTY;
		public boolean bp3_save = true;
		public ItemStack bp3_slot0 = ItemStack.EMPTY;
		public ItemStack bp3_slot1 = ItemStack.EMPTY;
		public ItemStack bp3_slot10 = ItemStack.EMPTY;
		public ItemStack bp3_slot11 = ItemStack.EMPTY;
		public ItemStack bp3_slot12 = ItemStack.EMPTY;
		public ItemStack bp3_slot13 = ItemStack.EMPTY;
		public ItemStack bp3_slot14 = ItemStack.EMPTY;
		public ItemStack bp3_slot15 = ItemStack.EMPTY;
		public ItemStack bp3_slot16 = ItemStack.EMPTY;
		public ItemStack bp3_slot17 = ItemStack.EMPTY;
		public ItemStack bp3_slot18 = ItemStack.EMPTY;
		public ItemStack bp3_slot19 = ItemStack.EMPTY;
		public ItemStack bp3_slot2 = ItemStack.EMPTY;
		public ItemStack bp3_slot20 = ItemStack.EMPTY;
		public ItemStack bp3_slot4 = ItemStack.EMPTY;
		public ItemStack bp3_slot5 = ItemStack.EMPTY;
		public ItemStack bp3_slot6 = ItemStack.EMPTY;
		public ItemStack bp3_slot7 = ItemStack.EMPTY;
		public ItemStack bp3_slot8 = ItemStack.EMPTY;
		public ItemStack bp3_slot9 = ItemStack.EMPTY;
		public boolean NGV_Black_Shader = false;
		public boolean NVG_Black_Check = false;
		public boolean rig_save = true;
		public ItemStack rig_slot0 = ItemStack.EMPTY;
		public ItemStack rig_slot1 = ItemStack.EMPTY;
		public ItemStack rig_slot2 = ItemStack.EMPTY;
		public ItemStack rig_slot3 = ItemStack.EMPTY;
		public ItemStack bp3_slot3 = ItemStack.EMPTY;
		public double NVG_Black_Tube_Gain = 0;
		public boolean Helmet_Using = false;
		public boolean TVG_Check = false;
		public boolean TVG_Shader = false;

		public void syncPlayerVariables(Entity entity) {
			if (entity instanceof ServerPlayer serverPlayer)
				MmMod.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(entity.level()::dimension), new PlayerVariablesSyncMessage(this, entity.getId()));
		}

		public Tag writeNBT() {
			CompoundTag nbt = new CompoundTag();
			nbt.putBoolean("bp1_save", bp1_save);
			nbt.put("bp1_slot0", bp1_slot0.save(new CompoundTag()));
			nbt.put("bp1_slot1", bp1_slot1.save(new CompoundTag()));
			nbt.put("bp1_slot2", bp1_slot2.save(new CompoundTag()));
			nbt.put("bp1_slot3", bp1_slot3.save(new CompoundTag()));
			nbt.put("bp1_slot4", bp1_slot4.save(new CompoundTag()));
			nbt.put("bp1_slot5", bp1_slot5.save(new CompoundTag()));
			nbt.put("bp1_slot6", bp1_slot6.save(new CompoundTag()));
			nbt.put("bp1_slot7", bp1_slot7.save(new CompoundTag()));
			nbt.put("bp1_slot8", bp1_slot8.save(new CompoundTag()));
			nbt.putBoolean("bp2_save", bp2_save);
			nbt.put("bp2_slot0", bp2_slot0.save(new CompoundTag()));
			nbt.put("bp2_slot1", bp2_slot1.save(new CompoundTag()));
			nbt.put("bp2_slot10", bp2_slot10.save(new CompoundTag()));
			nbt.put("bp2_slot11", bp2_slot11.save(new CompoundTag()));
			nbt.put("bp2_slot12", bp2_slot12.save(new CompoundTag()));
			nbt.put("bp2_slot13", bp2_slot13.save(new CompoundTag()));
			nbt.put("bp2_slot14", bp2_slot14.save(new CompoundTag()));
			nbt.put("bp2_slot2", bp2_slot2.save(new CompoundTag()));
			nbt.put("bp2_slot3", bp2_slot3.save(new CompoundTag()));
			nbt.put("bp2_slot4", bp2_slot4.save(new CompoundTag()));
			nbt.put("bp2_slot5", bp2_slot5.save(new CompoundTag()));
			nbt.put("bp2_slot6", bp2_slot6.save(new CompoundTag()));
			nbt.put("bp2_slot7", bp2_slot7.save(new CompoundTag()));
			nbt.put("bp2_slot8", bp2_slot8.save(new CompoundTag()));
			nbt.put("bp2_slot9", bp2_slot9.save(new CompoundTag()));
			nbt.putBoolean("bp3_save", bp3_save);
			nbt.put("bp3_slot0", bp3_slot0.save(new CompoundTag()));
			nbt.put("bp3_slot1", bp3_slot1.save(new CompoundTag()));
			nbt.put("bp3_slot10", bp3_slot10.save(new CompoundTag()));
			nbt.put("bp3_slot11", bp3_slot11.save(new CompoundTag()));
			nbt.put("bp3_slot12", bp3_slot12.save(new CompoundTag()));
			nbt.put("bp3_slot13", bp3_slot13.save(new CompoundTag()));
			nbt.put("bp3_slot14", bp3_slot14.save(new CompoundTag()));
			nbt.put("bp3_slot15", bp3_slot15.save(new CompoundTag()));
			nbt.put("bp3_slot16", bp3_slot16.save(new CompoundTag()));
			nbt.put("bp3_slot17", bp3_slot17.save(new CompoundTag()));
			nbt.put("bp3_slot18", bp3_slot18.save(new CompoundTag()));
			nbt.put("bp3_slot19", bp3_slot19.save(new CompoundTag()));
			nbt.put("bp3_slot2", bp3_slot2.save(new CompoundTag()));
			nbt.put("bp3_slot20", bp3_slot20.save(new CompoundTag()));
			nbt.put("bp3_slot4", bp3_slot4.save(new CompoundTag()));
			nbt.put("bp3_slot5", bp3_slot5.save(new CompoundTag()));
			nbt.put("bp3_slot6", bp3_slot6.save(new CompoundTag()));
			nbt.put("bp3_slot7", bp3_slot7.save(new CompoundTag()));
			nbt.put("bp3_slot8", bp3_slot8.save(new CompoundTag()));
			nbt.put("bp3_slot9", bp3_slot9.save(new CompoundTag()));
			nbt.putBoolean("NGV_Black_Shader", NGV_Black_Shader);
			nbt.putBoolean("NVG_Black_Check", NVG_Black_Check);
			nbt.putBoolean("rig_save", rig_save);
			nbt.put("rig_slot0", rig_slot0.save(new CompoundTag()));
			nbt.put("rig_slot1", rig_slot1.save(new CompoundTag()));
			nbt.put("rig_slot2", rig_slot2.save(new CompoundTag()));
			nbt.put("rig_slot3", rig_slot3.save(new CompoundTag()));
			nbt.put("bp3_slot3", bp3_slot3.save(new CompoundTag()));
			nbt.putDouble("NVG_Black_Tube_Gain", NVG_Black_Tube_Gain);
			nbt.putBoolean("Helmet_Using", Helmet_Using);
			nbt.putBoolean("TVG_Check", TVG_Check);
			nbt.putBoolean("TVG_Shader", TVG_Shader);
			return nbt;
		}

		public void readNBT(Tag Tag) {
			CompoundTag nbt = (CompoundTag) Tag;
			bp1_save = nbt.getBoolean("bp1_save");
			bp1_slot0 = ItemStack.of(nbt.getCompound("bp1_slot0"));
			bp1_slot1 = ItemStack.of(nbt.getCompound("bp1_slot1"));
			bp1_slot2 = ItemStack.of(nbt.getCompound("bp1_slot2"));
			bp1_slot3 = ItemStack.of(nbt.getCompound("bp1_slot3"));
			bp1_slot4 = ItemStack.of(nbt.getCompound("bp1_slot4"));
			bp1_slot5 = ItemStack.of(nbt.getCompound("bp1_slot5"));
			bp1_slot6 = ItemStack.of(nbt.getCompound("bp1_slot6"));
			bp1_slot7 = ItemStack.of(nbt.getCompound("bp1_slot7"));
			bp1_slot8 = ItemStack.of(nbt.getCompound("bp1_slot8"));
			bp2_save = nbt.getBoolean("bp2_save");
			bp2_slot0 = ItemStack.of(nbt.getCompound("bp2_slot0"));
			bp2_slot1 = ItemStack.of(nbt.getCompound("bp2_slot1"));
			bp2_slot10 = ItemStack.of(nbt.getCompound("bp2_slot10"));
			bp2_slot11 = ItemStack.of(nbt.getCompound("bp2_slot11"));
			bp2_slot12 = ItemStack.of(nbt.getCompound("bp2_slot12"));
			bp2_slot13 = ItemStack.of(nbt.getCompound("bp2_slot13"));
			bp2_slot14 = ItemStack.of(nbt.getCompound("bp2_slot14"));
			bp2_slot2 = ItemStack.of(nbt.getCompound("bp2_slot2"));
			bp2_slot3 = ItemStack.of(nbt.getCompound("bp2_slot3"));
			bp2_slot4 = ItemStack.of(nbt.getCompound("bp2_slot4"));
			bp2_slot5 = ItemStack.of(nbt.getCompound("bp2_slot5"));
			bp2_slot6 = ItemStack.of(nbt.getCompound("bp2_slot6"));
			bp2_slot7 = ItemStack.of(nbt.getCompound("bp2_slot7"));
			bp2_slot8 = ItemStack.of(nbt.getCompound("bp2_slot8"));
			bp2_slot9 = ItemStack.of(nbt.getCompound("bp2_slot9"));
			bp3_save = nbt.getBoolean("bp3_save");
			bp3_slot0 = ItemStack.of(nbt.getCompound("bp3_slot0"));
			bp3_slot1 = ItemStack.of(nbt.getCompound("bp3_slot1"));
			bp3_slot10 = ItemStack.of(nbt.getCompound("bp3_slot10"));
			bp3_slot11 = ItemStack.of(nbt.getCompound("bp3_slot11"));
			bp3_slot12 = ItemStack.of(nbt.getCompound("bp3_slot12"));
			bp3_slot13 = ItemStack.of(nbt.getCompound("bp3_slot13"));
			bp3_slot14 = ItemStack.of(nbt.getCompound("bp3_slot14"));
			bp3_slot15 = ItemStack.of(nbt.getCompound("bp3_slot15"));
			bp3_slot16 = ItemStack.of(nbt.getCompound("bp3_slot16"));
			bp3_slot17 = ItemStack.of(nbt.getCompound("bp3_slot17"));
			bp3_slot18 = ItemStack.of(nbt.getCompound("bp3_slot18"));
			bp3_slot19 = ItemStack.of(nbt.getCompound("bp3_slot19"));
			bp3_slot2 = ItemStack.of(nbt.getCompound("bp3_slot2"));
			bp3_slot20 = ItemStack.of(nbt.getCompound("bp3_slot20"));
			bp3_slot4 = ItemStack.of(nbt.getCompound("bp3_slot4"));
			bp3_slot5 = ItemStack.of(nbt.getCompound("bp3_slot5"));
			bp3_slot6 = ItemStack.of(nbt.getCompound("bp3_slot6"));
			bp3_slot7 = ItemStack.of(nbt.getCompound("bp3_slot7"));
			bp3_slot8 = ItemStack.of(nbt.getCompound("bp3_slot8"));
			bp3_slot9 = ItemStack.of(nbt.getCompound("bp3_slot9"));
			NGV_Black_Shader = nbt.getBoolean("NGV_Black_Shader");
			NVG_Black_Check = nbt.getBoolean("NVG_Black_Check");
			rig_save = nbt.getBoolean("rig_save");
			rig_slot0 = ItemStack.of(nbt.getCompound("rig_slot0"));
			rig_slot1 = ItemStack.of(nbt.getCompound("rig_slot1"));
			rig_slot2 = ItemStack.of(nbt.getCompound("rig_slot2"));
			rig_slot3 = ItemStack.of(nbt.getCompound("rig_slot3"));
			bp3_slot3 = ItemStack.of(nbt.getCompound("bp3_slot3"));
			NVG_Black_Tube_Gain = nbt.getDouble("NVG_Black_Tube_Gain");
			Helmet_Using = nbt.getBoolean("Helmet_Using");
			TVG_Check = nbt.getBoolean("TVG_Check");
			TVG_Shader = nbt.getBoolean("TVG_Shader");
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		MmMod.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
	}

	public static class PlayerVariablesSyncMessage {
		private final int target;
		private final PlayerVariables data;

		public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
			this.data = new PlayerVariables();
			this.data.readNBT(buffer.readNbt());
			this.target = buffer.readInt();
		}

		public PlayerVariablesSyncMessage(PlayerVariables data, int entityid) {
			this.data = data;
			this.target = entityid;
		}

		public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
			buffer.writeNbt((CompoundTag) message.data.writeNBT());
			buffer.writeInt(message.target);
		}

		public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
			NetworkEvent.Context context = contextSupplier.get();
			context.enqueueWork(() -> {
				if (!context.getDirection().getReceptionSide().isServer()) {
					PlayerVariables variables = ((PlayerVariables) Minecraft.getInstance().player.level().getEntity(message.target).getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()));
					variables.bp1_save = message.data.bp1_save;
					variables.bp1_slot0 = message.data.bp1_slot0;
					variables.bp1_slot1 = message.data.bp1_slot1;
					variables.bp1_slot2 = message.data.bp1_slot2;
					variables.bp1_slot3 = message.data.bp1_slot3;
					variables.bp1_slot4 = message.data.bp1_slot4;
					variables.bp1_slot5 = message.data.bp1_slot5;
					variables.bp1_slot6 = message.data.bp1_slot6;
					variables.bp1_slot7 = message.data.bp1_slot7;
					variables.bp1_slot8 = message.data.bp1_slot8;
					variables.bp2_save = message.data.bp2_save;
					variables.bp2_slot0 = message.data.bp2_slot0;
					variables.bp2_slot1 = message.data.bp2_slot1;
					variables.bp2_slot10 = message.data.bp2_slot10;
					variables.bp2_slot11 = message.data.bp2_slot11;
					variables.bp2_slot12 = message.data.bp2_slot12;
					variables.bp2_slot13 = message.data.bp2_slot13;
					variables.bp2_slot14 = message.data.bp2_slot14;
					variables.bp2_slot2 = message.data.bp2_slot2;
					variables.bp2_slot3 = message.data.bp2_slot3;
					variables.bp2_slot4 = message.data.bp2_slot4;
					variables.bp2_slot5 = message.data.bp2_slot5;
					variables.bp2_slot6 = message.data.bp2_slot6;
					variables.bp2_slot7 = message.data.bp2_slot7;
					variables.bp2_slot8 = message.data.bp2_slot8;
					variables.bp2_slot9 = message.data.bp2_slot9;
					variables.bp3_save = message.data.bp3_save;
					variables.bp3_slot0 = message.data.bp3_slot0;
					variables.bp3_slot1 = message.data.bp3_slot1;
					variables.bp3_slot10 = message.data.bp3_slot10;
					variables.bp3_slot11 = message.data.bp3_slot11;
					variables.bp3_slot12 = message.data.bp3_slot12;
					variables.bp3_slot13 = message.data.bp3_slot13;
					variables.bp3_slot14 = message.data.bp3_slot14;
					variables.bp3_slot15 = message.data.bp3_slot15;
					variables.bp3_slot16 = message.data.bp3_slot16;
					variables.bp3_slot17 = message.data.bp3_slot17;
					variables.bp3_slot18 = message.data.bp3_slot18;
					variables.bp3_slot19 = message.data.bp3_slot19;
					variables.bp3_slot2 = message.data.bp3_slot2;
					variables.bp3_slot20 = message.data.bp3_slot20;
					variables.bp3_slot4 = message.data.bp3_slot4;
					variables.bp3_slot5 = message.data.bp3_slot5;
					variables.bp3_slot6 = message.data.bp3_slot6;
					variables.bp3_slot7 = message.data.bp3_slot7;
					variables.bp3_slot8 = message.data.bp3_slot8;
					variables.bp3_slot9 = message.data.bp3_slot9;
					variables.NGV_Black_Shader = message.data.NGV_Black_Shader;
					variables.NVG_Black_Check = message.data.NVG_Black_Check;
					variables.rig_save = message.data.rig_save;
					variables.rig_slot0 = message.data.rig_slot0;
					variables.rig_slot1 = message.data.rig_slot1;
					variables.rig_slot2 = message.data.rig_slot2;
					variables.rig_slot3 = message.data.rig_slot3;
					variables.bp3_slot3 = message.data.bp3_slot3;
					variables.NVG_Black_Tube_Gain = message.data.NVG_Black_Tube_Gain;
					variables.Helmet_Using = message.data.Helmet_Using;
					variables.TVG_Check = message.data.TVG_Check;
					variables.TVG_Shader = message.data.TVG_Shader;
				}
			});
			context.setPacketHandled(true);
		}
	}
}
