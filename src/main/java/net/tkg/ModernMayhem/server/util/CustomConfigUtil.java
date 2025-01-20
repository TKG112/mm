package net.tkg.ModernMayhem.server.util;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import net.tkg.ModernMayhem.ModernMayhemMod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class CustomConfigUtil {

    public static void CreateMaterialConfigFile(ArmorConfigs pArmorConfig) {

        // Create a new file
        File file = new File("config/modern-mayhem/armor-configs/" + pArmorConfig.getName() + ".json");
        try {
            // Create a new file
            if (!file.getParentFile().mkdirs()) ModernMayhemMod.LOGGER.error("ModerMayhem - ERROR : Failed to create armor config file for {} !", pArmorConfig.getName());
            if (file.createNewFile()) {
                // Write the armor config to the file
                FileWriter writer = new FileWriter(file);
                ObjectMapper objectMapper = new ObjectMapper(new Gson());
                writer.write(objectMapper.writeValueAsString(new ArmorConfigs.SerializedArmorConfig(pArmorConfig)));
                writer.close();
            } else {
                ModernMayhemMod.LOGGER.error("ModerMayhem - ERROR : Failed to create armor config file for {} !", pArmorConfig.getName());
            }
        } catch (IOException e) {
            ModernMayhemMod.LOGGER.error("ModerMayhem - ERROR : Failed to create armor config file for {} !", pArmorConfig.getName());
        }
    }

    public static boolean DoesMaterialConfigExist(ArmorConfigs pArmorConfig) {
        File file = new File("config/modern-mayhem/armor-configs/" + pArmorConfig.getName() + ".json");
        return file.exists();
    }

    public static ArmorConfigs.SerializedArmorConfig GetMaterialConfig(ArmorConfigs pArmorConfig) {
        File file = new File("config/modern-mayhem/armor-configs/" + pArmorConfig.getName() + ".json");
        if (file.exists()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper(new Gson());
                return objectMapper.readValue(Files.readString(file.toPath(), Charset.defaultCharset()), ArmorConfigs.SerializedArmorConfig.class);
            } catch (IOException e) {
                ModernMayhemMod.LOGGER.error("ModerMayhem - ERROR : Failed to read armor config file for {} !\nMaking a new one for you :) !", pArmorConfig.getName());
                CreateMaterialConfigFile(pArmorConfig);
                return GetMaterialConfig(pArmorConfig);
            }
        }
        return null;
    }

    public static void init() {
        for (ArmorConfigs armorConfig : ArmorConfigs.values()) {
            if (!DoesMaterialConfigExist(armorConfig)) {
                CreateMaterialConfigFile(armorConfig);
            }

        }
    }
}
