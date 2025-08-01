package myshampooisdrunk.drunk_server_toolkit.example.multiblock;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.AbstractMultiblockStructureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class ExampleMultiblockEntity extends AbstractMultiblockStructureEntity<WardenEntity> {
    public ExampleMultiblockEntity(String  id) {
        super(EntityType.WARDEN, id);
    }

    @Override
    public void onInteract(PlayerEntity user, Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        super.onInteract(user, entity, hand, cir);
        if(user.getActiveHand() == hand) {
            user.giveItemStack(Items.ACACIA_FENCE.getDefaultStack());
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player, Entity me, CallbackInfo ci) {
        player.setVelocity(0, 3, 0);
    }
}