package cat.xtec.merli.duc.client.editors.lists;

import cat.xtec.merli.domain.taxa.Category;


/**
 * Editor for a dynamic list of categories.
 */
public class CategoryListEditor extends EntityListEditor {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-CategoryListEditor";

    /**
     * Editor constructor.
     */
    public CategoryListEditor() {
        super(Category.class);
        addStyleName(STYLE_NAME);
    }

}
