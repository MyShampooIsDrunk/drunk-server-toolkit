package myshampooisdrunk.drunk_server_toolkit.recipe.potion;

import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public record CustomBrewingRecipe(Predicate<ItemStack> input, Predicate<ItemStack> ingredient, ItemStack output) {
    public boolean test(ItemStack input, ItemStack ingredient) {
        return this.input.test(input) && this.ingredient.test(ingredient);
    }
    public boolean testIngredient(ItemStack ingredient) {
        return this.ingredient.test(ingredient);
    }

    public ItemStack craft(ItemStack input, ItemStack ingredient) {
        if(test(input,ingredient)){
            return output;
        }
        return null;
    }
}
