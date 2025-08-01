package myshampooisdrunk.drunk_server_toolkit.mixin;

import myshampooisdrunk.drunk_server_toolkit.cooldown.CustomItemCooldownManager;
import myshampooisdrunk.drunk_server_toolkit.cooldown.CustomItemCooldownManagerI;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerEntity.class,priority = Integer.MAX_VALUE-10)
public abstract class PlayerEntityMixin_customCooldowns extends LivingEntity implements CustomItemCooldownManagerI {
    @Unique
    private final CustomItemCooldownManager manager = new CustomItemCooldownManager();
    @Override
    public CustomItemCooldownManager drunk_server_toolkit$getCustomItemCooldownManager(){
        return manager;
    }
    @Inject(at=@At(value="INVOKE",target = "Lnet/minecraft/entity/player/ItemCooldownManager;update()V"),method="tick")
    public void updateCustomEnchantMan(CallbackInfo ci){
        manager.update();
    }

    protected PlayerEntityMixin_customCooldowns(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
}
