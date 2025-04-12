package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class EntityMultiblockData implements MultiblockData{
    private Identifier multiblockId = null;
    private UUID multiblockUUID = null;
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
    public void setMultiblock(MultiblockStructure structure) {
        multiblockId = structure.id();
        multiblockUUID = structure.getUUID();
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if(tag.contains("multiblock_id"))
            multiblockId = Identifier.of(tag.getString("multiblock_id"));
        if(tag.contains("multiblock_instance"))
            multiblockUUID = UUID.fromString(tag.getString("multiblock_instance"));
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if(multiblockId != null)
            tag.putString("multiblock_id",multiblockId.toString());

        if(multiblockUUID != null)
            tag.putString("multiblock_instance", multiblockUUID.toString());
    }
}
