package myshampooisdrunk.drunk_server_toolkit.item;

import com.mojang.datafixers.util.Either;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class AbstractRecipeItem<T extends RecipeInput> extends AbstractCustomItem implements CustomRecipe<T>{
    private final Recipe<T> recipe;
    public AbstractRecipeItem(Item item, Identifier identifier, Recipe<T> recipe) {
        super(item, identifier);
        this.recipe = recipe;
    }

    public AbstractRecipeItem(Item item, Identifier identifier, @Nullable String itemName, Recipe<T> recipe) {
        super(item, identifier, itemName);
        this.recipe = recipe;
    }

    protected AbstractRecipeItem(Item item, Identifier identifier, String itemName, @Nullable Either<CustomModelDataComponent, Identifier> customModelData, Recipe<T> recipe) {
        super(item, identifier, itemName, customModelData);
        this.recipe = recipe;
    }

    @Override
    public Recipe<T> recipe() {
        return recipe;
    }
}
