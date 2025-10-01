package net.labymod.addon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.labymod.api.TransformerType;

/**
 * Minimal stub of the Labymod AddonTransformer used for compiling the demo addon.
 * It records the registered transformers so unit tests or demo code can inspect them
 * without depending on the real Labymod runtime.
 */
public abstract class AddonTransformer {

  private final List<Entry> entries = new ArrayList<>();

  protected abstract void registerTransformers();

  protected final void registerTransformer(TransformerType type, String config) {
    entries.add(new Entry(type, config));
  }

  public final List<Entry> getEntries() {
    return Collections.unmodifiableList(entries);
  }

  public static final class Entry {
    private final TransformerType type;
    private final String config;

    private Entry(TransformerType type, String config) {
      this.type = type;
      this.config = config;
    }

    public TransformerType getType() {
      return type;
    }

    public String getConfig() {
      return config;
    }
  }
}
