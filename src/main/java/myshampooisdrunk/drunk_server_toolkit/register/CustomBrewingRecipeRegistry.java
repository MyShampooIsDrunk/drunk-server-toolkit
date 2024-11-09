package myshampooisdrunk.drunk_server_toolkit.register;

import myshampooisdrunk.drunk_server_toolkit.recipe.potion.CustomBrewingRecipe;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class CustomBrewingRecipeRegistry {
    public static final Set<CustomBrewingRecipe> BREWING_RECIPES = new HashSet<>();
    public static boolean isValidInput(ItemStack stack){
        for(CustomBrewingRecipe custom : BREWING_RECIPES){
            if(custom.input().test(stack)) return true;
        }
        return false;
    }
    public static boolean isValidIngredient(ItemStack stack){
        for(CustomBrewingRecipe custom : BREWING_RECIPES){
            if(custom.ingredient().test(stack)) return true;
        }
        return false;
    }
    public static void registerRecipe(CustomBrewingRecipe rec){
        BREWING_RECIPES.add(rec);
    }
}
