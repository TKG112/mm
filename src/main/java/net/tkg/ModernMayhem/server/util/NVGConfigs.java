package net.tkg.ModernMayhem.server.util;

import net.tkg.ModernMayhem.server.item.generic.GenericNVGGogglesItem;

public class NVGConfigs {
    public static GenericNVGGogglesItem.NVGConfig[] WHITE_PHOSPHOR_GPVNG = {
        new GenericNVGGogglesItem.NVGConfig(
                0.3f,
                0.7f,
                1,
                1,
                "textures/screens/gpnvg_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.4f,
                0.7f,
                1,
                1,
                "textures/screens/gpnvg_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.5f,
                0.7f,
                1,
                1,
                "textures/screens/gpnvg_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.6f,
                0.7f,
                1,
                1,
                "textures/screens/gpnvg_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.7f,
                0.7f,
                1,
                1,
                "textures/screens/gpnvg_overlay.png"
        )
    };

    public static GenericNVGGogglesItem.NVGConfig[] GREEN_PHOSPHOR_GPVNG = {
        new GenericNVGGogglesItem.NVGConfig(
                0.2f,
                0.4f,
                1,
                0.4f,
                "textures/screens/gpnvg_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.4f,
                0.4f,
                1,
                0.4f,
                "textures/screens/gpnvg_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.6f,
                0.4f,
                1,
                0.4f,
                "textures/screens/gpnvg_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.8f,
                0.4f,
                1,
                0.4f,
                "textures/screens/gpnvg_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                1.0f,
                0.4f,
                1,
                0.4f,
                "textures/screens/gpnvg_overlay.png"
        )
    };

    public static GenericNVGGogglesItem.NVGConfig[] WHITE_PHOSPHOR_PVS14 = {
        new GenericNVGGogglesItem.NVGConfig(
                0.4f,
                0.7f,
                1,
                1,
                "textures/screens/pvs7_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.5f,
                0.7f,
                1,
                1,
                "textures/screens/pvs7_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.6f,
                0.7f,
                1,
                1,
                "textures/screens/pvs7_overlay.png"
        )
    };


    public static GenericNVGGogglesItem.NVGConfig[] GREEN_PHOSPHOR_PVS14 = {
        new GenericNVGGogglesItem.NVGConfig(
                0.3f,
                0.2f,
                1,
                0.2f,
                "textures/screens/pvs7_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.5f,
                0.2f,
                1,
                0.2f,
                "textures/screens/pvs7_overlay.png"
        ),
        new GenericNVGGogglesItem.NVGConfig(
                0.7f,
                0.2f,
                1,
                0.2f,
                "textures/screens/pvs7_overlay.png"
        )
    };

    public static GenericNVGGogglesItem.NVGConfig[] WHITE_PHOSPHOR_PVS7 = {
            new GenericNVGGogglesItem.NVGConfig(
                    0.4f,
                    0.7f,
                    1,
                    1,
                    "textures/screens/pvs7_overlay.png"
            ),
            new GenericNVGGogglesItem.NVGConfig(
                    0.5f,
                    0.7f,
                    1,
                    1,
                    "textures/screens/pvs7_overlay.png"
            ),
            new GenericNVGGogglesItem.NVGConfig(
                    0.6f,
                    0.7f,
                    1,
                    1,
                    "textures/screens/pvs7_overlay.png"
            )
    };

    public static GenericNVGGogglesItem.NVGConfig[] GREEN_PHOSPHOR_PVS7 = {
            new GenericNVGGogglesItem.NVGConfig(
                    0.4f,
                    0.2f,
                    1,
                    0.2f,
                    "textures/screens/pvs7_overlay.png"
            ),
            new GenericNVGGogglesItem.NVGConfig(
                    0.5f,
                    0.2f,
                    1,
                    0.2f,
                    "textures/screens/pvs7_overlay.png"
            ),
            new GenericNVGGogglesItem.NVGConfig(
                    0.6f,
                    0.2f,
                    1,
                    0.2f,
                    "textures/screens/pvs7_overlay.png"
            )
    };

    private static int redValue = 100;
    private static int greenValue = 0;
    private static int blueValue = 0;


    public static float getUltraGamerRedValue() {
        if (redValue > 0 && greenValue == 100) {
            redValue--;
        } else if (redValue < 100 && blueValue == 100) {
            redValue++;
        }
        return redValue / 100.0f;
    }
    public static float getUltraGamerGreenValue() {
        if (greenValue > 0 && blueValue == 100) {
            greenValue--;
        } else if (greenValue < 100 && redValue == 100) {
            greenValue++;
        }
        return greenValue / 100.0f;
    }
    public static float getUltraGamerBlueValue() {
        if (blueValue > 0 && redValue == 100) {
            blueValue--;
        } else if (blueValue < 100 && greenValue == 100) {
            blueValue++;
        }
        return blueValue / 100.0f;
    }
}
