package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EntityMultiblockData implements MultiblockData{
    private Identifier multiblockId = null;
    private UUID multiblockUUID = null;
    private String id = null;
    public EntityMultiblockData(Entity e){
    }

    @Override
    public UUID getUUID() {
        return multiblockUUID;
    }
    @Override
    public Identifier getMultiblockID() {
        return multiblockId;
    }

    @Override
    public void setEntityId(String id) {
        this.id = id;
    }

    @Override
    public String getEntityId() {
        return id;
    }

    @Override
    public void setMultiblock(MultiblockStructure structure, Vec3d pos) {
        multiblockId = structure.id();
        multiblockUUID = structure.getUUID();
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if(tag.contains("multiblock_id")) {
            String mId = tag.getString("multiblock_id");
            if(!mId.isEmpty()) multiblockId = Identifier.of(mId);
        }
        if(tag.contains("id")) {
            String i = tag.getString("id");
            if(!i.isEmpty()) id = i;
        }
        if(tag.contains("multiblock_instance")) {
            String uuid = tag.getString("multiblock_instance");
            if(!uuid.isEmpty()) multiblockUUID = UUID.fromString(uuid);
        }
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if(multiblockId != null)
            tag.putString("multiblock_id",multiblockId.toString());
        else tag.putString("multiblock_id","");

        if(multiblockUUID != null)
            tag.putString("multiblock_instance", multiblockUUID.toString());
        else tag.putString("multiblock_instance", "");

        if(id != null)
            tag.putString("id", id);
        else tag.putString("id","");
    }
}
