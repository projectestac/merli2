package cat.xtec.merli.duc.client.widgets;


/**
 * An input box with a status indicator.
 */
public class StringBox extends InputBox {

    /** CSS style for this widget */
    public static final String STYLE_NAME = "duc-StringBox";


    /**
     * Creates an empty input box.
     */
    public StringBox() {
        super();
        updateStyleNames();
        addKeyUpHandler(e -> updateStyleNames());
    }


    /**
     * Updates this editor CSS styles..
     */
    protected void updateStyleNames() {
        String value = getValue();
        String styleName = getStyleNameFor(value);

        setStyleName(InputBox.STYLE_NAME);
        addStyleName(STYLE_NAME);
        addStyleName(styleName);
    }


    /**
     * Returns a style name for the given value.
     *
     * @param value     String value
     * @return          Style name
     */
    protected String getStyleNameFor(String value) {
        return (value.trim().isEmpty() == false)?
            "duc-STRING" : "duc-EDIT_BOX";
    }

}
