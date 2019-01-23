package cat.xtec.merli.duc.client.editors.lom;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.lom.GeneralDetails;
import cat.xtec.merli.domain.taxa.Category;
import cat.xtec.merli.duc.client.editors.voc.StructureEditor;
import cat.xtec.merli.duc.client.editors.lists.IdentifierListEditor;
import cat.xtec.merli.duc.client.editors.lists.LangStringListEditor;
import cat.xtec.merli.duc.client.editors.lists.TermListEditor;
import cat.xtec.merli.duc.client.widgets.InputBox;
import cat.xtec.merli.duc.client.widgets.TextAreaBox;


/**
 * General information that describes a learning object as a whole.
 */
public class GeneralDetailsEditor extends Composite
    implements Editor<GeneralDetails> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-GeneralDetailsEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("GeneralDetailsEditor.ui.xml")
    interface Binder extends UiBinder<Widget, GeneralDetailsEditor> {}

    /** Description of the content */
    // @UiField TextAreaBox description;

    /** Name of the learning object */
    @UiField LangStringListEditor titles;

    /** Organizational structure of the learning object */
    @UiField StructureEditor structure;

    /** Catalog entries for the learning object */
    @UiField IdentifierListEditor identifiers;

    /** Keywords that describe this learning object */
    @UiField TermListEditor keywords;


    /**
     *
     */
    public GeneralDetailsEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }

}
