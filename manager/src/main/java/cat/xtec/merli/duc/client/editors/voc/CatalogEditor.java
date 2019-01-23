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
        setStylePrimaryName(STYLE_NAME);
    }

}