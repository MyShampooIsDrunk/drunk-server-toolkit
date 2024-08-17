package myshampooisdrunk.drunk_server_toolkit.datagen;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RawShapedRecipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CustomRecipeProvider extends FabricRecipeProvider {
    public CustomRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        WeaponAPI.RECIPES.forEach(rItem -> exporter.accept(rItem.getIdentifier(), rItem.recipe(), null));
        WeaponAPI.ITEM_RECIPES.forEach((recipe, id) -> exporter.accept(id, recipe, null));
    }
}