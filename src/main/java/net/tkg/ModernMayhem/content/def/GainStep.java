package net.tkg.ModernMayhem.content.def;

import net.minecraft.resources.ResourceLocation;

/**
 * One rung of a goggle's gain ladder -- the Increase/Decrease Gain keys step through these.
 * <p>
 * Note there is no min/max gain here: ModernMayhem derives the gain range from the spread of
 * {@link #brightness} across the whole ladder, so a step only declares its own values.
 *
 * @param brightness       tube brightness at this step
 * @param red              phosphor red   (green vs white phosphor lives here)
 * @param green            phosphor green
 * @param blue             phosphor blue
 * @param overlay          screen overlay texture (the tube shape)
 * @param noise            grain multiplier
 * @param autoGainSpeed    how fast auto-gain reacts
 * @param autoGainOffset   shifts the low end of the gain range
 * @param autoGatingOffset shifts the high end of the gain range
 * @param autoGatingSpeed  how fast auto-gating reacts
 */
public record GainStep(
        float brightness,
        float red,
        float green,
        float blue,
        ResourceLocation overlay,
        float noise,
        float autoGainSpeed,
        float autoGainOffset,
        float autoGatingOffset,
        float autoGatingSpeed
) {
}
