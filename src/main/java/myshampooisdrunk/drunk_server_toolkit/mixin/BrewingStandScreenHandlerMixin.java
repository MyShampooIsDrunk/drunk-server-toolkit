package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.registry.CustomBrewingRecipeRegistry;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "net.minecraft.screen.BrewingStandScreenHandler$PotionSlot")
public abstract class BrewingStandScreenHandlerMixin {

    @Inject(method = "matches", at = @At("HEAD"), cancellable = true)
    private static void modifyMatches(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        if(CustomBrewingRecipeRegistry.isValidInput(stack)){
            cir.setReturnValue(true);
        }
    }
}