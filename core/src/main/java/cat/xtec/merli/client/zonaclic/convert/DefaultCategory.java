package cat.xtec.merli.client.zonaclic.convert;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.taxa.Category;


/**
 * Default resource classification categories. This categories will be
 * attached to all the resources converted from projects.
 */
public enum DefaultCategory {

    /** Tecnologia educativa > Tecnologies de l'aprenentatge... */
    LEARNING_TECHNOLOGIES(UID.from(39541));

    /** Category value */
    private final Category category;


    /**
     * Enumeration constructor
     *
     * @param id        Category identifier
     */
    DefaultCategory(UID id) {
        category = new Category(id);
    }


    /**
     * Returns the category identifier.
     */
    public UID identifier() {
        return category.getUID();
    }


    /**
     * Returns the category instance.
     */
    public Category category() {
        return category;
    }


    /**
     * Returns a defined category given an identifier.
     *
     * @param identifier        Category identifier
     * @throws IllegalArgumentException
     */
    public static Category category(UID identifier) {
        for (DefaultCategory object : DefaultCategory.values()) {
            if (identifier.equals(object.identifier()))
                return object.category();
        }

        throw new IllegalArgumentException();
    }

}
