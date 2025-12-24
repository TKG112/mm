package net.tkg.ModernMayhem.server.registry;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.tkg.ModernMayhem.server.network.*;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.lwjgl.glfw.GLFW;

public class KeyMappingRegistryMM {
    public static final String CATEGORY = "key.categories.mm";

    public static final KeyMapping TOGGLE_NVG_KEY = new KeyMapping("key.mm.toggle_nvg", GLFW.GLFW_KEY_N, CATEGORY) {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                if (CuriosUtil.hasNVGEquipped(Minecraft.getInstance().player)) {
                    PacketsRegistryMM.getChannel().sendToServer(new SwitchNVGStatusPacket());
                }
            }
            isDownOld = isDown;
        }

    };

    public static final KeyMapping INCREASE_TUBE_GAIN_KEY = new KeyMapping("key.mm.increase_tube_gain", GLFW.GLFW_KEY_UP, CATEGORY) {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                if (CuriosUtil.hasNVGEquipped(Minecraft.getInstance().player)) {
                    PacketsRegistryMM.getChannel().sendToServer(new NVGTubeGainUpPacket());
                }
            }
            isDownOld = isDown;
        }

    };

    public static final KeyMapping DECREASE_TUBE_GAIN_KEY = new KeyMapping("key.mm.decrease_tube_gain", GLFW.GLFW_KEY_DOWN, CATEGORY) {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                if (CuriosUtil.hasNVGEquipped(Minecraft.getInstance().player)) {
                    PacketsRegistryMM.getChannel().sendToServer(new NVGTubeGainDownPacket());
                }
            }
            isDownOld = isDown;
        }

    };

    public static final KeyMapping TOGGLE_AUTO_GAIN_KEY = new KeyMapping("key.mm.toggle_auto_gain", GLFW.GLFW_KEY_LEFT, CATEGORY) {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                if (CuriosUtil.hasNVGEquipped(Minecraft.getInstance().player)) {
                    PacketsRegistryMM.getChannel().sendToServer(new NVGAutoGainTogglePacket());
                }
            }
            isDownOld = isDown;
        }
    };

    public static final KeyMapping OPEN_BACKPACK_KEY = new KeyMapping("key.mm.open_backpack", GLFW.GLFW_KEY_B, CATEGORY) {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                Player player = Minecraft.getInstance().player;
                if (CuriosUtil.hasBackpackEquipped(player)) {
                    PacketsRegistryMM.getChannel().sendToServer(new OpenBackpackKeyPacket());
                }
            }
            isDownOld = isDown;
        }

    };

    public static final KeyMapping OPEN_RIG_KEY = new KeyMapping("key.mm.open_rig", GLFW.GLFW_KEY_0, CATEGORY) {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                Player player = Minecraft.getInstance().player;
                if (CuriosUtil.hasRigEquipped(player)) {
                    PacketsRegistryMM.getChannel().sendToServer(new OpenRigKeyPacket());
                }
            }
            isDownOld = isDown;
        }
    };
}
