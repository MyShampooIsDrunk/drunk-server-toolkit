package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockEntityData;
import myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry;
import net.minecraft.entity.*;
import net.minecraft.util.TypeFilter;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

import static myshampooisdrunk.drunk_server_toolkit.multiblock.registry.MultiblockRegistry.ENTITY_TYPES;

public class MultiblockEntityType<E extends Entity, T extends MultiblockEntity<E, T>> implements TypeFilter<Entity, E> {
    public static final Codec<MultiblockEntityType<?,?>> TYPE_CODEC = Codec.STRING.comapFlatMap(s -> {
        MultiblockEntityType<?,?> type = MultiblockRegistry.ENTITY_TYPES.getOrDefault(s, null);
        return type != null ? DataResult.success(type) : DataResult.error(() -> "Invalid entity type specified: " + s + " registered ");
    }, t -> ENTITY_TYPES.inverse().get(t));

    private final Factory<E,T> factory;
    private final EntityType<E> entityType;

    public MultiblockEntityType(EntityType<E> entityType, Factory<E, T> factory) {
        this.factory = factory;
        this.entityType = entityType;
    }

    public EntityType<E> getEntityType() {
        return entityType;
    }

    @Nullable
    public T create(World world) {
        return this.factory.create(this, world);
    }

    public T createFromEntity(E entity) {
        T ret = this.factory.create(this, entity.getEntityWorld(), entity);
        if(ret == null) {
            DST.LOGGER.warn("ATTEMPTED INITIALIZING NULL MULTIBLOCK ENTITY WITH UUID {} AND TYPE {}", entity.getUuid(), getId(this));
            return null;
        }
        ret.initializeFromData(entity);
        return ret;
    }

    public static <E extends Entity, T extends MultiblockEntity<E, T>> MultiblockEntityType<E, T> register(String key, MultiblockEntityType.Builder<E, T> type) {
        MultiblockEntityType<E, T> ret = type.build();
        ENTITY_TYPES.put(key, ret);
        return ret;
    }

    public static String getId(MultiblockEntityType<?, ?> type) {
        return ENTITY_TYPES.inverse().get(type);
    }

    public static Optional<MultiblockEntityType<?, ?>> get(String id) {
        return Optional.ofNullable(ENTITY_TYPES.getOrDefault(id,null));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @Nullable E downcast(Entity entity) {
        MultiblockEntityData data = entity.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
        boolean bl = data.getType() == this && entity.getType() == entityType;
//        System.out.println("Entity data: " + entity + " " + bl);
        return (E)(bl ? entity : null);
    }

    @Override
    public Class<? extends Entity> getBaseClass() {
        return Entity.class;
    }

    public record Builder<E extends Entity, T extends MultiblockEntity<E, T>>(EntityType<E> entityType, Factory<E, T> factory) {

        public static <E extends Entity, T extends MultiblockEntity<E, T>> Builder<E, T> create(Factory<E, T> factory, EntityType<E> entityType) {
            return new Builder<>(entityType, factory);
        }

//        public static <E extends Entity, T extends MultiblockEntity<E>> Builder<E, T> create(EntityType<E> entityType) {
//            return new Builder<>(entityType, (type, world, reason) -> );
//        }

        public MultiblockEntityType<E, T> build() {
            return new MultiblockEntityType<>(this.entityType, this.factory);
        }
    }

    @Override
    public String toString() {
        return getId(this);
    }

    @FunctionalInterface
    public interface Factory<E extends Entity, T extends MultiblockEntity<E, T>> {
        @Nullable T create(MultiblockEntityType<E, T> type, World world, @Nullable E entity);

        default T create(MultiblockEntityType<E,T> type, World world) {
            return create(type, world, null);
        }
    }
}
