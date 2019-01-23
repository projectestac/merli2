package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;


/**
 *
 */
public class FlexSpace extends Widget {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-FlexSpace";


    /**
     *
     */
    public FlexSpace() {
        super();
        setElement(DOM.createDiv());
        setStyleName(STYLE_NAME);
    }

}
