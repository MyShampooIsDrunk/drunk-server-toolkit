package myshampooisdrunk.drunk_server_toolkit.example.multiblock;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockHitboxEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.hitbox.SolidHitboxGenerator;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructureType;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ExampleMultiblock extends MultiblockStructure {

    public ExampleMultiblock(MultiblockStructureType<?> type, MultiblockCoreEntity core) {
        super(type, core);
    }

    @Override
    public boolean spawnStructure(BlockPos pos) {
        boolean ret = super.spawnStructure(pos);
        if(ret) {
            ExampleMultiblockEntity ex = MultiblockRegistryExample.EXAMPLE_ENTITY.create(world);
            assert ex != null;
            MultiblockEntity.spawnEntity(ex, this, pos, new Vec3d(3, 3, 3));

            MultiblockHitboxEntity.SolidHitboxEntity solid = MultiblockRegistry.SOLID_HITBOX_ENTITY.create(world);
            assert solid != null;
            MultiblockEntity.spawnEntity(solid, this, pos, new Vec3d(1.1, 1.1, 1.1));

            MultiblockHitboxEntity.BlockHitboxEntity liquid = MultiblockRegistry.BLOCK_HITBOX_ENTITY.create(world);
            assert liquid != null;
            liquid.setBlock(Blocks.ACACIA_STAIRS.getDefaultState());
            MultiblockEntity.spawnEntity(liquid, this, pos, new Vec3d(1.1, 1.1, 1.1));

            SolidHitboxGenerator generator = new SolidHitboxGenerator.Builder().add(
                    new Box(0,0,0,1,1,1), Blocks.GOLD_BLOCK.getDefaultState()
            ).add(new Box(0,0,0,-1.2,2,2),Blocks.EMERALD_BLOCK.getDefaultState()).add(
                    new Box(2,2,2,3,3,3), Blocks.DIAMOND_BLOCK.getDefaultState()).add(
                    new Box(2,2,2,1.8,4,4), Blocks.NETHERITE_BLOCK.getDefaultState()
            ).build();

            generator.get(world, new Vec3d(0.8,-5,3)).forEach((vec, ents) -> {
                for (MultiblockHitboxEntity<?, ?> e : ents) {
                    MultiblockEntity.spawnEntity(e, this, pos, vec);
                }
            });
        }

        return ret;
    }
}
