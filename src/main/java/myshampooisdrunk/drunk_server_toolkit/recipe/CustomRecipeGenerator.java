package myshampooisdrunk.drunk_server_toolkit.recipe;

import myshampooisdrunk.drunk_server_toolkit.DST;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;

public class CustomRecipeGenerator extends RecipeGenerator {
    public CustomRecipeGenerator(RegistryWrapper.WrapperLookup registries, RecipeExporter exporter) {
        super(registries, exporter);
    }

    @Override
    public void generate() {
        DST.RECIPES.forEach(rItem -> exporter.accept(RegistryKey.of(RegistryKeys.RECIPE, rItem.getIdentifier()), rItem.recipe(), null));
        DST.ITEM_RECIPES.forEach((recipe, id) -> exporter.accept(RegistryKey.of(RegistryKeys.RECIPE, id), recipe, null));
    }
}
