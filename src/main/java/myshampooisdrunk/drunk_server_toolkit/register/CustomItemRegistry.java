package myshampooisdrunk.drunk_server_toolkit.register;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import myshampooisdrunk.drunk_server_toolkit.item.CustomRecipeItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class CustomItemRegistry {
    public static AbstractCustomItem registerItem(AbstractCustomItem item) {
        WeaponAPI.ITEMS.add(item);
        return item;
//        WeaponAPI.ITEMS.computeIfAbsent(item.getItem(), k -> new HashSet<>());
//        WeaponAPI.ITEMS.get(item.getItem()).add(item);
    }

    public static CustomRecipeItem<?> registerWithRecipe(CustomRecipeItem<?> recipeItem) {
        registerItem(recipeItem);
        WeaponAPI.RECIPES.add(recipeItem);
        return recipeItem;
    }

    public static void registerRecipe(Recipe<?> recipe, Identifier id){
        WeaponAPI.ITEM_RECIPES.put(recipe, id);
    }

    public static void addToGroup(AbstractCustomItem item, RegistryKey<ItemGroup> g){
        ItemGroupEvents.modifyEntriesEvent(g).register(content -> content.add(item.create()));
    }
    public static void addToGroup(AbstractCustomItem item, RegistryKey<ItemGroup> g, ItemStack after){
        ItemGroupEvents.modifyEntriesEvent(g).register(content -> content.addAfter(after, item.create()));
    }
//    public static void registerRecipe(CraftingRecipe r, Identifier id, AbstractCustomItem item){
//        WeaponAPI.CUSTOM_RECIPES.put(id, new Pair<>(r,item));
//    }
//    public static void registerCustomEnchantment(AbstractCustomEnchantment e){
//        WeaponAPI.ENCHANTMENTS.put(e.getId(),e);
//    }
//    public static void addCustomEnchants(RegistryKey<ItemGroup> g){
//        ItemGroupEvents.modifyEntriesEvent(g).register(content -> {
//            WeaponAPI.ENCHANTMENTS.values().stream().map(
//                    ench -> CustomEnchantmentHelper.forEnchantment(
//                            new CustomEnchantmentInstance(ench,ench.getMaxLevel()))
//            ).forEach(content::add);
//        });
//    }
//    public static void registerEmptyRecipe(Identifier id, AbstractCustomItem item){
//        registerRecipe(null,id,item);
//    }
}
