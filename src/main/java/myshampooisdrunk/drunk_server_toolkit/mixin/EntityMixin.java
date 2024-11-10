package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.item.CustomItemHelper;
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
    @Inject(at=@At("HEAD"),method = "setSneaking", cancellable = true)
    public void onSneak(boolean sneaking, CallbackInfo ci){
        if(e instanceof PlayerEntity p){
            ItemStack item = p.getStackInHand(Hand.MAIN_HAND);
            CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.onSneak(sneaking,p,ci));
        }
    }

}
