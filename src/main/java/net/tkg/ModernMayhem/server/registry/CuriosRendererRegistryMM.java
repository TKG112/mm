package net.tkg.ModernMayhem.server.registry;

import net.minecraft.world.item.Item;
import net.tkg.ModernMayhem.client.renderer.curios.facewear.GenericSpecialGogglesRenderer;
import net.tkg.ModernMayhem.content.DataDrivenContent;
import net.tkg.ModernMayhem.content.client.DataCurioRenderer;
import net.tkg.ModernMayhem.content.item.DataCurioItem;
import net.tkg.ModernMayhem.content.item.DataGogglesItem;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CuriosRendererRegistryMM {

    public static void register() {

        // Registering Curios Renderers

        // Data-driven content-pack items -- shared renderers, registered by loop.
        for (Item item : DataDrivenContent.registeredItems()) {
            if (item instanceof DataCurioItem) {
                CuriosRendererRegistry.register(item, DataCurioRenderer::new);
            } else if (item instanceof DataGogglesItem) {
                CuriosRendererRegistry.register(item, GenericSpecialGogglesRenderer::new);
            }
        }
    }
}
