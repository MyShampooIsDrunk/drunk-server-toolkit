package myshampooisdrunk.drunk_server_toolkit.register;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import myshampooisdrunk.drunk_server_toolkit.item.CustomRecipeItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomItemRegistry {
    public static AbstractCustomItem registerItem(AbstractCustomItem item) {
        DST.ITEMS.add(item);
        return item;
    }

    public static CustomRecipeItem<?> registerWithRecipe(CustomRecipeItem<?> recipeItem) {
        registerItem(recipeItem);
        DST.RECIPES.add(recipeItem);
        return recipeItem;
    }

    public static void registerRecipe(Recipe<?> recipe, Identifier id){
        DST.ITEM_RECIPES.put(recipe, id);
    }

    public static void addToGroup(AbstractCustomItem item, RegistryKey<ItemGroup> g){
        ItemGroupEvents.modifyEntriesEvent(g).register(content -> content.add(item.create()));
    }
    public static void addToGroup(AbstractCustomItem item, RegistryKey<ItemGroup> g, ItemStack after){
        ItemGroupEvents.modifyEntriesEvent(g).register(content -> content.addAfter(after, item.create()));
    }
//    public static void registerRecipe(CraftingRecipe r, Identifier id, AbstractCustomItem item){
//        DST.CUSTOM_RECIPES.put(id, new Pair<>(r,item));
//    }
//    public static void registerEmptyRecipe(Identifier id, AbstractCustomItem item){
//        registerRecipe(null,id,item);
//    }
}
