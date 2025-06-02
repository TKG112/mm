package net.tkg.ModernMayhem.server.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LightLayer;

public class RenderingUtil {

    public static int getPackedLightAt(int x, int y, int z) {
        BlockPos skyBlockPos = new BlockPos( x, y, z );
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return 0;
        int skyLight  = level.getBrightness(LightLayer.SKY, skyBlockPos);
        int blockLight = level.getBrightness(LightLayer.BLOCK, skyBlockPos);
        return LightTexture.pack(skyLight, blockLight);
    }
}
