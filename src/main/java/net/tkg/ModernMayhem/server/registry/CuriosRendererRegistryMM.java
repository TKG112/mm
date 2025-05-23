package net.tkg.ModernMayhem.server.registry;

import net.tkg.ModernMayhem.client.renderer.curios.back.BackpackRenderer;
import net.tkg.ModernMayhem.client.renderer.curios.body.BandoleerRenderer;
import net.tkg.ModernMayhem.client.renderer.curios.body.PlateCarrierRenderer;
import net.tkg.ModernMayhem.client.renderer.curios.body.ReconRigRenderer;
import net.tkg.ModernMayhem.client.renderer.curios.facewear.NVGGogglesRenderer;
import net.tkg.ModernMayhem.client.renderer.curios.head.HeadGearRenderer;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CuriosRendererRegistryMM {

    public static void register() {

        // Registering Curios Renderers

        // Facewear Gear
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_GPNVG.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_GPNVG.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_PVS14.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_PVS14.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.GREEN_PVS14.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.ULTRA_GAMER_GPNVG.get(), NVGGogglesRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_PVS7.get(), NVGGogglesRenderer::new);

        // Head Gear
        CuriosRendererRegistry.register(ItemRegistryMM.BALACLAVA.get(), HeadGearRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_GLASSES.get(), HeadGearRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_GOGGLES.get(), HeadGearRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_HEADSET.get(), HeadGearRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_MILITARY_BALACLAVA.get(), HeadGearRenderer::new);


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
