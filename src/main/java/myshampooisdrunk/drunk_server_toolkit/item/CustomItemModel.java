package myshampooisdrunk.drunk_server_toolkit.item;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class CustomItemModel extends Model {
    private final int id;
    private final String remap;
    public CustomItemModel(Optional<Identifier> parent, Optional<String> variant, Optional<String> redirectPath, int id, TextureKey... requiredTextureKeys) {
        super(parent, variant, requiredTextureKeys);
        this.id=id;
        this.remap = redirectPath.orElse("");
    }

    @Override
    public JsonObject createJson(Identifier identifier, Map<TextureKey, Identifier> textures){
        JsonObject object = super.createJson(identifier,textures);
        JsonObject customData = new JsonObject();
        customData.addProperty("custom_model_data",id);
        object.add("predicate",customData);
        object.addProperty("model",remap);
        return object;
    }
}