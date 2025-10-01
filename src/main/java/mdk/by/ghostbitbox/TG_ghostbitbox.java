package mdk.by.ghostbitbox;

import java.util.List;
import mdk.by.ghostbitbox.client.GuiUtilsScreen;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement.IconData;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

public class TG_ghostbitbox extends LabyModAddon {

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

  @Override
  public void onKey(int keyCode, boolean pressed) {
    if (keyCode != Keyboard.KEY_R || !pressed) {
      return;
    }

    Minecraft minecraft = Minecraft.getMinecraft();
    GuiScreen current = minecraft.getCurrentScreen();
    if (current instanceof GuiUtilsScreen) {
      minecraft.displayGuiScreen(null);
      return;
    }

    minecraft.displayGuiScreen(new GuiUtilsScreen());
  }
}
