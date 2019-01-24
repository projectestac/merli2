package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.TextBox;


/**
 * A widget that extends a text box.
 */
public class InputBox extends TextBox {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-InputBox";


    /**
     * Creates an empty input box.
     */
    public InputBox() {
        super(DOM.createInputText());
        setStylePrimaryName(STYLE_NAME);
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
