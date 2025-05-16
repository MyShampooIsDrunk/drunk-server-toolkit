package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import java.util.function.Supplier;

public class MultiblockEntityType<E extends Entity> {
    private final String id;
    private final Class<E> eClass;
    private final AbstractMultiblockStructureEntity<E> defaultEntity;

    public MultiblockEntityType(String id, Class<E> eClass, AbstractMultiblockStructureEntity<E> defaultEntity) {
        this.id = id;
        this.eClass = eClass;
        this.defaultEntity = defaultEntity;
    }

    public String getId(){
        return id;
    }

    public boolean matches(Entity entity){
        return eClass.isInstance(entity);
    }

    public AbstractMultiblockStructureEntity<E> defaultEntity(){
        return defaultEntity;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> MultiblockEntityType<T> create(String id, EntityType<T> type, AbstractMultiblockStructureEntity<T> entity, Class<AbstractMultiblockStructureEntity<T>> defaultClass) {
        ;
        return new MultiblockEntityType<>(id, (Class<T>) type.getBaseClass(), defaultClass.cast(entity));
    }
}
