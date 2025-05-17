package myshampooisdrunk.drunk_server_toolkit;

import myshampooisdrunk.drunk_server_toolkit.command.CustomGiveCommand;
import myshampooisdrunk.drunk_server_toolkit.command.GivePotionCommand;
import myshampooisdrunk.drunk_server_toolkit.component.EntityMultiblockData;
import myshampooisdrunk.drunk_server_toolkit.component.ItemDisplayEntityMultiblockCoreData;
import myshampooisdrunk.drunk_server_toolkit.example.CustomPotionRegistryExample;
import myshampooisdrunk.drunk_server_toolkit.example.GoofySillyGoofyItem;
import myshampooisdrunk.drunk_server_toolkit.example.HopefullyThisItemWorks;
import myshampooisdrunk.drunk_server_toolkit.example.multiblock.MultiblockRegistryExample;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import myshampooisdrunk.drunk_server_toolkit.item.CustomRecipeItem;
import myshampooisdrunk.drunk_server_toolkit.item.potion.CustomPotion;
import myshampooisdrunk.drunk_server_toolkit.register.CustomItemRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.*;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DST implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("drunk_server_toolkit");
//	public static final Map<Identifier, AbstractCustomEnchantment> ENCHANTMENTS = new HashMap<>();
//	public static final Map<Item,Set<AbstractCustomItem>> ITEMS = new HashMap<>();
public static final ComponentKey<EntityMultiblockData> ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY = ComponentRegistry.getOrCreate(
		id("multiblock_data"), EntityMultiblockData.class);
	public static final ComponentKey<ItemDisplayEntityMultiblockCoreData> MULTIBLOCK_CORE_DATA_COMPONENT_KEY = ComponentRegistry.getOrCreate(
			id("multiblock_core_data"), ItemDisplayEntityMultiblockCoreData.class);
	public static final Set<AbstractCustomItem> ITEMS = new HashSet<>();
//	public static final Map<Item,Integer> MODEL_COUNT = new HashMap<>();
	public static final Set<CustomRecipeItem<?>> RECIPES = new HashSet<>();
	public static final Map<Recipe<?>, Identifier> ITEM_RECIPES = new HashMap<>();
	public static final Map<Identifier, CustomPotion> POTIONS = new HashMap<>();
//	public static final Map<Identifier, Pair<CraftingRecipe,AbstractCustomItem>> CUSTOM_RECIPES = new HashMap<>();
//	public static final Map<Identifier,Triple<Identifier,Identifier,Identifier>> RECIPE_IDS = new HashMap<>();

	public static void initializeRecipes(){
//		for(Identifier id: CUSTOM_RECIPES.keySet()){
//			RECIPE_IDS.put(id,Triple.of(
//					new Identifier(id.getNamespace(),id.getPath() + "_recipe"),
//					new Identifier(id.getNamespace(),id.getPath() + "_recipe_output"),
//					new Identifier(id.getNamespace(),id.getPath() + "_recipe_advancement")
//			));
//		}
	}
	public static void initializeCommands(){
		CommandRegistrationCallback.EVENT.register(
				(dispatcher, registryAccess, environment) -> {
					GivePotionCommand.register(dispatcher);
					CustomGiveCommand.register(dispatcher);
				}
		);
	}

	@Override
	public void onInitialize() {
		MultiblockRegistryExample.init();
		CustomPotionRegistryExample.init();
		AbstractCustomItem bbbbbbbbbbbbbbbbbb = new GoofySillyGoofyItem();
		AbstractCustomItem bbbbbbbbbbbbbbbbbb2 = new HopefullyThisItemWorks();
		CustomItemRegistry.registerItem(bbbbbbbbbbbbbbbbbb);
		CustomItemRegistry.registerItem(bbbbbbbbbbbbbbbbbb2);
		CustomItemRegistry.addToGroup(bbbbbbbbbbbbbbbbbb2, ItemGroups.COMBAT);
		CustomItemRegistry.addToGroup(bbbbbbbbbbbbbbbbbb, ItemGroups.COMBAT);
		initializeRecipes();

		initializeCommands();
	}

	public static Identifier id(String path){
		return Identifier.of(LOGGER.getName(), path);
	}
}