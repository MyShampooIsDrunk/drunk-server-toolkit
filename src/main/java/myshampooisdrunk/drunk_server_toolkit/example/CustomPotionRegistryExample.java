package myshampooisdrunk.drunk_server_toolkit.example;

import myshampooisdrunk.drunk_server_toolkit.item.potion.CustomPotion;
import myshampooisdrunk.drunk_server_toolkit.recipe.CustomIngredient;
import myshampooisdrunk.drunk_server_toolkit.recipe.potion.CustomBrewingRecipe;
import myshampooisdrunk.drunk_server_toolkit.register.CustomBrewingRecipeRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class CustomPotionRegistryExample {
    public static final CustomPotion potionOfBaldness = CustomPotion.builder()
            .addEffect(
                    (dur, pot, type) -> 1000 * pot,
                    StatusEffects.BLINDNESS
            )
            .color(1234567890)
            .name("potion of super brain rot")
            .textModifier((text, potion) -> ((MutableText)text).setStyle(
                    Style.EMPTY
                            .withBold(true)
                            .withColor(-8583859)
                            .withItalic(true)
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    Text.literal("xiahongshu!!!")))
            ))
            .build(Identifier.of("example_mod","baldness"));
    public static void init() {
        potionOfBaldness.registerBrewingRecipes(Potions.INFESTED, CustomIngredient.ofStacks(
                new GoofySillyGoofyItem().create(), Items.FURNACE.getDefaultStack(), Items.CHORUS_FRUIT.getDefaultStack()));
        potionOfBaldness.registerBrewingRecipes(
                Optional.of(CustomIngredient.ofStacks(Items.DIAMOND_SWORD.getDefaultStack())),
                Optional.of(CustomIngredient.ofStacks(Items.ENCHANTED_GOLDEN_APPLE.getDefaultStack())),
                Optional.of(CustomIngredient.ofStacks(Items.TURTLE_SPAWN_EGG.getDefaultStack(), Items.ACACIA_BOAT.getDefaultStack())),
                CustomIngredient.ofStacks(
                        new GoofySillyGoofyItem().create(),
                        Items.FURNACE.getDefaultStack(),
                        Items.CHORUS_FRUIT.getDefaultStack()
                ));
    }
}
