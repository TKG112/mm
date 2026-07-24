package net.tkg.ModernMayhem.content;

import com.google.gson.Gson;
import net.minecraft.ChatFormatting;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.tkg.ModernMayhem.content.def.ArmorDefinition;
import net.tkg.ModernMayhem.content.def.AttributeBonus;
import net.tkg.ModernMayhem.content.def.ArmorFeatures;
import net.tkg.ModernMayhem.content.def.ArmorStats;
import net.tkg.ModernMayhem.content.def.CurioDefinition;
import net.tkg.ModernMayhem.content.def.DisplaySettings;
import net.tkg.ModernMayhem.content.def.GainStep;
import net.tkg.ModernMayhem.content.def.GogglesDefinition;
import net.tkg.ModernMayhem.content.def.KeybindDefinition;
import net.tkg.ModernMayhem.content.def.PaletteDefinition;
import net.tkg.ModernMayhem.server.item.generic.GenericSpecialGogglesItem.GoggleType;
import net.tkg.ModernMayhem.content.def.StorageSettings;
import net.tkg.ModernMayhem.content.def.TooltipLine;
import net.tkg.ModernMayhem.content.def.WornEffect;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Discovers ModernMayhem content packs and parses their definitions.
 * <p>
 * Packs live directly under {@code <gamedir>/modernmayhem/}. Each child that is a folder or a
 * {@code .zip} and looks like a pack (has {@code pack.mcmeta}, a {@code data/} dir, or an
 * {@code assets/} dir) is scanned. Definition files live at
 * {@code data/<namespace>/modernmayhem/armor/<name>.json}; the item id is derived from that path.
 * <p>
 * A malformed definition or pack is logged and skipped -- it never aborts the scan (essential for
 * non-programmer pack authors).
 */
public final class ContentPackLoader {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new Gson();

    /** Matches {@code data/<namespace>/modernmayhem/<category>/<name>.json} using forward slashes. */
    private static final String PATH_TEMPLATE =
            "^data/([a-z0-9_.-]+)/modernmayhem/%s/([a-z0-9_.-]+)\\.json$";
    private static final Pattern ARMOR_PATH = Pattern.compile(String.format(PATH_TEMPLATE, "armor"));
    private static final Pattern CURIO_PATH = Pattern.compile(String.format(PATH_TEMPLATE, "curio"));
    private static final Pattern KEYBIND_PATH = Pattern.compile(String.format(PATH_TEMPLATE, "keybind"));
    private static final Pattern GOGGLES_PATH = Pattern.compile(String.format(PATH_TEMPLATE, "goggles"));
    /** A pack's sound definitions, in the same place and format a resource pack puts them. */
    private static final Pattern SOUNDS_PATH = Pattern.compile("^assets/([a-z0-9_.-]+)/(sounds)\\.json$");

    /** A per-definition callback; may throw to signal a bad definition (logged and skipped). */
    @FunctionalInterface
    private interface DefVisitor {
        void visit(String packName, ResourceLocation id, JsonObject json) throws Exception;
    }

    private ContentPackLoader() {}

    /**
     * Scans the packs root for armor definitions. Creates the root folder if missing (so users can
     * find where to drop packs). Never throws -- returns whatever parsed successfully.
     */
    public static List<ArmorDefinition> scanArmor(Path packsRoot) {
        List<ArmorDefinition> out = new ArrayList<>();
        if (!ensureRoot(packsRoot)) {
            return out;
        }
        forEachArmorJson(packsRoot, (packName, id, json) -> {
            out.add(parseArmor(json, id));
            LOGGER.info("[MM] Parsed armor '{}' from pack '{}'", id, packName);
        });
        LOGGER.info("[MM] Loaded {} data-driven armor definition(s)", out.size());
        return out;
    }

    /**
     * Scans the packs root for curio definitions (backpacks, rigs, cosmetics). Never throws --
     * returns whatever parsed successfully.
     */
    public static List<CurioDefinition> scanCurios(Path packsRoot) {
        List<CurioDefinition> out = new ArrayList<>();
        if (!Files.isDirectory(packsRoot)) {
            return out;
        }
        forEachDefinition(packsRoot, CURIO_PATH, (packName, id, json) -> {
            out.add(parseCurio(json, id));
            LOGGER.info("[MM] Parsed curio '{}' from pack '{}'", id, packName);
        });
        LOGGER.info("[MM] Loaded {} data-driven curio definition(s)", out.size());
        return out;
    }

