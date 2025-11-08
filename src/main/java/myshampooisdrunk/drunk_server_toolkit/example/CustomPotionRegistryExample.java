package myshampooisdrunk.drunk_server_toolkit.example;

import myshampooisdrunk.drunk_server_toolkit.item.potion.CustomPotion;
import myshampooisdrunk.drunk_server_toolkit.recipe.CustomIngredient;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class CustomPotionRegistryExample {
    public static final CustomPotion potionOfBaldness = CustomPotion.builder()
            .addEffect(
                    (dur, pot, type) -> 1000 * (pot+1),
                    StatusEffects.BLINDNESS
            )
            .color(1234567890)
            .name("potion of super brain rot")
            .textModifier((text, potion) -> ((MutableText)text).setStyle(
                    Style.EMPTY
                            .withBold(true)
                            .withColor(-8583859)
                            .withItalic(true)
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
