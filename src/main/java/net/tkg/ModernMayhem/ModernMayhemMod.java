package net.tkg.ModernMayhem;

import com.mojang.logging.LogUtils;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.tkg.ModernMayhem.client.config.ClientConfig;
import net.tkg.ModernMayhem.client.event.ItemInteractionEvent;
import net.tkg.ModernMayhem.client.event.RenderNVGFirstPerson;
import net.tkg.ModernMayhem.client.registry.ClientItemRegistryMM;
import net.tkg.ModernMayhem.server.compat.OculusCompat;
import net.tkg.ModernMayhem.server.config.ArmorConfig;
import net.tkg.ModernMayhem.server.config.CommonConfig;
import net.tkg.ModernMayhem.server.config.ServerConfig;
import net.tkg.ModernMayhem.server.registry.*;
import org.slf4j.Logger;

@Mod(ModernMayhemMod.ID)
public class ModernMayhemMod {
    public static final String ID = "mm";
    public static final Logger LOGGER = LogUtils.getLogger();
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
        AttributesRegistryMM.init(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::onGameReady);

        if (FMLEnvironment.dist.isClient()) {
            ClientItemRegistryMM.init(modEventBus);
            ItemInteractionEvent.register();
        }

        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.CONFIG, "modern-mayhem-common.toml");
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CONFIG, "modern-mayhem-client.toml");
        context.registerConfig(ModConfig.Type.SERVER, ServerConfig.CONFIG, "modern-mayhem-server.toml");
        ArmorConfig.init();

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (ModList.get().isLoaded("oculus")) {
            LOGGER.info("Oculus is loaded, enabling Oculus compatibility");
            OculusCompat.init(modLoadingContext.getModEventBus());
        }
    }

    private void clientSetup(FMLClientSetupEvent event) {
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