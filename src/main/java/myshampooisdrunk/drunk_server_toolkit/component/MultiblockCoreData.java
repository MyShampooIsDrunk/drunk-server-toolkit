package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructureType;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.Map;
import java.util.UUID;

public interface MultiblockCoreData extends Component {
//
//    /**
//     * called when the multiblock is created
//    */
//    void setBlockstateData(Map<BlockPos, BlockState> data);

    /**
     * called when the multiblock is destroyed
    */
    Map<BlockPos, BlockState> getBlockstateData();
    void initialize(MultiblockStructure structure);
    MultiblockStructureType<?> getStructureType();
}
