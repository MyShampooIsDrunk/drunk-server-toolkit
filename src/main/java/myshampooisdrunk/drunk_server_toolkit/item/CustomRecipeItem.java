package myshampooisdrunk.drunk_server_toolkit.item;

import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class CustomRecipeItem<T extends RecipeInput> extends AbstractCustomItem{
    public CustomRecipeItem(Item item, Identifier identifier) {
        super(item, identifier);
    }

    public CustomRecipeItem(Item item, Identifier identifier, @Nullable String itemName) {
        super(item, identifier, itemName);
    }

    public CustomRecipeItem(Item item, Identifier identifier, @Nullable String itemName, boolean customModel) {
        super(item, identifier, itemName, customModel);
    }

    public abstract Recipe<T> recipe();
}
