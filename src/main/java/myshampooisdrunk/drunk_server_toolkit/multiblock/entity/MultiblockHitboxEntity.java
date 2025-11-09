package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.mixin.ArmorStandEntityInvoker;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

public abstract class MultiblockHitboxEntity<E extends Entity, T extends MultiblockHitboxEntity<E,T>> extends MultiblockEntity<E,T> {

    public MultiblockHitboxEntity(MultiblockEntityType<E, T> type, World world, E entity) {
        super(type, world, entity);
    }

    @Override
    public void handleAttack(Entity attacker, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false);
    }

    @Override
    public void onInteract(PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        cir.setReturnValue(ActionResult.PASS);
    }

    abstract public void setSize(float size);

    public static class BlockHitboxEntity extends MultiblockHitboxEntity<DisplayEntity.BlockDisplayEntity, BlockHitboxEntity> {

        public BlockHitboxEntity(MultiblockEntityType<DisplayEntity.BlockDisplayEntity, BlockHitboxEntity> type, World world,
                                 DisplayEntity.BlockDisplayEntity entity) {
            super(type, world, entity);
        }

        @Override
        public void setSize(float size) {
            this.entity.setTransformation(new AffineTransformation(new Vector3f(), new Quaternionf(),
                    new Vector3f(size,size,size), new Quaternionf()));
        }

        public void setBlock(BlockState state) {
            this.entity.setBlockState(state);
        }

        @Override
        public DisplayEntity.BlockDisplayEntity create(MultiblockStructure structure, BlockPos center, Vec3d relative, SpawnReason reason) {
            DST.LOGGER.info("created a block display ts vro");
            return super.create(structure, center, relative, reason);
        }
    }

    public static class SolidHitboxEntity extends MultiblockHitboxEntity<ArmorStandEntity, SolidHitboxEntity> {

        private final ShulkerEntity rider;

        public SolidHitboxEntity(MultiblockEntityType<ArmorStandEntity, SolidHitboxEntity> type, World world, ArmorStandEntity entity) {
            super(type, world, entity);
            if(this.entity.hasPassengers() && this.entity.getFirstPassenger() instanceof ShulkerEntity s) rider = s;
            else rider = EntityType.SHULKER.create(world, SpawnReason.MOB_SUMMONED);
        }

        @Override
        public void setSize(float size) {
            if(size <= 0) {
                DST.LOGGER.info("SIZE CANNOT BE LESS THAN ZERO");
                return;
            }
            if(size != 1)
                Objects.requireNonNull(rider.getAttributes().getCustomInstance(EntityAttributes.SCALE)).setBaseValue(size);
        }

        @Override
        public void initializeFromData(ArmorStandEntity entity) {
            super.initializeFromData(entity);
//            if(entity.getFirstPassenger() instanceof ShulkerEntity s) this.rider = s;
//            ((MultiblockCacheI) world).drunk_server_toolkit$linkEntity(coreUuid, rider.getUuid());
        }

        @Override
        public ArmorStandEntity create(MultiblockStructure structure, BlockPos center, Vec3d relative, SpawnReason reason) {
            double size = rider.getAttributeValue(EntityAttributes.SCALE);
            super.create(structure, center, relative.add(size/2,0,size/2), reason);
            entity.setHideBasePlate(true);
            entity.setInvisible(true);
            ((ArmorStandEntityInvoker) entity).invokeSetMarker(true);

            rider.setInvisible(true);
            rider.setNoGravity(true);
            rider.setInvulnerable(true);
            rider.setSilent(true);
            rider.setAiDisabled(true);
            rider.setPersistent();
            rider.refreshPositionAndAngles(center,0,0);
            rider.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, -1, 0, false, false));
            rider.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, -1, 5, false, false));
            rider.startRiding(entity, true, false);

            return entity;
        }

        @Override
        public void remove() {
            rider.discard();
            super.remove();
        }
    }
}
