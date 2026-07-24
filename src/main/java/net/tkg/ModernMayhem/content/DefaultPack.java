package net.tkg.ModernMayhem.content;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.tkg.ModernMayhem.ModernMayhemMod;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Stream;

/**
 * Writes ModernMayhem's own gear out as an ordinary content pack in the packs folder.
 * <p>
 * Shipping the defaults as a real, visible pack rather than burying them in the jar means authors can
 * read and copy from a complete working example, and anyone who wants to strip ModernMayhem back to
 * bare mechanics can simply delete the items they don't want.
 * <p>
 * By default the pack is rewritten on every launch, so a damaged one repairs itself. Setting
 * {@code regenerate_default_pack=false} in {@code modernmayhem.properties} stops that, and your edits
 * (including deletions) survive.
 * <p>
 * Runs from the mod constructor, before definitions are scanned. It deliberately avoids Forge's config
 * system: config values aren't loaded yet that early, so the flag lives in a plain properties file
 * beside the packs.
 */
public final class DefaultPack {
    private static final Logger LOGGER = LogUtils.getLogger();

    /** Folder the default pack is written to, inside the packs folder. */
    public static final String PACK_FOLDER = "ModernMayhem";
    /** Where the pack template lives inside the mod jar. */
    private static final String IN_JAR_ROOT = "modernmayhem/default_pack";

    private static final String SETTINGS_FILE = "modernmayhem.properties";
    private static final String REGENERATE_KEY = "regenerate_default_pack";

    private DefaultPack() {}

    /** Extracts the default pack unless the user has opted out of regeneration. */
    public static void ensureExtracted(Path packsRoot) {
        try {
            Files.createDirectories(packsRoot);

            if (!shouldRegenerate(packsRoot)) {
                LOGGER.info("[MM] '{}' is false -- leaving the default pack alone", REGENERATE_KEY);
                return;
            }

            Path source = locateInJar();
            if (source == null) {
                LOGGER.warn("[MM] No default pack found in the mod jar; skipping extraction");
                return;
            }

            Path target = packsRoot.resolve(PACK_FOLDER);
            int written = copyTree(source, target);
            LOGGER.info("[MM] Wrote default pack to '{}' ({} file(s))", target, written);
        } catch (Exception e) {
            LOGGER.error("[MM] Failed to write the default pack: {}", e.toString());
        }
    }

    /**
     * Reads the regenerate flag, creating a commented settings file the first time so the option is
     * discoverable rather than hidden in documentation.
     */
    private static boolean shouldRegenerate(Path packsRoot) throws IOException {
        Path settings = packsRoot.resolve(SETTINGS_FILE);

        if (!Files.exists(settings)) {
            String contents = """
                    # ModernMayhem content pack settings.
                    #
                    # ModernMayhem ships its own gear as a normal content pack in this folder, so you can
                    # read it as a worked example -- or change it.
                    #
                    # While this is true, that pack is rewritten every time the game starts, so any edits
                    # you make to it are lost (and a broken one repairs itself).
                    # Set it to false to take ownership of the default pack: your edits, and any items you
                    # delete, will then stick.
                    #
                    # Packs you add yourself are never touched by this setting.
                    %s=true
                    """.formatted(REGENERATE_KEY);
            Files.writeString(settings, contents);
            return true;
        }

        Properties properties = new Properties();
        try (InputStream in = Files.newInputStream(settings)) {
            properties.load(in);
        }
        return !"false".equalsIgnoreCase(properties.getProperty(REGENERATE_KEY, "true").trim());
    }

    /** The pack template inside the mod jar, or null if this build doesn't ship one. */
    private static Path locateInJar() {
        IModFileInfo modFile = ModList.get().getModFileById(ModernMayhemMod.ID);
        if (modFile == null) {
            return null;
        }
        Path path = modFile.getFile().findResource(IN_JAR_ROOT);
        return path != null && Files.exists(path) ? path : null;
    }

    /** Copies the template over the target, overwriting files but leaving unrelated ones in place. */
    private static int copyTree(Path source, Path target) throws IOException {
        int[] count = {0};
        try (Stream<Path> files = Files.walk(source)) {
            for (Path from : (Iterable<Path>) files::iterator) {
                String relative = source.relativize(from).toString().replace('\\', '/');
                Path to = relative.isEmpty() ? target : target.resolve(relative);

                if (Files.isDirectory(from)) {
                    Files.createDirectories(to);
                    continue;
                }
                Files.createDirectories(to.getParent());
                try (InputStream in = Files.newInputStream(from);
                     OutputStream out = Files.newOutputStream(to)) {
                    in.transferTo(out);
                }
                count[0]++;
            }
        }
        return count[0];
    }
}
