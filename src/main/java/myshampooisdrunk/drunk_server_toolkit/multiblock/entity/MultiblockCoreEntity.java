package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import myshampooisdrunk.drunk_server_toolkit.mixin.ItemDisplayEntityInvoker;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MultiblockCoreEntity extends AbstractMultiblockStructureEntity<DisplayEntity.ItemDisplayEntity> {
    private final ItemStack structureDisplay;

    public MultiblockCoreEntity(Identifier id) {
        super(EntityType.ITEM_DISPLAY, Identifier.of(id.getNamespace(),id.getPath()+"_core"));
        this.structureDisplay = null;
    }

    public MultiblockCoreEntity(Identifier id, ItemStack structureDisplay) {
        super(EntityType.ITEM_DISPLAY, Identifier.of(id.getNamespace(),id.getPath()+"_core"));
        this.structureDisplay = structureDisplay;
    }

    @Override
    public DisplayEntity.ItemDisplayEntity create(World world, MultiblockStructure structure, BlockPos center, Vec3d relative){

        DisplayEntity.ItemDisplayEntity entity = super.create(world, structure, center, relative);
        assert entity != null;
        NbtCompound nbt = new NbtCompound();
        nbt.putString("MultiblockId", id.toString());
        entity.writeNbt(nbt);
        if(structureDisplay != null)
            ((ItemDisplayEntityInvoker) entity).invokeSetItemStack(structureDisplay);
        entity.setNoGravity(true);
        entity.setInvulnerable(true);
        entity.setSilent(true);
        return entity;
    }

}
