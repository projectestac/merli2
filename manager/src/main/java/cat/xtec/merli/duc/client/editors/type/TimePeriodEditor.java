package cat.xtec.merli.duc.client.editors.type;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.type.TimePeriod;


/**
 * TODO: REFACTOR
 */
public class TimePeriodEditor extends Composite implements Editor<TimePeriod> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-TimePeriodEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("TimePeriodEditor.ui.xml")
    interface Binder extends UiBinder<Widget, TimePeriodEditor> {}


    /**
     *
     */
    public TimePeriodEditor() {
        initWidget(binder.createAndBindUi(this));
        setStylePrimaryName(STYLE_NAME);
    }

}
