package myshampooisdrunk.drunk_server_toolkit.util;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.Property;

import java.util.Collection;
import java.util.List;

public class Util {
    @SuppressWarnings({"unchecked"})
    public static <T> T forceCast(Object obj) {
        return (T) obj;
    }
    public static <T> Collection<T> addAndGet(Collection<T> init, T item){
        init.add(item);
        return init;
    }
    public static <T> Collection<T> addAllAndGet(Collection<T> init, Collection<T> items){
        init.addAll(items);
        return init;
    }
    @SafeVarargs
    public static <T> Collection<T> addAllAndGet(Collection<T> init, T... items){
        init.addAll(List.of(items));
        return init;
    }
}
