package net.tkg.ModernMayhem;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tkg.ModernMayhem.client.event.RenderNVGFirstPerson;
import net.tkg.ModernMayhem.server.config.ArmorConfigGenerator;
import net.tkg.ModernMayhem.server.config.TestConfig;
import net.tkg.ModernMayhem.server.registry.*;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModernMayhemMod.ID)
public class ModernMayhemMod
{
    // Define mod id in a common place for everything to reference
    public static final String ID = "mm";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Check if game initialized
    private static boolean isGameReady = false;

    private static FMLJavaModLoadingContext modLoadingContext = null;

    public ModernMayhemMod(FMLJavaModLoadingContext context) {
        modLoadingContext = context;
        IEventBus modEventBus = context.getModEventBus();

        ItemRegistryMM.init(modEventBus);
        BlockRegistryMM.init(modEventBus);
        BlockEntityRegistryMM.init(modEventBus);
        PacketsRegistryMM.init();
        SoundRegistryMM.init(modEventBus);
        CreativeTabsRegistryMM.init(modEventBus);
        GUIRegistryMM.init(modEventBus);
        EntityRegistryMM.init(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::onGameReady);

        context.registerConfig(ModConfig.Type.COMMON, TestConfig.CONFIG);
        ArmorConfigGenerator.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Common setup
        LOGGER.info("HELLO FROM COMMON SETUP");

    }

    private void clientSetup(FMLClientSetupEvent event) {
        // All client only
        LOGGER.info("HELLO FROM CLIENT SETUP");

        CuriosRendererRegistryMM.register();
        ScreenRegistryMM.register(event);
        RenderNVGFirstPerson.initialiseFirstPersonRenderer();
    }

    private void onGameReady(FMLLoadCompleteEvent event) {
        isGameReady = true;
    }

    public static boolean isGameReady() {
        return isGameReady;
    }

    public static FMLJavaModLoadingContext getModLoadingContext() { return modLoadingContext; }
}
