package cat.xtec.merli.duc.client.editors.lom;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.duc.client.editors.lists.CategoryListEditor;
import cat.xtec.merli.duc.client.editors.lists.ResourceRelationListEditor;


/**
 * Learning object.
 */
public class ResourceEditor extends Composite implements Editor<Resource> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ResourceEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("ResourceEditor.ui.xml")
    interface Binder extends UiBinder<Widget, ResourceEditor> {}

    /** General information */
    @UiField GeneralDetailsEditor generalDetails;

    /** History and current state */
    @UiField LifeCycleDetailsEditor lifeCycleDetails;

    /** Technical characteristics */
    @UiField TechnicalDetailsEditor technicalDetails;

    /** Physical characteristics */
    @UiField PhysicalDetailsEditor physicalDetails;

    /** Pedagogical and educational characteristics */
    @UiField EducationalDetailsEditor educationalDetails;

    /** Conditions of use */
    @UiField RightsDetailsEditor rightsDetails;

    /** Categories this object is a subclass-of */
    @UiField CategoryListEditor parents;

    /** Relations with other resources */
    @UiField ResourceRelationListEditor relations;


    /**
     *
     */
    public ResourceEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }

}
