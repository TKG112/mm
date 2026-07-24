package net.tkg.ModernMayhem.content;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Mounts every content pack into Minecraft's pack system, for both pack types:
 * <ul>
 *   <li>{@link PackType#CLIENT_RESOURCES} -- exposes {@code assets/} so GeckoLib can load the pack's
 *       models, textures and animations.</li>
 *   <li>{@link PackType#SERVER_DATA} -- exposes {@code data/} so the pack's <b>tags</b> (and recipes,
 *       loot tables, advancements) load normally. This is essential: Curios decides slot eligibility
 *       from the {@code curios:<slot>} item tags, and ModernMayhem gates NVG mounting on
 *       {@code mm:has_head_mount} / {@code mm:has_visor_mount}. Without this a pack could never put
 *       an item into a Curios slot.</li>
 * </ul>
 * Packs are always-on and sit on top of the stack. Folder packs and zip packs are handled uniformly.
 * <p>
 * Lives in the common package (not {@code client}) because the datapack half must also work on a
 * dedicated server.
 */
public final class ContentPackAssets {
    private static final Logger LOGGER = LogUtils.getLogger();

    private ContentPackAssets() {}

    public static void onAddPackFinders(AddPackFindersEvent event) {
        PackType type = event.getPackType();
        Path root = FMLPaths.GAMEDIR.get().resolve(DataDrivenContent.PACKS_DIR);
        if (!Files.isDirectory(root)) {
            return;
        }
        event.addRepositorySource(consumer -> {
            try (Stream<Path> children = Files.list(root)) {
                children.forEach(child -> addPack(child, type, consumer));
            } catch (IOException e) {
                LOGGER.error("[MM] Failed to enumerate content packs for {}: {}", type, e.toString());
            }
        });
    }

    private static void addPack(Path entry, PackType type, Consumer<Pack> consumer) {
        String name = entry.getFileName().toString();
        boolean isDir = Files.isDirectory(entry);
        boolean isZip = !isDir && name.toLowerCase().endsWith(".zip");
        if (!isDir && !isZip) {
            return;
        }
        String packId = "mm_content/" + name;
        Pack.ResourcesSupplier supplier = id -> isZip
                ? new FilePackResources(id, entry.toFile(), false)
                : new PathPackResources(id, entry, false);
        try {
            Pack pack = Pack.readMetaAndCreate(packId, Component.literal("MM Content: " + name), true, supplier, type, Pack.Position.TOP, PackSource.BUILT_IN);
            if (pack != null) {
                consumer.accept(pack);
                LOGGER.info("[MM] Mounted content pack '{}' for {}", name, type);
            } else {
                LOGGER.warn("[MM] Content pack '{}' has no readable pack.mcmeta; not mounted for {}", name, type);
            }
        } catch (Exception e) {
            LOGGER.error("[MM] Failed to mount content pack '{}' for {}: {}", name, type, e.toString());
        }
    }
}
