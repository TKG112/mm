package net.mcreator.mm.item;

import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.SlotContext;

import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.animatable.GeoItem;

import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.model.HumanoidModel;

import net.mcreator.mm.client.renderer.BlackNVG21Renderer;

import java.util.function.Consumer;

public class BlackNVG21Item extends Item implements GeoItem, ICurioItem {
	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
	public String animationprocedure = "empty";

	// Animations
	private static final RawAnimation ANIM_IDLE = RawAnimation.begin().thenLoop("animation.black_nvg21.idle");
	private static final RawAnimation ANIM_OPEN = RawAnimation.begin().thenPlayAndHold("animation.black_nvg21.opening").thenLoop("animation.black_nvg21.opened");
	private static final RawAnimation ANIM_CLOSE = RawAnimation.begin().thenPlayAndHold("animation.black_nvg21.closing").thenLoop("animation.black_nvg21.closed");

	public BlackNVG21Item() {
		super(new Item.Properties().stacksTo(1).durability(0));
	}

	@Override
	public void curioTick(SlotContext slotContext, ItemStack stack) {
		Entity entity = slotContext.entity();
		if (entity instanceof LivingEntity livingEntity) {
			if (stack.hasTag() && stack.getTag().contains("NvgCheck")) {
				boolean isNvgCheck = stack.getTag().getBoolean("NvgCheck");
				if (isNvgCheck && !this.animationprocedure.equals("Close")) {
					this.animationprocedure = "Close";
				} else if (!isNvgCheck && !this.animationprocedure.equals("Open")) {
					this.animationprocedure = "Open";
				}
			}
		} else {
			this.animationprocedure = "Idle";
		}
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private GeoArmorRenderer<?> renderer;

			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
				if (this.renderer == null)
					this.renderer = new BlackNVG21Renderer();
				this.renderer.prepForRender(livingEntity, itemStack, equipmentSlot, original);
				return this.renderer;
			}
		});
	}


	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
		data.add(new AnimationController<>(this, "procedureController", 5,state -> {
			if (!this.animationprocedure.equals("Idle")) {
				if (this.animationprocedure.equals("Close")) {
					// If the animation is not the current animation, set the animation to close
					if (!state.isCurrentAnimation(ANIM_CLOSE)) {state.getController().setAnimation(ANIM_CLOSE);}
				} else if (this.animationprocedure.equals("Open")) {
					// If the animation is not the current animation, set the animation to open
					if (!state.isCurrentAnimation(ANIM_OPEN)) {state.getController().setAnimation(ANIM_OPEN);}
				} else {
					// If the animation is neither close nor open, set the animation to idle
					state.getController().setAnimation(ANIM_IDLE);

				}
			}
			return PlayState.CONTINUE;
		} ));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache() {
		return this.cache;
	}
}