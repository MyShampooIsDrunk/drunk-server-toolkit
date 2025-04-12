package myshampooisdrunk.drunk_server_toolkit.multiblock.structure;

import myshampooisdrunk.drunk_server_toolkit.component.MultiblockData;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.AbstractMultiblockStructureEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypeFilter;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class MultiblockStructure {
    private final HashMap<BlockPos, Block> blockList;
    private final HashMap<BlockPos, TagKey<Block>> blockTagList;
    private final HashMap<Vec3d, AbstractMultiblockStructureEntity<?>> entityList;
    private final UUID multiblockUUID;
    private Box structureBox; //used to detect the multiblock and remove it
    private final Identifier id;

    public MultiblockStructure(Identifier id) {
        blockList = new HashMap<>();
        blockTagList = new HashMap<>();
        entityList = new HashMap<>();
        multiblockUUID = UUID.randomUUID();
        structureBox = Box.of(Vec3d.ZERO,0,0,0);
        this.id = id;
        this.attachEntity(Vec3d.ZERO, new MultiblockCoreEntity(id));
    }

    public UUID getUUID(){
        return multiblockUUID;
    }

    public Identifier id() {
        return id;
    }

    public Box relativeBox() {
        return structureBox;
    }

    public MultiblockStructure addBlock(BlockPos relative, Block block) {
        blockList.put(relative,block);
        structureBox = structureBox
                .withMaxX(Math.max(structureBox.maxX,relative.getX()))
                .withMaxY(Math.max(structureBox.maxY,relative.getY()))
                .withMaxZ(Math.max(structureBox.maxZ,relative.getZ()))
                .withMinX(Math.min(structureBox.minX,relative.getX()))
                .withMinY(Math.min(structureBox.minY,relative.getY()))
                .withMinZ(Math.min(structureBox.minZ,relative.getZ()));
        return this;
    }

    public MultiblockStructure addBlock(BlockPos relative, TagKey<Block> block) {
        blockTagList.put(relative,block);
        structureBox = structureBox
                .withMaxX(Math.max(structureBox.maxX,relative.getX()))
                .withMaxY(Math.max(structureBox.maxY,relative.getY()))
                .withMaxZ(Math.max(structureBox.maxZ,relative.getZ()))
                .withMinX(Math.min(structureBox.minX,relative.getX()))
                .withMinY(Math.min(structureBox.minY,relative.getY()))
                .withMinZ(Math.min(structureBox.minZ,relative.getZ()));
        return this;
    }

    public MultiblockStructure attachEntity(Vec3d relative, AbstractMultiblockStructureEntity<?> entity) {
        entityList.put(relative,entity);
        return this;
    }

    public MultiblockStructure attachEntity(BlockPos relative, AbstractMultiblockStructureEntity<?> entity) {
        return attachEntity(new Vec3d(relative.getX(),relative.getY(),relative.getZ()), entity);
    }

    public boolean detect(World world, BlockPos center) {
        for(double x = structureBox.minX; x <= structureBox.maxX; x++){
            for(double y = structureBox.minY; y <= structureBox.maxY; y++){
                for(double z = structureBox.minZ; z <= structureBox.maxZ; z++){
                    //theoretically casting could be dangerous but we only ever add integers soooo should be fine
                    BlockPos pos = center.add((int)x, (int)y, (int)z);
                    BlockState block = world.getBlockState(pos);
                    if(!block.isAir() && !block.isOf(blockList.get(pos)) && !block.isIn(blockTagList.get(pos)))
                        return false;
                }
            }
        }
        return true;
    }

    public void generate(World world, BlockPos pos) {
        entityList.forEach((v,e) -> world.spawnEntity(e.create(world, this, pos, v)));
    }

    public void remove(World world, BlockPos pos) {
        List<? extends Class<? extends Entity>> types = entityList.values().stream()
                .map(AbstractMultiblockStructureEntity::getType).map(EntityType::getBaseClass).toList();
        List<Entity> entities = new ArrayList<>();
        types.forEach(t -> world.getEntitiesByType(
                TypeFilter.equals(t),
                structureBox.offset(pos),
                e->this.multiblockUUID.equals(((MultiblockData)e).getUUID()))
        );
    }

}
