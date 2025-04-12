package myshampooisdrunk.drunk_server_toolkit.item.potion;

import com.mojang.datafixers.util.Either;
import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.recipe.CustomIngredient;
import myshampooisdrunk.drunk_server_toolkit.recipe.potion.CustomBrewingRecipe;
import myshampooisdrunk.drunk_server_toolkit.register.CustomBrewingRecipeRegistry;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public record CustomPotion(Identifier id, boolean translatable, @Nullable String itemName,
                           @Nullable Text overrideName, Optional<TextModifier> textModifier,
                           Optional<Integer> customColor, Optional<RegistryEntry<Potion>> basePotion,
                           Map<DurationFunction, RegistryEntry<StatusEffect>> statusEffects, int maxPotency, int maxDuration,
                           Either<Predicate<ItemStack>, List<Predicate<ItemStack>>> potencyIngredient,
                           Either<Predicate<ItemStack>, List<Predicate<ItemStack>>> durationIngredient,
                           Predicate<ItemStack> splashIngredient, Predicate<ItemStack> lingerIngredient) {

    public ItemStack create(int potency, int durLvl, PotionType type) {
        ComponentMap.Builder components = ComponentMap.builder();
        Text name = this.overrideName != null ? overrideName : this.getText(type);
        name = textModifier.isPresent() ? textModifier.get().apply(name, this) : name;
        ItemStack ret = new ItemStack(type.item);
        List<StatusEffectInstance> effects = new ArrayList<>();
        statusEffects.forEach((func, eff) ->
                effects.add(new StatusEffectInstance(eff, func.apply(durLvl, potency, type) * (type == PotionType.LINGER ? 4:1), potency)));
        components.add(DataComponentTypes.ITEM_NAME, name);
        components.add(DataComponentTypes.POTION_CONTENTS,
                new PotionContentsComponent(basePotion, customColor, effects));
        ret.applyComponentsFrom(components.build());
        return ret;
    }

    public void registerBrewingRecipes(RegistryEntry<Potion> potion, Predicate<ItemStack> reagent) {
        registerBrewingRecipes(
                Optional.of(CustomIngredient.ofStacks(
                        PotionContentsComponent.createStack(Items.POTION,potion))),
                Optional.of(CustomIngredient.ofStacks(
                        PotionContentsComponent.createStack(Items.SPLASH_POTION,potion))),
                Optional.of(CustomIngredient.ofStacks(
                        PotionContentsComponent.createStack(Items.LINGERING_POTION,potion))),
                reagent);
    }


    /*
    rP -> reactantPotion
    rS -> reactantSplash
    rL -> reactantLingering
    re -> reagant

    Sp -> drink to splash ingredient
    Li -> splash to linger ingredient

    P -> potion

    re + rP = P_drink
    re + rS = P_splash
    re + rL = P_linger

    P_drink + Sp = P_splash
    P_splash + Li = P_linger

    rDur(n) = ingredient for to get to n+1 duration
    rPot(n) = ingredient for to get to n+1 potency

    P_x(dur(n), pot(0)) + rDur(n) = P_x(dur(n+1),pot(0))
    P_x(dur(0), pot(n)) + rPot(n) = P_x(dur(0),pot(n+1))


    */

    public void registerBrewingRecipes(Optional<Predicate<ItemStack>> reactantPotion,
                                       Optional<Predicate<ItemStack>> reactantSplash,
                                       Optional<Predicate<ItemStack>> reactantLingering,
                                       Predicate<ItemStack> reagent) {

        //7 equations
        //reactant to potion
        if(reactantPotion.isPresent()) {
            CustomBrewingRecipe recipe = new CustomBrewingRecipe(reactantPotion.get(), reagent,
                    this.create(0, 0, PotionType.DRINK));
            CustomBrewingRecipeRegistry.registerRecipe(recipe);
        }
        if(reactantSplash.isPresent()) {
            CustomBrewingRecipe recipe = new CustomBrewingRecipe(reactantSplash.get(), reagent,
                    this.create(0, 0, PotionType.SPLASH));
            CustomBrewingRecipeRegistry.registerRecipe(recipe);
        }
        if(reactantLingering.isPresent()) {
            CustomBrewingRecipe recipe = new CustomBrewingRecipe(reactantLingering.get(), reagent,
                    this.create(0, 0, PotionType.LINGER));
            CustomBrewingRecipeRegistry.registerRecipe(recipe);
        }

        //4 more equations

        //drink to splash
        for (int i = 0; i <= maxDuration(); i++) {
            CustomBrewingRecipe recipe = new CustomBrewingRecipe(
                    CustomIngredient.ofStacks(true,this.create(0,i,PotionType.DRINK)),
                    splashIngredient,
                    this.create(0,i,PotionType.SPLASH));
            CustomBrewingRecipeRegistry.registerRecipe(recipe);
        }
        if(maxPotency >= 1) for (int i = 1; i <= maxPotency(); i++) {
            CustomBrewingRecipe recipe = new CustomBrewingRecipe(
                    CustomIngredient.ofStacks(true,this.create(i,0,PotionType.DRINK)),
                    splashIngredient,
                    this.create(i,0,PotionType.SPLASH));
            CustomBrewingRecipeRegistry.registerRecipe(recipe);
        }
        //splash to linger
        for (int i = 0; i <= maxDuration(); i++) {
            CustomBrewingRecipe recipe = new CustomBrewingRecipe(
                    CustomIngredient.ofStacks(true,this.create(0,i,PotionType.SPLASH)),
                    lingerIngredient,
                    this.create(0,i,PotionType.LINGER));
            CustomBrewingRecipeRegistry.registerRecipe(recipe);
        }
        if(maxPotency >= 1) for (int i = 1; i <= maxPotency(); i++) {
            CustomBrewingRecipe recipe = new CustomBrewingRecipe(
                    CustomIngredient.ofStacks(true,this.create(i,0,PotionType.SPLASH)),
                    lingerIngredient,
                    this.create(i,0,PotionType.LINGER));
            CustomBrewingRecipeRegistry.registerRecipe(recipe);
        }

        //potency and duration
        for (PotionType type : PotionType.values()) {
            Optional<Predicate<ItemStack>> left1;
            Optional<List<Predicate<ItemStack>>> right1;
            Optional<Predicate<ItemStack>> left2;
            Optional<List<Predicate<ItemStack>>> right2;
            //potency
            if((left1 = potencyIngredient.left()).isPresent()) {
                if(maxPotency >= 1){
                    for (int i = 0; i < maxPotency; i++) {
                        CustomBrewingRecipe recipe = new CustomBrewingRecipe(
                                CustomIngredient.ofStacks(true,this.create(i,0,type)),
                                left1.get(),
                                this.create(i+1, 0, type));
                        CustomBrewingRecipeRegistry.registerRecipe(recipe);
                    }
                }
            } else {
                right1 = potencyIngredient.right();
                assert right1.isPresent();
                if(maxPotency >= 1){
                    for (int i = 0; i < maxPotency; i++) {
                        CustomBrewingRecipe recipe = new CustomBrewingRecipe(
                                CustomIngredient.ofStacks(true,this.create(i,0,type)),
                                right1.get().get(i),
                                this.create(i+1, 0, type));
                        CustomBrewingRecipeRegistry.registerRecipe(recipe);
                    }
                }
            }

            //duration
            if((left2 = durationIngredient.left()).isPresent()) {
                if(maxDuration >= 1){
                    for (int i = 0; i < maxDuration; i++) {
                        CustomBrewingRecipe recipe = new CustomBrewingRecipe(
                                CustomIngredient.ofStacks(true,this.create(0,i,type)),
                                left2.get(),
                                this.create(0, i+1, type));
                        CustomBrewingRecipeRegistry.registerRecipe(recipe);
                    }
                }
            } else {
                right2 = durationIngredient.right();
                assert right2.isPresent();
                if(maxDuration >= 1){
                    for (int i = 0; i < maxDuration; i++) {
                        CustomBrewingRecipe recipe = new CustomBrewingRecipe(
                                CustomIngredient.ofStacks(true,this.create(0,i,type)),
                                right2.get().get(i),
                                this.create(0, i+1, type));
                        CustomBrewingRecipeRegistry.registerRecipe(recipe);
                    }
                }
            }
        }
    }

    Text getText(PotionType type){
        Text ret;
        if(translatable){
            String potion = switch (type) {
                case DRINK -> "";
                case SPLASH -> "splash_";
                case LINGER -> "lingering_";
            };
            Identifier id2 = Identifier.of(id.getNamespace(), potion + id.getPath());
            ret = Text.translatable(id2.toTranslationKey());
//you should end up getting keys like "test_mod:potion_of_test.name", "test_mod:splash_potion_of_test.name", etc.
        } else {
            String potion = switch (type) {
                case DRINK -> "";
                case SPLASH -> "Splash ";
                case LINGER -> "Lingering ";
            };
            ret = Text.literal(potion + itemName);//itemName should be "Potion of ___"
        }
        return ret;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder {
        @Nullable
        private String itemName;
        @Nullable
        private Text overrideName;
        private boolean translatable = true;
        private Optional<TextModifier> textModifier = Optional.empty();
        Optional<RegistryEntry<Potion>> potion = Optional.empty();
        Optional<Integer> color = Optional.empty();
        @NotNull
        private final Map<DurationFunction, RegistryEntry<StatusEffect>> statusEffects = new HashMap<>();
        int maxPotency = 1;
        int maxDurationLevel = 1;
        Predicate<ItemStack> splashIngredient = Ingredient.ofItems(Items.GUNPOWDER);
        Predicate<ItemStack> lingerIngredient = Ingredient.ofItems(Items.DRAGON_BREATH);
        Either<Predicate<ItemStack>, List<Predicate<ItemStack>>> potencyIngredient = Either.left(Ingredient.ofItems(Items.GLOWSTONE_DUST));
        Either<Predicate<ItemStack>, List<Predicate<ItemStack>>> durationIngredient = Either.left(Ingredient.ofItems(Items.REDSTONE));

        Builder(){
        }

        public Builder splashPotionIngredient(Predicate<ItemStack> ingredient){
            splashIngredient = ingredient;
            return this;
        }

        public Builder lingeringPotionIngredient(Predicate<ItemStack> ingredient){
            lingerIngredient = ingredient;
            return this;
        }

        public Builder maxPotency(int potency){
            maxPotency = potency;
            return this;
        }

        public Builder maxPotency(int potency, Predicate<ItemStack> potencyIngredient) {
            this.potencyIngredient = Either.left(potencyIngredient);
            return maxPotency(potency);
        }

        public Builder maxPotency(int potency, List<Predicate<ItemStack>> potencyIngredient) {
            if (potencyIngredient.size() != potency-2)
                throw new RuntimeException("potencyIngredient collection length must be the same as the potency level - 2");
            this.potencyIngredient = Either.right(potencyIngredient);
            return maxPotency(potency);
        }

        public Builder maxDurationLevel(int durationLevel){
            maxDurationLevel = durationLevel;
            return this;
        }

        public Builder maxDurationLevel(int durationLevel, Predicate<ItemStack> durationIngredient) {
            this.durationIngredient = Either.left(durationIngredient);
            return maxDurationLevel(durationLevel);
        }

        public Builder maxDurationLevel(int durationLevel, List<Predicate<ItemStack>> durationIngredient) {

            if (durationIngredient.size() != durationLevel-2)
                throw new RuntimeException("durationIngredient collection length must be the same as the duration level - 2");
            this.durationIngredient = Either.right(durationIngredient);
            return maxDurationLevel(durationLevel);
        }

        public Builder addEffect(DurationFunction func, RegistryEntry<StatusEffect> effect){
            statusEffects.put(func,effect);
            return this;
        }
        public Builder basePotion(RegistryEntry<Potion> potion){
            this.potion = Optional.of(potion);
            return this;
        }
        public Builder color(int color){
            this.color = Optional.of(color);
            return this;
        }
        public Builder color(int r, int g, int b){
            return color(r << 16 + g << 8 + b);
        }

        //this is an override name, so if you dont want it to be splash potoin of ___ or lingering potion of ___,
        // you can use this to just make it whatever you want. (I don't know why you'd want this, but so be it)
        // by default it'll use
        public Builder overrideName(Text name){
            this.translatable = false;
            this.itemName = null;
            this.overrideName = name;
            return this;
        }

        public Builder name(String name){//should be smth like "Potion of ___"
            this.overrideName = null;
            this.translatable = false;
            this.itemName = name;
            return this;
        }

        public Builder textModifier(TextModifier textModifier){
            this.textModifier = Optional.of(textModifier);
            return this;
        }

        public CustomPotion build(Identifier id){
            assert maxDurationLevel >= 0;
            assert maxPotency >= 0;
            CustomPotion ret = new CustomPotion(id, translatable, itemName, overrideName,
                    textModifier, color, potion, statusEffects,maxPotency, maxDurationLevel, potencyIngredient,
                    durationIngredient, splashIngredient,lingerIngredient);
            DST.POTIONS.put(id,ret);
            return ret;
        }
    }

    public enum PotionType {
        DRINK(Items.POTION),
        SPLASH(Items.SPLASH_POTION),
        LINGER(Items.LINGERING_POTION);

        public final Item item;
        PotionType(Item item){
            this.item = item;
        }
        public static String[] types(){
            return new String[]{"drink", "splash", "linger"};
        }
        @Override
        public String toString(){
            return switch(this) {
                case DRINK -> "potion";
                case SPLASH -> "splash_potion";
                case LINGER -> "lingering_potion";
            };
        }

        public static PotionType fromString(String s) {
            return switch (s){
                default -> DRINK;
                case "splash" -> SPLASH;
                case "linger" -> LINGER;
            };
        }
    }

    public interface DurationFunction {
        int apply(int durationLevel, int potency, PotionType type);
    }
    public interface TextModifier extends BiFunction<Text, CustomPotion, Text> {
    }

}