    /**
     * Scans the packs root for goggle definitions (night vision, thermal, visor). Never throws --
     * returns whatever parsed successfully.
     */
    public static List<GogglesDefinition> scanGoggles(Path packsRoot) {
        List<GogglesDefinition> out = new ArrayList<>();
        if (!Files.isDirectory(packsRoot)) {
            return out;
        }
        forEachDefinition(packsRoot, GOGGLES_PATH, (packName, id, json) -> {
            out.add(parseGoggles(json, id));
            LOGGER.info("[MM] Parsed goggles '{}' from pack '{}'", id, packName);
        });
        if (!out.isEmpty()) {
            LOGGER.info("[MM] Loaded {} data-driven goggle definition(s)", out.size());
        }
        return out;
    }

    /**
     * Scans packs for sound events to register, reading each pack's {@code assets/<ns>/sounds.json}.
     * <p>
     * That file is the ordinary resource-pack format -- the same one a pack already needs so the game
     * can find its {@code .ogg} files. Every entry in it becomes a registered {@code SoundEvent}, which
     * is the part a resource pack alone cannot do, so a pack can reference its own sounds by id.
     */
    public static List<ResourceLocation> scanSounds(Path packsRoot) {
        List<ResourceLocation> out = new ArrayList<>();
        if (!Files.isDirectory(packsRoot)) {
            return out;
        }
        forEachAsset(packsRoot, SOUNDS_PATH, (packName, id, json) -> {
            String namespace = id.getNamespace();
            for (String key : json.keySet()) {
                if (!ResourceLocation.isValidPath(key)) {
                    LOGGER.warn("[MM] Pack '{}' declares sound '{}' with an invalid name -- skipping",
                            packName, key);
                    continue;
                }
                out.add(ResourceLocation.fromNamespaceAndPath(namespace, key));
            }
        });
        if (!out.isEmpty()) {
            LOGGER.info("[MM] Loaded {} data-driven sound event(s)", out.size());
        }
        return out;
    }

    /**
     * Scans the packs root for keybind definitions. Never throws -- returns whatever parsed
     * successfully.
     */
    public static List<KeybindDefinition> scanKeybinds(Path packsRoot) {
        List<KeybindDefinition> out = new ArrayList<>();
        if (!Files.isDirectory(packsRoot)) {
            return out;
        }
        forEachDefinition(packsRoot, KEYBIND_PATH, (packName, id, json) -> {
            out.add(parseKeybind(json, id));
            LOGGER.info("[MM] Parsed keybind '{}' from pack '{}'", id, packName);
        });
        if (!out.isEmpty()) {
            LOGGER.info("[MM] Loaded {} data-driven keybind(s)", out.size());
        }
        return out;
    }

    /**
     * Re-reads just the optional {@code "display"} framing blocks for every armor definition. Cheap
     * enough to run on each resource reload so framing can be tuned live (edit JSON, press F3+T).
     */
    public static Map<ResourceLocation, DisplaySettings> scanDisplayOverrides(Path packsRoot) {
        Map<ResourceLocation, DisplaySettings> out = new LinkedHashMap<>();
        if (!Files.isDirectory(packsRoot)) {
            return out;
        }
        DefVisitor visitor = (packName, id, json) -> {
            DisplaySettings settings = parseDisplay(json);
            if (!settings.entries().isEmpty()) {
                out.put(id, settings);
            }
        };
        // Every category that can carry a "display" block must be listed here, or its framing is
        // silently parsed into nothing and the item falls back to the slot defaults.
        forEachDefinition(packsRoot, ARMOR_PATH, visitor);
        forEachDefinition(packsRoot, CURIO_PATH, visitor);
        forEachDefinition(packsRoot, GOGGLES_PATH, visitor);
        return out;
    }

    private static boolean ensureRoot(Path packsRoot) {
        try {
            if (!Files.exists(packsRoot)) {
                Files.createDirectories(packsRoot);
                LOGGER.info("[MM] Created content-pack folder: {}", packsRoot);
                return false;
            }
            return true;
        } catch (IOException e) {
            LOGGER.error("[MM] Failed to access content-pack folder {}: {}", packsRoot, e.toString());
            return false;
        }
    }

    private static void forEachArmorJson(Path packsRoot, DefVisitor visitor) {
        forEachDefinition(packsRoot, ARMOR_PATH, visitor);
    }

    private static void forEachDefinition(Path packsRoot, Pattern pathPattern, DefVisitor visitor) {
        forEachFile(packsRoot, "data", pathPattern, visitor);
    }

