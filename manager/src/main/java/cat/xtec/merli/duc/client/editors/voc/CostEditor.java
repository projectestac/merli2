package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.RadioButtonEditor;
import cat.xtec.merli.domain.voc.Cost;


/**
 * Whether use of the resource requires payment (LOMv1.0).
 */
public class CostEditor extends RadioButtonEditor<Cost> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-CostEditor";

    /** Group name for this radio button editor */
    public static final String GROUP_NAME = "cost";


    /**
     * Constructs a Cost editor.
     */
    public CostEditor() {
        super(Cost.class, GROUP_NAME);
        setStylePrimaryName(STYLE_NAME);
    }

}