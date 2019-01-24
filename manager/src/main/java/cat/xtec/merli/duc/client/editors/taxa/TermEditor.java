package cat.xtec.merli.duc.client.editors.taxa;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.taxa.Term;
import cat.xtec.merli.duc.client.editors.lists.TermListEditor;
import cat.xtec.merli.duc.client.editors.lists.TermRelationListEditor;
import cat.xtec.merli.duc.client.editors.lists.LangStringListEditor;
import cat.xtec.merli.duc.client.widgets.TextAreaBox;


/**
 * A vocabulary item.
 */
public class TermEditor extends Composite implements Editor<Term> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-TermEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("TermEditor.ui.xml")
    interface Binder extends UiBinder<Widget, TermEditor> {}

    /** Entity type */
    @UiField TermTypeEditor type;

    /** Label for the term */
    @UiField LangStringListEditor labels;

    /** Meaning of the term and its usage */
    @UiField TextAreaBox description;

    /** Term this object is a subclass-of */
    @UiField TermListEditor parents;

    /** Relationships originating from this term */
    @UiField TermRelationListEditor relations;


    /**
     *
     */
    public TermEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }

}
