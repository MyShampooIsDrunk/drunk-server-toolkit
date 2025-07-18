package myshampooisdrunk.drunk_server_toolkit.multiblock.structure;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockCoreData;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.AbstractMultiblockStructureEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.*;

public class MultiblockStructure {
    private static final Block FILLER_BLOCK = Blocks.STRUCTURE_VOID;
    private final Map<BlockPos, Block> blockList;
    private final Map<BlockPos, TagKey<Block>> blockTagList;
    private final Map<AbstractMultiblockStructureEntity<?>, Vec3d> entityList;
    protected UUID multiblockUUID;
    protected Box structureBox; //used to detect the multiblock and remove it
    private final Identifier id;
    private final Set<Block> controllers;
    protected Box entityBox;

    public MultiblockStructure(Identifier id, Set<Block> controllers) {
        this.controllers = controllers;
        blockList = new HashMap<>();
        blockTagList = new HashMap<>();
        entityList = new HashMap<>();
        multiblockUUID = UUID.randomUUID();
        structureBox = Box.of(Vec3d.ZERO,0,0,0);
        entityBox = new Box(Vec3d.ZERO, Vec3d.ZERO);
        this.id = id;
    }

    public MultiblockStructure(Identifier id, Block controller) {
        controllers = new HashSet<>();
        controllers.add(controller);
        blockList = new HashMap<>();
        blockTagList = new HashMap<>();
        entityList = new HashMap<>();
        multiblockUUID = UUID.randomUUID();
        structureBox = Box.of(Vec3d.ZERO,0,0,0);
        entityBox = new Box(Vec3d.ZERO, Vec3d.ZERO);
        this.id = id;
    }

    public MultiblockStructure setCore(MultiblockCoreEntity core){
        this.attachEntity(new Vec3d(0,1,0), core);
        return this;
    }

    protected void addController(Block controller) {
        controllers.add(controller);
    }

    public Collection<Block> getControllerBlocks() {
        return controllers;
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
        if(relative.equals(new BlockPos(0,0,0))) return this;
        blockList.put(relative,block);
        if (!structureBox.contains(relative.getX(), relative.getY(), relative.getZ()))
            structureBox = new Box(Math.max(structureBox.maxX,relative.getX()),Math.max(structureBox.maxY,relative.getY()),
                    Math.max(structureBox.maxZ,relative.getZ()),Math.min(structureBox.minX,relative.getX()),
                    Math.min(structureBox.minY,relative.getY()),Math.min(structureBox.minZ,relative.getZ()));
        return this;
    }

    public MultiblockStructure addBlock(BlockPos relative, TagKey<Block> tag) {
        if(relative.equals(new BlockPos(0,0,0))) return this;
        blockTagList.put(relative,tag);
        if (!structureBox.contains(relative.getX(), relative.getY(), relative.getZ()))
            structureBox = new Box(Math.max(structureBox.maxX,relative.getX()),Math.max(structureBox.maxY,relative.getY()),
                    Math.max(structureBox.maxZ,relative.getZ()),Math.min(structureBox.minX,relative.getX()),
                    Math.min(structureBox.minY,relative.getY()),Math.min(structureBox.minZ,relative.getZ()));
        return this;
    }

    public MultiblockStructure addBlock(int x, int y, int z, Block block) {
        return this.addBlock(new BlockPos(x,y,z), block);
    }

    public MultiblockStructure addBlock(int x, int y, int z, TagKey<Block> tag) {
        return this.addBlock(new BlockPos(x,y,z), tag);
    }

    public MultiblockStructure attachEntity(Vec3d relative, AbstractMultiblockStructureEntity<?> entity) {
        if (!entityBox.contains(relative))
            entityBox = new Box(
                    Math.max(entityBox.maxX,relative.x),Math.max(entityBox.maxY,relative.y), Math.max(entityBox.maxZ,relative.z),
                    Math.min(entityBox.minX,relative.x), Math.min(entityBox.minY,relative.y),Math.min(entityBox.minZ,relative.z));
        entityList.put(entity, relative);
        return this;
    }

    public MultiblockStructure attachEntity(BlockPos relative, AbstractMultiblockStructureEntity<?> entity) {
        return attachEntity(new Vec3d(relative.getX(),relative.getY(),relative.getZ()), entity);
    }

    public MultiblockStructure attachEntity(double x, double y, double z, AbstractMultiblockStructureEntity<?> entity) {
        return attachEntity(new Vec3d(x, y, z), entity);
    }

