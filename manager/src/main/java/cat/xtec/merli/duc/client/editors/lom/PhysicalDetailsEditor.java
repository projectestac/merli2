package cat.xtec.merli.duc.client.editors.lom;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.lom.PhysicalDetails;
import cat.xtec.merli.duc.client.editors.lists.MediumListEditor;
import cat.xtec.merli.duc.client.editors.lists.LangStringListEditor;


/**
 * Technical requirements and characteristics of a learning object.
 */
public class PhysicalDetailsEditor extends Composite
    implements Editor<PhysicalDetails> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-PhysicalDetailsEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("PhysicalDetailsEditor.ui.xml")
    interface Binder extends UiBinder<Widget, PhysicalDetailsEditor> {}

    /** Physical characteristics of the learning object */
    @UiField LangStringListEditor descriptions;

    /** Physical types of the the learning object */
    @UiField MediumListEditor mediums;


    /**
     *
     */
    public PhysicalDetailsEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }

}
