package myshampooisdrunk.drunk_server_toolkit.mixin;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper.CUSTOM_ENCHANT_KEY;

@Mixin(ItemStack.class)
public class ItemStackMixin{
    private ItemStack dis = (ItemStack)(Object)this;
    @Inject(method = "hasEnchantments",at=@At("TAIL"),cancellable = true)
    public void doesActuallyIsHasEnchantments(CallbackInfoReturnable<Boolean> cir){
        if (dis.getNbt() != null && dis.getNbt().contains(CUSTOM_ENCHANT_KEY, NbtElement.LIST_TYPE)) {
            cir.setReturnValue(!dis.getNbt().getList(CUSTOM_ENCHANT_KEY, NbtElement.COMPOUND_TYPE).isEmpty());
        }
    }
}
