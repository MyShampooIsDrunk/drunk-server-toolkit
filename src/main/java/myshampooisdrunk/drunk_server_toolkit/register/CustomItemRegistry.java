package myshampooisdrunk.drunk_server_toolkit.register;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.enchantment.AbstractCustomEnchantment;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentInstance;
import myshampooisdrunk.drunk_server_toolkit.weapon.AbstractCustomItem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.HashSet;

public class CustomItemRegistry {

    public static void registerItem(AbstractCustomItem item){
        WeaponAPI.ITEMS.computeIfAbsent(item.getItem(), k -> new HashSet<>());
        WeaponAPI.ITEMS.get(item.getItem()).add(item);
    }
    public static void addToGroup(AbstractCustomItem item, RegistryKey<ItemGroup> g){
        ItemGroupEvents.modifyEntriesEvent(g).register(content -> {
            content.add(item.create());
        });
    }
    public static void addToGroup(AbstractCustomItem item, RegistryKey<ItemGroup> g, ItemStack after){
        ItemGroupEvents.modifyEntriesEvent(g).register(content -> {
            content.addAfter(after, item.create());
        });
    }
    public static void registerRecipe(CraftingRecipe r, Identifier id, AbstractCustomItem item){
        WeaponAPI.CUSTOM_RECIPES.put(id, new Pair<>(r,item));
    }
    public static void registerCustomEnchantment(AbstractCustomEnchantment e){
        WeaponAPI.ENCHANTMENTS.put(e.getId(),e);
    }
    public static void addCustomEnchants(RegistryKey<ItemGroup> g){
        ItemGroupEvents.modifyEntriesEvent(g).register(content -> {
            WeaponAPI.ENCHANTMENTS.values().stream().map(
                    ench -> CustomEnchantmentHelper.forEnchantment(
                            new CustomEnchantmentInstance(ench,ench.getMaxLevel()))
            ).forEach(content::add);
        });
    }

    public static void registerEmptyRecipe(Identifier id, AbstractCustomItem item){
        registerRecipe(null,id,item);
    }
}
