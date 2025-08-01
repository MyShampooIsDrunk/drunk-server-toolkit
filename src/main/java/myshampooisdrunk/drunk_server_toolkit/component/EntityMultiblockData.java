package myshampooisdrunk.drunk_server_toolkit.component;

import com.mojang.serialization.Codec;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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
    public void readData(ReadView readView) {
        readView.read("multiblock_id", Codec.STRING).ifPresent(s -> {if (!s.isEmpty()) multiblockId = Identifier.of(s);});
        readView.read("id", Codec.STRING).ifPresent(s -> {if (!s.isEmpty()) id = s;});
        readView.read("multiblock_instance", Codec.STRING).ifPresent(s -> {if(!s.isEmpty()) multiblockUUID = UUID.fromString(s);});
    }

    @Override
    public void writeData(WriteView writeView) {
        if(multiblockId != null)
            writeView.putString("multiblock_id",multiblockId.toString());
        else writeView.putString("multiblock_id","");

        if(multiblockUUID != null)
            writeView.putString("multiblock_instance", multiblockUUID.toString());
        else writeView.putString("multiblock_instance", "");

        if(id != null)
            writeView.putString("id", id);
        else writeView.putString("id","");
    }
}
