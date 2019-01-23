package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.i18n.client.HasDirection.Direction;

import cat.xtec.merli.domain.EnumString;
import cat.xtec.merli.domain.type.LangString;


/**
 * This widget is a label that accepts {@code EnumString} and
 * {@code LangString} objects as text values.
 */
public class StringLabel extends Label {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-EnumLabel";


    /**
     * Creates an empty label.
     */
    public StringLabel() {
        this(LangString.EMPTY_TEXT);
    }


    /**
     * Creates a label with a text.
     */
    public StringLabel(String text) {
        super(text);
        addStyleName(STYLE_NAME);
    }


    /**
     * Creates a label for an enumeration constant.
     */
    public StringLabel(EnumString string) {
        this(string.value());
    }


    /**
     * Creates a label for a language string.
     */
    public StringLabel(LangString string) {
        this(string.getText());
    }


    /**
     * Sets this label's text given an enumeration string.
     *
     * @param string        Enumeration string
     */
    public void setText(EnumString string) {
        this.setText(string.value());
    }


    /**
     * Sets this label's text given an language string.
     *
     * @param string        Language string
     */
    public void setText(LangString string) {
        this.setText(string.getText());
    }


    /**
     * Sets this label's text given an enumeration string.
     *
     * @param string        Enumeration string
     * @param direction     Text direction
     */
    public void setText(EnumString string, Direction direction) {
        this.setText(string.value(), direction);
    }


    /**
     * Sets this label's text given an language string.
     *
     * @param string        Language string
     * @param direction     Text direction
     */
    public void setText(LangString string, Direction direction) {
        this.setText(string.getText(), direction);
    }

}
