package net.labymod.api;

import java.util.List;
import net.labymod.settings.elements.SettingsElement;

/**
 * Minimal stub of the Labymod addon base class. Only the methods used by the
 * demo addon are declared so that the project can be compiled without the
 * original Labymod runtime.
 */
public abstract class LabyModAddon {

  public abstract void onEnable();

  public abstract void loadConfig();

  protected abstract void fillSettings(List<SettingsElement> list);
}
