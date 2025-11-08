package myshampooisdrunk.drunk_server_toolkit.multiblock.registry;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockHitboxEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntityType;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructureType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

import java.util.HashSet;
import java.util.Set;

public class MultiblockRegistry {
//    public static final Map<Identifier, MultiblockStructure> STRUCTURES = new HashMap<>();
//    public static final Map<Block, Set<Identifier>> BLOCKS = new HashMap<>();
//    public static final Map<String, AbstractMultiblockStructureEntity<?>> MULTIBLOCK_ENTITIES = new HashMap<>();
    public static final BiMap<String, MultiblockEntityType<? extends Entity, ? extends MultiblockEntity<?, ?>>> ENTITY_TYPES = HashBiMap.create();
    public static final BiMap<String, MultiblockStructureType<?>> STRUCTURE_TYPES = HashBiMap.create();

    public static final MultiblockEntityType<MarkerEntity, MultiblockCoreEntity> CORE_ENTITY = MultiblockEntityType.register("core",
            MultiblockEntityType.Builder.create(MultiblockCoreEntity::new, EntityType.MARKER));

    public static final MultiblockEntityType<DisplayEntity.BlockDisplayEntity, MultiblockHitboxEntity.BlockHitboxEntity> BLOCK_HITBOX_ENTITY
            = MultiblockEntityType.register("block_hitbox", MultiblockEntityType.Builder.create(MultiblockHitboxEntity.BlockHitboxEntity::new, EntityType.BLOCK_DISPLAY));

    public static final MultiblockEntityType<ArmorStandEntity, MultiblockHitboxEntity.SolidHitboxEntity> SOLID_HITBOX_ENTITY
            = MultiblockEntityType.register("solid_hitbox", MultiblockEntityType.Builder.create(MultiblockHitboxEntity.SolidHitboxEntity::new, EntityType.ARMOR_STAND));

//    public static MultiblockStructure registry(MultiblockStructure structure){
//        Identifier id = structure.getId();
//        STRUCTURES.put(id,structure);
//        for (Block block : structure.getControllerBlocks()) {
//            if(!BLOCKS.containsKey(block)) BLOCKS.put(block, new HashSet<>());
//            BLOCKS.get(block).add(id);
//        }
//        return structure;
//    }

//    public static <E extends Entity> AbstractMultiblockStructureEntity<E> registerEntity(AbstractMultiblockStructureEntity<E> entity) {
//        MULTIBLOCK_ENTITIES.put(entity.id(),entity);
//        return entity;
//    }

    public static void init(){}

    public static Set<MultiblockStructureType<?>> getPotentialMultiblocks(BlockPos pos, BlockView world) {
        Set<MultiblockStructureType<?>> ret = new HashSet<>();

        BlockState state = world.getBlockState(pos);
        for (MultiblockStructureType<?> type : STRUCTURE_TYPES.values()) {
            if(type.getControllers().test(state)) ret.add(type);
        }

        return ret;
    }
}
