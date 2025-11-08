package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructureType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.MarkerEntity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class MarkerMultiblockCoreData implements MultiblockCoreData{
    private final Map<BlockPos, BlockState> initialStates;
    private MultiblockStructureType<?> type;

    public MarkerMultiblockCoreData(MarkerEntity e){
        initialStates = new HashMap<>();
    }

    @Override
    public void readData(ReadView readView) {
        for (ReadView block : readView.getListReadView("blocks")) {
            BlockPos pos = block.read("pos",BlockPos.CODEC).orElseThrow();
            BlockState state = block.read("block",BlockState.CODEC).orElseThrow();
            initialStates.put(pos, state);
        }

        readView.read("structure_type", MultiblockStructureType.TYPE_CODEC).ifPresent(t -> type = t);
    }

    @Override
    public void writeData(WriteView writeView) {
        WriteView.ListView list = writeView.getList("blocks");
        initialStates.forEach((pos, state) -> {
            WriteView pair = list.add();
            pair.put("pos", BlockPos.CODEC, pos);
            pair.put("block", BlockState.CODEC, state);
        });

        if(type != null) writeView.put("structure_type", MultiblockStructureType.TYPE_CODEC, type);
    }

    public Map<BlockPos, BlockState> getBlockstateData() {
        return initialStates;
    }

    @Override
    public void initialize(MultiblockStructure structure) {
        type = structure.getType();
        initialStates.putAll(structure.getStructureBlocks());
    }

    @Override
    public MultiblockStructureType<?> getStructureType() {
        return type;
    }
}
