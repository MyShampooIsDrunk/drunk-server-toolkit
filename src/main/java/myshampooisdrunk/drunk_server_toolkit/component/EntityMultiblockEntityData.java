package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Uuids;

import java.util.UUID;

public class EntityMultiblockEntityData implements MultiblockEntityData {
    private UUID coreUuid;
    private MultiblockEntityType<?,?> type;

    public EntityMultiblockEntityData(Entity e){
    }

    @Override
    public void readData(ReadView readView) {
        readView.read("core_id", Uuids.STRING_CODEC).ifPresent(u -> coreUuid = u);
        readView.read("entity_type", MultiblockEntityType.TYPE_CODEC).ifPresent(t -> type = t);
    }

    @Override
    public void writeData(WriteView writeView) {
        if(coreUuid != null) writeView.put("core_id", Uuids.STRING_CODEC, coreUuid);
        if(type != null) writeView.put("entity_type", MultiblockEntityType.TYPE_CODEC, type);
    }

    @Override
    public UUID getCoreUuid() {
        return coreUuid;
    }

    @Override
    public MultiblockEntityType<?, ?> getType() {
        return type;
    }

    @Override
    public void initialize(MultiblockEntity<?,?> entity) {
        this.type = entity.getType();
        this.coreUuid = entity.getCoreUuid();
    }
}
