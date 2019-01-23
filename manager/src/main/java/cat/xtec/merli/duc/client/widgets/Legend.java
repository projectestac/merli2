package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Label;


/**
 * A {@code Label} widget tepresented by a &lt;legent&gt; element.
 *
 * {@inheritDoc}
 */
public class Legend extends Label {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-Legend";


    /**
     * {@inheritDoc}
     */
    public Legend() {
        super(Document.get().createLegendElement());
        setStylePrimaryName(STYLE_NAME);
    }

}
