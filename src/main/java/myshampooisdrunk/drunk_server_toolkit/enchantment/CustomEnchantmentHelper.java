package myshampooisdrunk.drunk_server_toolkit.enchantment;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;

import java.awt.event.WindowEvent;
import java.util.*;
import java.util.function.Predicate;

public class CustomEnchantmentHelper {
//    public static final String STORED_CUSTOM_ENCHANT_KEY = "stored_custom_enchants";
//    public static final String CUSTOM_ENCHANT_KEY = "shampoos_custom_enchants";
//    public static final String CUSTOM_ID_KEY = "id";
//    public static final String CUSTOM_LEVEL_KEY = "level";
//    public static NbtCompound createNbt(Identifier id, int level){
//        NbtCompound nbtCompound = new NbtCompound();
//        nbtCompound.putString(CUSTOM_ID_KEY, String.valueOf(id));
//        nbtCompound.putInt(CUSTOM_LEVEL_KEY, level);
//        return nbtCompound;
//    }
//    public static void set(Map<AbstractCustomEnchantment, Integer> enchantments, ItemStack stack) {
//        NbtList nbtList = new NbtList();
//        for (Map.Entry<AbstractCustomEnchantment, Integer> entry : enchantments.entrySet()) {
//            AbstractCustomEnchantment enchantment = entry.getKey();
//            if (enchantment == null) continue;
//            int i = entry.getValue();
//            nbtList.add(createNbt(getEnchantmentId(enchantment), i));
//            if (!stack.isOf(Items.ENCHANTED_BOOK)) continue;
//            addBookEnchant(stack, new CustomEnchantmentInstance(enchantment, i));
//        }
//        if (nbtList.isEmpty()) {
//            stack.removeSubNbt(CUSTOM_ENCHANT_KEY);
//        } else if (!stack.isOf(Items.ENCHANTED_BOOK)) {
//            stack.setSubNbt(CUSTOM_ENCHANT_KEY, nbtList);
//        }
//    }
//    public static NbtList getEnchantmentNbt(ItemStack stack) {
//        NbtCompound nbtCompound = stack.getNbt();
//        if (nbtCompound != null) {
//            return nbtCompound.getList(STORED_CUSTOM_ENCHANT_KEY, NbtElement.COMPOUND_TYPE);
//        }
//        return new NbtList();
//    }
//
//    public static int getLevel(AbstractCustomEnchantment enchantment, ItemStack stack) {
//        if (stack.isEmpty()) {
//            return 0;
//        }
//        Identifier identifier = getEnchantmentId(enchantment);
//        NbtList nbtList = getEnchantments(stack);
//        for (int i = 0; i < nbtList.size(); ++i) {
//            NbtCompound nbtCompound = nbtList.getCompound(i);
//            Identifier identifier2 = getIdFromNbt(nbtCompound);
//            if (identifier2 == null || !identifier2.equals(identifier)) continue;
//            return getLevelFromNbt(nbtCompound);
//        }
//        return 0;
//    }
//    public static void writeLevelToNbt(NbtCompound nbt, int lvl) {
//        nbt.putInt(CUSTOM_LEVEL_KEY, lvl);
//    }
//
//    public static int getLevelFromNbt(NbtCompound nbt) {
//        return nbt.getInt(CUSTOM_LEVEL_KEY);
//    }
//
//    @Nullable
//    public static Identifier getIdFromNbt(NbtCompound nbt) {
//        return Identifier.tryParse(nbt.getString(CUSTOM_ID_KEY));
//    }
//
//    @Nullable
//    public static Identifier getEnchantmentId(AbstractCustomEnchantment enchantment) {
//        return enchantment.getId();
//    }
//    public static NbtList getEnchantments(ItemStack stack){
//        NbtCompound nbt = stack.getOrCreateNbt();
//        if (nbt != null) {
//            if(nbt.contains(CUSTOM_ENCHANT_KEY))
//                return nbt.getList(CUSTOM_ENCHANT_KEY, NbtElement.COMPOUND_TYPE);
//        }
//        return new NbtList();
//    }
//    //public void appendCustomTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context, CallbackInfo ci){
//    //        ItemStack.appendEnchantments(tooltip, CustomEnchantmentHelper.getEnchantmentNbt(stack));
//    //    }
//    public static void appendCustomEnchantments(List<Text> tooltip, NbtList enchantments) {
//        if(enchantments.isEmpty())return;
//        for (int i = 0; i < enchantments.size(); ++i) {
//            NbtCompound nbtCompound = enchantments.getCompound(i);
//            AbstractCustomEnchantment e = WeaponAPI.ENCHANTMENTS.getOrDefault(getIdFromNbt(nbtCompound),null);
//            if(e != null){
//                tooltip.add(e.getName(CustomEnchantmentHelper.getLevelFromNbt(nbtCompound)));
//            }
//        }
//    }
//
//
//    public static NbtList getEnchantText(NbtList enchantments) {
//        NbtList ret = new NbtList();
//        List<Text> tooltip = new ArrayList<>();
//        if(enchantments.isEmpty())return null;
//        for (int i = 0; i < enchantments.size(); ++i) {
//            NbtCompound nbtCompound = enchantments.getCompound(i);
//            AbstractCustomEnchantment e = WeaponAPI.ENCHANTMENTS.getOrDefault(getIdFromNbt(nbtCompound),null);
//            if(e != null){
//                tooltip.add(e.getName(CustomEnchantmentHelper.getLevelFromNbt(nbtCompound)));
//            }
//        }
//        for(Text t : tooltip){
//            ret.add(NbtString.of(Text.Serialization.toJsonString(t)));
//        }
//
//        return ret;
//    }
//
//    public static void forEachCustomEnchantment(Consumer consumer, ItemStack stack){
//        if (stack.isEmpty()) {
//            return;
//        }
//        NbtList nbtList = getEnchantments(stack);
//        for (int i = 0; i < nbtList.size(); ++i) {
//            NbtCompound compound = nbtList.getCompound(i);
//            AbstractCustomEnchantment ench = WeaponAPI.ENCHANTMENTS.getOrDefault(getIdFromNbt(compound),null);
//            if(ench != null){
//                consumer.accept(ench, getLevelFromNbt(compound));
//            }
//        }
//    }
//    public static void addCustomEnchantment(ItemStack stack, CustomEnchantmentInstance instance) {
//        NbtList old = getEnchantments(stack);
//        NbtCompound nbt = stack.getOrCreateNbt();
//        if (!nbt.contains(CUSTOM_ENCHANT_KEY, NbtElement.LIST_TYPE)) {
//            nbt.put(CUSTOM_ENCHANT_KEY, new NbtList());
//        }
//        NbtList nbtList = nbt.getList(CUSTOM_ENCHANT_KEY, NbtElement.COMPOUND_TYPE);
//        nbtList.add(createNbt(getEnchantmentId(instance.enchantment), instance.level));
//        stack.setSubNbt(CUSTOM_ENCHANT_KEY,nbtList);
//        CustomEnchantmentHelper.setEnchantLore(stack,old,CustomEnchantmentHelper.getEnchantments(stack));
//    }
//    public static void addBookEnchant(ItemStack stack, CustomEnchantmentInstance instance){
//        NbtList old = getEnchantmentNbt(stack);
//        NbtList nbtList = EnchantedBookItem.getEnchantmentNbt(stack);
//        boolean bl = true;
//        Identifier identifier = getEnchantmentId(instance.enchantment);
//        for (int i = 0; i < nbtList.size(); ++i) {
//            NbtCompound nbtCompound = nbtList.getCompound(i);
//            Identifier identifier2 = getIdFromNbt(nbtCompound);
//            if (identifier2 == null || !identifier2.equals(identifier)) continue;
//            if (getLevelFromNbt(nbtCompound) < instance.level) {
//                writeLevelToNbt(nbtCompound, instance.level);
//            }
//            bl = false;
//            break;
//        }
//        if (bl) {
//            nbtList.add(createNbt(identifier, instance.level));
//        }
//        stack.getOrCreateNbt().put(STORED_CUSTOM_ENCHANT_KEY, nbtList);
//        CustomEnchantmentHelper.setEnchantLore(stack,old,CustomEnchantmentHelper.getEnchantmentNbt(stack));
//    }
//    public static void addCustomEnchantment(ItemStack stack, AbstractCustomEnchantment enchantment, int level){
//        addCustomEnchantment(stack,new CustomEnchantmentInstance(enchantment,level));
//    }
//    public static Map<AbstractCustomEnchantment, Integer> get(ItemStack stack) {
//        NbtList nbtList = stack.isOf(Items.ENCHANTED_BOOK) ? getEnchantmentNbt(stack) : getEnchantments(stack);
//        return CustomEnchantmentHelper.fromNbt(nbtList);
//    }
//    public static Map<AbstractCustomEnchantment, Integer> fromNbt(NbtList list) {
//        LinkedHashMap<AbstractCustomEnchantment, Integer> map = Maps.newLinkedHashMap();
//        for (int i = 0; i < list.size(); ++i) {
//            NbtCompound compound = list.getCompound(i);
//            AbstractCustomEnchantment ench = WeaponAPI.ENCHANTMENTS.getOrDefault(getIdFromNbt(compound),null);
//            if(ench != null){
//                map.put(ench, getLevelFromNbt(compound));
//            }
//        }
//        return map;
//    }
//    public static void forEachCustomEnchantments(Consumer consumer, Iterable<ItemStack> stacks) {
//        for (ItemStack itemStack : stacks) {
//            forEachCustomEnchantment(consumer, itemStack);
//        }
//    }
//
//    public static int getProtectionAmount(Iterable<ItemStack> equipment, DamageSource source) {
//        MutableInt mutableInt = new MutableInt();
//        forEachCustomEnchantments((AbstractCustomEnchantment enchantment, int level) -> mutableInt.add(enchantment.getProtectionAmount(level, source)), equipment);
//        return mutableInt.intValue();
//    }
//
//    public static float getAttackDamage(ItemStack stack, EntityGroup group) {
//        MutableFloat mutableFloat = new MutableFloat();
//        forEachCustomEnchantment((AbstractCustomEnchantment enchantment, int level) -> mutableFloat.add(enchantment.getAttackDamage(level, group)), stack);
//        return mutableFloat.floatValue();
//    }
//    public static void onUserDamaged(LivingEntity user, Entity attacker) {
//        Consumer consumer = (enchantment, level) -> enchantment.onUserDamaged(user, attacker, level);
//        if (user != null) {
//            forEachCustomEnchantments(consumer, user.getItemsEquipped());
//        }
//        if (attacker instanceof PlayerEntity) {
//            forEachCustomEnchantment(consumer, user.getMainHandStack());
//        }
//    }
//
//    public static void onTargetDamaged(LivingEntity user, Entity target) {
//        Consumer consumer = (enchantment, level) -> enchantment.onTargetDamaged(user, target, level);
//        if (user != null) {
//            forEachCustomEnchantments(consumer, user.getItemsEquipped());
//        }
//        if (user instanceof PlayerEntity) {
//            forEachCustomEnchantment(consumer, user.getMainHandStack());
//        }
//    }
//    public static int getEquipmentLevel(AbstractCustomEnchantment enchantment, LivingEntity entity) {
//        Collection<ItemStack> iterable = enchantment.getEquipment(entity).values();
//        int i = 0;
//        for (ItemStack itemStack : iterable) {
//            int j = getLevel(enchantment, itemStack);
//            if (j <= i) continue;
//            i = j;
//        }
//        return i;
//    }
//    @Nullable
//    public static Map.Entry<EquipmentSlot, ItemStack> chooseEquipmentWith(AbstractCustomEnchantment enchantment, LivingEntity entity) {
//        return chooseEquipmentWith(enchantment, entity, stack -> true);
//    }
//    @Nullable
//    public static Map.Entry<EquipmentSlot, ItemStack> chooseEquipmentWith(AbstractCustomEnchantment enchantment, LivingEntity entity, Predicate<ItemStack> condition) {
//        Map<EquipmentSlot, ItemStack> map = enchantment.getEquipment(entity);
//        if (map.isEmpty()) {
//            return null;
//        }
//        ArrayList<Map.Entry<EquipmentSlot, ItemStack>> list = Lists.newArrayList();
//        for (Map.Entry<EquipmentSlot, ItemStack> entry : map.entrySet()) {
//            ItemStack itemStack = entry.getValue();
//            if (itemStack.isEmpty() || getLevel(enchantment, itemStack) <= 0 || !condition.test(itemStack)) continue;
//            list.add(entry);
//        }
//        return list.isEmpty() ? null : list.get(entity.getRandom().nextInt(list.size()));
//    }
//    public static int calculateRequiredExperienceLevel(net.minecraft.util.math.random.Random random, int slotIndex, int bookshelfCount, ItemStack stack) {
//        Item item = stack.getItem();
//        int i = item.getEnchantability();
//        if (i <= 0) {
//            return 0;
//        }
//        if (bookshelfCount > 15) {
//            bookshelfCount = 15;
//        }
//        int j = random.nextInt(8) + 1 + (bookshelfCount >> 1) + random.nextInt(bookshelfCount + 1);
//        if (slotIndex == 0) {
//            return Math.max(j / 3, 1);
//        }
//        if (slotIndex == 1) {
//            return j * 2 / 3 + 1;
//        }
//        return Math.max(j, bookshelfCount * 2);
//    }
//    public static ItemStack enchant(net.minecraft.util.math.random.Random random, ItemStack target, int level, boolean treasureAllowed) {
//        List<CustomEnchantmentInstance> list = generateEnchantments(random, target, level, treasureAllowed);
//        boolean bl = target.isOf(Items.BOOK);
//        if (bl) {
//            target = new ItemStack(Items.ENCHANTED_BOOK);
//        }
//        for (CustomEnchantmentInstance instance : list) {
//            if (bl) {
//                CustomEnchantsOnEnchantedBook.addCustomEnchantment(target, instance);
//                continue;
//            }else {
//                addCustomEnchantment(target, instance);
//            }
//        }
//        return target;
//    }
//    public static List<CustomEnchantmentInstance> generateEnchantments(Random random, ItemStack stack, int level, boolean treasureAllowed) {
//        ArrayList<CustomEnchantmentInstance> list = Lists.newArrayList();
//        Item item = stack.getItem();
//        int i = item.getEnchantability();
//        if (i <= 0) {
//            return list;
//        }
//        level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
//        float f = (random.nextFloat() + random.nextFloat() - 1.0f) * 0.15f;
//        List<CustomEnchantmentInstance> list2 = getPossibleEntries(level = MathHelper.clamp(Math.round((float)level + (float)level * f), 1, Integer.MAX_VALUE), stack, treasureAllowed);
//        if (!list2.isEmpty()) {
//            Weighting.getRandom(random, list2).ifPresent(list::add);
//            while (random.nextInt(50) <= level) {
//                if (!list.isEmpty()) {
//                    removeConflicts(list2, Util.getLast(list));
//                }
//                if (list2.isEmpty()) break;
//                Weighting.getRandom(random, list2).ifPresent(list::add);
//                level /= 2;
//            }
//        }
//        return list;
//    }
//    public static Pair<List<CustomEnchantmentInstance>,List<EnchantmentLevelEntry>> generate(Random random, ItemStack stack, int level, boolean treasureAllowed){
//        ArrayList<CustomEnchantmentInstance> custom = Lists.newArrayList();
//        ArrayList<EnchantmentLevelEntry> ench = Lists.newArrayList();
//        Pair<List<CustomEnchantmentInstance>,List<EnchantmentLevelEntry>> ret = new Pair<>(custom,ench);
//        Item item = stack.getItem();
//        int i = item.getEnchantability();
//        if (i <= 0) {
//            return ret;
//        }
//        level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
//        float f = (random.nextFloat() + random.nextFloat() - 1.0f) * 0.15f;
//        List<EnchantmentLevelEntry> possEnch = EnchantmentHelper.getPossibleEntries(level = MathHelper.clamp(Math.round((float)level + (float)level * f), 1, Integer.MAX_VALUE), stack, treasureAllowed);
//        List<CustomEnchantmentInstance> possCustom = getPossibleEntries(level = MathHelper.clamp(Math.round((float)level + (float)level * f), 1, Integer.MAX_VALUE), stack, treasureAllowed);
//        List<Pair<CustomEnchantmentInstance,EnchantmentLevelEntry>> pairs = Lists.newArrayList();
//        System.out.println("possCustom: " + possCustom);
//        for(EnchantmentLevelEntry inst : possEnch){
//            pairs.add(new Pair<>(null,inst));
//        }
//        for(CustomEnchantmentInstance inst : possCustom){
//            pairs.add(new Pair<>(inst,null));
//        }
//        if (!pairs.isEmpty()){
//            Optional<Pair<CustomEnchantmentInstance,EnchantmentLevelEntry>> rand = getRandomPair(random, pairs);
//            if(rand.isPresent()){
//
//                if(rand.get().getLeft() != null){
//                    custom.add(rand.get().getLeft());
//                }
//                if(rand.get().getRight() != null){
//                    ench.add(rand.get().getRight());
//                }
//            }
//            while (random.nextInt(50) <= level) {
//                if (!custom.isEmpty()) removeConflicts(possCustom, Util.getLast(custom));
//                if (!ench.isEmpty()) EnchantmentHelper.removeConflicts(possEnch,Util.getLast(ench));
//                if (possCustom.isEmpty() && possEnch.isEmpty()) break;
//
//                level /= 2;
//            }
//        }
//        ret = new Pair<>(custom,ench);
//        return ret;
//    }
//
//
//    public static void removeConflicts(List<CustomEnchantmentInstance> possibleEntries, CustomEnchantmentInstance pickedEntry) {
//        Iterator<CustomEnchantmentInstance> iterator = possibleEntries.iterator();
//        while (iterator.hasNext()) {
//            if (pickedEntry.enchantment.canCombine(iterator.next().enchantment)) continue;
//            iterator.remove();
//        }
//    }
//
//    public static boolean isCompatible(Collection<AbstractCustomEnchantment> existing, AbstractCustomEnchantment candidate) {
//        for (AbstractCustomEnchantment enchantment : existing) {
//            if (enchantment.canCombine(candidate)) continue;
//            return false;
//        }
//        return true;
//    }
//    public static List<CustomEnchantmentInstance> getPossibleEntries(int power, ItemStack stack, boolean treasureAllowed) {
//        ArrayList<CustomEnchantmentInstance> list = Lists.newArrayList();
//        Item item = stack.getItem();
//        boolean bl = stack.isOf(Items.BOOK);
//        block0: for (AbstractCustomEnchantment enchantment : WeaponAPI.ENCHANTMENTS.values()) {
//            if (enchantment.isTreasure() && !treasureAllowed || !enchantment.isAvailableForRandomSelection() || !enchantment.target.isAcceptableItem(item) && !bl) continue;
//            for (int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
//                if (power < enchantment.getMinPower(i) || power > enchantment.getMaxPower(i)) continue;
//                list.add(new CustomEnchantmentInstance(enchantment, i));
//                continue block0;
//            }
//        }
//        return list;
//    }
//    public static Map<AbstractCustomEnchantment,Integer> getEnchantmentList(ItemStack item){
//        return CustomEnchantmentHelper.fromNbt(getEnchantments(item));
//    }
//    @FunctionalInterface
//    public interface Consumer {
//        public void accept(AbstractCustomEnchantment var1, int var2);
//    }
//    public static ItemStack forEnchantment(CustomEnchantmentInstance info) {
//        ItemStack itemStack = new ItemStack(Items.ENCHANTED_BOOK);
//        CustomEnchantsOnEnchantedBook.addCustomEnchantment(itemStack, info);
//        return itemStack;
//    }
//
//    public static <V> V echo(V a, String s){
//        System.out.println(s + a);
//        return a;
//    }
//    public static void handleCustomCombine(ItemStack stack1, ItemStack stack2, ItemStack ret){
//        boolean book1 = stack1.isOf(Items.ENCHANTED_BOOK) && !getEnchantmentNbt(stack1).isEmpty();
//        boolean book2 = stack2.isOf(Items.ENCHANTED_BOOK) && !getEnchantmentNbt(stack2).isEmpty();
//        Map<AbstractCustomEnchantment, Integer> finalStoredEnch = new HashMap<>();
//        Map<AbstractCustomEnchantment, Integer> finalEnch = new HashMap<>();
//        Set<AbstractCustomEnchantment> blacklist = new HashSet<>();
//        if(book1 && book2){
//            Map<AbstractCustomEnchantment,Integer> ench1 = get(stack1);
//            Map<AbstractCustomEnchantment,Integer> ench2 = get(stack2);
//            for(AbstractCustomEnchantment ench : ench1.keySet()) {
//                if (ench2.containsKey(ench)) {
//                    if (ench1.get(ench).equals(ench2.get(ench))) {
//                        if (ench1.get(ench) < ench.getMaxLevel())
//                            finalStoredEnch.put(ench, ench1.get(ench) + 1);
//                        else finalStoredEnch.put(ench, ench.getMaxLevel());
//                    } else
//                        finalStoredEnch.put(ench, Math.max(ench1.get(ench), ench2.get(ench)));
//                }else{
//                    finalStoredEnch.put(ench,ench1.get(ench));
//                    for(AbstractCustomEnchantment ench22 : ench2.keySet()){
//                        if(!ench.canAccept(ench22)){
//                            blacklist.add(ench22);
//                        }
//                    }
//                }
//            }
//            for(AbstractCustomEnchantment ench : ench2.keySet()){
//                if(!blacklist.contains(ench) && !finalStoredEnch.containsKey(ench))finalStoredEnch.put(echo(ench,"enchant in blacklist: "),ench2.get(ench));
//            }
//
//        }
//        blacklist.clear();
//        boolean bool = stack1.isOf(stack2.getItem()) && !stack2.isOf(Items.ENCHANTED_BOOK);//if same item
//        if(bool){
//            Map<AbstractCustomEnchantment,Integer> ench1 = fromNbt(getEnchantments(stack1));
//            Map<AbstractCustomEnchantment,Integer> ench2 = fromNbt(getEnchantments(stack2));
//            for(AbstractCustomEnchantment ench : ench1.keySet()) {
//                if (ench2.containsKey(ench)) {
//                    if (ench1.get(ench).equals(ench2.get(ench))) {
//                        if (ench1.get(ench) < ench.getMaxLevel())
//                            finalEnch.put(ench, ench1.get(ench) + 1);
//                        else finalEnch.put(ench, ench.getMaxLevel());
//                    } else
//                        finalEnch.put(ench, Math.max(ench1.get(ench), ench2.get(ench)));
//                }else{
//                    finalEnch.put(ench,ench1.get(ench));
//                    for(AbstractCustomEnchantment ench22 : ench2.keySet()){
//                        if(!ench.canAccept(ench22)){
//                            blacklist.add(ench22);
//                        }
//                    }
//                }
//            }
//            for(AbstractCustomEnchantment ench : ench2.keySet()){
//                if(!blacklist.contains(ench) && !finalEnch.containsKey(ench))finalEnch.put(ench,ench2.get(ench));
//            }
//        }
//        blacklist.clear();
//        if(book2 && !stack1.isOf(Items.ENCHANTED_BOOK)){
//            Map<AbstractCustomEnchantment,Integer> ench1 = fromNbt(getEnchantments(stack1));
//            Map<AbstractCustomEnchantment,Integer> ench2 = get(stack2);
//            for(AbstractCustomEnchantment ench : ench1.keySet()) {
//                if (ench2.containsKey(ench)) {
//                    if (ench1.get(ench).equals(ench2.get(ench))) {
//                        if (ench1.get(ench) < ench.getMaxLevel())
//                            finalEnch.put(ench, ench1.get(ench) + 1);
//                        else finalEnch.put(ench, ench.getMaxLevel());
//                    } else
//                        finalEnch.put(ench, Math.max(ench1.get(ench), ench2.get(ench)));
//                }else{
//                    finalEnch.put(ench,ench1.get(ench));
//                    for(AbstractCustomEnchantment ench22 : ench2.keySet()){
//                        if(!ench.canAccept(ench22)){
//                            blacklist.add(ench22);
//                        }
//                    }
//                }
//            }
//            for(AbstractCustomEnchantment ench : ench2.keySet()){
//                if(!blacklist.contains(ench) && !finalEnch.containsKey(ench))finalEnch.put(ench,ench2.get(ench));
//            }
//        }
//        if(!finalEnch.isEmpty())set(finalEnch,ret);
//        if(!finalStoredEnch.isEmpty())
//            for(Map.Entry<AbstractCustomEnchantment,Integer> i : finalStoredEnch.entrySet()){
//                addBookEnchant(ret,new CustomEnchantmentInstance(i.getKey(),i.getValue()));
//            }
//        setEnchantLore(ret, getEnchantments(stack1), getEnchantments(ret), stack2);
//    }
//
//    public static boolean canCombine(ItemStack stack1, ItemStack stack2, PlayerEntity p){
//        Map<AbstractCustomEnchantment, Integer> ench1 = get(stack1);
//        Map<AbstractCustomEnchantment, Integer> ench2 = get(stack2);
//        boolean b1 = !ench1.isEmpty();
//        boolean b2 = !ench2.isEmpty();
//        if(stack2.isOf(Items.ENCHANTED_BOOK)) {
//            if (b2) {
//                boolean b3 = false;
//                for (AbstractCustomEnchantment e : ench2.keySet()) {
//                    if (e.isAcceptableItem(stack1) || stack1.isOf(Items.ENCHANTED_BOOK)) {
//                        b3 = true;
//                        break;
//                    }
//                }
//                return b3 || p.isCreative();
//            }
//        }else if(stack1.isOf(stack2.getItem())){
//            return b1||b2;
//        }
//        return false;
//    }
//
//    private static NbtList getLoreForMerge(ItemStack stack, boolean otherLoreExists) {
//        NbtCompound nbtCompound2;
//        NbtCompound nbtCompound;
//        if (stack.hasNbt()) {
//            nbtCompound = stack.getNbt();
//        } else if (otherLoreExists) {
//            nbtCompound = new NbtCompound();
//            stack.setNbt(nbtCompound);
//        } else {
//            return null;
//        }
//        if (nbtCompound.contains("display", NbtElement.COMPOUND_TYPE)) {
//            nbtCompound2 = nbtCompound.getCompound("display");
//        } else if (otherLoreExists) {
//            nbtCompound2 = new NbtCompound();
//            nbtCompound.put("display", nbtCompound2);
//        } else {
//            return null;
//        }
//        if (nbtCompound2.contains("Lore", NbtElement.LIST_TYPE)) {
//            return nbtCompound2.getList("Lore", NbtElement.STRING_TYPE);
//        }
//        if (otherLoreExists) {
//            NbtList nbtList = new NbtList();
//            nbtCompound2.put("Lore", nbtList);
//            return nbtList;
//        }
//        return null;
//    }
//    public static void setEnchantLore(ItemStack i, NbtList o, NbtList e){setEnchantLore(i,o,e,null);}
//    public static void setEnchantLore(ItemStack item, NbtList oldEnch, NbtList newEnch, @Nullable ItemStack stack2){
//        if(item.getNbt() == null)return;
//        NbtList retLore = new NbtList();
//        NbtCompound nbtCompound = item.getNbt().getCompound("display");
//        NbtList oldLore = oldEnch != null ? getEnchantText(oldEnch):null;
//        NbtList newLore = newEnch != null ? getEnchantText(newEnch):null;
//        ItemStack item2 = stack2 != null ? stack2.copy():null;
//        if(item2!=null){
//            if(item2.getNbt() != null) {
//                NbtCompound nbt = item2.getNbt().getCompound("display");
//                NbtList lore2 = getLoreForMerge(item2,nbt.contains(ItemStack.LORE_KEY));
//                if(lore2!=null){
//                    if(oldLore!=null)lore2.removeAll(oldLore);
//                    if(newLore!=null)lore2.removeAll(newLore);
//                    setEnchantLore(item,oldEnch,newEnch,null);
//                    assert item.getNbt() != null;//I already know it isnt null so if it is smth went wrong fs
//                    NbtCompound display = item.getNbt().getCompound("display");
//                    NbtList ret = display.getList("Lore", NbtElement.STRING_TYPE);
//                    ret.addAll(lore2);
//                    display.put(ItemStack.LORE_KEY,ret);
//                    item.setSubNbt(ItemStack.DISPLAY_KEY,display);
//                    return;
//                }
//            }
//        }
//        NbtList lore = getLoreForMerge(item, nbtCompound.contains(ItemStack.LORE_KEY));
//        if(newLore == null)return;
//        if(lore == null){
//            retLore.addAll(newLore);
//            nbtCompound.put(ItemStack.LORE_KEY,retLore);
//            item.setSubNbt(ItemStack.DISPLAY_KEY,nbtCompound);
//            return;
//        }
//        if(oldLore == null){
//            retLore.addAll(lore);
//            retLore.removeAll(newLore);
//            retLore.addAll(newLore);
//            nbtCompound.put(ItemStack.LORE_KEY,retLore);
//            item.setSubNbt(ItemStack.DISPLAY_KEY,nbtCompound);
//            return;
//        }
//        lore.removeAll(oldLore);
//        lore.removeAll(newLore);
//        retLore.addAll(newLore);
//        retLore.addAll(lore);
//        nbtCompound.put(ItemStack.LORE_KEY,retLore);
//        item.setSubNbt(ItemStack.DISPLAY_KEY,nbtCompound);
//    }
//
//    public static <T extends Weighted, R extends Weighted> Optional<Pair<T,R>> getRandomPair(Random random, List<Pair<T,R>> pool) {
//        int totalWeight = 0;
//        for(Pair<T,R> p : pool){
//            if(p.getRight() == null && p.getLeft() != null){
//                totalWeight += p.getLeft().getWeight().getValue();
//            }else if(p.getLeft() == null && p.getRight() != null){
//                totalWeight += p.getRight().getWeight().getValue();
//            }
//        }
//        if (totalWeight == 0) {
//            return Optional.empty();
//        }
//        int i = random.nextInt(totalWeight);
//        return getAtPair(pool, i);
//    }
//    public static <T extends Weighted, R extends Weighted> Optional<Pair<T,R>> getAtPair(List<Pair<T,R>> pool, int totalWeight){
//        for (Pair<T,R> pair : pool) {
//            Weighted weighted = pair.getLeft() != null ? pair.getLeft() : pair.getRight();
//            if ((totalWeight -= weighted.getWeight().getValue()) >= 0) continue;
//            return Optional.of(pair);
//        }
//        return Optional.empty();
//    }
}