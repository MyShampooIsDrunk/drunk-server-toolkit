package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockEntityData;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

public abstract class MultiblockEntity<E extends Entity, T extends MultiblockEntity<E,T>> {
    protected final MultiblockEntityType<E, T> type;
    protected final World world;
    protected UUID coreUuid;
    protected final E entity;

//    public MultiblockEntity(MultiblockEntityType<E, T> type, World world) {
//        this.type = type;
//        this.world = world;
//        this.entity = type.getEntityType().create(world, SpawnReason.MOB_SUMMONED);
//    }

    public MultiblockEntity(MultiblockEntityType<E, T> type, World world, E entity) {
        this.entity = entity == null ? type.getEntityType().create(world, SpawnReason.MOB_SUMMONED) : entity;
        this.type = type;
        this.world = world;
    }
//
//    @SuppressWarnings("unchecked")
//    public MultiblockEntity(E entity) {
//        MultiblockEntityData data = entity.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
//        this.type = (MultiblockEntityType<E, T>) data.getType();
//        this.entity = entity;
//        this.world = entity.getWorld();
//    }

    public E getEntity() {
        return entity;
    }

    public World getWorld() {
        return world;
    }

    public final UUID getUuid() {
        return entity.getUuid();
    }

    public final UUID getCoreUuid() {
        return coreUuid;
    }

    public void onRemoved(Entity.RemovalReason reason, CallbackInfo ci) {
    }

    public void remove() {
//        DST.LOGGER.info("REMOVING {} | UUID: {} | ENTIY:{}", this.type.toString(), this.getUuid(), this.entity);
        entity.discard();
//        if ( !((MultiblockCacheI) world).drunk_server_toolkit$unloadMultiblockEntity(this.getUuid()) ) {4
//            DST.LOGGER.warn("ENTITY WITH UUID {} WAS UNABLE TO BE DISCARDED", this.getUuid());
//        }
    }

    public MultiblockEntityType<E, T> getType() {
        return type;
    }

    public void initializeFromData(E entity) {
//        this.entity = entity;
        MultiblockEntityData data = this.entity.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
        this.coreUuid = data.getCoreUuid();
//        DST.LOGGER.info("this uuid: {} | entity uuid: {}", this.entity.getUuidAsString(), this.entity.getUuidAsString());
//        ((MultiblockCacheI) world).drunk_server_toolkit$linkEntity(coreUuid, this.getUuid());

    }

    @SuppressWarnings("unchecked")
    public E create(MultiblockStructure structure, BlockPos center, Vec3d relative, SpawnReason reason) {
        if (world.isClient()) return null;

        if(this.type != MultiblockRegistry.CORE_ENTITY && this.coreUuid == null) this.coreUuid = structure.getCore().getUuid();
        else this.coreUuid = getUuid();

        Vec3d spawnPos = relative.add(new Vec3d(center));

//        double d;
        entity.setPosition(spawnPos);
        entity.setInvulnerable(true);
        if(entity instanceof LivingEntity living) {
            living.setNoGravity(true);
        }
        if(entity instanceof MobEntity mob) {
            mob.setPersistent();
            mob.setAiDisabled(true);
        }
//        d = getOriginY(world, spawnPos, entity.getBoundingBox());

//        entity.refreshPositionAndAngles(spawnPos.getX() + 0.5, spawnPos.getY() + d, spawnPos.getZ() + 0.5, 0, 0);
        entity.refreshPositionAndAngles(spawnPos, 0, 0);

        MultiblockEntityData data = entity.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);

        data.initialize(this);

        ((MultiblockCacheI) world).drunk_server_toolkit$loadMultiblockEntity((T) this);
        ((MultiblockCacheI) world).drunk_server_toolkit$linkEntity(coreUuid, this.getUuid());

        return entity;
    }

    protected static double getOriginY(WorldView world, Vec3d pos, Box boundingBox) {
        Box box = new Box(pos.subtract(0.5), pos.add(0.5));

        Iterable<VoxelShape> iterable = world.getCollisions(null, box);
        return 1.0 + VoxelShapes.calculateMaxOffset(Direction.Axis.Y, boundingBox, iterable, -1.0);
    }

    public void onInteract(PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir){
    }

    public void onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
    }

    //this will be called by the mixin
    //to implement custom tick code, you should override
    //super.tick(me, ci) if you want to cancel entity.tick() but not entity.baseTick()
    //tick(true, me, ci) if you want to cancel entity.tick() and entity.baseTick()
    //   alternatively just use ci.cancel()
    public void tick(CallbackInfo ci) {
        tick(false, ci);
    }

    public final void tick(boolean cancelBaseTick, CallbackInfo ci) {
        ci.cancel();
        if(!cancelBaseTick) entity.baseTick();
    }

    public void onDamaged(DamageSource damageSource, CallbackInfo ci) {
    }

    public void onBlockCollision(BlockState state, CallbackInfo ci) {
    }

    public void onStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
    }

    //cir.setReturnValue(true) to not tick damage
    public void handleAttack(Entity attacker, CallbackInfoReturnable<Boolean> cir) {
    }

    public void interactAt(PlayerEntity player, Vec3d hitPos, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    }

    //LivingEntity
    public void damage(ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
    }

    public void takeShieldHit(ServerWorld world, LivingEntity attacker, CallbackInfo ci){
    }

    public void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cir){
    }

    public static <E extends Entity, T extends MultiblockEntity<E,T>> void spawnEntity(MultiblockEntity<E,T> entity, MultiblockStructure structure,
                                                                                       BlockPos center, Vec3d relative, @Nullable SpawnReason reason) {
        if(entity.world instanceof ServerWorld sw)
            sw.spawnEntityAndPassengers(entity.create(structure, center, relative, reason == null ? SpawnReason.MOB_SUMMONED : reason));
    }
    public static <E extends Entity, T extends MultiblockEntity<E,T>> void spawnEntity(MultiblockEntity<E,T> entity, MultiblockStructure structure,
                                                                                       BlockPos center, Vec3d relative) {
        spawnEntity(entity, structure, center, relative, null);
    }
}