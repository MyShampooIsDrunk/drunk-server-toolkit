package myshampooisdrunk.drunk_server_toolkit.item;

import com.mojang.datafixers.util.Either;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRecipeItem<T extends RecipeInput> extends AbstractCustomItem implements CustomRecipe<T>{
     public AbstractRecipeItem(Item item, Identifier identifier) {
        super(item, identifier);
    }

    public AbstractRecipeItem(Item item, Identifier identifier, @Nullable String itemName) {
        super(item, identifier, itemName);
    }

    protected AbstractRecipeItem(Item item, Identifier identifier, String itemName, @Nullable Either<CustomModelDataComponent, Identifier> customModelData) {
        super(item, identifier, itemName, customModelData);
    }

    @Override
    public abstract Recipe<T> recipe();
}
