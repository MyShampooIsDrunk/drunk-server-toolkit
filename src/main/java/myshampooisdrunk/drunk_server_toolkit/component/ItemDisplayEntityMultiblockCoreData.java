package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ItemDisplayEntityMultiblockCoreData implements MultiblockCoreData{
    private final Map<BlockPos, BlockState> initialStates;
    private final World world;
    public ItemDisplayEntityMultiblockCoreData(DisplayEntity.ItemDisplayEntity e){
        initialStates = new HashMap<>();
        world = e.getWorld();
    }

    @Override
    public void setBlockstateData(Map<BlockPos, BlockState> data) {
        initialStates.clear();
        initialStates.putAll(data);
    }

    @Override
    public void readData(ReadView readView) {
        for (ReadView block : readView.getListReadView("blocks")) {
            BlockPos pos = block.read("pos",BlockPos.CODEC).orElseThrow();
            BlockState state = block.read("block",BlockState.CODEC).orElseThrow();
            initialStates.put(pos, state);
        }
    }

    @Override
    public void writeData(WriteView writeView) {

        WriteView.ListView list = writeView.getList("blocks");
        initialStates.forEach((pos, state) -> {
            WriteView pair = list.add();
            pair.put("pos", BlockPos.CODEC, pos);
            pair.put("block", BlockState.CODEC, state);
        });
    }

    public Map<BlockPos, BlockState> getBlockstateData() {
        return initialStates;
    }
}
