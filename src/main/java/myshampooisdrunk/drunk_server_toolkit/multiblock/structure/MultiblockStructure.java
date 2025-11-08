package myshampooisdrunk.drunk_server_toolkit.multiblock.structure;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntity;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;

public abstract class MultiblockStructure {
    public static final Transformation SOUTH = (x,y,z) -> new Vec3i(z,y,-x);
    public static final Transformation EAST = Vec3i::new;
    public static final Transformation WEST = (x,y,z) -> new Vec3i(-x,y,-z);
    public static final Transformation NORTH = (x,y,z) -> new Vec3i(-z,y,x);

    protected final MultiblockStructureType<?> type;
    protected final MultiblockCoreEntity core;
    protected final Map<BlockPos, BlockState> structureBlocks;
    protected final World world;

    public MultiblockStructure(MultiblockStructureType<?> type, MultiblockCoreEntity core) {
        this.type = type;
        this.core = core;
        this.world = core.getWorld();
        this.structureBlocks = new HashMap<>();
    }

    public MultiblockCoreEntity getCore() {
        return core;
    }

    public void destroy() {
        structureBlocks.forEach((pos, state) ->
            world.setBlockState(pos, state, Block.NOTIFY_LISTENERS | Block.SKIP_BLOCK_ENTITY_REPLACED_CALLBACK)
        );
        core.removeMultiblock();
    }

    public final Collection<UUID> getLinkedEntities() {
        return ((MultiblockCacheI) world).drunk_server_toolkit$getLinkedEntities(core.getUuid());
    }

    public boolean spawnStructure(BlockPos pos) {
        boolean ret = type.getTemplate().testAndThen(world, pos, p -> structureBlocks.put(p, world.getBlockState(p)));
        if(!ret) return false;

//        System.out.println("blocks: " + structureBlocks);

        structureBlocks.keySet().forEach(p -> world.removeBlock(p, false));

        MultiblockEntity.spawnEntity(this.core, this, pos, Vec3d.ZERO);

        ((MultiblockCacheI) world).drunk_server_toolkit$spawnMultiblock(pos, core, this);

        return true;
    }

    public Map<BlockPos, BlockState> getStructureBlocks() {
        return structureBlocks;
    }

    void copyStructureBlocks(Map<BlockPos, BlockState> blocks) {
        this.structureBlocks.clear();
        structureBlocks.putAll(blocks);
    }

    public MultiblockStructureType<?> getType() {
        return type;
    }

    public record Template(Map<Vec3i, Character> pattern, Map<Character, Predicate<BlockState>> keys, Set<Transformation> transformations) {

        public static TemplateBuilder builder() {
            return new TemplateBuilder();
        }

        public static TemplateBuilder builder(char[][][] pattern, Map<Character, Predicate<BlockState>> keys, char center) {
            TemplateBuilder ret = builder();
            Vec3i offset = Vec3i.ZERO;
            Map<Vec3i, Character> tempPattern = new HashMap<>();
            for (int x = 0; x < pattern.length; x++) {
                for (int y = 0; y < pattern[x].length; y++) {
                    for (int z = 0; z < pattern[x][y].length; z++) {
                        if(pattern[x][y][z] == center)
                            offset = new Vec3i(x, y, z);
                        else if(keys.containsKey(pattern[x][y][z]))
                            tempPattern.put(new Vec3i(x, y, z), pattern[x][y][z]);
                    }
                }
            }
            for (Vec3i pos : tempPattern.keySet()) {
                Vec3i p = pos.subtract(offset);
                ret = ret.add(p, tempPattern.get(p), keys.get(tempPattern.get(p)));
            }

            return ret;
        }

        public boolean test(BlockView world, BlockPos pos) {
            Set<Transformation> bad = new HashSet<>();
            for (Map.Entry<Vec3i, Character> entry : pattern.entrySet()) {
                for (Transformation t : transformations) {
                    if(!bad.contains(t)) {
                        if(!keys.get(entry.getValue()).test(world.getBlockState(pos.add(t.transform(entry.getKey()))))) bad.add(t);
                    }
                }
                if(bad.size() == transformations.size()) return false;
            }
            return true;

//            for (Map.Entry<Vec3i, Character> entry : pattern.entrySet()) {
//                if(!keys.get(entry.getValue()).test(world.getBlockState(pos.add(entry.getKey())))) return false;
//            }
        }

        public boolean testAndThen(BlockView world, BlockPos pos, Consumer<BlockPos> consumer) {
            Set<Transformation> bad = new HashSet<>();
            for (Map.Entry<Vec3i, Character> entry : pattern.entrySet()) {
                for (Transformation t : transformations) {
                    if(!bad.contains(t)) {
                        boolean bl = keys.get(entry.getValue()).test(world.getBlockState(pos.add(t.transform(entry.getKey()))));
                        if(!bl) bad.add(t);
                    }
                }
                if(bad.size() == transformations.size()) return false;
            }
            Transformation used = transformations.stream().filter(t -> !bad.contains(t)).findFirst().orElse(null);

            if(used == null) return false;

            pattern.keySet().forEach(p -> consumer.accept(pos.add(used.transform(p))));

            return true;
        }
    }

    public static class TemplateBuilder {
        private final Map<Vec3i, Character> pattern;
        private final Map<Character, Predicate<BlockState>> keys;
        private final Set<Transformation> transformation;

        private TemplateBuilder() {
            pattern = new HashMap<>();
            keys = new HashMap<>();
            transformation = new HashSet<>();
        }

        public TemplateBuilder add(Vec3i relative, Character key, Predicate<BlockState> predicate) {
            pattern.put(relative, key);
            keys.put(key, predicate);
            return this;
        }

        public TemplateBuilder add(int relx, int rely, int relz, Character key, Predicate<BlockState> predicate) {
            pattern.put(new Vec3i(relx, rely, relz), key);
            keys.put(key, predicate);
            return this;
        }

        public TemplateBuilder transformations(Transformation... transformations) {
            transformation.addAll(Arrays.asList(transformations));
            return this;
        }

        public Template build() {
            boolean t = false;

            if(transformation.isEmpty()) {
                transformation.add(EAST);
//                t = true;
//                DST.LOGGER.error("template has no transformations registered");
            }
            if(keys.isEmpty()) {
                t = true;
                DST.LOGGER.error("template has no blocks registered");
            }

            if(t) return null;

            return new Template(pattern, keys, transformation);
        }
    }

    @FunctionalInterface
    public interface Transformation {
        Vec3i transform(int x, int y, int z);
        default Vec3i transform(Vec3i pos) {
            return transform(pos.getX(), pos.getY(), pos.getZ());
        }
    }

}

/*
(x,y,z) ->
E -> rel = (x,y,z)
W -> rel = (-x,y,-z)
N -> rel = (-z,y,x)
S -> rel = (z,y,-x)
*/
