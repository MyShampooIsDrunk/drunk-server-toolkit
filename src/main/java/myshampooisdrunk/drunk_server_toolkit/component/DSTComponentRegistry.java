package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.DST;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;

public class DSTComponentRegistry implements EntityComponentInitializer {
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(Entity.class, DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY, EntityMultiblockData::new);
        registry.registerFor(DisplayEntity.ItemDisplayEntity.class, DST.MULTIBLOCK_CORE_DATA_COMPONENT_KEY, ItemDisplayEntityMultiblockCoreData::new);
    }
}
