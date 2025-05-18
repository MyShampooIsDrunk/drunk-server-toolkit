package myshampooisdrunk.drunk_server_toolkit.item;

import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.Identifier;

public interface CustomRecipe<T extends RecipeInput>{
    Recipe<T> recipe();
    Identifier getIdentifier();
}
