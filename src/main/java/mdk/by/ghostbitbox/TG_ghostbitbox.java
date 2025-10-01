package mdk.by.ghostbitbox;

import java.util.List;
import mdk.by.ghostbitbox.client.GuiUtilsScreen;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement.IconData;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import org.lwjgl.input.Keyboard;

public class TG_ghostbitbox extends LabyModAddon {

  private static final int GLFW_KEY_R = 82;

  @Override
  public void onEnable() {
  }

  @Override
  public void loadConfig() {
  }

  @Override
  protected void fillSettings(List<SettingsElement> list) {
    list.add(new BooleanElement("test", new IconData(Material.ACACIA_FENCE)));
  }

  private boolean toggleHeld;

  @Override
  public void onKey(int keyCode, boolean pressed) {
    if (!isToggleKey(keyCode)) {
      return;
    }

    if (pressed) {
      handleTogglePress();
    } else {
      toggleHeld = false;
    }
  }

  @Override
  public void onTick() {
    if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
      handleTogglePress();
    } else {
      toggleHeld = false;
    }
  }

  private void handleTogglePress() {
    if (toggleHeld) {
      return;
    }
    toggleHeld = true;
    toggleShowcase();
  }

  private void toggleShowcase() {
    Minecraft minecraft = Minecraft.getInstance();
    Screen current = minecraft.getCurrentScreen();
    if (current instanceof GuiUtilsScreen) {
      closeScreen(minecraft);
    } else {
      openScreen(minecraft);
    }
  }

  private void openScreen(Minecraft minecraft) {
    GuiUtilsScreen screen = new GuiUtilsScreen();
    minecraft.setScreen(screen);
    minecraft.displayGuiScreen(screen);
  }

  private void closeScreen(Minecraft minecraft) {
    minecraft.setScreen(null);
    minecraft.displayGuiScreen(null);
  }

  private boolean isToggleKey(int keyCode) {
    return keyCode == Keyboard.KEY_R || keyCode == GLFW_KEY_R || keyCode == 'R' || keyCode == 'r';
  }
}
