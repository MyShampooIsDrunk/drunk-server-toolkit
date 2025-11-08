package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntityType;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.entity.SpawnReason;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.UUID;

public interface MultiblockEntityData extends Component {
    UUID getCoreUuid();
    MultiblockEntityType<?,?> getType();

    /**
     stores the multiblock entity's data in the entity (stuff related to the multiblock specifically like uuids n stuff)
     called in {@link MultiblockEntity#create(MultiblockStructure, BlockPos, Vec3d, SpawnReason) }
     */
    void initialize(MultiblockEntity<?,?> entity);

}
/*

*/