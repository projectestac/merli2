package cat.xtec.merli.duc.client.editors.taxa;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.RelationType;


/**
 * Specific kind of resource (LOMv1.0).
 */
public class RelationTypeEditor extends ListBoxEditor<RelationType> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-RelationTypeEditor";


    /**
     * Constructs a relation type editor.
     */
    public RelationTypeEditor() {
        this(RelationType.values());
    }


    /**
     * Constructs a relation type editor restricted to the given
     * entity class.
     *
     * @param type          Entity class
     */
    public <T extends Entity> RelationTypeEditor(Class<T> type) {
        this(RelationType.valuesFor(type));
    }


    /**
     * Constructs a relation type editor initialized with the
     * given set of enumeration constants.
     *
     * @param values        Enumeration constants
     */
    public RelationTypeEditor(RelationType[] values) {
        super(values);
        this.refreshView();
        options.addChangeHandler(e -> refreshView());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(RelationType value) {
        super.setValue(value);
        this.refreshView();
    }


    /**
     * Refreshes this editor's view properties.
     */
    protected void refreshView() {
        RelationType type = getValue();

        setStyleName(STYLE_NAME);
        addStyleName(getStyleNameFor(type));
    }


    /**
     * Returns a style name for the given relation type.
     *
     * @param type      Relation type
     * @return          Style name
     */
    protected String getStyleNameFor(RelationType type) {
        return (type instanceof RelationType) ?
            "duc-RELATION" : "duc-EDIT_BOX";
    }

}
