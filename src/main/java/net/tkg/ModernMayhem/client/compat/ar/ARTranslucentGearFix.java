package net.tkg.ModernMayhem.client.compat.ar;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tkg.ModernMayhem.ModernMayhemMod;
import net.tkg.ModernMayhem.content.item.DataCurioItem;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * Renders entities wearing translucent gear through Accelerated Rendering's vanilla pipeline.
 * <p>
 * Accelerated Rendering batches entity geometry and submits it on its own schedule. That is fine for
 * opaque gear, where the depth buffer sorts everything out no matter what order things are drawn in,
 * but translucent gear depends on being drawn <em>after</em> whatever should show through it. Under
 * the accelerated pipeline a goggle lens is drawn before the wearer's head, so the head then fails
 * the depth test against the lens and the player's face disappears.
 * <p>
 * Forcing the vanilla pipeline for the duration of that one entity's render restores the ordinary
 * ordering. This mirrors {@code GameRendererThermalARMixin}, which does the same thing for the whole
 * level while the thermal view is active.
 * <p>
 * The cost is that an entity wearing translucent gear loses acceleration, so this is deliberately
 * scoped as narrowly as possible: only when Accelerated Rendering is installed, and only for entities
 * actually wearing something translucent.
 */
@Mod.EventBusSubscriber(modid = ModernMayhemMod.ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class ARTranslucentGearFix {

    /**
     * The entity we pushed the pipeline for, so the pop in {@code Post} can only ever match a push in
     * {@code Pre}. AR's pipeline control is a balanced stack -- an unmatched push or pop would leave
     * every later entity on the wrong pipeline.
     */
    private static LivingEntity pushedFor;

    private ARTranslucentGearFix() {
    }

    // LOWEST so any other listener that wants to cancel the render has already done so: a cancelled
    // Pre means Post never fires, which would strand the push.
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRenderPre(RenderLivingEvent.Pre<?, ?> event) {
        if (!ARCompat.isLoaded() || event.isCanceled() || pushedFor != null) return;
        if (!wearsTranslucentGear(event.getEntity())) return;

        ARCompat.useVanillaEntityPipeline();
        pushedFor = event.getEntity();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRenderPost(RenderLivingEvent.Post<?, ?> event) {
        if (pushedFor != event.getEntity()) return;

        ARCompat.resetEntityPipeline();
        pushedFor = null;
    }

    private static boolean wearsTranslucentGear(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity).map(inventory -> {
            for (var handler : inventory.getCurios().values()) {
                var stacks = handler.getStacks();
                for (int i = 0; i < stacks.getSlots(); i++) {
                    if (isTranslucent(stacks.getStackInSlot(i))) return true;
                }
            }
            return false;
        }).orElse(false);
    }

    private static boolean isTranslucent(ItemStack stack) {
        return stack.getItem() instanceof DataCurioItem curio
                && curio.definition().transparent();
    }
}
