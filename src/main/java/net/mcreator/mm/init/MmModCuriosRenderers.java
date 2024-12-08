package net.mcreator.mm.init;

import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;

import net.mcreator.mm.client.renderer.TanVisorRenderer;
import net.mcreator.mm.client.renderer.TanPlateCarrierRenderer;
import net.mcreator.mm.client.renderer.TanPlateCarrierPouchesRenderer;
import net.mcreator.mm.client.renderer.TanPlateCarrierAmmoRenderer;
import net.mcreator.mm.client.renderer.TanNVG21Renderer;
import net.mcreator.mm.client.renderer.TanGPNVGRenderer;
import net.mcreator.mm.client.renderer.TanBackpack3Renderer;
import net.mcreator.mm.client.renderer.TanBackpack2Renderer;
import net.mcreator.mm.client.renderer.TanBackpack1Renderer;
import net.mcreator.mm.client.renderer.ReconRenderer;
import net.mcreator.mm.client.renderer.MilitaryHeadsetRenderer;
import net.mcreator.mm.client.renderer.MilitaryGogglesRenderer;
import net.mcreator.mm.client.renderer.MilitaryGlassesRenderer;
import net.mcreator.mm.client.renderer.MilitaryBalaclavaRenderer;
import net.mcreator.mm.client.renderer.GreenNVG21Renderer;
import net.mcreator.mm.client.renderer.GreenBackpack3Renderer;
import net.mcreator.mm.client.renderer.GreenBackpack2Renderer;
import net.mcreator.mm.client.renderer.GreenBackpack1Renderer;
import net.mcreator.mm.client.renderer.BlackVisorRenderer;
import net.mcreator.mm.client.renderer.BlackPlateCarrierRenderer;
import net.mcreator.mm.client.renderer.BlackPlateCarrierPouchesRenderer;
import net.mcreator.mm.client.renderer.BlackPlateCarrierAmmoRenderer;
import net.mcreator.mm.client.renderer.BlackNVG21Renderer;
import net.mcreator.mm.client.renderer.BlackGPNVGRenderer;
import net.mcreator.mm.client.renderer.BlackBackpack3Renderer;
import net.mcreator.mm.client.renderer.BlackBackpack2Renderer;
import net.mcreator.mm.client.renderer.BlackBackpack1Renderer;
import net.mcreator.mm.client.renderer.BandoleerRenderer;
import net.mcreator.mm.client.renderer.BalaclavaRenderer;
import net.mcreator.mm.client.model.Modelplaceholder;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MmModCuriosRenderers {
	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt) {
		evt.registerLayerDefinition(MmModLayerDefinitions.BALACLAVA, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.MILITARY_BALACLAVA, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.MILITARY_GLASSES, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.MILITARY_GOGGLES, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.MILITARY_HEADSET, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BLACK_BACKPACK_1, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BLACK_BACKPACK_2, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BLACK_BACKPACK_3, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BLACK_PLATE_CARRIER, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BLACK_PLATE_CARRIER_AMMO, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BLACK_PLATE_CARRIER_POUCHES, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BANDOLEER, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.RECON, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BLACK_VISOR, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BLACK_NVG_21, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BLACK_GPNVG, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.GREEN_NVG_21, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.TAN_GPNVG, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.TAN_NVG_21, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.TAN_VISOR, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.GREEN_BACKPACK_1, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.GREEN_BACKPACK_2, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.GREEN_BACKPACK_3, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.TAN_BACKPACK_1, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.TAN_BACKPACK_2, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.TAN_BACKPACK_3, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.TAN_PLATE_CARRIER_AMMO, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.TAN_PLATE_CARRIER, Modelplaceholder::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.TAN_PLATE_CARRIER_POUCHES, Modelplaceholder::createBodyLayer);
	}

	@SubscribeEvent
	public static void clientSetup(final FMLClientSetupEvent evt) {
		CuriosRendererRegistry.register(MmModItems.BALACLAVA.get(), BalaclavaRenderer::new);
		CuriosRendererRegistry.register(MmModItems.MILITARY_BALACLAVA.get(), MilitaryBalaclavaRenderer::new);
		CuriosRendererRegistry.register(MmModItems.MILITARY_GLASSES.get(), MilitaryGlassesRenderer::new);
		CuriosRendererRegistry.register(MmModItems.MILITARY_GOGGLES.get(), MilitaryGogglesRenderer::new);
		CuriosRendererRegistry.register(MmModItems.MILITARY_HEADSET.get(), MilitaryHeadsetRenderer::new);
		CuriosRendererRegistry.register(MmModItems.BLACK_BACKPACK_1.get(), BlackBackpack1Renderer::new);
		CuriosRendererRegistry.register(MmModItems.BLACK_BACKPACK_2.get(), BlackBackpack2Renderer::new);
		CuriosRendererRegistry.register(MmModItems.BLACK_BACKPACK_3.get(), BlackBackpack3Renderer::new);
		CuriosRendererRegistry.register(MmModItems.BLACK_PLATE_CARRIER.get(), BlackPlateCarrierRenderer::new);
		CuriosRendererRegistry.register(MmModItems.BLACK_PLATE_CARRIER_AMMO.get(), BlackPlateCarrierAmmoRenderer::new);
		CuriosRendererRegistry.register(MmModItems.BLACK_PLATE_CARRIER_POUCHES.get(), BlackPlateCarrierPouchesRenderer::new);
		CuriosRendererRegistry.register(MmModItems.BANDOLEER.get(), BandoleerRenderer::new);
		CuriosRendererRegistry.register(MmModItems.RECON.get(), ReconRenderer::new);
		CuriosRendererRegistry.register(MmModItems.BLACK_VISOR.get(), BlackVisorRenderer::new);
		CuriosRendererRegistry.register(MmModItems.BLACK_NVG_21.get(), BlackNVG21Renderer::new);
		CuriosRendererRegistry.register(MmModItems.BLACK_GPNVG.get(), BlackGPNVGRenderer::new);
		CuriosRendererRegistry.register(MmModItems.GREEN_NVG_21.get(), GreenNVG21Renderer::new);
		CuriosRendererRegistry.register(MmModItems.TAN_GPNVG.get(), TanGPNVGRenderer::new);
		CuriosRendererRegistry.register(MmModItems.TAN_NVG_21.get(), TanNVG21Renderer::new);
		CuriosRendererRegistry.register(MmModItems.TAN_VISOR.get(), TanVisorRenderer::new);
		CuriosRendererRegistry.register(MmModItems.GREEN_BACKPACK_1.get(), GreenBackpack1Renderer::new);
		CuriosRendererRegistry.register(MmModItems.GREEN_BACKPACK_2.get(), GreenBackpack2Renderer::new);
		CuriosRendererRegistry.register(MmModItems.GREEN_BACKPACK_3.get(), GreenBackpack3Renderer::new);
		CuriosRendererRegistry.register(MmModItems.TAN_BACKPACK_1.get(), TanBackpack1Renderer::new);
		CuriosRendererRegistry.register(MmModItems.TAN_BACKPACK_2.get(), TanBackpack2Renderer::new);
		CuriosRendererRegistry.register(MmModItems.TAN_BACKPACK_3.get(), TanBackpack3Renderer::new);
		CuriosRendererRegistry.register(MmModItems.TAN_PLATE_CARRIER_AMMO.get(), TanPlateCarrierAmmoRenderer::new);
		CuriosRendererRegistry.register(MmModItems.TAN_PLATE_CARRIER.get(), TanPlateCarrierRenderer::new);
		CuriosRendererRegistry.register(MmModItems.TAN_PLATE_CARRIER_POUCHES.get(), TanPlateCarrierPouchesRenderer::new);
	}
}