    /** As {@link #forEachDefinition}, but walking the pack's {@code assets/} tree instead. */
    private static void forEachAsset(Path packsRoot, Pattern pathPattern, DefVisitor visitor) {
        forEachFile(packsRoot, "assets", pathPattern, visitor);
    }

    private static void forEachFile(Path packsRoot, String topDir, Pattern pathPattern, DefVisitor visitor) {
        try (Stream<Path> children = Files.list(packsRoot)) {
            children.forEach(child -> visitPack(child, topDir, pathPattern, visitor));
        } catch (IOException e) {
            LOGGER.error("[MM] Failed to read content-pack folder {}: {}", packsRoot, e.toString());
        }
    }

    private static void visitPack(Path packEntry, String topDir, Pattern pathPattern, DefVisitor visitor) {
        String name = packEntry.getFileName().toString();
        try {
            if (Files.isDirectory(packEntry)) {
                if (looksLikePack(packEntry)) {
                    visitRoot(packEntry, name, topDir, pathPattern, visitor);
                }
            } else if (name.toLowerCase().endsWith(".zip")) {
                try (FileSystem zip = FileSystems.newFileSystem(packEntry, (ClassLoader) null)) {
                    Path root = zip.getPath("/");
                    if (looksLikePack(root)) {
                        visitRoot(root, name, topDir, pathPattern, visitor);
                    }
                }
            }
            // anything else (stray files) is silently ignored
        } catch (Exception e) {
            LOGGER.error("[MM] Skipping content pack '{}': {}", name, e.toString());
        }
    }

    private static boolean looksLikePack(Path root) {
        return Files.exists(root.resolve("pack.mcmeta"))
                || Files.exists(root.resolve("data"))
                || Files.exists(root.resolve("assets"));
    }

    private static void visitRoot(Path root, String packName, String topDir, Pattern pathPattern, DefVisitor visitor) throws IOException {
        Path dataDir = root.resolve(topDir);
        if (!Files.isDirectory(dataDir)) {
            return;
        }
        try (Stream<Path> files = Files.walk(dataDir)) {
            files.filter(Files::isRegularFile).forEach(file -> {
                String rel = root.relativize(file).toString().replace('\\', '/');
                Matcher m = pathPattern.matcher(rel);
                if (!m.matches()) {
                    return;
                }
                ResourceLocation id = ResourceLocation.fromNamespaceAndPath(m.group(1), m.group(2));
                try {
                    JsonObject json = readJson(file);
                    visitor.visit(packName, id, json);
                } catch (Exception e) {
                    LOGGER.error("[MM] Bad armor definition '{}' in pack '{}': {}", rel, packName, e.getMessage());
                }
            });
        }
    }

