package myshampooisdrunk.drunk_server_toolkit.example.multiblock;

import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.util.Identifier;

public class ExampleMultiblock extends MultiblockStructure {
    public ExampleMultiblock() {
        super(Identifier.of("test","furnace"));
    }
}
