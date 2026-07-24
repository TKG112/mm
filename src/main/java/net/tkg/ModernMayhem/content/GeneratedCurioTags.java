package net.tkg.ModernMayhem.content;

import com.mojang.logging.LogUtils;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraftforge.event.AddPackFindersEvent;
import net.tkg.ModernMayhem.content.def.CurioDefinition;
import net.tkg.ModernMayhem.content.def.GogglesDefinition;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A virtual datapack that supplies the {@code curios:<slot>} item tags for data-driven curios.
 * <p>
 * Curios decides slot eligibility from those tags, but a curio definition already declares its slot,
 * so making authors hand-write a tag file too would be redundant boilerplate. This generates the tag
 * entries in memory instead. The generated tags use {@code "replace": false}, so they merge with
 * ModernMayhem's own tags and with anything other packs or mods contribute.
 */
public final class GeneratedCurioTags {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String PACK_ID = "mm_generated_curio_tags";

    private GeneratedCurioTags() {}

    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) {
            return;
        }
        event.addRepositorySource(consumer -> {
            Pack pack = Pack.readMetaAndCreate(
                    PACK_ID,
                    Component.literal("ModernMayhem generated curio tags"),
                    true,
                    id -> new TagPackResources(),
                    PackType.SERVER_DATA,
                    Pack.Position.TOP,
                    PackSource.BUILT_IN
            );
            if (pack != null) {
                consumer.accept(pack);
            }
        });
    }

    /** Curios slot every goggle lives in. */
    private static final String GOGGLES_SLOT = "facewear";

    /** Builds {@code slot -> [item ids]} from the loaded curio and goggle definitions. */
    private static Map<String, Set<ResourceLocation>> collectBySlot() {
        Map<String, Set<ResourceLocation>> bySlot = new HashMap<>();
        for (CurioDefinition def : DataDrivenContent.curioDefinitions()) {
            bySlot.computeIfAbsent(def.slot(), s -> new LinkedHashSet<>()).add(def.id());
        }
        for (GogglesDefinition def : DataDrivenContent.gogglesDefinitions()) {
            bySlot.computeIfAbsent(GOGGLES_SLOT, s -> new LinkedHashSet<>()).add(def.id());
        }
        return bySlot;
    }

    private static byte[] tagJson(Set<ResourceLocation> ids) {
        StringBuilder sb = new StringBuilder("{\"replace\":false,\"values\":[");
        boolean first = true;
        for (ResourceLocation id : ids) {
            if (!first) sb.append(',');
            sb.append('"').append(id).append('"');
            first = false;
        }
        sb.append("]}");
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    /** Serves only {@code curios:tags/items/<slot>.json} plus the pack metadata. */
    private static final class TagPackResources implements PackResources {

        @Nullable
        @Override
        public IoSupplier<InputStream> getRootResource(String... path) {
            return null;
        }

        @Nullable
        @Override
        public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
            if (type != PackType.SERVER_DATA || !location.getNamespace().equals("curios")) {
                return null;
            }
            String slot = slotFromPath(location.getPath());
            if (slot == null) {
                return null;
            }
            Set<ResourceLocation> ids = collectBySlot().get(slot);
            if (ids == null || ids.isEmpty()) {
                return null;
            }
            byte[] json = tagJson(ids);
            return () -> new ByteArrayInputStream(json);
        }

        @Override
        public void listResources(PackType type, String namespace, String path, ResourceOutput output) {
            if (type != PackType.SERVER_DATA || !namespace.equals("curios")) {
                return;
            }
            collectBySlot().forEach((slot, ids) -> {
                ResourceLocation location = ResourceLocation.fromNamespaceAndPath("curios", "tags/items/" + slot + ".json");
                if (location.getPath().startsWith(path)) {
                    byte[] json = tagJson(ids);
                    output.accept(location, () -> new ByteArrayInputStream(json));
                }
            });
        }

        @Override
        public Set<String> getNamespaces(PackType type) {
            return type == PackType.SERVER_DATA && !collectBySlot().isEmpty() ? Set.of("curios") : Set.of();
        }

        @Nullable
        @Override
        public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
            if (serializer == PackMetadataSection.TYPE) {
                return (T) new PackMetadataSection(
                        Component.literal("ModernMayhem generated curio tags"),
                        SharedConstants.getCurrentVersion().getPackVersion(PackType.SERVER_DATA));
            }
            return null;
        }

        @Override
        public String packId() {
            return PACK_ID;
        }

        @Override
        public boolean isBuiltin() {
            return true;
        }

        @Override
        public void close() {
        }

        /** {@code tags/items/<slot>.json} -> {@code <slot>} */
        @Nullable
        private static String slotFromPath(String path) {
            String prefix = "tags/items/";
            String suffix = ".json";
            if (!path.startsWith(prefix) || !path.endsWith(suffix)) {
                return null;
            }
            return path.substring(prefix.length(), path.length() - suffix.length());
        }
    }

    static {
        LOGGER.debug("[MM] Generated curio tag pack ready");
    }
}
