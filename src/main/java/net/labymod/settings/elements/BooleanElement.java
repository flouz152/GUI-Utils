package net.labymod.settings.elements;

/**
 * Basic boolean toggle stub for compilation purposes.
 */
public class BooleanElement extends ControlElement {

  private final String key;
  private final IconData icon;
  private boolean value;

  public BooleanElement(String key, IconData icon) {
    this.key = key;
    this.icon = icon;
  }

  public String getKey() {
    return key;
  }

  public IconData getIcon() {
    return icon;
  }

  public boolean isEnabled() {
    return value;
  }

  public void setEnabled(boolean value) {
    this.value = value;
  }
}
