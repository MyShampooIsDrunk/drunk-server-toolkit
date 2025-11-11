package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.item.CustomItemHelper;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{
    @Shadow public abstract @Nullable ItemStack getBlockingItem();

    @Inject(at=@At("HEAD"), method="damage", cancellable = true)
    private void injectMultiblockEntityDamage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
            ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).damage(world, source, amount, cir);
        }
    }

    @Inject(at=@At("HEAD"), method="takeShieldHit", cancellable = true)
    private void injectMultiblocktakeShieldHit(ServerWorld world, LivingEntity attacker, CallbackInfo ci) {
        if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
            ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).takeShieldHit(world, attacker, ci);
        }
    }

    @Inject(method = "tryUseDeathProtector", at=@At("HEAD"), cancellable = true)
    private void injectMultiblockEntityTryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if(getEntityWorld().isClient()) return;
        if(((MultiblockCacheI) getEntityWorld()).drunk_server_toolkit$containsUuid(uuid)) {
            ((MultiblockCacheI) getEntityWorld()).drunk_server_toolkit$getMultiblockEntity(uuid).tryUseTotem(source, cir);
        }
    }

    @Inject(at=@At("HEAD"),method= "jump", cancellable = true)
    private void jump(CallbackInfo ci) {
        if((Entity)this instanceof PlayerEntity user){
            ItemStack item = user.getStackInHand(Hand.MAIN_HAND);
            CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.onJump(user,ci));
        }
    }

    @Inject(method = "takeShieldHit", at = @At("HEAD"))
    public void injectItemTakeShieldHit(ServerWorld world, LivingEntity attacker, CallbackInfo ci) {
        ItemStack item = getBlockingItem();
        CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.onTakeShieldHit(world, attacker, (LivingEntity)(Object)this, ci));
    }

    public LivingEntityMixin(EntityType<?> type, World world) {super(type, world);}
}
