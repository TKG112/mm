package net.tkg.ModernMayhem.registry;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.tkg.ModernMayhem.network.SwitchNVGStatusPacket;
import org.lwjgl.glfw.GLFW;

public class KeyMappingRegistryMM {
    public static final String CATEGORY = "key.categories.mm";

    public static final KeyMapping TOGGLE_NVG_KEY = new KeyMapping("key.mm.toggle_nvg", GLFW.GLFW_KEY_N, CATEGORY) {
        private boolean isDownOld = false;

        @Override
        public void setDown(boolean isDown) {
            super.setDown(isDown);
            if (isDownOld != isDown && isDown) {
                PacketsRegistryMM.getChannel().sendToServer(new SwitchNVGStatusPacket());
                System.out.println("Key " + this.getDefaultKey().getName() + " pressed");
            }
            isDownOld = isDown;
        }

    };

    public static final KeyMapping INCREASE_TUBE_GAIN_KEY = new KeyMapping("key.mm.increase_tube_gain", GLFW.GLFW_KEY_UP, CATEGORY);

    public static final KeyMapping DECREASE_TUBE_GAIN_KEY = new KeyMapping("key.mm.decrease_tube_gain", GLFW.GLFW_KEY_DOWN, CATEGORY);

    public static final KeyMapping OPEN_BACKPACK_KEY = new KeyMapping("key.mm.open_backpack", GLFW.GLFW_KEY_B, CATEGORY);

    public static final KeyMapping OPEN_RIG_KEY = new KeyMapping("key.mm.open_rig", GLFW.GLFW_KEY_0, CATEGORY);
}
