package myshampooisdrunk.drunk_server_toolkit;

import myshampooisdrunk.drunk_server_toolkit.command.CustomGiveCommand;
import myshampooisdrunk.drunk_server_toolkit.command.GiveBookCommand;
import myshampooisdrunk.drunk_server_toolkit.cooldown.CustomEnchantCooldownManagerI;
import myshampooisdrunk.drunk_server_toolkit.example.GoofySillyGoofyItem;
import myshampooisdrunk.drunk_server_toolkit.example.HopefullyThisItemWorks;
import myshampooisdrunk.drunk_server_toolkit.example.RandomToolItemCustomVeryVeryFastCustomCustomCustomCustom;
import myshampooisdrunk.drunk_server_toolkit.register.CustomItemRegistry;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

public class WeaponAPI implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("drunk_server_toolkit");
//	public static final Map<Identifier, AbstractCustomEnchantment> ENCHANTMENTS = new HashMap<>();
//	public static final Map<Item,Set<AbstractCustomItem>> ITEMS = new HashMap<>();
	public static final Set<AbstractCustomItem> ITEMS = new HashSet<>();
	public static final Map<Item,Integer> MODEL_COUNT = new HashMap<>();
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
//					CustomEnchantCommand.register(dispatcher);
//					GiveBookCommand.register(dispatcher);
					CustomGiveCommand.register(dispatcher);
				}
		);
	}

	@Override
	public void onInitialize() {
//		AbstractCustomEnchantment veryVerySillyEnchantment = new AbstractCustomEnchantment(
//				new Identifier("shampoos_stupid_mod","bruhhhhhhhhhhhhhhhh"),
//				AbstractCustomEnchantment.Rarity.COMMON,
//				EnchantmentTarget.WEAPON,
//				new EquipmentSlot[] {}
//		){
//			@Override
//			public void onUse(World world, PlayerEntity user, Hand hand, int level, CallbackInfoReturnable cir){
//				Vec3d rotation = user.getRotationVector();
//				FireballEntity fireball = new FireballEntity(world,user,3*rotation.x,3*rotation.y,3*rotation.z,level*5);
//				world.spawnEntity(fireball);
//			}
//			@Override
//			public int getMaxLevel(){return 12;}
//		};
//		AbstractCustomEnchantment notAsSillyEnchant = new AbstractCustomEnchantment(
//				new Identifier("shampoos_stupid_mod","a"),
//				AbstractCustomEnchantment.Rarity.COMMON,
//				EnchantmentTarget.WEAPON,
//				new EquipmentSlot[] {}
//		){
//			@Override
//			public void onUse(World world, PlayerEntity user, Hand hand, int level, CallbackInfoReturnable cir){
//				if(!((CustomEnchantCooldownManagerI)user).getCustomEnchantCooldownManager().isCoolingDown(this)){
//					((CustomEnchantCooldownManagerI)user).getCustomEnchantCooldownManager();
//					user.sendMessage(Text.literal("The fog is coming..."),true);
//					user.eatFood(world,new ItemStack(Items.ROTTEN_FLESH));
//					user.damage(world.getDamageSources().explosion(user,user),2*(getMaxLevel()-level+1));
//					((CustomEnchantCooldownManagerI)user).getCustomEnchantCooldownManager().set(this,100);
//				}else{
//					user.sendMessage(Text.translatable("silly.cooldown.message",
//							(int)(0.95+((CustomEnchantCooldownManagerI) user).getCustomEnchantCooldownManager().getCooldownProgress(this,0)*5)), true);
//				}
//
//			}
//			@Override
//			public int getMinPower(int level){
//				return level + 1;
//			}
//			@Override
//			public boolean isCursed(){
//				return false;
//			}
//			@Override
//			public int getMaxLevel(){return 7;}
//		};

//		AbstractCustomEnchantment destEnchant = new AbstractCustomEnchantment(
//				new Identifier("shampoos_stupid_mod","some_enchant_idk_how_explain"),
//				AbstractCustomEnchantment.Rarity.COMMON,
//				EnchantmentTarget.ARMOR,
//				new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.FEET}
//		) {
//			@Override
//			public int getMaxLevel(){
//				return 6;
//			}
//			@Override
//			public int getProtectionAmount(int level, DamageSource source){
//				return 100;
//			}
//			@Override
//			public void onUserDamaged(LivingEntity user, Entity attacker, int level){
//				Vec3d fin = attacker.getPos().subtract(user.getPos());
//				attacker.setVelocity(fin.multiply(fin.getX()-10,fin.getY()-10,fin.getZ()-10).offset(Direction.UP,1).multiply(0.1*level));
////				attacker.setVelocity(fin.multiply((double)level/20*(5-Math.abs(attacker.getX()-user.getX())),(double)level/20*(5-Math.abs(attacker.getY()-user.getY())),(double)level/20*(5-Math.abs(attacker.getZ()-user.getZ()))).offset(Direction.UP,0.1));
////				attacker.setVelocity(fin.multiply(level/attacker.distanceTo(user)).offset(Direction.UP,5));
//				attacker.damage(user.getDamageSources().magic(),4);
//				if(attacker instanceof PlayerEntity p) p.sendMessage(Text.of("go shuck yourself basshole"),true);
//			}
//		};
//		CustomItemRegistry.registerCustomEnchantment(veryVerySillyEnchantment);
//		CustomItemRegistry.registerCustomEnchantment(notAsSillyEnchant);
//		CustomItemRegistry.registerCustomEnchantment(destEnchant);
//		AbstractCustomItem bbbbbbbbbbbbbbbbbb = new GoofySillyGoofyItem();
//		AbstractCustomItem bbbbbbbbbbbbbbbbbb2 = new HopefullyThisItemWorks();
//		AbstractCustomItem gijaietgjhjoerhkitjaeigjsirjnthij = new RandomToolItemCustomVeryVeryFastCustomCustomCustomCustom();//what the fuck is going on in this one line lmao; this shit chaotic af
//		CustomItemRegistry.registerItem(bbbbbbbbbbbbbbbbbb);
//		CustomItemRegistry.registerItem(bbbbbbbbbbbbbbbbbb2);
//		CustomItemRegistry.registerItem(gijaietgjhjoerhkitjaeigjsirjnthij);
//		CustomItemRegistry.registerRecipe(new ShapelessRecipe("", CraftingRecipeCategory.MISC,
//				bbbbbbbbbbbbbbbbbb.create(),
//				DefaultedList.copyOf(Ingredient.EMPTY,Ingredient.ofItems(Items.STONE),Ingredient.ofItems(Items.STONE))
//		),bbbbbbbbbbbbbbbbbb.getIdentifier(),bbbbbbbbbbbbbbbbbb);
//		CustomItemRegistry.registerRecipe(new ShapedRecipe("", CraftingRecipeCategory.MISC,
//				RawShapedRecipe.create(Map.of('d',Ingredient.ofItems(Items.DEEPSLATE),'a',Ingredient.EMPTY),"ddd","dda","daa")
//				,bbbbbbbbbbbbbbbbbb2.create()
//		),bbbbbbbbbbbbbbbbbb2.getIdentifier(),bbbbbbbbbbbbbbbbbb2);
//		CustomItemRegistry.addToGroup(bbbbbbbbbbbbbbbbbb2, ItemGroups.COMBAT);
//		CustomItemRegistry.addToGroup(bbbbbbbbbbbbbbbbbb, ItemGroups.COMBAT);
//		CustomItemRegistry.addToGroup(gijaietgjhjoerhkitjaeigjsirjnthij, ItemGroups.BUILDING_BLOCKS);
//		initializeRecipes();
//		CustomItemRegistry.addCustomEnchants(ItemGroups.COMBAT);
		initializeCommands();
	}
}