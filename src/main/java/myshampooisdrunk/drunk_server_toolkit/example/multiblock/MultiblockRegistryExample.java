package myshampooisdrunk.drunk_server_toolkit.example.multiblock;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntityType;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructureType;
import myshampooisdrunk.drunk_server_toolkit.util.BlockUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.Identifier;

public class MultiblockRegistryExample {

    public static final MultiblockEntityType<WardenEntity, ExampleMultiblockEntity> EXAMPLE_ENTITY = MultiblockEntityType
            .register("example", MultiblockEntityType.Builder.create(ExampleMultiblockEntity::new, EntityType.WARDEN));

    public static final MultiblockStructureType<ExampleMultiblock> EXAMPLE_MULTIBLOCK = MultiblockStructureType.create("example",
            ExampleMultiblock::new,
            MultiblockStructure.Template.builder().transformations(MultiblockStructure.EAST)
                    .add(0,-1,0, 'a', BlockUtil.simpleLookup(Blocks.NETHERITE_BLOCK)).build(),
            BlockUtil.simpleLookup(Blocks.ENDER_CHEST));
//    public static final MultiblockStructure EXAMPLE_STRUCTURE = MultiblockRegistry.registry(new ExampleMultiblock(Identifier.of("example", "structure")));
//    public static final AbstractMultiblockStructureEntity<WardenEntity> EXAMPLE_ENTITY = MultiblockRegistry.registerEntity(new ExampleMultiblockEntity("example_entity"));

    public static void init() {
    }
}
