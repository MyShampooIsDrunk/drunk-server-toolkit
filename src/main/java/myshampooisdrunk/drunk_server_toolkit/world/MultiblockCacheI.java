package myshampooisdrunk.drunk_server_toolkit.world;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;
import java.util.UUID;

public interface MultiblockCacheI {

    /**
    caches multiblock in respect to UUID stored within core entity
    called when core entity is loaded
    */
    void drunk_server_toolkit$addMultiblock(MultiblockCoreEntity core);

    void drunk_server_toolkit$spawnMultiblock(BlockPos pos, MultiblockCoreEntity core, MultiblockStructure structure);

    /**
    gets structure based on id
    used when doing structure-based functions (eg. crafting based on structure inventory, adding stuff to structure inventory, etc.)
    */
    <S extends MultiblockStructure> S drunk_server_toolkit$getStructure(UUID uuid);

    /**
    loads multiblock entity to cache based on entity data
    called when any entity with specific ccapi data is loaded
    calls addMultiblock when a coreentity is loaded
    */
    <E extends Entity, T extends MultiblockEntity<E, T>> T drunk_server_toolkit$loadMultiblockEntity(E e);

    /**
     <p>loads new multiblock entity.</p>
     should only really be called in {@link MultiblockEntity#spawnEntity(MultiblockEntity, MultiblockStructure, BlockPos, Vec3d, SpawnReason)} tbh
    */
    <E extends Entity, T extends MultiblockEntity<E, T>> void drunk_server_toolkit$loadMultiblockEntity(T t);

    /**
     unloads multiblockentities to save ram
     called when an entity with specific ccapidata is unloaded
     calls unloadMultiblock when a coreentity is unloaded
     removes a multiblock entity from hashmap
     also called in {@link MultiblockEntity#remove}

    true -> unloaded entity successfully
    false -> couldnt unload idk (log warning?)
    */
    boolean drunk_server_toolkit$unloadMultiblockEntity(UUID uuid);

    boolean drunk_server_toolkit$unloadMultiblockStructure(UUID uuid);

     /**

     */
    <E extends Entity, T extends MultiblockEntity<E,T>> T drunk_server_toolkit$getMultiblockEntity(UUID uuid);

    Collection<UUID> drunk_server_toolkit$getLinkedEntities(UUID uuid);

    boolean drunk_server_toolkit$containsUuid(UUID uuid);

    boolean drunk_server_toolkit$linkEntity(UUID core, UUID entity);

    Collection<UUID> drunk_server_toolkit$listMultiblockEntities();

    Collection<UUID> drunk_server_toolkit$listMultiblocks();

    Collection<UUID> drunk_server_toolkit$getStructuresAtBlock(BlockPos pos);
}
