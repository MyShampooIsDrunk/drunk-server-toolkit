package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.WeaponAPI;
import myshampooisdrunk.drunk_server_toolkit.enchantment.CustomEnchantmentHelper;
import myshampooisdrunk.drunk_server_toolkit.weapon.AbstractCustomItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Entity.class)
public class EntityMixin {
    Entity e = (Entity)(Object)this;
    @Inject(at=@At("HEAD"),method = "setSneaking")
    public void onSneak(boolean sneaking, CallbackInfo ci){
        if(e instanceof PlayerEntity p){
            ItemStack item = p.getStackInHand(Hand.MAIN_HAND);
            CustomEnchantmentHelper.getEnchantmentList(item).forEach((enchant,level)->{
               enchant.onSneak(sneaking,p,level,ci);
            });
            if(WeaponAPI.ITEMS.containsKey(item.getItem())) {
                for (AbstractCustomItem custom : WeaponAPI.ITEMS.get(item.getItem())) {
                    if (custom.getItem().equals(item.getItem()) && item.getOrCreateNbt().getInt("CustomModelData") == custom.getId()) {
                        custom.onSneak(sneaking, p, ci);
                        break;
                    }
                }
            }
        }
    }

}
