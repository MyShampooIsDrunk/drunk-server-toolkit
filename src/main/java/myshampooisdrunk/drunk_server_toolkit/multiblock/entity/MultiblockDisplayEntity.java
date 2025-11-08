package myshampooisdrunk.drunk_server_toolkit.multiblock.entity;

import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class MultiblockDisplayEntity extends MultiblockEntity<DisplayEntity.ItemDisplayEntity, MultiblockDisplayEntity>{

    public MultiblockDisplayEntity(MultiblockEntityType<DisplayEntity.ItemDisplayEntity, MultiblockDisplayEntity> type, World world,
                                   DisplayEntity.ItemDisplayEntity entity) {
        super(type, world, entity);
    }

    public void set(@Nullable ItemStack stack, @Nullable ItemDisplayContext context) {
        if(stack != null) this.entity.setItemStack(stack);
        if(context != null) this.entity.setItemDisplayContext(context);
    }
}
