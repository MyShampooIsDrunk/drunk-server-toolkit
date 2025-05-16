package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.Map;
import java.util.UUID;

public interface MultiblockCoreData extends Component {
    void setBlockstateData(Map<BlockPos, BlockState> data);
    Map<BlockPos, BlockState> getBlockstateData();
}
