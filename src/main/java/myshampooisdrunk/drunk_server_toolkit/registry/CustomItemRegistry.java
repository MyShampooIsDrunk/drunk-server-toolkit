package myshampooisdrunk.drunk_server_toolkit.registry;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractRecipeItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomItemRegistry {
    public static <T extends AbstractCustomItem> T registerItem(T item) {
        DST.ITEMS.add(item);
        return item;
    }

    public static <I extends RecipeInput, T extends AbstractRecipeItem<I>> T registerWithRecipe(T item) {
        DST.RECIPES.add(item);
        return registerItem(item);
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
