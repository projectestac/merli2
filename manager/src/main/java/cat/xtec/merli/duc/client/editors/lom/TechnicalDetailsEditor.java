package cat.xtec.merli.duc.client.editors.lom;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.lom.TechnicalDetails;
import cat.xtec.merli.duc.client.editors.URIEditor;
import cat.xtec.merli.duc.client.editors.DurationEditor;
import cat.xtec.merli.duc.client.editors.lists.FormatListEditor;
import cat.xtec.merli.duc.client.widgets.TextAreaBox;


/**
 * Technical requirements and characteristics of a learning object.
 */
public class TechnicalDetailsEditor extends Composite implements Editor<TechnicalDetails> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-TechnicalDetailsEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("TechnicalDetailsEditor.ui.xml")
    interface Binder extends UiBinder<Widget, TechnicalDetailsEditor> {}

    /** Duration of the learning object */
    // @UiField DurationEditor duration;

    /** Media types of the learning object */
    @UiField FormatListEditor formats;

    /** URIs of the learning object */
    @UiField URIEditor location;

    /** Physical characteristics of the learning object */
    // @UiField TextAreaBox medium;


    /**
     *
     */
    public TechnicalDetailsEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }

}
