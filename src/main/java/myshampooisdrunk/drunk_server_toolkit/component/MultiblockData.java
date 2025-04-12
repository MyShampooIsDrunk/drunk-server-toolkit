package myshampooisdrunk.drunk_server_toolkit.component;

import myshampooisdrunk.drunk_server_toolkit.multiblock.structure.MultiblockStructure;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.Component;

import java.util.UUID;

public interface MultiblockData extends Component {
    UUID getUUID();
    Identifier getMultiblockID();
    void setMultiblock(MultiblockStructure structure);
}
