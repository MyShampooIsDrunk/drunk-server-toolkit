package myshampooisdrunk.drunk_server_toolkit.item.potion;

import com.mojang.datafixers.util.Either;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

public record CustomPotionItem(Identifier id, boolean translatable, @Nullable String itemName,
                               @Nullable Text overrideName, Optional<TextModifier> textModifier,
                               Optional<Integer> customColor, Optional<RegistryEntry<Potion>> basePotion,
                               Map<DurationFunction, RegistryEntry<StatusEffect>> statusEffects) {
//    private final Identifier id;
//    private final Optional<Integer> customColor;
//    private final Optional<RegistryEntry<Potion>> basePotion;
//    private final ComponentMap.Builder components;
//    private final Map<DurationFunction, RegistryEntry<StatusEffect>> statusEffects;
//    private final boolean translatable;
//    @Nullable
//    private final String itemName;
//    @Nullable
//    private final Text overrideName;
//    private final Optional<TextModifier> textModifier;
//    //itemName shouldn't be null if translatable is false
//    CustomPotionItem(Identifier id, boolean translatable, @Nullable String itemName, @Nullable Text overrideName,
//            Optional<TextModifier> textModifier, Optional<Integer> customColor, Optional<RegistryEntry<Potion>> basePotion) {
//        this.id = id;
//        this.components = ComponentMap.builder();
//        this.basePotion = basePotion;
//        this.customColor = customColor;
//        this.statusEffects = new HashMap<>();
//        this.translatable = translatable;
//        this.itemName = itemName;
//        this.overrideName = overrideName;
//        this.textModifier = textModifier;
//    }

    public ItemStack create(int potency, int durLvl, PotionType type) {
        ComponentMap.Builder components = ComponentMap.builder();
        Text name = this.overrideName != null ? overrideName : this.getText(type);
        name = textModifier.isPresent() ? textModifier.get().apply(name, this) : name;
        ItemStack ret = new ItemStack(type.item);
        List<StatusEffectInstance> effects = new ArrayList<>();
        statusEffects.forEach((func, eff) ->
                effects.add(new StatusEffectInstance(eff, func.apply(potency, durLvl, type), potency)));
        components.add(DataComponentTypes.ITEM_NAME, name);
        components.add(DataComponentTypes.POTION_CONTENTS,
                new PotionContentsComponent(basePotion, customColor, effects));
        ret.applyComponentsFrom(components.build());
        return ret;
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
            ret = Text.translatable(id2.toTranslationKey("", type + ".name"));
//you should end up getting keys like "test_mod:potion_of_test.name", "test_mod:splash_potion_of_test.name", etc.
        }else {
            String potion = switch (type) {
                case DRINK -> "";
                case SPLASH -> "Splash ";
                case LINGER -> "Lingering ";
            };
            ret = Text.literal(potion + itemName);//itemName should be "Potion of ___"
        }
        return ret;
    }

    public Builder builder(){
        return new Builder();
    }

    static class Builder {
        private boolean translatable = true;
        @Nullable
        private String itemName;
        @Nullable
        private Text overrideName;
        private Optional<TextModifier> textModifier = Optional.empty();
        private final Map<DurationFunction, RegistryEntry<StatusEffect>> statusEffects = new HashMap<>();
        Optional<RegistryEntry<Potion>> potion = Optional.empty();
        Optional<Integer> color = Optional.empty();

        Builder(){
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

        public CustomPotionItem build(Identifier id){
            return new CustomPotionItem(id, translatable, itemName, overrideName,
                    textModifier, color, potion, statusEffects);
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

        @Override
        public String toString(){
            return switch(this) {
                case DRINK-> "potion";
                case SPLASH -> "splash_potion";
                case LINGER -> "lingering_potion";
            };
        }
    }

    interface DurationFunction {
        int apply(int durationLevel, int potency, PotionType type);
    }
    interface TextModifier {
        Text apply(Text text, CustomPotionItem potion);
    }
}