    public boolean detect(ServerWorld world, BlockPos center) {
        for(double x = structureBox.minX; x <= structureBox.maxX; x++){
            for(double y = structureBox.minY; y <= structureBox.maxY; y++){
                for(double z = structureBox.minZ; z <= structureBox.maxZ; z++){
                    if(x == 0 && y == 0 && z == 0) continue;
                    BlockPos offset = new BlockPos((int)x,(int)y,(int)z);
                    BlockPos pos = center.add(offset);
                    BlockState block = world.getBlockState(pos);
                    if(blockList.containsKey(offset) || blockTagList.containsKey(offset)) {
                        boolean bl = false;
                        if(blockList.containsKey(offset)) {
                            bl |= block.isOf(blockList.get(offset));
                        }
                        if(blockTagList.containsKey(offset)) {
                            bl |= block.isIn(blockTagList.get(offset));
                        }
                        if(!bl) return false;
                    } else if(!block.isAir()) return false;
                }
            }
        }
        return true;
    }

    public boolean detectBuiltMultiblock(ServerWorld world, BlockPos center) {
        for(double x = structureBox.minX; x <= structureBox.maxX; x++){
            for(double y = structureBox.minY; y <= structureBox.maxY; y++){
                for(double z = structureBox.minZ; z <= structureBox.maxZ; z++){
                    if(x == 0 && y == 0 && z == 0) continue;
                    BlockPos offset = new BlockPos((int)x,(int)y,(int)z);
                    BlockPos pos = center.add(offset);
                    BlockState block = world.getBlockState(pos);
                    if(blockList.containsKey(offset) || blockTagList.containsKey(offset)) {
                        if(!block.isOf(FILLER_BLOCK)) return false;
                    } else if(!block.isAir()) return false;
                }
            }
        }
        return true;
    }

    public void generate(ServerWorld world, BlockPos pos) {
        if(world.isClient) return;
        this.multiblockUUID = UUID.randomUUID();
        Set<BlockPos> posSet = new HashSet<>();
        posSet.addAll(blockTagList.keySet());
        posSet.addAll(blockList.keySet());
        Map<BlockPos, BlockState> initialStates = new HashMap<>();
        for (BlockPos relative : posSet) {
            BlockPos blockPos = relative.add(pos);
            initialStates.put(blockPos, world.getBlockState(blockPos));
            world.setBlockState(blockPos, FILLER_BLOCK.getDefaultState());
        }

        entityList.forEach((ent, vec) -> {
            Entity e;
            if(ent instanceof MultiblockCoreEntity core){
                DisplayEntity.ItemDisplayEntity coreEntity = core.create(world, this, pos, vec);
                MultiblockCoreData coreData = coreEntity.getComponent(DST.MULTIBLOCK_CORE_DATA_COMPONENT_KEY);
                coreData.setBlockstateData(initialStates);
                e = coreEntity;
            } else {
                e = ent.create(world, this, pos, vec);
            }
            boolean bl = world.spawnEntity(e);
        });

    }

    public void remove(ServerWorld world, BlockPos pos) {
        List<? extends EntityType<?>> types = entityList.keySet().stream()
                .map(AbstractMultiblockStructureEntity::getType).toList();

        List<Entity> entities = new ArrayList<>();
        UUID coreUUID = this.getCoreEntityUUID(world, pos);
        if (coreUUID == null) return;
//        System.out.println(coreUUID);
        types.forEach(t -> entities.addAll(world.getEntitiesByType(
                t,
                entityBox.offset(pos).expand(1),
                e -> coreUUID.equals(e.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY).getUUID())))
        );
        for (Entity e : entities) {
            if(e instanceof DisplayEntity.ItemDisplayEntity core) {
                MultiblockCoreData coreData = core.getComponent(DST.MULTIBLOCK_CORE_DATA_COMPONENT_KEY);
                coreData.getBlockstateData().forEach(world::setBlockState);
            }
            e.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    public Identifier getId(){
        return id;
    }

    public UUID getCoreEntityUUID(ServerWorld world, BlockPos pos) {
        List<? extends EntityType<?>> types = entityList.keySet().stream()
                .map(AbstractMultiblockStructureEntity::getType).toList();
        List<Entity> entities = new ArrayList<>();
        types.forEach(t -> entities.addAll(world.getEntitiesByType(
                t,
                entityBox.offset(pos).expand(1),
                e -> {
                    if(e instanceof DisplayEntity.ItemDisplayEntity core) {
                        MultiblockCoreData coreData = core.getComponent(DST.MULTIBLOCK_CORE_DATA_COMPONENT_KEY);
                        return !coreData.getBlockstateData().isEmpty();
                    }
                    return false;
                }))
        );
        entities.sort(Comparator.comparingDouble(t -> t.squaredDistanceTo(pos.toCenterPos())));
        if(entities.isEmpty()) return null;
        return entities.getFirst().getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY).getUUID();
    }

}
