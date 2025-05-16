package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import myshampooisdrunk.drunk_server_toolkit.DST;
import myshampooisdrunk.drunk_server_toolkit.component.MultiblockData;
import myshampooisdrunk.drunk_server_toolkit.mixin.ItemDisplayEntityInvoker;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MultiblockCoreEntity extends AbstractMultiblockStructureEntity<DisplayEntity.ItemDisplayEntity> {
    private final ItemStack structureDisplay;

    public MultiblockCoreEntity(String id) {
        super(EntityType.ITEM_DISPLAY, id);
        this.structureDisplay = null;
    }

    public MultiblockCoreEntity(String  id, ItemStack structureDisplay) {
        super(EntityType.ITEM_DISPLAY, id);
        this.structureDisplay = structureDisplay;
    }

    @Override
    public DisplayEntity.ItemDisplayEntity create(ServerWorld world, MultiblockStructure structure, BlockPos center, Vec3d relative){
        DisplayEntity.ItemDisplayEntity entity = super.create(world, structure, center, relative);
        assert entity != null;
        MultiblockData data = entity.getComponent(DST.ENTITY_MULTIBLOCK_DATA_COMPONENT_KEY);
        data.setMultiblock(structure, relative.add(center.getX(), center.getY(), center.getZ()));
        if(structureDisplay != null)
            ((ItemDisplayEntityInvoker) entity).invokeSetItemStack(structureDisplay);

        return entity;
    }

}
