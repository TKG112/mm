package net.mcreator.mm.init;

import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.EntityRenderersEvent;

import net.mcreator.mm.client.renderer.NVGRenderer;
import net.mcreator.mm.client.renderer.MilitaryHeadsetRenderer;
import net.mcreator.mm.client.renderer.MilitaryGogglesRenderer;
import net.mcreator.mm.client.renderer.MilitaryGlassesRenderer;
import net.mcreator.mm.client.renderer.MilitaryBalaclavaRenderer;
import net.mcreator.mm.client.renderer.BalaclavaRenderer;
import net.mcreator.mm.client.model.Modelmodel_Converted;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MmModCuriosRenderers {
	@SubscribeEvent
	public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions evt) {
		evt.registerLayerDefinition(MmModLayerDefinitions.NVG, Modelmodel_Converted::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.BALACLAVA, Modelmodel_Converted::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.MILITARY_BALACLAVA, Modelmodel_Converted::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.MILITARY_GLASSES, Modelmodel_Converted::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.MILITARY_GOGGLES, Modelmodel_Converted::createBodyLayer);
		evt.registerLayerDefinition(MmModLayerDefinitions.MILITARY_HEADSET, Modelmodel_Converted::createBodyLayer);
	}

	@SubscribeEvent
	public static void clientSetup(final FMLClientSetupEvent evt) {
		CuriosRendererRegistry.register(MmModItems.NVG.get(), NVGRenderer::new);
		CuriosRendererRegistry.register(MmModItems.BALACLAVA.get(), BalaclavaRenderer::new);
		CuriosRendererRegistry.register(MmModItems.MILITARY_BALACLAVA.get(), MilitaryBalaclavaRenderer::new);
		CuriosRendererRegistry.register(MmModItems.MILITARY_GLASSES.get(), MilitaryGlassesRenderer::new);
		CuriosRendererRegistry.register(MmModItems.MILITARY_GOGGLES.get(), MilitaryGogglesRenderer::new);
		CuriosRendererRegistry.register(MmModItems.MILITARY_HEADSET.get(), MilitaryHeadsetRenderer::new);
	}
}
