package net.labymod.settings.elements;

import net.labymod.utils.Material;

public class ControlElement extends SettingsElement {

    public static class IconData {
        private final Material material;

        public IconData(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            return material;
        }
    }
}