    private static JsonObject readJson(Path file) throws IOException {
        try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            JsonObject json = GSON.fromJson(reader, JsonObject.class);
            if (json == null) {
                throw new JsonParseException("empty or invalid JSON");
            }
            return json;
        }
    }

    private static ArmorDefinition parseArmor(JsonObject json, ResourceLocation id) {
        ArmorItem.Type slot = parseSlot(requireString(json, "slot"));
        String material = optString(json, "material", "generic");
        ResourceLocation model = rl(requireString(json, "model"));
        ResourceLocation texture = rl(requireString(json, "texture"));
        ResourceLocation animation = json.has("animation")
                ? rl(json.get("animation").getAsString())
                : ResourceLocation.fromNamespaceAndPath("mm", "animations/empty.animation.json");
        ResourceLocation slimModel = optRl(json, "slim_model");
        ResourceLocation slimTexture = optRl(json, "slim_texture");
        ResourceLocation icon = optRl(json, "icon_texture");

        ArmorStats stats = parseStats(json.has("stats") ? json.getAsJsonObject("stats") : null);
        ArmorFeatures features = parseFeatures(json.has("features") ? json.getAsJsonObject("features") : null, id);

        ResourceLocation screenOverlay = optRl(json, "screen_overlay");
        boolean hidesSkinOverlay = json.has("hide_skin_overlay")
                && json.get("hide_skin_overlay").getAsBoolean();

        return new ArmorDefinition(id, slot, material, model, texture, animation,
                slimModel, slimTexture, icon, stats, features, screenOverlay,
                parseTooltip(json, id), parseEffects(json), hidesSkinOverlay);
    }

    private static CurioDefinition parseCurio(JsonObject json, ResourceLocation id) {
        String slot = parseCurioSlot(requireString(json, "slot"), id);
        EquipmentSlot attachesTo = json.has("attaches_to")
                ? parseAttachment(json.get("attaches_to").getAsString())
                : defaultAttachmentFor(slot);
        ResourceLocation model = rl(requireString(json, "model"));
        ResourceLocation texture = rl(requireString(json, "texture"));
        ResourceLocation animation = json.has("animation")
                ? rl(json.get("animation").getAsString())
                : ResourceLocation.fromNamespaceAndPath("mm", "animations/empty.animation.json");
        ResourceLocation icon = optRl(json, "icon_texture");

        StorageSettings storage = parseStorage(json.has("storage") ? json.getAsJsonObject("storage") : null);
        ArmorStats stats = parseStats(json.has("stats") ? json.getAsJsonObject("stats") : null);
        List<AttributeBonus> attributes = parseAttributes(json);
        ResourceLocation conflictsWithHelmets = optRl(json, "conflicts_with_helmets");
        boolean transparent = json.has("transparent") && json.get("transparent").getAsBoolean();

        return new CurioDefinition(id, slot, attachesTo, model, texture, animation, icon, storage, stats,
                attributes, conflictsWithHelmets, optRl(json, "immune_to_effects"),
                transparent, parseTooltip(json, id));
    }

    /**
     * Which player part a curio's model rides along with, i.e. which set of GeckoLib armor bones is
     * drawn. Independent of the Curios slot -- a pack-invented {@code belt} slot still renders on the
     * torso.
     */
    private static EquipmentSlot parseAttachment(String s) {
        return switch (s.toLowerCase()) {
            case "head" -> EquipmentSlot.HEAD;
            case "chest", "body", "torso" -> EquipmentSlot.CHEST;
            case "legs" -> EquipmentSlot.LEGS;
            case "feet", "boots" -> EquipmentSlot.FEET;
            default -> throw new JsonParseException("unknown attaches_to '" + s
                    + "' (expected head, chest, legs or feet)");
        };
    }

    /** Sensible attachment for ModernMayhem's own slots; custom slots default to the torso. */
    private static EquipmentSlot defaultAttachmentFor(String curiosSlot) {
        return switch (curiosSlot) {
            case "head", "facewear", "earwear" -> EquipmentSlot.HEAD;
            case "knees" -> EquipmentSlot.LEGS;
            default -> EquipmentSlot.CHEST; // back, body, and anything a pack invents
        };
    }

    private static GogglesDefinition parseGoggles(JsonObject json, ResourceLocation id) {
        GoggleType type = parseGoggleType(optString(json, "goggle_type", "night_vision"));

        GogglesDefinition.Visuals worn = parseVisuals(json, "worn", true);
        // First-person visuals are optional; a goggle with none simply reuses its worn set.
        GogglesDefinition.Visuals firstPerson = json.has("first_person")
                ? parseVisuals(json, "first_person", true)
                : worn;

        ResourceLocation activate = null;
        ResourceLocation deactivate = null;
        if (json.has("sounds")) {
            JsonObject sounds = json.getAsJsonObject("sounds");
            activate = optRl(sounds, "activate");
            deactivate = optRl(sounds, "deactivate");
        }

        GogglesDefinition.GoggleFeatures features = parseGoggleFeatures(
                json.has("features") ? json.getAsJsonObject("features") : null, id);

        List<GainStep> gainSteps = parseGainSteps(json, id);
        int defaultGain = json.has("default_gain") ? json.get("default_gain").getAsInt() : 0;
        List<PaletteDefinition> palettes = parsePalettes(json, id);
        ResourceLocation postChain = optRl(json, "post_chain");

        ArmorStats stats = parseStats(json.has("stats") ? json.getAsJsonObject("stats") : null);

        return new GogglesDefinition(id, type, worn, firstPerson, activate, deactivate,
                features, stats, defaultGain, gainSteps, palettes, postChain, parseTooltip(json, id));
    }

    /**
     * Thermal palettes. A goggle with none simply cycles ModernMayhem's built-ins, so only a pack that
     * wants its own color schemes needs this block.
     */
    private static List<PaletteDefinition> parsePalettes(JsonObject json, ResourceLocation id) {
        List<PaletteDefinition> palettes = new ArrayList<>();
        if (!json.has("palettes")) {
            return palettes;
        }
        for (var element : json.getAsJsonArray("palettes")) {
            JsonObject p = element.getAsJsonObject();
            String name = optString(p, "name", "Palette");
            Vector3f world = parseVec3(p, "world_color", 1f, 1f, 1f);
            boolean inverted = p.has("inverted") && p.get("inverted").getAsBoolean();
            boolean linear = p.has("linear") && p.get("linear").getAsBoolean();

            Vector3f base = parseVec3(p, "base_color", 0f, 0f, 0f);
            List<PaletteDefinition.Stop> stops = new ArrayList<>();

            if (p.has("signature")) {
                // Full form: an ordered gradient of stops.
                for (var s : p.getAsJsonArray("signature")) {
                    JsonObject stop = s.getAsJsonObject();
                    float at = (float) optDouble(stop, "at", 1.0);
                    Vector3f c = parseVec3(stop, "color", 1f, 1f, 1f);
                    stops.add(new PaletteDefinition.Stop(at, c.x, c.y, c.z));
                }
            } else if (p.has("signature_color")) {
                // Shorthand: one color, expanded to a black -> color -> white ramp.
                Vector3f c = parseVec3(p, "signature_color", 1f, 1f, 1f);
                stops.add(new PaletteDefinition.Stop(0.6f, c.x, c.y, c.z));
                stops.add(new PaletteDefinition.Stop(1.0f, 1f, 1f, 1f));
            } else {
                throw new JsonParseException("palette '" + name
                        + "' needs either 'signature' stops or a 'signature_color'");
            }

            palettes.add(new PaletteDefinition(name, new float[]{world.x, world.y, world.z},
                    inverted, new float[]{base.x, base.y, base.z}, stops, linear));
        }
        LOGGER.info("[MM] '{}' declares {} thermal palette(s)", id, palettes.size());
        return palettes;
    }

    private static GoggleType parseGoggleType(String s) {
        return switch (s.toLowerCase()) {
            case "night_vision", "nvg" -> GoggleType.NIGHT_VISION;
            case "thermal" -> GoggleType.THERMAL;
            case "visor" -> GoggleType.VISOR;
            default -> throw new JsonParseException("unknown goggle_type '" + s
                    + "' (expected night_vision, thermal or visor)");
        };
    }

    /** Reads a model/texture/animation set plus its optional COTI variants. */
    private static GogglesDefinition.Visuals parseVisuals(JsonObject parent, String key, boolean required) {
        if (!parent.has(key)) {
            if (required) throw new JsonParseException("missing required '" + key + "' block");
            return null;
        }
        JsonObject o = parent.getAsJsonObject(key);
        ResourceLocation model = rl(requireString(o, "model"));
        ResourceLocation texture = rl(requireString(o, "texture"));
        ResourceLocation animation = o.has("animation")
                ? rl(o.get("animation").getAsString())
                : ResourceLocation.fromNamespaceAndPath("mm", "animations/empty.animation.json");

        ResourceLocation cotiModel = null;
        ResourceLocation cotiTexture = null;
        ResourceLocation cotiAnimation = null;
        if (o.has("coti")) {
            JsonObject coti = o.getAsJsonObject("coti");
            cotiModel = optRl(coti, "model");
            cotiTexture = optRl(coti, "texture");
            cotiAnimation = optRl(coti, "animation");
        }
        return new GogglesDefinition.Visuals(model, texture, animation, cotiModel, cotiTexture, cotiAnimation);
    }

    private static GogglesDefinition.GoggleFeatures parseGoggleFeatures(@Nullable JsonObject f, ResourceLocation id) {
        if (f == null) {
            return GogglesDefinition.GoggleFeatures.NONE;
        }
        Set<String> known = Set.of("can_hold_coti", "auto_gain", "auto_gating", "rainbow_phosphor");
        for (String key : f.keySet()) {
            if (!known.contains(key)) {
                LOGGER.warn("[MM] '{}' declares unknown goggle feature '{}' -- ignoring", id, key);
            }
        }
        return new GogglesDefinition.GoggleFeatures(
                f.has("can_hold_coti") && f.get("can_hold_coti").getAsBoolean(),
                f.has("auto_gain") && f.get("auto_gain").getAsBoolean(),
                f.has("auto_gating") && f.get("auto_gating").getAsBoolean(),
                f.has("rainbow_phosphor") && f.get("rainbow_phosphor").getAsBoolean());
    }

    /**
     * The gain ladder. Visors have no tubes, so an empty ladder is allowed; night vision and thermal
     * need at least one step to have anything to show.
     */
    private static List<GainStep> parseGainSteps(JsonObject json, ResourceLocation id) {
        List<GainStep> steps = new ArrayList<>();
        if (!json.has("gain_steps")) {
            return steps;
        }
        for (var element : json.getAsJsonArray("gain_steps")) {
            JsonObject s = element.getAsJsonObject();
            Vector3f color = parseVec3(s, "color", 1f, 1f, 1f);
            steps.add(new GainStep(
                    (float) optDouble(s, "brightness", 0.5),
                    color.x, color.y, color.z,
                    rl(requireString(s, "overlay")),
                    (float) optDouble(s, "noise", 1.0),
                    (float) optDouble(s, "auto_gain_speed", 0.05),
                    (float) optDouble(s, "auto_gain_offset", 0.0),
                    (float) optDouble(s, "auto_gating_offset", 0.1),
                    (float) optDouble(s, "auto_gating_speed", 0.1)));
        }
        return steps;
    }

    private static KeybindDefinition parseKeybind(JsonObject json, ResourceLocation id) {
        String slot = parseCurioSlot(requireString(json, "opens_slot"), id);
        String defaultKey = optString(json, "default_key", "key.keyboard.unknown");
        // Accept the friendly short form ("k") as well as the full "key.keyboard.k".
        if (!defaultKey.startsWith("key.")) {
            defaultKey = "key.keyboard." + defaultKey.toLowerCase();
        }
        return new KeybindDefinition(id, slot, defaultKey);
    }

    /**
     * Parses the optional {@code attributes} array. Each entry names an attribute by id and supplies
     * an amount, e.g. {@code {"attribute": "mm:safe_fall_distance", "amount": 4}}.
     */
    private static List<AttributeBonus> parseAttributes(JsonObject json) {
        if (!json.has("attributes")) return List.of();
        if (!json.get("attributes").isJsonArray()) {
            throw new JsonParseException("'attributes' must be an array");
        }

        List<AttributeBonus> out = new ArrayList<>();
        for (var element : json.getAsJsonArray("attributes")) {
            if (!element.isJsonObject()) {
                throw new JsonParseException("each 'attributes' entry must be an object");
            }
            JsonObject o = element.getAsJsonObject();
            ResourceLocation attribute = rl(requireString(o, "attribute"));
            if (!o.has("amount")) {
                throw new JsonParseException("attribute '" + attribute + "' is missing 'amount'");
            }
            double amount = o.get("amount").getAsDouble();
            AttributeModifier.Operation operation = o.has("operation")
                    ? parseOperation(o.get("operation").getAsString())
                    : AttributeModifier.Operation.ADDITION;
            String name = o.has("name") ? o.get("name").getAsString() : attribute.getPath();
            out.add(new AttributeBonus(attribute, amount, operation, name));
        }
        return List.copyOf(out);
    }

    /**
     * Parses the optional {@code tooltip} array. Entries may be a bare translation key, or an object
     * carrying a color with it.
     */
    private static List<TooltipLine> parseTooltip(JsonObject json, ResourceLocation id) {
        if (!json.has("tooltip")) return List.of();
        if (!json.get("tooltip").isJsonArray()) {
            throw new JsonParseException("'tooltip' must be an array");
        }

        List<TooltipLine> out = new ArrayList<>();
        for (var element : json.getAsJsonArray("tooltip")) {
            if (element.isJsonPrimitive()) {
                out.add(new TooltipLine(element.getAsString(), ChatFormatting.GRAY));
                continue;
            }
            if (!element.isJsonObject()) {
                throw new JsonParseException("each 'tooltip' entry must be a string or an object");
            }
            JsonObject o = element.getAsJsonObject();
            String key = requireString(o, "key");
            ChatFormatting color = ChatFormatting.GRAY;
            if (o.has("color")) {
                String name = o.get("color").getAsString();
                color = ChatFormatting.getByName(name);
                if (color == null) {
                    LOGGER.warn("[MM] '{}' uses unknown tooltip colour '{}' -- falling back to gray", id, name);
                    color = ChatFormatting.GRAY;
                }
            }
            out.add(new TooltipLine(key, color));
        }
        return List.copyOf(out);
    }

    /** Parses the optional {@code effects} array of mob effects granted while worn. */
    private static List<WornEffect> parseEffects(JsonObject json) {
        if (!json.has("effects")) return List.of();
        if (!json.get("effects").isJsonArray()) {
            throw new JsonParseException("'effects' must be an array");
        }

        List<WornEffect> out = new ArrayList<>();
        for (var element : json.getAsJsonArray("effects")) {
            if (!element.isJsonObject()) {
                throw new JsonParseException("each 'effects' entry must be an object");
            }
            JsonObject o = element.getAsJsonObject();
            out.add(new WornEffect(
                    rl(requireString(o, "effect")),
                    o.has("amplifier") ? o.get("amplifier").getAsInt() : 0,
                    o.has("requires_full_set") && o.get("requires_full_set").getAsBoolean(),
                    !o.has("show_particles") || o.get("show_particles").getAsBoolean(),
                    !o.has("show_icon") || o.get("show_icon").getAsBoolean()));
        }
        return List.copyOf(out);
    }

    private static AttributeModifier.Operation parseOperation(String s) {
        return switch (s.toLowerCase()) {
            case "addition", "add" -> AttributeModifier.Operation.ADDITION;
            case "multiply_base" -> AttributeModifier.Operation.MULTIPLY_BASE;
            case "multiply_total" -> AttributeModifier.Operation.MULTIPLY_TOTAL;
            default -> throw new JsonParseException("unknown attribute operation '" + s
                    + "' (expected addition, multiply_base or multiply_total)");
        };
    }

    private static StorageSettings parseStorage(@Nullable JsonObject s) {
        if (s == null) return StorageSettings.NONE;
        int rows = s.has("rows") ? s.get("rows").getAsInt() : 0;
        int columns = s.has("columns") ? s.get("columns").getAsInt() : 0;
        if (rows < 0 || rows > 9 || columns < 0 || columns > 9) {
            throw new JsonParseException("storage rows/columns must be between 0 and 9 (got "
                    + rows + "x" + columns + ")");
        }
        boolean suppliesAmmo = s.has("supplies_ammo") && s.get("supplies_ammo").getAsBoolean();
        return new StorageSettings(rows, columns, suppliesAmmo);
    }

    /** Slot identifiers ModernMayhem ships Curios slots for. */
    private static final Set<String> BUILTIN_CURIO_SLOTS =
            Set.of("back", "body", "head", "knees", "earwear", "facewear");

    /**
     * Accepts any syntactically valid Curios slot identifier, not just ModernMayhem's own.
     * <p>
     * Curios slot <i>types</i> are themselves datapack-defined, and a content pack's {@code data/}
     * folder is loaded as a datapack -- so an author can ship
     * {@code data/<ns>/curios/slots/<slot>.json} (plus an {@code entities/} file assigning it) to
     * invent an entirely new slot and then put items in it. Rejecting unknown names here would make
     * that impossible, so an unrecognized slot is only a warning.
     */
    private static String parseCurioSlot(String s, ResourceLocation id) {
        String slot = s.toLowerCase();
        if (!slot.matches("[a-z0-9_.-]+")) {
            throw new JsonParseException("invalid curio slot '" + s
                    + "' (letters, digits, underscore, dot and dash only)");
        }
        if (!BUILTIN_CURIO_SLOTS.contains(slot)) {
            LOGGER.info("[MM] '{}' uses custom curio slot '{}' -- make sure your pack defines it in "
                    + "data/<namespace>/curios/slots/{}.json and assigns it in curios/entities/", id, slot, slot);
        }
        return slot;
    }

    /**
     * Parses the optional {@code "features"} block. Unknown keys are warned about rather than ignored
     * silently, so a typo doesn't leave an author wondering why nothing happened.
     */
    private static ArmorFeatures parseFeatures(@Nullable JsonObject f, ResourceLocation id) {
        if (f == null) {
            return ArmorFeatures.NONE;
        }
        for (String key : f.keySet()) {
            if (!key.equals("head_mount") && !key.equals("visor_mount")) {
                LOGGER.warn("[MM] '{}' declares unknown feature '{}' -- ignoring", id, key);
            }
        }
        boolean headMount = f.has("head_mount") && f.get("head_mount").getAsBoolean();
        boolean visorMount = f.has("visor_mount") && f.get("visor_mount").getAsBoolean();
        return new ArmorFeatures(headMount, visorMount);
    }

    /**
     * Parses the optional {@code "display"} block, mirroring vanilla model display contexts.
     * {@code "icon"} is accepted as a shorthand alias for {@code display.gui}. Only contexts actually
     * present are returned; everything else falls back to the per-slot defaults.
     */
    private static DisplaySettings parseDisplay(JsonObject json) {
        EnumMap<ItemDisplayContext, DisplaySettings.Entry> map = new EnumMap<>(ItemDisplayContext.class);

        if (json.has("display") && json.get("display").isJsonObject()) {
            JsonObject display = json.getAsJsonObject("display");
            for (String key : display.keySet()) {
                if (!display.get(key).isJsonObject()) {
                    throw new JsonParseException("display entry '" + key + "' must be an object");
                }
                DisplaySettings.Entry entry = parseDisplayEntry(display.getAsJsonObject(key));
                for (ItemDisplayContext ctx : contextsFor(key)) {
                    map.put(ctx, entry);
                }
            }
        }
        if (json.has("icon") && json.get("icon").isJsonObject()) {
            map.put(ItemDisplayContext.GUI, parseDisplayEntry(json.getAsJsonObject("icon")));
        }
        return new DisplaySettings(map);
    }

    private static DisplaySettings.Entry parseDisplayEntry(JsonObject o) {
        Vector3f rotation = parseVec3(o, "rotation", 0f, 0f, 0f);
        Vector3f translation = o.has("offset")
                ? parseVec3(o, "offset", 0f, 0f, 0f)
                : parseVec3(o, "translation", 0f, 0f, 0f);
        Vector3f scale = parseScale(o);
        return new DisplaySettings.Entry(rotation, translation, scale);
    }

    /** Scale may be a single number or a 3-element array. */
    private static Vector3f parseScale(JsonObject o) {
        if (!o.has("scale")) {
            return new Vector3f(1f, 1f, 1f);
        }
        if (o.get("scale").isJsonArray()) {
            return parseVec3(o, "scale", 1f, 1f, 1f);
        }
        float s = o.get("scale").getAsFloat();
        return new Vector3f(s, s, s);
    }

    private static List<ItemDisplayContext> contextsFor(String key) {
        return switch (key.toLowerCase()) {
            case "gui" -> List.of(ItemDisplayContext.GUI);
            case "fixed" -> List.of(ItemDisplayContext.FIXED);
            case "ground" -> List.of(ItemDisplayContext.GROUND);
            case "head" -> List.of(ItemDisplayContext.HEAD);
            case "thirdperson_righthand" -> List.of(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND);
            case "thirdperson_lefthand" -> List.of(ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
            case "firstperson_righthand" -> List.of(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND);
            case "firstperson_lefthand" -> List.of(ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
            case "thirdperson" -> List.of(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
            case "firstperson" -> List.of(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, ItemDisplayContext.FIRST_PERSON_LEFT_HAND);
            default -> throw new JsonParseException("unknown display context '" + key + "'");
        };
    }

    private static ArmorStats parseStats(@Nullable JsonObject s) {
        if (s == null) return ArmorStats.EMPTY;
        double protection = optDouble(s, "protection", 0);
        double toughness = optDouble(s, "toughness", 0);
        double knockback = optDouble(s, "knockback", 0);
        int durability = s.has("durability") ? s.get("durability").getAsInt() : 400;
        return new ArmorStats(protection, toughness, knockback, durability);
    }

    private static ArmorItem.Type parseSlot(String s) {
        return switch (s.toLowerCase()) {
            case "helmet", "head" -> ArmorItem.Type.HELMET;
            case "chestplate", "chest", "body" -> ArmorItem.Type.CHESTPLATE;
            case "leggings", "legs" -> ArmorItem.Type.LEGGINGS;
            case "boots", "feet" -> ArmorItem.Type.BOOTS;
            default -> throw new JsonParseException("unknown armor slot '" + s + "'");
        };
    }

    private static Vector3f parseVec3(JsonObject o, String key, float dx, float dy, float dz) {
        if (!o.has(key) || !o.get(key).isJsonArray()) {
            return new Vector3f(dx, dy, dz);
        }
        var arr = o.getAsJsonArray(key);
        if (arr.size() != 3) {
            throw new JsonParseException("'" + key + "' must have exactly 3 numbers");
        }
        return new Vector3f(arr.get(0).getAsFloat(), arr.get(1).getAsFloat(), arr.get(2).getAsFloat());
    }

    private static String requireString(JsonObject o, String key) {
        if (!o.has(key)) throw new JsonParseException("missing required field '" + key + "'");
        return o.get(key).getAsString();
    }

    private static String optString(JsonObject o, String key, String def) {
        return o.has(key) ? o.get(key).getAsString() : def;
    }

    private static double optDouble(JsonObject o, String key, double def) {
        return o.has(key) ? o.get(key).getAsDouble() : def;
    }

    @Nullable
    private static ResourceLocation optRl(JsonObject o, String key) {
        return o.has(key) ? rl(o.get(key).getAsString()) : null;
    }

    private static ResourceLocation rl(String s) {
        ResourceLocation loc = ResourceLocation.tryParse(s);
        if (loc == null) throw new JsonParseException("invalid resource location '" + s + "'");
        return loc;
    }
}
