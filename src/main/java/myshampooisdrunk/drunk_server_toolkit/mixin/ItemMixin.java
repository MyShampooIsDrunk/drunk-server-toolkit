package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.item.CustomItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin{
	@Inject(at = @At("HEAD"), method = "use",cancellable = true)
	private void useItem(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack item = user.getStackInHand(hand);
//		CustomEnchantmentHelper.getEnchantmentList(item).forEach((enchant, level)->{
//			enchant.onUse(world, user, hand,level,cir);
//		});
		CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.use(world,user,hand,cir));
	}

	@Inject(at=@At("HEAD"),method = "onStoppedUsing", cancellable = true)
	private void afterStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.onStoppedUsing(stack,world,user,remainingUseTicks,cir));
//		CustomEnchantmentHelper.getEnchantmentList(stack).forEach((enchant, level)->{
//			enchant.onStoppedUsing(stack,world,user,remainingUseTicks);
//		});
	}
	@Inject(at = @At("HEAD"), method = "getMaxUseTime", cancellable = true)
	public void getMaxUseTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir){
		CustomItemHelper.getCustomItem(stack).ifPresent(c -> cir.setReturnValue(c.getMaxUseTime(stack,user)));
	}
//	@ModifyReturnValue(method = "canRepair", at=@At("RETURN"))
//    private boolean canRepairCustom(boolean original, @Local(ordinal = 0, argsOnly = true) ItemStack stack,
//									@Local(ordinal = 1, argsOnly = true) ItemStack ingredient){
//		if(CustomItemHelper.getCustomItem(stack).orElse(null) instanceof CustomToolItem tool) {
//			return original || tool.getMaterial().getRepairIngredient().test(ingredient);
//		}
//		return original;
//	} can be done with components now!!!
	@Inject(at=@At("HEAD"), method = "finishUsing", cancellable = true)
	public void finishUsingCustom(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.finishUsing(stack,world,user,cir));
	}
	@Inject(at=@At("HEAD"), method = "postProcessComponents", cancellable = true)
	public void postProcessComponentsCustom(ItemStack stack, CallbackInfo ci){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.postProcessComponents(stack,ci));
	}
	@Inject(at=@At("HEAD"), method = "onItemEntityDestroyed", cancellable = true)
	public void onItemEntityDestroyedCustom(ItemEntity entity, CallbackInfo ci){
		CustomItemHelper.getCustomItem(entity.getStack()).ifPresent(custom -> custom.onItemEntityDestroyed(entity,ci));
	}
	@Inject(at=@At("HEAD"), method = "usageTick", cancellable = true)
	public void usageTickCustom(World world, LivingEntity user, ItemStack stack, int remainingUseTicks, CallbackInfo ci){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.usageTick(world, user, stack,remainingUseTicks, ci));
	}
	@Inject(at=@At("HEAD"), method = "useOnBlock", cancellable = true)
	public void useOnBlockCustom(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
		CustomItemHelper.getCustomItem(context.getStack()).ifPresent(custom -> custom.useOnBlock(context,cir));
	}
	@Inject(at=@At("HEAD"), method = "inventoryTick", cancellable = true)
	public void useOnBlockCustom(ItemStack stack, World world, Entity entity, int slot, boolean selected, CallbackInfo ci){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.inventoryTick(stack,world,entity,slot,selected,ci));
	}
	@Inject(at=@At("HEAD"), method = "onCraftByPlayer", cancellable = true)
	public void useOnBlockCustom(ItemStack stack, World world, PlayerEntity player, CallbackInfo ci){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.onCraftByPlayer(stack,world,player,ci));
	}
	@Inject(at=@At("HEAD"), method = "isUsedOnRelease", cancellable = true)
	public void useOnBlockCustom(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> cir.setReturnValue(custom.isUsedOnRelease(stack)));
	}
	@Inject(at=@At("HEAD"), method = "postDamageEntity", cancellable = true)
	public void useOnBlockCustom(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfo ci){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.postDamageEntity(stack,target,attacker,ci));
	}
}