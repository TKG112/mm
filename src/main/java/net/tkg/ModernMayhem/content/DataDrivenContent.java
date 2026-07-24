package net.tkg.ModernMayhem.content;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.tkg.ModernMayhem.content.def.ArmorDefinition;
import net.tkg.ModernMayhem.content.def.CurioDefinition;
import net.tkg.ModernMayhem.content.def.GogglesDefinition;
import net.tkg.ModernMayhem.content.def.KeybindDefinition;
import net.tkg.ModernMayhem.content.item.DataArmorItem;
import net.tkg.ModernMayhem.content.item.DataCurioItem;
import net.tkg.ModernMayhem.content.item.DataGogglesItem;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Central entry point for the data-driven content system.
 * <p>
 * Definitions are scanned at construction time (before the item registry freezes) and the items are
 * registered via {@link RegisterEvent}, which -- unlike {@code DeferredRegister} -- allows each
 * pack's items to keep their own author namespace.
 */
public final class DataDrivenContent {
    private static final Logger LOGGER = LogUtils.getLogger();

    /** The packs folder: {@code <gamedir>/modernmayhem/}. */
    public static final String PACKS_DIR = "modernmayhem";

    private static List<ArmorDefinition> armorDefs = List.of();
    private static List<CurioDefinition> curioDefs = List.of();
    private static List<KeybindDefinition> keybindDefs = List.of();
    private static List<GogglesDefinition> gogglesDefs = List.of();
    private static List<ResourceLocation> soundIds = List.of();
    private static final Map<ResourceLocation, Item> REGISTERED = new LinkedHashMap<>();

    private DataDrivenContent() {}

    /** Called from the mod constructor. Scans packs now and hooks item registration. */
    public static void init(IEventBus modEventBus) {
        Path packsRoot = FMLPaths.GAMEDIR.get().resolve(PACKS_DIR);
        DefaultPack.ensureExtracted(packsRoot);
        armorDefs = ContentPackLoader.scanArmor(packsRoot);
        curioDefs = ContentPackLoader.scanCurios(packsRoot);
        keybindDefs = ContentPackLoader.scanKeybinds(packsRoot);
        gogglesDefs = ContentPackLoader.scanGoggles(packsRoot);
        soundIds = ContentPackLoader.scanSounds(packsRoot);
        modEventBus.addListener(DataDrivenContent::onRegisterItems);
        modEventBus.addListener(DataDrivenContent::onRegisterSounds);
        modEventBus.addListener(ContentPackAssets::onAddPackFinders);
        modEventBus.addListener(GeneratedCurioTags::onAddPackFinders);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(CurioSlotSync::onPlayerLogin);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(WornArmorEffects.class);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(CurioEffectImmunity.class);
    }

    private static void onRegisterItems(RegisterEvent event) {
        if (!event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            return;
        }
        for (ArmorDefinition def : armorDefs) {
            if (isDuplicate(def.id())) continue;
            DataArmorItem item = new DataArmorItem(def);
            event.register(ForgeRegistries.Keys.ITEMS, def.id(), () -> item);
            REGISTERED.put(def.id(), item);
            LOGGER.info("[MM] Registered data-driven armor '{}'", def.id());
        }
        for (CurioDefinition def : curioDefs) {
            if (isDuplicate(def.id())) continue;
            DataCurioItem item = new DataCurioItem(def);
            event.register(ForgeRegistries.Keys.ITEMS, def.id(), () -> item);
            REGISTERED.put(def.id(), item);
            LOGGER.info("[MM] Registered data-driven curio '{}' (slot: {})", def.id(), def.slot());
        }
        registerGoggles(event);
    }

    /**
     * Registers a {@code SoundEvent} for every entry in every pack's {@code sounds.json}.
     * <p>
     * A resource pack can supply the audio files but cannot create the registry entries, which is why
     * this has to happen in code even though the sounds themselves are pure assets.
     */
    private static void onRegisterSounds(RegisterEvent event) {
        if (!event.getRegistryKey().equals(ForgeRegistries.Keys.SOUND_EVENTS)) {
            return;
        }
        for (ResourceLocation id : soundIds) {
            if (ForgeRegistries.SOUND_EVENTS.containsKey(id)) {
                continue;
            }
            event.register(ForgeRegistries.Keys.SOUND_EVENTS, id,
                    () -> SoundEvent.createVariableRangeEvent(id));
            LOGGER.info("[MM] Registered data-driven sound '{}'", id);
        }
    }

    private static void registerGoggles(RegisterEvent event) {
        for (GogglesDefinition def : gogglesDefs) {
            if (isDuplicate(def.id())) continue;
            DataGogglesItem item = new DataGogglesItem(def);
            event.register(ForgeRegistries.Keys.ITEMS, def.id(), () -> item);
            REGISTERED.put(def.id(), item);
            LOGGER.info("[MM] Registered data-driven goggles '{}' (type: {})", def.id(), def.goggleType());
            if (def.postChain() != null) {
                LOGGER.info("[MM] '{}' uses custom post chain '{}' (validated when first switched on)",
                        def.id(), def.postChain());
            }
        }
    }

    private static boolean isDuplicate(ResourceLocation id) {
        if (REGISTERED.containsKey(id)) {
            LOGGER.warn("[MM] Duplicate data-driven item id '{}' -- keeping the first, skipping the rest", id);
            return true;
        }
        return false;
    }

    /** All loaded curio definitions (used for renderer registration and generated Curios tags). */
    public static List<CurioDefinition> curioDefinitions() {
        return curioDefs;
    }

    /** All loaded goggle definitions (used for generated facewear tags). */
    public static List<GogglesDefinition> gogglesDefinitions() {
        return gogglesDefs;
    }

    /** All loaded keybind definitions (registered client-side as real KeyMappings). */
    public static List<KeybindDefinition> keybindDefinitions() {
        return keybindDefs;
    }

    /** All successfully registered data-driven items, in load order (for the creative tab). */
    public static Collection<Item> registeredItems() {
        return REGISTERED.values();
    }

    /** Ids of all registered data-driven items (for client-side model injection). */
    public static java.util.Set<ResourceLocation> registeredIds() {
        return REGISTERED.keySet();
    }
}
