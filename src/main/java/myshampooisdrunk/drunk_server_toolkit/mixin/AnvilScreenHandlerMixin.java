package myshampooisdrunk.drunk_server_toolkit.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AnvilScreenHandler.class, priority = 100)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
//    @Inject(
//            method = "updateResult",
//            slice=@Slice(
//                    from=@At(value="INVOKE", target = "Lnet/minecraft/inventory/Inventory;getStack(I)Lnet/minecraft/item/ItemStack;",ordinal = 1),
//                    to = @At("TAIL")
//            ), at=@At("RETURN")
//    )
//    public void injectThisCode(CallbackInfo ci, @Share("arg") LocalRef<ItemStack> argRef){
//        ItemStack itemStack2 = argRef.get();
//        ItemStack itemStack = this.input.getStack(0);
//        ItemStack itemStack3 = this.input.getStack(1);
//        ItemStack ret;
//        if(this.output.getStack(0).isEmpty()){
//            ret=itemStack2;
//        }else{
//            ret=this.output.getStack(0);
//        }
//        if(CustomEnchantmentHelper.echo(CustomEnchantmentHelper.canCombine(itemStack,itemStack3,this.player),"can combine: ")){
//            CustomEnchantmentHelper.handleCustomCombine(itemStack, itemStack3, ret);
//        }else{
//            ret = null;
//        }
//        if(ret!=null){
//            this.output.setStack(0,ret);
//            this.levelCost.set(1);
//            CustomEnchantmentHelper.setEnchantLore(ret,CustomEnchantmentHelper.getEnchantments(itemStack),CustomEnchantmentHelper.getEnchantments(ret));
//        }
//    }
//
//    @ModifyExpressionValue(method = "updateResult",at= @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;copy()Lnet/minecraft/item/ItemStack;",ordinal = 0))
//    public ItemStack modifyValue(ItemStack original, @Share("arg") LocalRef<ItemStack> argRef){
//        argRef.set(original);
//        return original;
//    }
//
//    @Shadow @Final private Property levelCost;

    //why the fuck did I code all of this...
/*
    @Shadow @Final private Property levelCost;

    @Shadow private int repairItemUsage;

    @Unique
    private boolean realbl222;

    @Shadow private @Nullable String newItemName;

    @Inject(method="updateResult",at=@At(value = "INVOKE",target = "Lnet/minecraft/enchantment/EnchantmentHelper;get(Lnet/minecraft/item/ItemStack;)Ljava/util/Map;",ordinal = 0))
    public void actuallyUpdateResult(CallbackInfo ci,@Local(ordinal = 1) ItemStack stack2, @Local(ordinal = 0) int i,@Local(ordinal = 1) int j,@Local(ordinal = 2) int k){
        ItemStack stack = this.input.getStack(0);
        ItemStack stack3 = this.input.getStack(1);

//        Map<AbstractCustomEnchantment, Integer> map = CustomEnchantmentHelper.get(stack2);
//        j += stack.getRepairCost() + (stack3.isEmpty() ? 0 : stack3.getRepairCost());
//        this.repairItemUsage = 0;
//        if(!stack3.isEmpty()) {
//            boolean bl;
//            boolean bl2 = bl = stack3.isOf(Items.ENCHANTED_BOOK) && !CustomEnchantmentHelper.getEnchantmentNbt(stack).isEmpty();
//            System.out.println(CustomEnchantmentHelper.getEnchantmentNbt(stack));
//            Map<AbstractCustomEnchantment, Integer> map2 = CustomEnchantmentHelper.get(stack3);
//            boolean bl222 = false;
//            boolean bl3 = false;
//            for (AbstractCustomEnchantment enchantment : map2.keySet()) {
//                int r;
//                if (enchantment == null) continue;
//                int q = map.getOrDefault(enchantment, 0);
//                r = q == (r = map2.get(enchantment)) ? r + 1 : Math.max(r, q);
//                boolean bl4 = enchantment.isAcceptableItem(stack);
//                if (this.player.getAbilities().creativeMode || stack.isOf(Items.ENCHANTED_BOOK)) {
//                    bl4 = true;
//                }
//                for (AbstractCustomEnchantment enchantment2 : map.keySet()) {
//                    if (enchantment2 == enchantment || enchantment.canCombine(enchantment2)) continue;
//                    bl4 = false;
//                    ++i;
//                }
//                if (!bl4) {
//                    bl3 = true;
//                    continue;
//                }
//                bl222 = true;
//                if (r > enchantment.getMaxLevel()) {
//                    r = enchantment.getMaxLevel();
//                }
//                map.put(enchantment, r);
//                int s = switch (enchantment.getRarity()) {
//                    case COMMON -> 1;
//                    case UNCOMMON -> 2;
//                    case RARE -> 4;
//                    case VERY_RARE -> 8;
//                };
//                if (bl) {
//                    s = Math.max(1, s / 2);
//                }
//                i += s * r;
//                if (stack.getCount() <= 1) continue;
//                i = 40;
//            }
//            this.realbl222 = bl3 && !bl222 ;
//            System.out.println("FIRSTFUCKINGFUNCTION: "+realbl222);
//        }
//        CustomEnchantmentHelper.set(map,stack2);
        realbl222 = true;
        CustomEnchantmentHelper.set(CustomEnchantmentHelper.handleCustomCombine(stack,stack3),stack2);
    }
    @ModifyVariable(method = "updateResult",at=@At(value = "STORE"),name = "bl22",ordinal = 3)
    private boolean changeValue(boolean value){
        System.out.println("real: " + realbl222 + "; init: " + value);
        return value || realbl222;
    }
    @Redirect(method="updateResult",at=@At(value = "INVOKE",target = "Lnet/minecraft/item/EnchantedBookItem;getEnchantmentNbt(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/nbt/NbtList;",ordinal = 0))
    public NbtList FUCKYOU(ItemStack stack){
        NbtList ret = EnchantedBookItem.getEnchantmentNbt(stack).copy();
        ret.addAll(CustomEnchantmentHelper.getEnchantmentNbt(stack));
        return ret;
    }

    @Redirect(method = "updateResult", at=@At(value = "INVOKE",target = "Lnet/minecraft/nbt/NbtList;isEmpty()Z",ordinal = 0))
    public boolean AAAAAAAAAAAAAAAAAAAAAAAAA(NbtList instance){
        System.out.println("instance: " + instance + " ISEMPTY: " + instance.isEmpty());

        return instance.isEmpty();
    }
//    @ModifyVariable(method = "updateResult",at=@At(value = "STORE"),name = "bl",ordinal = 1)
//    private boolean changeValue2(boolean value){
//        return value && !CustomEnchantmentHelper.getEnchantmentNbt(stack22).isEmpty();
//    }

*/
    public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {super(type, syncId, playerInventory, context);}
}
