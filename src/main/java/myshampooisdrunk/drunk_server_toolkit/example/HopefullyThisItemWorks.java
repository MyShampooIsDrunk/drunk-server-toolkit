package myshampooisdrunk.drunk_server_toolkit.example;

import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HopefullyThisItemWorks extends AbstractCustomItem {
    public HopefullyThisItemWorks() {
        super(Items.BONE,Identifier.of("drunk_server_toolkit","greatest_programmer_ever"),"if.this.breaks.i.will.cry");
    }

    @Override
    public void use(World world, LivingEntity entity, Hand hand, CallbackInfoReturnable cir) {
        if(entity instanceof PlayerEntity p) p.attack(p);
    }
}
