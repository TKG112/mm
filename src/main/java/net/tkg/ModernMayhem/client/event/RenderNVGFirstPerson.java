package net.tkg.ModernMayhem.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.client.models.custom.NVGFirstPersonModel;
import net.tkg.ModernMayhem.client.item.NVGFirstPersonFakeItem;
import net.tkg.ModernMayhem.client.renderer.custom.NVGFirstPersonRenderer;
import net.tkg.ModernMayhem.server.registry.ItemRegistryMM;
import net.tkg.ModernMayhem.server.util.CuriosUtil;
import org.joml.Quaternionf;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT)
public class RenderNVGFirstPerson {

    private static final Minecraft mc = Minecraft.getInstance();

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT) // Just to be safe, this event is only fired on the client side (even if it's only a client event)
    public static void onRenderOverlay(RenderGuiOverlayEvent.Post event) {
        if (mc.player == null || !shouldRender()) return;
        ItemStack stack = new ItemStack(ItemRegistryMM.FIRST_PERSON_NVG.get());

        // Get the renderer from IClientItemExtensions

        IClientItemExtensions extensions = IClientItemExtensions.of(stack);
        BlockEntityWithoutLevelRenderer renderer = extensions.getCustomRenderer();

        if (renderer instanceof NVGFirstPersonRenderer realRenderer) {
            PoseStack poseStack = event.getGuiGraphics().pose();
            poseStack.pushPose();
            LocalPlayer player = mc.player;
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();

            // Move the model to the center of the screen and behind every other GUI elements
            poseStack.translate(screenWidth / 2f, screenHeight / 2f, 0);
            // Resize the model to fit the screen
            poseStack.scale(1000f, 1000f, 1000f);

            // Just leaving this here for reference, it was used to try and position the model at the player's eye position (we can use this later if we need to with an other model)
            // System.out.println(player.getEyePosition().x * screenWidth * 0.001 + " " + player.getEyePosition().z * screenHeight * 0.001);
            // poseStack.translate(player.getEyePosition().x * screenWidth * 0.001, player.getEyePosition().z * screenHeight * 0.001, 0);


            // Move the model to the correct position in the player's view
            poseStack.translate(-0.5, -0.75, 0);

            MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
            realRenderer.renderByItem(
                    stack,
                    ItemDisplayContext.FIXED,
                    poseStack,
                    bufferSource,
                    LightTexture.FULL_BRIGHT,
                    OverlayTexture.NO_OVERLAY
            );

            bufferSource.endBatch();
            poseStack.popPose();
        } else System.err.println("No NVG First Person Renderer found for item: " + stack.getItem().getDescriptionId());
    }

    private static boolean shouldRender() {
        // Check if the player is wearing NVG goggles and if they are in first-person view
        if (mc.player == null) {
            return false;
        }
        if (!mc.options.getCameraType().isFirstPerson()) {
            return false;
        }
        if (!CuriosUtil.hasNVGEquipped(mc.player)) {
            return false;
        }
        return true;
    }

}
