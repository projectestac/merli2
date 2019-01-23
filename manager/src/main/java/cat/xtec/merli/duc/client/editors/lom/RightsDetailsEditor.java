package cat.xtec.merli.duc.client.editors.lom;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.lom.RightsDetails;
import cat.xtec.merli.duc.client.editors.voc.CopyrightEditor;
import cat.xtec.merli.duc.client.editors.voc.CostEditor;
import cat.xtec.merli.duc.client.widgets.TextAreaBox;


/**
 * Conditions of use of the resource
 */
public class RightsDetailsEditor extends Composite implements Editor<RightsDetails> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-RightsDetailsEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("RightsDetailsEditor.ui.xml")
    interface Binder extends UiBinder<Widget, RightsDetailsEditor> {}

    /** Whether use of the resource requires payment */
    @UiField CostEditor cost;

    /** Whether copyright or other restrictions apply */
    @UiField CopyrightEditor copyright;

    /** Comments on the conditions of use of the resource */
    // @UiField TextAreaBox description;


    /**
     *
     */
    public RightsDetailsEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }

}
