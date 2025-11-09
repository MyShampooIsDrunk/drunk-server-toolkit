package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.item.CustomItemHelper;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;


@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow protected UUID uuid;

    @Shadow private World world;
    Entity e = (Entity)(Object) this;

    @Inject(at=@At("HEAD"),method = "setSneaking", cancellable = true)
    public void onSneak(boolean sneaking, CallbackInfo ci){
        if(e instanceof PlayerEntity p){
            ItemStack item = p.getStackInHand(Hand.MAIN_HAND);
            CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.onSneak(sneaking,p,ci));
        }
    }

    @Inject(at=@At("HEAD"), method = "interact", cancellable = true)
    public void injectMultiblockEntityInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        if(!world.isClient()){// note that you interact with each hand once so 1 right click -> 1 offhand + 1 main hand interaction
            if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
                ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).onInteract(player, hand, cir);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "tick", cancellable = true)
    public void injectMultiblockEntityTick(CallbackInfo ci){
        if(!world.isClient()){
            if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
                ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).tick(ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "onPlayerCollision", cancellable = true)
    public void injectMultiblockEntityCollision(PlayerEntity player, CallbackInfo ci){
        if(!world.isClient()){
            if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
                ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).onPlayerCollision(player, ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "onDamaged", cancellable = true)
    public void injectMultiblockEntityOnDamaged(DamageSource damageSource, CallbackInfo ci){
        if(!world.isClient()){
            if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
                ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).onDamaged(damageSource, ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "onBlockCollision", cancellable = true)
    public void injectMultiblockEntityBlockCollision(BlockState state, CallbackInfo ci){
        if(!world.isClient()){
            if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
                ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).onBlockCollision(state, ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "remove")
    public void injectMultiblockEntityRemove(Entity.RemovalReason reason, CallbackInfo ci) {
        if(!world.isClient()){
            if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
                ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).onRemoved(reason, ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "onStruckByLightning", cancellable = true)
    public void injectMultiblockEntityStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo ci){
        if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
            ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).onStruckByLightning(world, lightning, ci);
        }
    }

    @Inject(at=@At("HEAD"), method = "handleAttack", cancellable = true)
    public void injectMultiblockEntityHandleAttack(Entity attacker, CallbackInfoReturnable<Boolean> cir){
        if(!world.isClient()){
            if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
                ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).handleAttack(attacker, cir);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "interactAt", cancellable = true)
    public void injectMultiblockEntityInteractAt(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        if(!world.isClient()){
            if(((MultiblockCacheI) world).drunk_server_toolkit$containsUuid(uuid)) {
                ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).interactAt(player, hitPos, hand, cir);
            }
        }
    }

}
