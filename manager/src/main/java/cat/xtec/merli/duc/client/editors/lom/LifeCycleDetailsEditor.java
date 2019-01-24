package cat.xtec.merli.duc.client.editors.lom;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.lom.LifeCycleDetails;
import cat.xtec.merli.duc.client.editors.lists.ContributionListEditor;
import cat.xtec.merli.duc.client.editors.lists.LangStringListEditor;
import cat.xtec.merli.duc.client.editors.voc.StatusEditor;


/**
 * Features related to the history and current state of a learning object
 * and those who have affected it during its evolution.
 */
public class LifeCycleDetailsEditor extends Composite implements Editor<LifeCycleDetails> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-LifeCycleDetailsEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("LifeCycleDetailsEditor.ui.xml")
    interface Binder extends UiBinder<Widget, LifeCycleDetailsEditor> {}

    /** Edition of the learning object */
    @UiField LangStringListEditor versions;

    /** State or condition of the learning object */
    @UiField StatusEditor status;

    /** Contributors to the learning object metadata */
    @UiField ContributionListEditor contributions;


    /**
     *
     */
    public LifeCycleDetailsEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }

}
