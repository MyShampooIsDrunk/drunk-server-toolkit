package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import myshampooisdrunk.drunk_server_toolkit.component.MultiblockData;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AbstractMultiblockStructureEntity<E extends Entity> {
    protected final Identifier id;
    protected final EntityType<E> type;

    public AbstractMultiblockStructureEntity(EntityType<E> type, Identifier id){
        this.id = id;
        this.type = type;
    }

    public Identifier id(){
        return id;
    }

    public E create(World world, MultiblockStructure structure, BlockPos center, Vec3d relative){
        Vec3d spawnPos = relative.add(center.toCenterPos());
        E entity = this.type.create(world);
        assert entity != null;

        NbtCompound nbt = new NbtCompound();
        nbt.putString("MultiblockId", id.toString()); //used to figure out which interactions it does
        nbt.putString("MultiblocKUUID", structure.getUUID().toString()); //used to figure out which instance of a multiblock it's part of
        ((MultiblockData)entity).setMultiblock(structure);

        entity.setPos(spawnPos.x, spawnPos.y, spawnPos.z);

        entity.setInvisible(true);
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

    public void onUse(){
    }

    public void onEntityCollision(Entity entity){
    }
}
