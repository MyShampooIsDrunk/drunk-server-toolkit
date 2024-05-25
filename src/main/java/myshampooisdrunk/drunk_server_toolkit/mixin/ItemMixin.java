package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin{
	@Inject(at = @At("HEAD"), method = "use",cancellable = true)
	private void useItem(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
		ItemStack item = user.getStackInHand(hand);
		CustomEnchantmentHelper.getEnchantmentList(item).forEach((enchant, level)->{
			enchant.onUse(world, user, hand,level,cir);
		});
		if(WeaponAPI.ITEMS.containsKey(item.getItem())){
			for(AbstractCustomItem custom : WeaponAPI.ITEMS.get(item.getItem())){
				if(custom.getItem().equals(item.getItem()) && item.getOrCreateNbt().getInt("CustomModelData") == custom.getId()){
					custom.onUse(world, user, hand,cir);
					break;
				}
			}
		}
		cir.setReturnValue(TypedActionResult.fail(item));
	}

	@Inject(at=@At("HEAD"),method = "onStoppedUsing")
	private void afterStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		CustomEnchantmentHelper.getEnchantmentList(stack).forEach((enchant, level)->{
			enchant.onStoppedUsing(stack,world,user,remainingUseTicks);
		});
		if(WeaponAPI.ITEMS.containsKey(stack.getItem())){
			for(AbstractCustomItem custom : WeaponAPI.ITEMS.get(stack.getItem())){
				if(custom.getItem().equals(stack.getItem()) && stack.getOrCreateNbt().getInt("CustomModelData") == custom.getId()){
					custom.onStoppedUsing(stack,world,user,remainingUseTicks);
					break;
				}
			}
		}
	}

}