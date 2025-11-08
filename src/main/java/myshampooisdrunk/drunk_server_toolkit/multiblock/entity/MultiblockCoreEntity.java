package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockCoreData;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructureType;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class MultiblockCoreEntity extends MultiblockEntity<MarkerEntity, MultiblockCoreEntity> {
    private MultiblockStructureType<?> structureType;

    public MultiblockCoreEntity(MultiblockEntityType<MarkerEntity, MultiblockCoreEntity> type, World world, MarkerEntity entity) {
        super(type, world, entity);
    }

    public MultiblockStructureType<?> getStructureType() {
        return structureType;
    }

    @Override
    public void initializeFromData(MarkerEntity entity) {
        super.initializeFromData(entity);
        this.structureType = entity.getComponent(DST.MULTIBLOCK_CORE_DATA_COMPONENT_KEY).getStructureType();
    }

    public void removeMultiblock() {
//        DST.LOGGER.info("REMOVING CORE");
        for (UUID uuid : ((MultiblockCacheI) world).drunk_server_toolkit$getLinkedEntities(this.getUuid())) {
            if(uuid == this.getUuid()) continue;
//            DST.LOGGER.info("GOODBYE ENTITY {}", uuid);
            ((MultiblockCacheI) world).drunk_server_toolkit$getMultiblockEntity(uuid).remove();
//            ((MultiblockCacheI) world).drunk_server_toolkit$unloadMultiblockEntity(uuid);
        }

        this.remove();
        ((MultiblockCacheI) world).drunk_server_toolkit$unloadMultiblockStructure(this.getUuid());
    }

    @Override
    public MarkerEntity create(MultiblockStructure structure, BlockPos pos, Vec3d relative, SpawnReason reason) {
        this.structureType = structure.getType();
        super.create(structure, pos, relative, reason);
        MultiblockCoreData data = entity.getComponent(DST.MULTIBLOCK_CORE_DATA_COMPONENT_KEY);
        data.initialize(structure);
        return entity;
    }
}