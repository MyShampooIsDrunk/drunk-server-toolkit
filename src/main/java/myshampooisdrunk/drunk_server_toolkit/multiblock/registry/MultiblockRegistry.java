package myshampooisdrunk.drunk_server_toolkit.multiblock.registry;

import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.util.Identifier;

import java.util.HashMap;

public class MultiblockRegistry {
    public static final HashMap<Identifier,MultiblockStructure> structures = new HashMap<>();

    public static void register(MultiblockStructure structure, Identifier id){
        structures.put(id,structure);
    }
}
