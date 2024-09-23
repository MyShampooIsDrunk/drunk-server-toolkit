package myshampooisdrunk.drunk_server_toolkit.recipe.potion;

import myshampooisdrunk.drunk_server_toolkit.recipe.CustomRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public class CustomBrewingRecipe extends CustomRecipe<Ingredient> {
    public CustomBrewingRecipe(Ingredient input, Ingredient output, Ingredient ingredient) {
        super(input, output, ingredient);
    }

    @Override
    public boolean matches(RecipeInput input, World world) {

        return false;
    }

    @Override
    public ItemStack craft(RecipeInput input, RegistryWrapper.WrapperLookup lookup) {
        return null;
    }
}
