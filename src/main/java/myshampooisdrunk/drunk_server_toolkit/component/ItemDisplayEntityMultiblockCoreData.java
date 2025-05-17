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
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        RegistryEntryLookup<Block> lookup = this.world.createCommandRegistryWrapper(RegistryKeys.BLOCK);
        NbtList list = tag.getList("blocks", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound pair = list.getCompound(i);
            BlockPos pos = NbtHelper.toBlockPos(pair, "pos").orElseThrow();
            BlockState state = NbtHelper.toBlockState(lookup, pair.getCompound("block"));
            initialStates.put(pos, state);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();
        initialStates.forEach((pos, state) -> {
            NbtCompound pair = new NbtCompound();
            pair.put("pos", NbtHelper.fromBlockPos(pos));
            pair.put("block", NbtHelper.fromBlockState(state));
            list.add(pair);
        });
        tag.put("blocks", list);
    }

    public Map<BlockPos, BlockState> getBlockstateData() {
        return initialStates;
    }
}
