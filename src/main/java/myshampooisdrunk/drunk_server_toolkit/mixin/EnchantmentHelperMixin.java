package myshampooisdrunk.drunk_server_toolkit.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(at=@At("TAIL"),method = "getProtectionAmount",cancellable = true)
    private static void injectCustomShit(Iterable<ItemStack> equipment, DamageSource source, CallbackInfoReturnable<Integer> cir, @Local MutableInt ret){
        ret.add(CustomEnchantmentHelper.getProtectionAmount(equipment,source));
        cir.setReturnValue(ret.intValue());
    }

    @Inject(at=@At("TAIL"),method = "getAttackDamage",cancellable = true)
    private static void injectCustomShit(ItemStack stack, EntityGroup group, CallbackInfoReturnable<Float> cir, @Local MutableFloat ret){
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
}
