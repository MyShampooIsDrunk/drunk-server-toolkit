package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper;
import myshampooisdrunk.drunk_server_toolkit.weapon.AbstractCustomItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public abstract class ItemMixin{
	@Inject(at = @At("HEAD"), method = "use")
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
	}

//	@Inject(method="hasGlint",at=@At("TAIL"), cancellable = true)//head or tail doesnt matter cuz its a 1-line method
//	private void modifyHasGlint(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
//		cir.setReturnValue(cir.getReturnValueZ() || CustomEnchantmentHelper.hasEnchantments(stack));
//	}
//	@Inject(method="appendTooltip",at=@At("TAIL"))
//	public void appendCustomTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci){
//		System.out.println(stack + "SILLY SHIT 33: " + stack.getNbt());
//		System.out.println("SILLY SHIT: "+ stack.getSubNbt(CustomEnchantmentHelper.CUSTOM_ENCHANT_KEY));
//		CustomEnchantmentHelper.appendCustomEnchantments(tooltip, CustomEnchantmentHelper.getEnchantments(stack));
//	}

}