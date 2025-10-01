package net.labymod.addon;

import net.labymod.api.TransformerType;

public abstract class AddonTransformer {

    protected void registerTransformer(TransformerType type, String configuration) {
        // no-op stub for compilation purposes
    }

    public abstract void registerTransformers();
}
