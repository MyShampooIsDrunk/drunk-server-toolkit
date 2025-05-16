package myshampooisdrunk.drunk_server_toolkit.example.multiblock;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.AbstractMultiblockStructureEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.util.Identifier;

public class MultiblockRegistryExample {

    public static final MultiblockStructure EXAMPLE_STRUCTURE = MultiblockRegistry.register(new ExampleMultiblock(Identifier.of("example", "structure")));
//    public static final AbstractMultiblockStructureEntity<WardenEntity> EXAMPLE_ENTITY = MultiblockRegistry.registerEntity(new ExampleMultiblockEntity("example_entity"));

    public static void init() {
    }
}
