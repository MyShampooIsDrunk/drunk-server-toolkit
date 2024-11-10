package myshampooisdrunk.drunk_server_toolkit.item;

import myshampooisdrunk.drunk_server_toolkit.DST;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

import java.util.Objects;
import java.util.Optional;

public class CustomItemHelper {
    public static Optional<AbstractCustomItem> getCustomItem(ItemStack stack){
        for(AbstractCustomItem custom : DST.ITEMS){
            if(stack.isOf(custom.getItem()) && stack.contains(DataComponentTypes.CUSTOM_DATA) &&
                    Objects.requireNonNull(stack.get(DataComponentTypes.CUSTOM_DATA)).contains("custom_item") &&
                    Objects.requireNonNull(stack.get(DataComponentTypes.CUSTOM_DATA)).getNbt().getString("custom_item")
                            .equals(custom.getIdentifier().toString())){
                return Optional.of(custom);

            }
        }
        return Optional.empty();
    }
}
