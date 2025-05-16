package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockData;
import myshampooisdrunk.drunk_server_toolkit.item.CustomItemHelper;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.AbstractMultiblockStructureEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract World getWorld();

    Entity e = (Entity)(Object)this;
    @Inject(at=@At("HEAD"),method = "setSneaking", cancellable = true)
    public void onSneak(boolean sneaking, CallbackInfo ci){
        if(e instanceof PlayerEntity p){
            ItemStack item = p.getStackInHand(Hand.MAIN_HAND);
            CustomItemHelper.getCustomItem(item).ifPresent(custom -> custom.onSneak(sneaking,p,ci));
        }
    }

    @Inject(at=@At("HEAD"), method = "interact", cancellable = true)
    public void injectMultiblockEntityInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        if(!getWorld().isClient()){// note that you interact with each hand once so 1 right click -> 1 offhand + 1 main hand interaction
            String id;
            MultiblockData data = ((Entity)(Object)this).getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.onInteract(player, e, hand, cir);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "tick", cancellable = true)
    public void injectMultiblockEntityTick(CallbackInfo ci){
        if(!getWorld().isClient()){
            String id;
            MultiblockData data = ((Entity)(Object)this).getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.tick(e, ci);
//                structureEntity.damage();
//                structureEntity.onEntityCollision();
//                structureEntity.handleAttack();
//                structureEntity.interactAt();
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "onPlayerCollision", cancellable = true)
    public void injectMultiblockEntityCollision(PlayerEntity player, CallbackInfo ci){
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = ((Entity) (Object) this).getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.onPlayerCollision(player, e, ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "onDamaged", cancellable = true)
    public void injectMultiblockEntityOnDamaged(DamageSource damageSource, CallbackInfo ci){
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = ((Entity) (Object) this).getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.onDamaged(damageSource, e, ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "onBlockCollision", cancellable = true)
    public void injectMultiblockEntityBlockCollision(BlockState state, CallbackInfo ci){
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = ((Entity) (Object) this).getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.onBlockCollision(state, e, ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "discard")
    public void injectMultiblockEntityDiscard(CallbackInfo ci) {
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = ((Entity) (Object) this).getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.onDiscard(e);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "onStruckByLightning", cancellable = true)
    public void injectMultiblockEntityStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo ci){
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = ((Entity) (Object) this).getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.onStruckByLightning(world, lightning, e, ci);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "handleAttack", cancellable = true)
    public void injectMultiblockEntityHandleAttack(Entity attacker, CallbackInfoReturnable<Boolean> cir){
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = ((Entity) (Object) this).getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.handleAttack(attacker, e, cir);
            }
        }
    }

    @Inject(at=@At("HEAD"), method = "interactAt", cancellable = true)
    public void injectMultiblockEntityInteractAt(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        if(!getWorld().isClient()) {
            String id;
            MultiblockData data = ((Entity) (Object) this).getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
            if ((id = data.getEntityId()) != null) {
                AbstractMultiblockStructureEntity<? extends Entity> structureEntity = MultiblockRegistry.ENTITY_TYPES.get(id).defaultEntity();
                structureEntity.interactAt(player, hitPos, hand, e, cir);
            }
        }
    }

}
