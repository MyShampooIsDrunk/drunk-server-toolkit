package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.registry.CustomBrewingRecipeRegistry;
import net.minecraft.block.entity.BrewingStandBlockEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandBlockEntity.class)
public abstract class BrewingStandBlockEntityMixin {
    @Inject(method = "isValid", at = @At("HEAD"), cancellable = true)
    private void modifyIsValid(int slot, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (slot != 4 && slot != 3 && CustomBrewingRecipeRegistry.isValidInput(stack)) {
            cir.setReturnValue(true);
        }
    }
}