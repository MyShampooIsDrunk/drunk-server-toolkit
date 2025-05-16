package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockData;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public abstract class AbstractMultiblockStructureEntity<E extends Entity> {
    protected final String id;
    protected final EntityType<E> type;

    @SuppressWarnings("unchecked")
    public AbstractMultiblockStructureEntity(EntityType<E> type, String id){
        MultiblockRegistry.registerEntityType(MultiblockEntityType.create(id, type, this, (Class<AbstractMultiblockStructureEntity<E>>) this.getClass()));
        this.id = id;
        this.type = type;
    }

    public String id(){
        return id;
    }

    public E create(ServerWorld world, MultiblockStructure structure, BlockPos center, Vec3d relative){
        Vec3d spawnPos = relative.add(center.toCenterPos());
        E entity = this.type.create(world);
        assert entity != null;
        MultiblockData data = entity.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);

        for (MultiblockEntityType<?> e : MultiblockRegistry.ENTITY_TYPES.values()) {
            if(e.defaultEntity().id().equals(id)) {
                data.setEntityId(id);
            }
        }

        data.setMultiblock(structure, spawnPos);

        entity.refreshPositionAndAngles(spawnPos,0 ,0);

        entity.setNoGravity(true);
        entity.setInvulnerable(true);
        entity.setSilent(true);
        if(entity instanceof MobEntity m) {
            m.setAiDisabled(true);
            m.setPersistent();
        }
        return entity;
    }

    public EntityType<E> getType() {
        return type;
    }

    public void onInteract(PlayerEntity user, Entity me, Hand hand, CallbackInfoReturnable<ActionResult> cir){
    }

    public void onPlayerCollision(PlayerEntity player, Entity me, CallbackInfo ci) {
    }

    //this will be called by the mixin
    //to implement custom tick code, you should override
    //super.tick(me, ci) if you want to cancel entity.tick() but not entity.baseTick()
    //tick(true, me, ci) if you want to cancel entity.tick() and entity.baseTick()
    //   alternatively just use ci.cancel()
    public void tick(Entity me, CallbackInfo ci) {
        tick(false, me, ci);
    }
    public final void tick(boolean cancelBaseTick, Entity me, CallbackInfo ci) {
        ci.cancel();
        if(!cancelBaseTick) me.baseTick();
    }

    public void onDamaged(DamageSource damageSource, Entity me, CallbackInfo ci) {
    }

    public void onBlockCollision(BlockState state, Entity me, CallbackInfo ci) {
    }

    public void onDiscard(Entity me) {
    }

    public void onStruckByLightning(ServerWorld world, LightningEntity lightning, Entity me, CallbackInfo ci) {
    }

    //cir.setReturnValue(true) to not tick damage
    public void handleAttack(Entity attacker, Entity me, CallbackInfoReturnable<Boolean> cir) {
    }

    public void interactAt(PlayerEntity player, Vec3d hitPos, Hand hand, Entity me, CallbackInfoReturnable<ActionResult> cir) {
    }

    //LivingEntity
    public void damage(DamageSource source, float amount, Entity me, CallbackInfoReturnable<Boolean> cir) {
    }

    public void takeShieldHit(LivingEntity attacker, Entity me, CallbackInfo ci){
    }

    public void tryUseTotem(DamageSource source, Entity me, CallbackInfoReturnable<Boolean> cir){
    }

}


/*
what I need for entities:
1) ways to identify its multiblock instance -> UUID
2) ways to get the AbsMbStrucEnt instance from the Entity instance
3) ways to get the multiblock from the AbsMbStrucEnt instance

constructor for AbsMbStrucEnt -> data used for create()


*/