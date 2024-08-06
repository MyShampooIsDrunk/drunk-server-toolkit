package myshampooisdrunk.drunk_server_toolkit.cooldown;

import myshampooisdrunk.drunk_server_toolkit.item.AbstractCustomItem;

import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.Map;

import net.minecraft.util.Pair;
import net.minecraft.util.math.MathHelper;

public class CustomItemCooldownManager {
    private final Map<Pair<AbstractCustomItem,String>,Entry> entries = Maps.newHashMap();
    private int tick;

    public boolean isCoolingDown(Pair<AbstractCustomItem,String> pair) {
        return this.getCooldownProgress(pair, 0.0f) > 0.0f;
    }

    public float getCooldownProgress(Pair<AbstractCustomItem,String> pair, float tickDelta) {
        Entry entry = this.entries.get(pair);
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
            Iterator<Map.Entry<Pair<AbstractCustomItem,String>, Entry>> iterator = this.entries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Pair<AbstractCustomItem,String>, Entry> entry = iterator.next();
                if (entry.getValue().endTick > this.tick) continue;
                iterator.remove();
                this.onCooldownUpdate(entry.getKey());
            }
        }
    }

    public void set(Pair<AbstractCustomItem,String> pair, int duration) {
        this.entries.put(pair, new Entry(this.tick, this.tick + duration));
        this.onCooldownUpdate(pair, duration);
    }

    public void remove(Pair<AbstractCustomItem,String> pair) {
        this.entries.remove(pair);
        this.onCooldownUpdate(pair);
    }

    protected void onCooldownUpdate(Pair<AbstractCustomItem,String> pair, int duration) {
    }

    protected void onCooldownUpdate(Pair<AbstractCustomItem,String> pair) {
    }

    record Entry(int startTick, int endTick) {
    }
}