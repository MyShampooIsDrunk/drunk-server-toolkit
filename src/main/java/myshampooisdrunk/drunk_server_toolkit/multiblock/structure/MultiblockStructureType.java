package myshampooisdrunk.drunk_server_toolkit.multiblock.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Predicate;

public class MultiblockStructureType<T extends MultiblockStructure> {
    //TODO: fix this stupid fucking bug
    public static final Codec<MultiblockStructureType<?>> TYPE_CODEC = Codec.STRING.comapFlatMap(s -> {
        MultiblockStructureType<?> type = MultiblockRegistry.STRUCTURE_TYPES.getOrDefault(s, null);
        return s != null ? DataResult.success(type) : DataResult.error(() -> "Invalid entity type specified: " + s);
    }, t -> MultiblockRegistry.STRUCTURE_TYPES.inverse().get(t));

    private final Factory<T> factory;
    //instead of having multiple predicates just use predicate.or
    private final Predicate<BlockState> controllers;
    private final MultiblockStructure.Template template;

    private MultiblockStructureType(Factory<T> factory, MultiblockStructure.Template template, Predicate<BlockState> controllers) {
        this.factory = factory;
        this.controllers = controllers;
        this.template = template;
    }

    public static <T extends MultiblockStructure> MultiblockStructureType<T> create(String id, Factory<T> factory, MultiblockStructure.Template template, Predicate<BlockState> controllers) {
        return register(id, new MultiblockStructureType<>(factory, template, controllers));
    }

    public String getId() {
        return MultiblockRegistry.STRUCTURE_TYPES.inverse().getOrDefault(this, "");
    }

    public MultiblockStructure.Template getTemplate() {
        return template;
    }

    public Predicate<BlockState> getControllers() {
        return controllers;
    }

    public T loadFromCore(MarkerEntity core) {
        T ret;
        MultiblockCacheI cache = (MultiblockCacheI) core.getEntityWorld();
        if(cache.drunk_server_toolkit$containsUuid(core.getUuid())) {
            MultiblockCoreEntity c = cache.drunk_server_toolkit$getMultiblockEntity(core.getUuid());
            ret = factory.create(this, c);
        }
        else {
            MultiblockCoreEntity c = cache.drunk_server_toolkit$loadMultiblockEntity(core);
            if(c == null) return null;
            ret = factory.create(this, c);
        }

        ret.copyStructureBlocks(core.getComponent(DST.MULTIBLOCK_CORE_DATA_COMPONENT_KEY).getBlockstateData());

        return ret;

    }

    public T loadFromCore(MultiblockCoreEntity core) {
        T ret;
        MultiblockCacheI cache = (MultiblockCacheI) core.getWorld();
        ret = factory.create(this, core);

        ret.copyStructureBlocks(core.getEntity().getComponent(DST.MULTIBLOCK_CORE_DATA_COMPONENT_KEY).getBlockstateData());

        return ret;
    }

    public final boolean createAtBlock(World world, BlockPos pos) {
        MultiblockCoreEntity core = MultiblockRegistry.CORE_ENTITY.create(world);

        if(core == null) {
            DST.LOGGER.error("ERROR LOADING MULTIBLOCK {} AT BLOCK {}", getId(), pos);
            return false;
        }

        T structure = factory.create(this, core);

        return structure.spawnStructure(pos);
    }

    public static <T extends MultiblockStructure> MultiblockStructureType<T> register(String id, MultiblockStructureType<T> type) {
        MultiblockRegistry.STRUCTURE_TYPES.put(id, type);
        return type;
    }

    @FunctionalInterface
    public interface Factory<T extends MultiblockStructure> {
        T create(MultiblockStructureType<T> type, MultiblockCoreEntity core);
    }
}
