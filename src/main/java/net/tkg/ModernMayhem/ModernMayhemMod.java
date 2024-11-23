package net.tkg.ModernMayhem;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tkg.ModernMayhem.client.renderer.BlackGPNVGRenderer;
import net.tkg.ModernMayhem.registry.ItemRegistryMM;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModernMayhemMod.ID)
public class ModernMayhemMod
{
    // Define mod id in a common place for everything to reference
    public static final String ID = "mm";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public ModernMayhemMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemRegistryMM.init(modEventBus);

        modEventBus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        // All client only
        LOGGER.info("HELLO FROM CLIENT SETUP");
        CuriosRendererRegistry.register(ItemRegistryMM.BLACK_GPNVG.get(), BlackGPNVGRenderer::new);
    }
}
