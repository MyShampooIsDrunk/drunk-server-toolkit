package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.item.CustomItemHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    PlayerEntity user = (PlayerEntity)(Object)this;
    @Inject(at=@At("HEAD"),method="attack", cancellable = true)
    private void attack(Entity target, CallbackInfo ci) {
        ItemStack item = user.getStackInHand(Hand.MAIN_HAND);
        CustomItemHelper.getCustomItem(item).ifPresent(custom -> {
            custom.postHit(item, target, this, ci);
        });
    }
    @Inject(at=@At("HEAD"),method="dropItem(Lnet/minecraft/item/ItemStack;ZZ)Lnet/minecraft/entity/ItemEntity;")
    private void dropItem(ItemStack stack, boolean throwRandomly, boolean retainOwnership, CallbackInfoReturnable<ItemEntity> cir){
        CustomItemHelper.getCustomItem(stack).ifPresent(custom -> custom.postDrop(user, stack, throwRandomly, retainOwnership, cir));
    }
    @Inject(at=@At("HEAD"),method="tick", cancellable = true)
    public void whileSneaking(CallbackInfo ci){
        if(this.isSneaking()){
            ItemStack item = user.getStackInHand(Hand.MAIN_HAND);
            CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.whileSneak((PlayerEntity) (Object) this, ci));
        }
    }
    @Inject(at=@At("HEAD"),method="interact", cancellable = true)
    public void onInteract(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        ItemStack item = user.getStackInHand(Hand.MAIN_HAND);
        CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.onEntityInteraction(user,entity,hand,cir));
    }
    @Inject(at=@At("HEAD"),method="jump", cancellable = true)
    private void jump(CallbackInfo ci) {
        ItemStack item = user.getStackInHand(Hand.MAIN_HAND);
        CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.onJump(user,ci));
    }
}
