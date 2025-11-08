package myshampooisdrunk.drunk_server_toolkit.mixin;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockCoreData;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockEntityData;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntityType;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.*;

@Mixin(World.class)
public class WorldMixin implements MultiblockCacheI {

    @Unique private final Map<UUID, MultiblockStructure> loadedStructures = new HashMap<>(); //core entity UUID -> structure object
    @Unique private final Multimap<UUID, UUID> linkedEntities = HashMultimap.create();
    @Unique private final Multimap<BlockPos, UUID> linkedBlocks = HashMultimap.create();
    @Unique private final Map<UUID, MultiblockEntity<?,?>> loadedEntities = new HashMap<>(); //any entity UUID -> MBEntity object

    @Override
    public void drunk_server_toolkit$addMultiblock(MultiblockCoreEntity core) {
        MultiblockStructure structure = core.getStructureType().loadFromCore(core);
        loadedStructures.put(core.getUuid(), structure);
    }

    @Override
    public void drunk_server_toolkit$spawnMultiblock(BlockPos pos, MultiblockCoreEntity core, MultiblockStructure structure) {
        loadedStructures.put(core.getUuid(), structure);
        linkedBlocks.put(pos, core.getUuid());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <S extends MultiblockStructure> S drunk_server_toolkit$getStructure(UUID uuid) {
        return (S) loadedStructures.getOrDefault(uuid, null);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity, T extends MultiblockEntity<E, T>> T drunk_server_toolkit$loadMultiblockEntity(E e) {
        MultiblockEntityData data = e.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
        if(data.getCoreUuid() == null || data.getType() == null || data.getType().getEntityType() != e.getType()) return null;
        if(!loadedEntities.containsKey(e.getUuid())) {
            T ret = ((MultiblockEntityType<E,T>) data.getType()).createFromEntity(e);
            drunk_server_toolkit$loadMultiblockEntity(ret);
            if(ret instanceof MultiblockCoreEntity core) {
//                DST.LOGGER.info("THIS IS CORE; UUID: {}", core.getUuid());
                drunk_server_toolkit$addMultiblock(core);
                linkedBlocks.put(e.getBlockPos(), core.getUuid());
            } else {
//                DST.LOGGER.info("CORE UUID: {}", data.getCoreUuid());
                linkedEntities.put(data.getCoreUuid(), e.getUuid());
            }
            return ret;
        } else {
//            DST.LOGGER.info("MULTIBLOCK ENTITY WITH UUID {} ALREADY LOADED", e.getUuidAsString());
            return (T) loadedEntities.get(e.getUuid());
        }
    }

    @Override
    public <E extends Entity, T extends MultiblockEntity<E, T>> void drunk_server_toolkit$loadMultiblockEntity(T t) {
        loadedEntities.put(t.getUuid(), t);
    }

    @Override
    public boolean drunk_server_toolkit$unloadMultiblockEntity(UUID uuid) {
        if(!loadedEntities.containsKey(uuid)) {
//            DST.LOGGER.info("UNABLE TO UNLOAD ENTITY WITH UUID {}", uuid);
            return false;
        }
//        DST.LOGGER.info("UNLOADING ENTITY WITH UUID {}", uuid);
        loadedEntities.remove(uuid);
        if(loadedStructures.containsKey(uuid)) drunk_server_toolkit$unloadMultiblockStructure(uuid);
        return true;
    }

    @Override
    public boolean drunk_server_toolkit$unloadMultiblockStructure(UUID uuid) {
        if(!loadedStructures.containsKey(uuid)) {
//            DST.LOGGER.info("UNABLE TO UNLOAD STRUCTURE WITH UUID {}", uuid);
            return false;
        }
//        DST.LOGGER.info("UNLOADING STRUCTURE WITH UUID {}", uuid);
        linkedBlocks.removeAll(loadedStructures.get(uuid).getCore().getEntity().getBlockPos());
        loadedStructures.remove(uuid);
        linkedEntities.removeAll(uuid);
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends Entity, T extends MultiblockEntity<E, T>> T drunk_server_toolkit$getMultiblockEntity(UUID uuid) {
        return (T) loadedEntities.getOrDefault(uuid, null);
    }

    @Override
    public boolean drunk_server_toolkit$containsUuid(UUID uuid) {
        return loadedEntities.containsKey(uuid);
    }

    @Override
    public Collection<UUID> drunk_server_toolkit$getLinkedEntities(UUID uuid) {
        return linkedEntities.get(uuid);
    }

    @Override
    public boolean drunk_server_toolkit$linkEntity(UUID core, UUID entity) {
//        if(!loadedStructures.containsKey(core)) {
//            return false;
//        }
        linkedEntities.put(core, entity);
        return true;
    }

    @Override
    public Collection<UUID> drunk_server_toolkit$listMultiblockEntities() {
        return loadedEntities.keySet();
    }

    @Override
    public Collection<UUID> drunk_server_toolkit$listMultiblocks() {
        return loadedStructures.keySet();
    }

    @Override
    public Collection<UUID> drunk_server_toolkit$getStructuresAtBlock(BlockPos pos) {
        return linkedBlocks.get(pos);
    }
}
/*
MBE ->
- multiblock UUID
- custom data
*/