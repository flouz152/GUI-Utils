package net.labymod.settings.elements;

public class BooleanElement extends SettingsElement {

    private final String label;
    private final ControlElement.IconData iconData;

    public BooleanElement(String label, ControlElement.IconData iconData) {
        this.label = label;
        this.iconData = iconData;
    }

    public String getLabel() {
        return label;
    }

    public ControlElement.IconData getIconData() {
        return iconData;
    }
}
