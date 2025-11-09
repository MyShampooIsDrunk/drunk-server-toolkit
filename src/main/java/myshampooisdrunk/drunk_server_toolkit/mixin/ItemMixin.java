package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.item.CustomItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
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
		CustomItemHelper.getCustomItem(item).ifPresent(custom -> {
			System.out.println("finding not the problem");
			custom.use(world, user, hand, cir);
		});
	}

	@Inject(at=@At("HEAD"),method = "onStoppedUsing", cancellable = true)
	private void afterStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfoReturnable<Boolean> cir) {
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.onStoppedUsing(stack,world,user,remainingUseTicks,cir));
	}

	@Inject(at = @At("HEAD"), method = "getMaxUseTime", cancellable = true)
	public void getMaxUseTime(ItemStack stack, LivingEntity user, CallbackInfoReturnable<Integer> cir){
		CustomItemHelper.getCustomItem(stack).ifPresent(c -> cir.setReturnValue(c.getMaxUseTime(stack,user)));
	}

	@Inject(at=@At("HEAD"), method = "finishUsing", cancellable = true)
	public void finishUsingCustom(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.finishUsing(stack,world,user,cir));
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
	public void useOnBlockCustom(ItemStack stack, ServerWorld world, Entity entity, EquipmentSlot slot, CallbackInfo ci){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.inventoryTick(stack,world,entity,slot,ci));
	}

	@Inject(at=@At("HEAD"), method = "onCraftByPlayer", cancellable = true)
	public void useOnBlockCustom(ItemStack stack, PlayerEntity player, CallbackInfo ci){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.onCraftByPlayer(stack,player,ci));
	}

	@Inject(at=@At("HEAD"), method = "onCraft", cancellable = true)
	public void useOnBlockCustom(ItemStack stack, World world, CallbackInfo ci){
		CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.onCraft(stack,world,ci));
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