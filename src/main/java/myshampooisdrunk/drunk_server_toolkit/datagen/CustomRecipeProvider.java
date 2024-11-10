package myshampooisdrunk.drunk_server_toolkit.datagen;

import myshampooisdrunk.drunk_server_toolkit.DST;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class CustomRecipeProvider extends FabricRecipeProvider {
    public CustomRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        DST.RECIPES.forEach(rItem -> exporter.accept(rItem.getIdentifier(), rItem.recipe(), null));
        DST.ITEM_RECIPES.forEach((recipe, id) -> exporter.accept(id, recipe, null));
    }
}