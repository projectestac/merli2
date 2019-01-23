package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.ui.TextArea;


/**
 * A widget that extends a text area.
 */
public class TextAreaBox extends TextArea {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-TextArea";


    /**
     * Creates an empty text area.
     */
    public TextAreaBox() {
        super();
        addStyleName(STYLE_NAME);
    }


    /**
     * Gets the placeholder attribute of this widget.
     *
     * @return          Placeholder text
     */
    public String getPlaceholder() {
        return getElement().getAttribute("placeholder");
    }


    /**
     * Sets the placeholder attribute of this widget.
     *
     * @param text      Placeholder text
     */
    public void setPlaceholder(String text) {
        getElement().setAttribute("placeholder", text);
    }

}
