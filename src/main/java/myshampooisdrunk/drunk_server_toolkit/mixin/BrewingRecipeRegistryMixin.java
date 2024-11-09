package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.recipe.potion.CustomBrewingRecipe;
import myshampooisdrunk.drunk_server_toolkit.register.CustomBrewingRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeRegistryMixin {

    @Inject(method = "craft", at = @At("HEAD"), cancellable = true)
    private void modifyCraft(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
        if (input == null || ingredient == null) return;

        for (CustomBrewingRecipe recipe : CustomBrewingRecipeRegistry.BREWING_RECIPES) {
            ItemStack stack = recipe.craft(input,ingredient);
            if(stack != null) {
                cir.setReturnValue(stack.copy());
                return;
            }
        }
    }

    @Inject(method = "hasRecipe", at = @At(value = "HEAD"), cancellable = true)
    private void modifyHasRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir){
        for (CustomBrewingRecipe recipe : CustomBrewingRecipeRegistry.BREWING_RECIPES) {
            if(recipe.test(input,ingredient)) cir.setReturnValue(true);
        }
    }

    @Inject(method = "isValidIngredient", at = @At(value = "HEAD"), cancellable = true)
    private void modifyIsValidIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        for (CustomBrewingRecipe recipe : CustomBrewingRecipeRegistry.BREWING_RECIPES) {
            if(recipe.testIngredient(stack)) cir.setReturnValue(true);
        }
    }

}