package myshampooisdrunk.drunk_server_toolkit.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.World;

public abstract class CustomRecipe<T> {
    final protected T input;
    final protected T ingredient;
    final protected T output;

    public CustomRecipe(T input, T output, T ingredient){
        this.input = input;
        this.output = output;
        this.ingredient = ingredient;
    }

    public T getInput(){
        return input;
    }

    public T getIngredient(){
        return ingredient;
    }

    public T getOutput() {
        return output;
    }

    public abstract boolean matches(RecipeInput input, World world);

    public abstract ItemStack craft(RecipeInput input, RegistryWrapper.WrapperLookup lookup);
}
