package myshampooisdrunk.drunk_server_toolkit.item;

import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public interface CustomRecipeItem<T extends RecipeInput>{
    Recipe<T> recipe();
    Identifier getIdentifier();
}
