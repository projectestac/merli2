package cat.xtec.merli.duc.client.editors.taxa;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.taxa.Category;
import cat.xtec.merli.duc.client.editors.voc.KnowledgeEditor;
import cat.xtec.merli.duc.client.editors.type.CategoryTypeEditor;
import cat.xtec.merli.duc.client.editors.lists.CategoryListEditor;
import cat.xtec.merli.duc.client.editors.lists.TermListEditor;
import cat.xtec.merli.duc.client.editors.lists.LangStringListEditor;
import cat.xtec.merli.duc.client.widgets.TextAreaBox;


/**
 * An educational content category.
 */
public class CategoryEditor extends Composite implements Editor<Category> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-CategoryEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("CategoryEditor.ui.xml")
    interface Binder extends UiBinder<Widget, CategoryEditor> {}

    /** Entity type */
    @UiField CategoryTypeEditor type;

    /** Label for the category */
    @UiField LangStringListEditor labels;

    /** Meaning and usage of the category */
    @UiField TextAreaBox description;

    /** Observations */
    @UiField TextAreaBox observation;

    /** References */
    @UiField TextAreaBox reference;

    /** Type of curricular knowledge  */
    @UiField KnowledgeEditor knowledge;

    /** Categories this object is a subclass-of */
    @UiField CategoryListEditor parents;

    /** Keywords that describe this node */
    @UiField TermListEditor keywords;


    /**
     *
     */
    public CategoryEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }

}
