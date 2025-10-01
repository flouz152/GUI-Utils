package mdk.by.ghostbitbox.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import im.com.slay.ui.core.UIButton;
import im.com.slay.ui.core.UIButton.AnimationStyle;
import im.com.slay.ui.core.UIConstraintContainer;
import im.com.slay.ui.core.UIContext;
import im.com.slay.ui.core.UIFlowContainer;
import im.com.slay.ui.core.UILabel;
import im.com.slay.ui.core.UIPanel;
import im.com.slay.ui.core.UISpacer;
import im.com.slay.ui.core.UITextField;
import im.com.slay.ui.geometry.Rect;
import im.com.slay.ui.layout.FlexLayout;
import im.com.slay.ui.math.Vec2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import mdk.by.ghostbitbox.client.render.GuiSurfaceRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import org.lwjgl.input.Keyboard;

/**
 * Custom GUI that showcases the available UI utilities. Displays a window with animated buttons,
 * a search field that filters the feature list and descriptive labels.
 */
public class GuiUtilsScreen extends Screen {

    private static final int BACKGROUND_COLOR = 0xAA101010;
    private static final int GLFW_KEY_ESCAPE = 256;

    private final UIConstraintContainer root = new UIConstraintContainer();
    private final UIPanel window;
    private final UITextField searchField;
    private final UILabel statusLabel;
    private final UIFlowContainer featureList;
    private final List<String> features = Arrays.asList(
            "Constraint based контейнер",
            "Гибкий FlexLayout",
            "Потоковый FlowLayout",
            "Состояния и наблюдатели",
            "Настраиваемые easing функции",
            "Модальные окна",
            "Поверхность отрисовки для Minecraft",
            "Поддержка элементов ввода",
            "Анимации hover для кнопок"
    );
    private final List<UIButton> buttons = new ArrayList<>();
    private final Map<UIButton, String> buttonDescriptions = new LinkedHashMap<>();
    private String persistentStatus = "Наведите на кнопки, чтобы увидеть анимации";

    private final GuiSurfaceRenderer renderer;

    public GuiUtilsScreen() {
        super(new StringTextComponent("GUI Utils Showcase"));
        window = new UIPanel(FlexLayout.vertical().spacing(8.0));
        window.setPadding(12.0);
        window.setBackgroundColor(0xEE1E1E1E);
        window.setBorderColor(0x55FFFFFF);
        window.setPreferredSize(new Vec2(360, 240));

        UILabel title = new UILabel("GUI Utils Showcase");
        title.setCentered(true);
        title.setColor(0xFFFFFFFF);
        window.addChild(title);

        searchField = new UITextField("Поиск возможностей...");
        searchField.setOnChanged(this::updateFeatureList);
        window.addChild(searchField);

        UIFlowContainer buttonRow = new UIFlowContainer();
        buttonRow.setLayout(FlexLayout.horizontal().spacing(6.0));

        UIButton elastic = createButton("Elastic pulse", AnimationStyle.ELASTIC,
                "Пружинящая анимация с отскоком");
        UIButton back = createButton("Back overshoot", AnimationStyle.BACK,
                "Плавная анимация с перелётом");
        UIButton linear = createButton("Linear glow", AnimationStyle.LINEAR,
                "Линейное свечение без излишней динамики");

        buttonRow.addChild(elastic);
        buttonRow.addChild(back);
        buttonRow.addChild(linear);

        window.addChild(buttonRow);
        window.addChild(new UISpacer(0.0, 6.0));

        statusLabel = new UILabel(persistentStatus);
        statusLabel.setExpandHorizontally(true);
        statusLabel.setColor(0xFFB5E4FF);
        window.addChild(statusLabel);

        featureList = new UIFlowContainer();
        featureList.setLayout(FlexLayout.vertical().spacing(4.0));
        featureList.setPreferredSize(new Vec2(0.0, 112.0));
        window.addChild(featureList);

        root.addChild(window);

        renderer = new GuiSurfaceRenderer(this);
        updateFeatureList("");
    }

    private UIButton createButton(String text, AnimationStyle style, String description) {
        UIButton button = new UIButton(text);
        button.setAnimationStyle(style);
        button.setPreferredSize(new Vec2(108, 22));
        button.setOnClick(b -> handleButtonClick(b, description));
        switch (style) {
            case ELASTIC:
                button.setHighlightColor(0x6633CCFF);
                break;
            case BACK:
                button.setHighlightColor(0x66FFAA33);
                break;
            case LINEAR:
                button.setHighlightColor(0x66FF3FD1);
                break;
            default:
                break;
        }
        buttons.add(button);
        buttonDescriptions.put(button, description);
        return button;
    }

    private void handleButtonClick(UIButton button, String description) {
        persistentStatus = "Нажата: " + button.getText() + " — " + description;
        statusLabel.setText(persistentStatus);
    }

    private void updateFeatureList(String query) {
        featureList.clearChildren();
        String trimmed = query == null ? "" : query.trim().toLowerCase(Locale.ROOT);
        for (String feature : features) {
            if (trimmed.isEmpty() || feature.toLowerCase(Locale.ROOT).contains(trimmed)) {
                UILabel label = new UILabel("• " + feature);
                label.setColor(0xFFE0E0E0);
                featureList.addChild(label);
            }
        }

        if (featureList.getChildren().isEmpty()) {
            UILabel empty = new UILabel("Ничего не найдено :(");
            empty.setColor(0xFFFF8080);
            featureList.addChild(empty);
        }
    }

    @Override
    protected void init() {
        searchField.moveCaretToEnd();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        fill(matrices, 0, 0, width, height, BACKGROUND_COLOR);

        updateHoverState(mouseX, mouseY);

        renderer.bind(matrices);
        UIContext context = new UIContext(System.currentTimeMillis());
        root.measure(new Vec2(width, height));
        root.render(context, renderer, new Rect(0, 0, width, height));
    }

    private void updateHoverState(int mouseX, int mouseY) {
        boolean hoveredAny = false;
        for (UIButton button : buttons) {
            Rect bounds = button.getLastRenderRect();
            boolean hovered = bounds.contains(mouseX, mouseY);
            button.setHovered(hovered);
            if (hovered) {
                hoveredAny = true;
                statusLabel.setText("Hover: " + buttonDescriptions.get(button));
            }
        }

        if (!hoveredAny) {
            statusLabel.setText(persistentStatus);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean consumed = false;

        Rect searchBounds = searchField.getLastRenderRect();
        boolean focusSearch = searchBounds.contains(mouseX, mouseY);
        searchField.setFocused(focusSearch);
        if (focusSearch) {
            consumed = true;
        }

        for (Map.Entry<UIButton, String> entry : buttonDescriptions.entrySet()) {
            UIButton uiButton = entry.getKey();
            if (uiButton.getLastRenderRect().contains(mouseX, mouseY)) {
                uiButton.click();
                consumed = true;
                break;
            }
        }

        return consumed || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == Keyboard.KEY_ESCAPE || keyCode == GLFW_KEY_ESCAPE) {
            Minecraft.getInstance().setScreen(null);
            return true;
        }

        if (searchField.isFocused() && searchField.handleKeyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }

        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (searchField.isFocused() && searchField.handleCharTyped(codePoint, modifiers)) {
            return true;
        }
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
