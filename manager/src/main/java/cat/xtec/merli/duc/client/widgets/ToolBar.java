package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.ui.FlowPanel;


/**
 * A widget that represents a toolbar.
 */
public class ToolBar extends FlowPanel {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ToolBar";


    /**
     * Creates a new toolbar.
     */
    public ToolBar() {
        super();
        setStylePrimaryName(STYLE_NAME);
    }

}
