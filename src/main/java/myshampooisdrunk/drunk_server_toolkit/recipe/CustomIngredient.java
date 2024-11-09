package myshampooisdrunk.drunk_server_toolkit.recipe;

import com.google.common.collect.Lists;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;

import java.util.*;
import java.util.function.Predicate;

public final class CustomIngredient implements Predicate<ItemStack> {

    private final HashSet<Entry> entries; //boolean represents whether or not it should enforce item data when testing

    private CustomIngredient(){
        entries = new HashSet<>();
    }

    public static CustomIngredient empty(){
        return new CustomIngredient();
    }

    public static CustomIngredient ofStacks(ItemStack... stacks){
        return ofStacks(true, stacks);
    }

    public static CustomIngredient ofStacks(boolean maintainData, ItemStack... stacks){
        CustomIngredient ret = new CustomIngredient();
        for (ItemStack stack : stacks) {
            if(maintainData) ret.entries.add(new StackEntry(stack));
            else ret.entries.add(new ItemEntry(stack.getItem()));
        }
        return ret;
    }

    public CustomIngredient addStacks(ItemStack... stacks) {
        for (ItemStack stack : stacks) {
            entries.add(new StackEntry(stack));
        }
        return this;
    }

    public static CustomIngredient ofItems(ItemConvertible... items) {
        CustomIngredient ret = new CustomIngredient();
        for (ItemConvertible item : items) {
            ret.entries.add(new ItemEntry(item));
        }
        return ret;
    }

    public CustomIngredient addItems(ItemConvertible... items) {
        for (ItemConvertible item : items) {
            entries.add(new ItemEntry(item));
        }
        return this;
    }

    @SafeVarargs
    public static CustomIngredient ofTags(TagKey<Item>... tags){
        CustomIngredient ret = new CustomIngredient();
        for (TagKey<Item> tag: tags) {
            ret.entries.add(new TagEntry(tag));
        }
        return ret;
    }

    @SafeVarargs
    public final CustomIngredient addTags(TagKey<Item>... tags) {
        for (TagKey<Item> tag: tags) {
            entries.add(new TagEntry(tag));
        }
        return this;
    }

    private static boolean testStacks(ItemStack ref, ItemStack stack) {
        if(ref.getItem() != stack.getItem()) return false;
        //checking to see if stack2 has all the same components of stack1 (ignore any extraneous ones)
        ComponentMap map = ref.getComponents();
        ComponentMap map2 = stack.getComponents();
        if(map.isEmpty()) return true;
        if(map2.isEmpty()) return false;
//        System.out.println("map1:" + map);
//        System.out.println("map2:" + map2);
        for (Component<?> component : map) {
            if(!map2.contains(component.type())) return false;
            ComponentType<?> type = component.type();
            if(!(Objects.equals(map2.get(component.type()), component.value()))) return false;
        }
//        System.out.println("map2 contains map1");
        return true;
    }

    public ItemStack[] getMatchingStacks() {
        if(this.entries.isEmpty())return null;
        ItemStack[] matchingStacks = this.entries.stream()
                .flatMap(entry -> entry.getStacks().stream())
                .distinct()
                .toArray(ItemStack[]::new);

        return matchingStacks;
    }

    public boolean isEmpty() {
        return entries.isEmpty();
    }

    @Override
    public boolean test(ItemStack itemStack) {
        if (itemStack == null) {
            return false;
        } else if (this.isEmpty()) {
            return itemStack.isEmpty();
        } else {
            for (ItemStack itemStack2 : getMatchingStacks()) {
                if(testStacks(itemStack2, itemStack)) return true;
            }
            return false;
        }
    }

    interface Entry {
        Collection<ItemStack> getStacks();
    }

    record StackEntry(ItemStack stack) implements CustomIngredient.Entry {
        @Override
        public Collection<ItemStack> getStacks() {
            return Collections.singleton(this.stack);
        }
    }

    record TagEntry(TagKey<Item> tag) implements Entry {
        @Override
        public Collection<ItemStack> getStacks() {
            List<ItemStack> list = Lists.newArrayList();

            for (RegistryEntry<Item> registryEntry : Registries.ITEM.iterateEntries(this.tag)) {
                list.add(new ItemStack(registryEntry));
            }

            return list;
        }
    }

    record ItemEntry(ItemConvertible item) implements Entry {
        @Override
        public Collection<ItemStack> getStacks() {
            return Collections.singleton(item.asItem().getDefaultStack());
        }
    }

}
