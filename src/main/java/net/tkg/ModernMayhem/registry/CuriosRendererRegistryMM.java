package net.tkg.ModernMayhem.registry;

import net.tkg.ModernMayhem.client.renderer.*;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CuriosRendererRegistryMM {

    public static void register() {

        // Registering Curios Renderers

        // Head Gear
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_GPNVG.get(), BlackGPNVGRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_GPNVG.get(), TanGPNVGRenderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_NVG21.get(), BlackNVG21Renderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_NVG21.get(), TanNVG21Renderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.GREEN_NVG21.get(), GreenNVG21Renderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.ULTRA_GAMER_GPNVG.get(), UltraGamerGPNVGRenderer::new);

        // Back Gear
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_BACKPACK_T1.get(), BlackBackpackT1Renderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_BACKPACK_T2.get(), BlackBackpackT2Renderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_BACKPACK_T3.get(), BlackBackpackT3Renderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_BACKPACK_T1.get(), TanBackpackT1Renderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_BACKPACK_T2.get(), TanBackpackT2Renderer::new);
        CuriosRendererRegistry.register(ItemRegistryMM.TAN_BACKPACK_T3.get(), TanBackpackT3Renderer::new);

    }
}
