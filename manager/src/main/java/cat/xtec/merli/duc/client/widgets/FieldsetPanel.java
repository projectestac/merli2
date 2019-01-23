package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.ui.HTMLPanel;


/**
 * An simple {@code HTMLPanel} that uses a &lt;fieldset&gt; tag as the
 * root HTML element for the panel.
 */
public class FieldsetPanel extends HTMLPanel {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-FieldsetPanel";


    /**
     * {@inheritDoc}
     */
    public FieldsetPanel(String html) {
        super("fieldset", html);
        setStylePrimaryName(STYLE_NAME);
    }

}
