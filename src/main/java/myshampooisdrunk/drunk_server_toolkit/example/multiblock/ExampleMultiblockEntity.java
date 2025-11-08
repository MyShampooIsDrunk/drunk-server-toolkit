package myshampooisdrunk.drunk_server_toolkit.example.multiblock;

import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockCoreEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntity;
import myshampooisdrunk.drunk_server_toolkit.multiblock.entity.MultiblockEntityType;
import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Consumer;

public class ExampleMultiblockEntity extends MultiblockEntity<WardenEntity, ExampleMultiblockEntity> {

    public ExampleMultiblockEntity(MultiblockEntityType<WardenEntity, ExampleMultiblockEntity> type, World world, WardenEntity entity) {
        super(type, world, entity);
    }

    @Override
    public void onInteract(PlayerEntity user, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        super.onInteract(user, hand, cir);
        if(user.getActiveHand() == hand) {
            user.giveItemStack(Items.ACACIA_FENCE.getDefaultStack());
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Override
    public void onPlayerCollision(PlayerEntity player, CallbackInfo ci) {
        if(player instanceof ServerPlayerEntity sp)
            sp.addVelocity(0, 0.5, 0);
    }

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning, CallbackInfo ci) {
        super.onStruckByLightning(world, lightning, ci);
        Consumer<TntEntity> c = (tnt) -> {
            tnt.refreshPositionAndAngles(this.getEntity().getPos().add(0,50,0),0 ,0);
            tnt.setVelocity(0,-3,0);
        };
        world.spawnEntity(EntityType.TNT.create(world, c, entity.getBlockPos(), SpawnReason.MOB_SUMMONED, false, false));
    }
}