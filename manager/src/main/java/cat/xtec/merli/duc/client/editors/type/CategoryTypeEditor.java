package cat.xtec.merli.duc.client.editors.type;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.domain.taxa.Category;


/**
 * Category type {@code SelectBox} editor.
 */
public class CategoryTypeEditor extends ListBoxEditor<EntityType> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-CategoryTypeEditor";


    /**
     * Constructs a category type editor.
     */
    public CategoryTypeEditor() {
        super(EntityType.valuesFor(Category.class));
        setStylePrimaryName(STYLE_NAME);
    }

}
