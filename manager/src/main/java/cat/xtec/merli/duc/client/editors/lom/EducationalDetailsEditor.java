package cat.xtec.merli.duc.client.editors.lom;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.lom.EducationalDetails;
import cat.xtec.merli.duc.client.editors.voc.ContextEditor;
import cat.xtec.merli.duc.client.editors.voc.UserRoleEditor;
import cat.xtec.merli.duc.client.editors.voc.ResourceTypeEditor;
import cat.xtec.merli.duc.client.editors.lists.LanguageListEditor;
import cat.xtec.merli.duc.client.editors.lists.LangStringListEditor;


/**
 * Pedagogical and educational characteristics.
 */
public class EducationalDetailsEditor extends Composite
    implements Editor<EducationalDetails> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-EducationalDetailsEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("EducationalDetailsEditor.ui.xml")
    interface Binder extends UiBinder<Widget, EducationalDetailsEditor> {}

    /** How the learning object is to be used */
    @UiField LangStringListEditor descriptions;

    /** User's natural languages */
    @UiField LanguageListEditor languages;

    /** Normal users of the learning object */
    @UiField UserRoleEditor userRoles;

    /** Typical learning environment */
    @UiField ContextEditor contexts;

    /** Specific kinds of resources */
    @UiField ResourceTypeEditor resourceTypes;


    /**
     *
     */
    public EducationalDetailsEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }

}
