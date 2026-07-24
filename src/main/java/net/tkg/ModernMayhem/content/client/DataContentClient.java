package net.tkg.ModernMayhem.content.client;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLPaths;
import net.tkg.ModernMayhem.content.ContentPackLoader;
import net.tkg.ModernMayhem.content.DataDrivenContent;
import net.tkg.ModernMayhem.content.def.DisplaySettings;
import net.tkg.ModernMayhem.content.item.DataArmorItem;
import net.tkg.ModernMayhem.content.item.DataCurioItem;
import net.tkg.ModernMayhem.content.item.DataGogglesItem;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Client-side glue for the data-driven content system: injects a "custom renderer" baked model for
 * each data item so its {@code GeoItemRenderer} BEWLR is used for the inventory/hand icon, without
 * requiring pack authors to hand-write a {@code models/item/*.json}. If a pack <em>does</em> ship one,
 * it is respected.
 * <p>
 * The injected model carries per-item display transforms so icons are framed correctly. The framing is
 * re-read from the packs' {@code "display"} blocks on every reload, so it can be tuned live (F3+T).
 * <p>
 * Pack mounting itself lives in {@code ContentPackAssets}, which runs on both sides.
 */
public final class DataContentClient {
    private static final Logger LOGGER = LogUtils.getLogger();

    private DataContentClient() {}

    public static void init(IEventBus modEventBus) {
        modEventBus.addListener(DataContentClient::onModifyBakingResult);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.addListener(DataKeybinds::onLoggingOut);
    }

    private static void onModifyBakingResult(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> models = event.getModels();
        Path packsRoot = FMLPaths.GAMEDIR.get().resolve(DataDrivenContent.PACKS_DIR);
        Map<ResourceLocation, DisplaySettings> overrides = ContentPackLoader.scanDisplayOverrides(packsRoot);
        ResourceManager resources = Minecraft.getInstance().getResourceManager();

        int injected = 0;
        for (Item item : DataDrivenContent.registeredItems()) {
            ResourceLocation id;
            DisplaySettings defaults;
            if (item instanceof DataArmorItem armor) {
                id = armor.definition().id();
                defaults = DisplaySettings.defaultFor(armor.getType());
            } else if (item instanceof DataCurioItem curio) {
                id = curio.definition().id();
                defaults = DisplaySettings.defaultForCurio();
            } else if (item instanceof DataGogglesItem goggles) {
                id = goggles.definition().id();
                defaults = DisplaySettings.defaultForCurio();
            } else {
                continue;
            }
            ModelResourceLocation mrl = new ModelResourceLocation(id, "inventory");

            if (hasAuthoredItemModel(resources, id)) {
                LOGGER.debug("[MM] '{}' ships its own item model; leaving it untouched", id);
                continue;
            }

            DisplaySettings framing = defaults.withOverrides(overrides.get(id));
            models.put(mrl, new CustomRendererModel(buildTransforms(framing)));
            injected++;
        }
        if (injected > 0) {
            LOGGER.info("[MM] Injected {} custom-renderer item model(s) for data-driven items", injected);
        }
    }

    /** True if some resource pack supplies {@code assets/<ns>/models/item/<name>.json} for this item. */
    private static boolean hasAuthoredItemModel(ResourceManager resources, ResourceLocation id) {
        ResourceLocation modelFile = ResourceLocation.fromNamespaceAndPath(
                id.getNamespace(), "models/item/" + id.getPath() + ".json");
        return resources.getResource(modelFile).isPresent();
    }

    private static ItemTransforms buildTransforms(DisplaySettings framing) {
        return new ItemTransforms(
                toTransform(framing.get(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)),
                toTransform(framing.get(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)),
                toTransform(framing.get(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)),
                toTransform(framing.get(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)),
                toTransform(framing.get(ItemDisplayContext.HEAD)),
                toTransform(framing.get(ItemDisplayContext.GUI)),
                toTransform(framing.get(ItemDisplayContext.GROUND)),
                toTransform(framing.get(ItemDisplayContext.FIXED))
        );
    }

    /** JSON translations are in model pixels; display translations are in blocks (1px = 1/16). */
    private static ItemTransform toTransform(DisplaySettings.Entry entry) {
        return new ItemTransform(
                new Vector3f(entry.rotation()),
                new Vector3f(entry.translation()).mul(0.0625f),
                new Vector3f(entry.scale()));
    }

    /** Minimal baked model whose only job is to route rendering to the item's BEWLR, with framing. */
    private static final class CustomRendererModel implements BakedModel {
        private final ItemTransforms transforms;

        CustomRendererModel(ItemTransforms transforms) {
            this.transforms = transforms;
        }

        @Override public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction dir, RandomSource rand) { return Collections.emptyList(); }
        @Override public boolean useAmbientOcclusion() { return false; }
        @Override public boolean isGui3d() { return true; }
        @Override public boolean usesBlockLight() { return false; }
        @Override public boolean isCustomRenderer() { return true; }
        @Override public TextureAtlasSprite getParticleIcon() {
            return Minecraft.getInstance().getModelManager()
                    .getAtlas(InventoryMenu.BLOCK_ATLAS)
                    .getSprite(MissingTextureAtlasSprite.getLocation());
        }
        @Override public ItemTransforms getTransforms() { return this.transforms; }
        @Override public ItemOverrides getOverrides() { return ItemOverrides.EMPTY; }
    }
}
