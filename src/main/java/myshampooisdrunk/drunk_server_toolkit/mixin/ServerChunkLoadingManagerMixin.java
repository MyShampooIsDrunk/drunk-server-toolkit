package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.world.MultiblockCacheI;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerChunkLoadingManager;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkLoadingManager.class)
public class ServerChunkLoadingManagerMixin {
    @Shadow @Final ServerWorld world;

    @Inject(method = "loadEntity", at = @At("HEAD"))
    private void loadMultiblockEntity(Entity entity, CallbackInfo ci) {
        ((MultiblockCacheI) world).drunk_server_toolkit$loadMultiblockEntity(entity);
    }

    @Inject(method = "unloadEntity", at = @At("HEAD"))
    private void unloadMultiblockEntity(Entity entity, CallbackInfo ci) {
        ((MultiblockCacheI) world).drunk_server_toolkit$unloadMultiblockEntity(entity.getUuid());
    }
}
