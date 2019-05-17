package cat.xtec.merli.duc.client.editors.type;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.type.TimePoint;


/**
 * TODO: REFACTOR
 */
public class TimePointEditor extends Composite implements Editor<TimePoint> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-TimePointEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("TimePointEditor.ui.xml")
    interface Binder extends UiBinder<Widget, TimePointEditor> {}


    /**
     *
     */
    public TimePointEditor() {
        initWidget(binder.createAndBindUi(this));
        setStylePrimaryName(STYLE_NAME);
    }

}
