package myshampooisdrunk.drunk_server_toolkit.example.multiblock;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class ExampleMultiblock extends MultiblockStructure {
    public ExampleMultiblock(Identifier id) {
        super(id, Set.of(Blocks.BLAST_FURNACE));
        this.addBlock(0,-4,0, Blocks.SLIME_BLOCK);
        this.addBlock(0,-3,0, Blocks.COBBLESTONE);
        this.addBlock(0,-2,0, BlockTags.ACACIA_LOGS);
        this.addBlock(0,-1,0, Blocks.DIAMOND_BLOCK);
        this.attachEntity(2,2,2, new ExampleMultiblockEntity("examplemf"));
        this.setCore(new MultiblockCoreEntity("core", Items.ACACIA_BOAT.getDefaultStack()));
        // idea --> instead of registering the entity in a list, you register the entity type in a list
        // save the entity type in ccapi nbt and use that entity type to detect which
    }
}
