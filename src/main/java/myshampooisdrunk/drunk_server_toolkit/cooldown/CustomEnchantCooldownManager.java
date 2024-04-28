package myshampooisdrunk.drunk_server_toolkit.cooldown;

import com.google.common.collect.Maps;
import myshampooisdrunk.drunk_server_toolkit.enchantment.AbstractCustomEnchantment;
import net.minecraft.util.math.MathHelper;

import java.util.Iterator;
import java.util.Map;

public class CustomEnchantCooldownManager {
    private final Map<AbstractCustomEnchantment,Entry> entries = Maps.newHashMap();
    private int tick;

    public boolean isCoolingDown(AbstractCustomEnchantment ench) {
        return this.getCooldownProgress(ench, 0.0f) > 0.0f;
    }

    public float getCooldownProgress(AbstractCustomEnchantment ench, float tickDelta) {
        Entry entry = this.entries.get(ench);
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
            Iterator<Map.Entry<AbstractCustomEnchantment, Entry>> iterator = this.entries.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<AbstractCustomEnchantment, Entry> entry = iterator.next();
                if (entry.getValue().endTick > this.tick) continue;
                iterator.remove();
                this.onCooldownUpdate(entry.getKey());
            }
        }
    }

    public void set(AbstractCustomEnchantment ench, int duration) {
        this.entries.put(ench, new Entry(this.tick, this.tick + duration));
        this.onCooldownUpdate(ench, duration);
    }

    public void remove(AbstractCustomEnchantment ench) {
        this.entries.remove(ench);
        this.onCooldownUpdate(ench);
    }

    protected void onCooldownUpdate(AbstractCustomEnchantment ench, int duration) {
    }

    protected void onCooldownUpdate(AbstractCustomEnchantment ench) {
    }

    static class Entry {
        final int startTick;
        final int endTick;

        Entry(int startTick, int endTick) {
            this.startTick = startTick;
            this.endTick = endTick;
        }
    }
}