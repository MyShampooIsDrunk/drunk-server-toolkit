package myshampooisdrunk.drunk_server_toolkit.cooldown;

import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

public class CustomItemCooldownManager {
    private final Map<String,Entry> entries = Maps.newHashMap();
    private int tick;

    public boolean isCoolingDown(String id) {
        return this.getCooldownProgress(id, 0.0f) > 0.0f;
    }

    public float getCooldownProgress(String id, float tickDelta) {
        Entry entry = this.entries.get(id);
        if (entry != null) {
            float f = entry.endTick - entry.startTick;
            float g = (float)entry.endTick - ((float)this.tick + tickDelta);
            return MathHelper.clamp(g / f, 0.0f, 1.0f);
        }
        return 0.0f;
    }

    public void update() {
        ++this.tick;
        if (!this.entries.isEmpty()) {
            Iterator<Map.Entry<String, Entry>> iterator = this.entries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Entry> entry = iterator.next();
                if (entry.getValue().endTick > this.tick) continue;
                iterator.remove();
                this.onCooldownUpdate(entry.getKey());
            }
        }
    }

    public void set(String id, int duration) {
        this.entries.put(id, new Entry(this.tick, this.tick + duration));
        this.onCooldownUpdate(id, duration);
    }

    public void remove(String id) {
        this.entries.remove(id);
        this.onCooldownUpdate(id);
    }

    protected void onCooldownUpdate(String id, int duration) {
    }

    protected void onCooldownUpdate(String id) {
    }

    record Entry(int startTick, int endTick) {
    }
}