package myshampooisdrunk.drunk_server_toolkit.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentInstance;
import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import myshampooisdrunk.drunk_server_toolkit.item.CustomSwordItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Pair;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(at=@At("TAIL"),method = "getProtectionAmount",cancellable = true)
    private static void injectCustomShit(Iterable<ItemStack> equipment, DamageSource source, CallbackInfoReturnable<Integer> cir, @Local MutableInt ret){
        ret.add(CustomEnchantmentHelper.getProtectionAmount(equipment,source));
        cir.setReturnValue(ret.intValue());
    }

    @Inject(at=@At("TAIL"),method = "getAttackDamage",cancellable = true)
    private static void injectCustomShit(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> cir, @Local MutableFloat ret){

        if(WeaponAPI.ITEMS.containsKey(stack.getItem())) {
            for (AbstractCustomItem custom : WeaponAPI.ITEMS.get(stack.getItem())) {
                if(custom instanceof CustomSwordItem item){
                    if(custom.getItem().equals(stack.getItem()) && stack.getOrCreateNbt().getInt("CustomModelData") == custom.getId()){
                        ret.add(item.getBonusDamage(group, ret.getValue()));
                    }
                }
            }
        }
        ret.add(CustomEnchantmentHelper.getAttackDamage(stack,group));
        cir.setReturnValue(ret.floatValue());
    }

    @Inject(at=@At("TAIL"),method = "onUserDamaged")
    private static void injectCustomShit(LivingEntity user, Entity attacker, CallbackInfo ci) {
        CustomEnchantmentHelper.Consumer consumer = (enchantment, level) -> enchantment.onUserDamaged(user, attacker, level);
        if (user != null) {
            CustomEnchantmentHelper.forEachCustomEnchantments(consumer, user.getItemsEquipped());
        }
        if (attacker instanceof PlayerEntity) {
            assert user != null;
            CustomEnchantmentHelper.forEachCustomEnchantment(consumer, user.getMainHandStack());
        }
    }

    @Inject(at=@At("TAIL"),method = "onTargetDamaged")
    private static void injectCustomShit2(LivingEntity user, Entity target, CallbackInfo ci) {
        CustomEnchantmentHelper.Consumer consumer = (enchantment, level) -> enchantment.onTargetDamaged(user, target, level);
        if (user != null) {
            CustomEnchantmentHelper.forEachCustomEnchantments(consumer, user.getItemsEquipped());
        }
        if (user instanceof PlayerEntity) {
            CustomEnchantmentHelper.forEachCustomEnchantment(consumer, user.getMainHandStack());
        }
    }
    @Inject(method="enchant",at=@At("HEAD"),cancellable = true)
    private static void ActuallyEnchantTheItem(Random random, ItemStack target, int level, boolean treasureAllowed, CallbackInfoReturnable<ItemStack> cir){
        Pair<List<CustomEnchantmentInstance>,List<EnchantmentLevelEntry>> pair = CustomEnchantmentHelper.generate(random, target, level, treasureAllowed);
        System.out.println("custom: " + pair.getLeft());
        System.out.println("real: " + pair.getRight());
        boolean bl = target.isOf(Items.BOOK);
        if (bl) {
            target = new ItemStack(Items.ENCHANTED_BOOK);
        }
        for (EnchantmentLevelEntry enchantmentLevelEntry : pair.getRight()) {
            if (bl) {
                EnchantedBookItem.addEnchantment(target, enchantmentLevelEntry);
                continue;
            }
            target.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
        }
        for (CustomEnchantmentInstance enchantmentLevelEntry : pair.getLeft()) {
            if (bl) {
                CustomEnchantmentHelper.addBookEnchant(target, enchantmentLevelEntry);
                continue;
            }
            CustomEnchantmentHelper.addCustomEnchantment(target,enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
        }
        cir.setReturnValue(target);
    }
}

