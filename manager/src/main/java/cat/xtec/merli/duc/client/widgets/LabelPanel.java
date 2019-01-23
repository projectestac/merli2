package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.ui.HTMLPanel;


/**
 * An simple {@code HTMLPanel} that uses a &lt;label&gt; tag as the root
 * HTML element for the panel.
 */
public class LabelPanel extends HTMLPanel {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-LabelPanel";


    /**
     * {@inheritDoc}
     */
    public LabelPanel() {
        this("");
    }


    /**
     * {@inheritDoc}
     */
    public LabelPanel(String html) {
        super("label", html);
        setStylePrimaryName(STYLE_NAME);
    }

}
