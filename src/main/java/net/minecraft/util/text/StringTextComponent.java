package net.minecraft.util.text;

/**
 * Basic immutable text component containing a plain string.
 */
public class StringTextComponent implements ITextComponent {

    private final String text;

    public StringTextComponent(String text) {
        this.text = text;
    }

    @Override
    public String getString() {
        return text;
    }
}
