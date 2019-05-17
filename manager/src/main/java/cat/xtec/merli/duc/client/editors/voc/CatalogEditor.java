package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.Catalog;


/**
 * The state or condition of a learning object (LOMv1.0).
 */
public class CatalogEditor extends ListBoxEditor<Catalog> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-CatalogEditor";


    /**
     * Constructs a Catalog editor.
     */
    public CatalogEditor() {
        super(Catalog.class);
        this.refreshView();
        options.addChangeHandler(e -> refreshView());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Catalog value) {
        super.setValue(value);
        this.refreshView();
    }


    /**
     * Refreshes this editor's view properties.
     */
    protected void refreshView() {
        Catalog value = getValue();

        setStyleName(STYLE_NAME);
        addStyleName(getStyleNameFor(value));
    }


    /**
     * Returns a style name for the given relation type.
     *
     * @param type      Relation type
     * @return          Style name
     */
    protected String getStyleNameFor(Catalog type) {
        return (type instanceof Catalog) ?
            "duc-CATALOG" : "duc-EDIT_BOX";
    }

}
