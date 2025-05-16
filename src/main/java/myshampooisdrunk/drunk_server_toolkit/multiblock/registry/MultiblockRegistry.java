package myshampooisdrunk.drunk_server_toolkit.multiblock.registry;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.AbstractMultiblockStructureEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntityType;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MultiblockRegistry {
    public static final Map<Identifier, MultiblockStructure> STRUCTURES = new HashMap<>();
    public static final Map<Block, Set<Identifier>> BLOCKS = new HashMap<>();
//    public static final Map<String, AbstractMultiblockStructureEntity<?>> MULTIBLOCK_ENTITIES = new HashMap<>();
    public static final Map<String, MultiblockEntityType<?>> ENTITY_TYPES = new HashMap<>();

    public static MultiblockStructure register(MultiblockStructure structure){
        Identifier id = structure.getId();
        STRUCTURES.put(id,structure);
        for (Block block : structure.getControllerBlocks()) {
            if(!BLOCKS.containsKey(block)) BLOCKS.put(block, new HashSet<>());
            BLOCKS.get(block).add(id);
        }
        return structure;
    }

    public static <E extends Entity> void registerEntityType (MultiblockEntityType<E> type) {
        if(!ENTITY_TYPES.containsKey(type.getId()))
            ENTITY_TYPES.put(type.getId(),type);
    }

//    public static <E extends Entity> AbstractMultiblockStructureEntity<E> registerEntity(AbstractMultiblockStructureEntity<E> entity) {
//        MULTIBLOCK_ENTITIES.put(entity.id(),entity);
//        return entity;
//    }
}
