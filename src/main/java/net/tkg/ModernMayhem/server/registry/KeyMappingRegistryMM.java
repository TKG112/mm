package net.tkg.ModernMayhem.server.registry;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tkg.ModernMayhem.client.thermal.render.ThermalRenderer;
import net.tkg.ModernMayhem.server.item.curios.facewear.TVGGogglesItem;
import net.tkg.ModernMayhem.server.network.*;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.lwjgl.glfw.GLFW;

@OnlyIn(Dist.CLIENT)
public class KeyMappingRegistryMM {
    public static final String CATEGORY = "key.categories.mm";

    /** The thermal (TVG) goggle repurposes the gain Up/Down keys to cycle thermal palettes. */
    private static boolean isThermalGoggleEquipped(Player player) {
        if (player == null) return false;
        ItemStack face = CuriosUtil.getFaceWearItem(player);
        return face != null && face.getItem() instanceof TVGGogglesItem;
    }

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
                Player player = Minecraft.getInstance().player;
                if (isThermalGoggleEquipped(player)) {
                    ThermalRenderer.cycleThermalPalette();
                    PacketsRegistryMM.getChannel().sendToServer(new ThermalPaletteCyclePacket(true));
                } else if (CuriosUtil.hasNVGEquipped(player)) {
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
                Player player = Minecraft.getInstance().player;
                if (isThermalGoggleEquipped(player)) {
                    // Thermal goggle: previous palette.
                    ThermalRenderer.cycleThermalPaletteBack();
                    PacketsRegistryMM.getChannel().sendToServer(new ThermalPaletteCyclePacket(false));
                } else if (CuriosUtil.hasNVGEquipped(player)) {
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

    public static final KeyMapping TOGGLE_COTI_KEY = new KeyMapping("key.mm.toggle_coti", GLFW.GLFW_KEY_RIGHT, CATEGORY) {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                if (CuriosUtil.hasNVGEquipped(Minecraft.getInstance().player)) {
                    PacketsRegistryMM.getChannel().sendToServer(new NVGCotiTogglePacket());
                }
            }
            isDownOld = isDown;
        }
    };
}