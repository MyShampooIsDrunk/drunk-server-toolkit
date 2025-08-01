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
import myshampooisdrunk.drunk_server_toolkit.item.CustomRecipe;
import myshampooisdrunk.drunk_server_toolkit.item.potion.CustomPotion;
import myshampooisdrunk.drunk_server_toolkit.register.CustomItemRegistry;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.item.ItemGroups;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DST implements ModInitializer {
	private static final boolean DEV_MODE = false;
    public static final Logger LOGGER = LoggerFactory.getLogger("drunk_server_toolkit");
	public static final ComponentKey<EntityMultiblockData> ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY = ComponentRegistry.getOrCreate(
		id("multiblock_data"), EntityMultiblockData.class);
	public static final ComponentKey<ItemDisplayEntityMultiblockCoreData> MULTIBLOCK_CORE_DATA_COMPONENT_KEY = ComponentRegistry.getOrCreate(
			id("multiblock_core_data"), ItemDisplayEntityMultiblockCoreData.class);
	public static final Set<AbstractCustomItem> ITEMS = new HashSet<>();
	public static final Set<CustomRecipe<?>> RECIPES = new HashSet<>();
	public static final Map<Recipe<?>, Identifier> ITEM_RECIPES = new HashMap<>();
	public static final Map<Identifier, CustomPotion> POTIONS = new HashMap<>();

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
		if(DEV_MODE) {
			MultiblockRegistryExample.init();
			CustomPotionRegistryExample.init();
			GoofySillyGoofyItem bbbbbbbbbbbbbbbbbb = new GoofySillyGoofyItem();
			HopefullyThisItemWorks bbbbbbbbbbbbbbbbbb2 = new HopefullyThisItemWorks();
			CustomItemRegistry.registerItem(bbbbbbbbbbbbbbbbbb);
			CustomItemRegistry.registerItem(bbbbbbbbbbbbbbbbbb2);
			CustomItemRegistry.addToGroup(bbbbbbbbbbbbbbbbbb2, ItemGroups.COMBAT);
			CustomItemRegistry.addToGroup(bbbbbbbbbbbbbbbbbb, ItemGroups.COMBAT);
		}

		initializeCommands();
	}

	public static Identifier id(String path){
		return Identifier.of(LOGGER.getName(), path);
	}
}