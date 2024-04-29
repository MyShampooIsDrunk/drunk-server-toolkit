package myshampooisdrunk.drunk_server_toolkit.example;

import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class HopefullyThisItemWorks extends AbstractCustomItem {
    public HopefullyThisItemWorks() {
        super(Items.BONE,new Identifier("drunk_server_toolkit","greatest_programmer_ever"),"if.this.breaks.i.will.cry");
    }

    @Override
    public void onUse(World world, PlayerEntity player, Hand hand, CallbackInfoReturnable cir) {
        player.attack(player);
    }
}
