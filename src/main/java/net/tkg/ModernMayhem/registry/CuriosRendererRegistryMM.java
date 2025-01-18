package net.tkg.ModernMayhem.registry;

import net.tkg.ModernMayhem.client.renderer.curios.back.BackpackRenderer;
import net.tkg.ModernMayhem.client.renderer.curios.body.BandoleerRenderer;
import net.tkg.ModernMayhem.client.renderer.curios.body.PlateCarrierRenderer;
import net.tkg.ModernMayhem.client.renderer.curios.body.ReconRigRenderer;
import net.tkg.ModernMayhem.client.renderer.curios.facewear.NVGGogglesRenderer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CuriosRendererRegistryMM {

    public static void register() {

        // Registering Curios Renderers

        // Head Gear
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_GPNVG.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_GPNVG.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_NVG21.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_NVG21.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.GREEN_NVG21.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.ULTRA_GAMER_GPNVG.get(), NVGGogglesRenderer::new);

        // Back Gear
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_BACKPACK_T1.get(), BackpackRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_BACKPACK_T2.get(), BackpackRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_BACKPACK_T3.get(), BackpackRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.GREEN_BACKPACK_T1.get(), BackpackRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.GREEN_BACKPACK_T2.get(), BackpackRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.GREEN_BACKPACK_T3.get(), BackpackRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_BACKPACK_T1.get(), BackpackRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_BACKPACK_T2.get(), BackpackRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_BACKPACK_T3.get(), BackpackRenderer::new);

        // Body Gear
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_PLATE_CARRIER.get(), PlateCarrierRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_PLATE_CARRIER_AMMO.get(), PlateCarrierRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_PLATE_CARRIER_POUCHES.get(), PlateCarrierRenderer::new);

        CuriosRendererRegistry.register(ItemRegistryMM.TAN_PLATE_CARRIER.get(), PlateCarrierRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_PLATE_CARRIER_AMMO.get(), PlateCarrierRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_PLATE_CARRIER_POUCHES.get(), PlateCarrierRenderer::new);

        CuriosRendererRegistry.register(ItemRegistryMM.TAN_BANDOLEER.get(), BandoleerRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.GREEN_RECON.get(), ReconRigRenderer::new);
    }
}
