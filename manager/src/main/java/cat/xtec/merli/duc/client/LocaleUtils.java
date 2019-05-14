package cat.xtec.merli.duc.client;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.google.gwt.i18n.client.LocaleInfo;

import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.domain.taxa.Relation;
import cat.xtec.merli.domain.taxa.RelationType;
import cat.xtec.merli.domain.type.LangString;


/**
 * Utilities to work with locales. Provides methods to compare string
 * and entities lexicographically.
 */
public final class LocaleUtils {

    /** A comparator used to sort entities */
    private static Comparator<Entity> ENTITY_COMPARATOR =
        (Entity a, Entity z) -> compare(a, z);

    /** A comparator used to sort relations */
    private static Comparator<Relation> RELATION_COMPARATOR =
        (Relation a, Relation z) -> compare(a, z);


    /**
     * Obtains a localized label for the given entity. That is the
     * entity's label tagged with the current GUI locale code or any
     * other label if none match the current locale.
     *
     * @param entity    Entity instance
     * @return          Language string or null
     */
    public static LangString getLabelFor(Entity entity) {
        if (entity instanceof Entity) {
            String code = getGUILocaleCode();
            return entity.getLabel(code);
        }

        return null;
    }


    /**
     * Obtains a localized string for the given entity. That is
     * the string for the entity's label tagged with the current
     * GUI locale code.
     *
     * This method always returns a string, even if no label for the
     * current GUI locale exists on the entity, a best-match string
     * will be returned or an empty string if the entity does not
     * contain any labels.
     *
     * @see             Entity#getLabel(String)
     * @param entity    Entity instance
     * @return          A string instance
     */
    public static String getStringFor(Entity entity) {
        String text = LangString.EMPTY_TEXT;

        if (entity instanceof Entity) {
            String code = getGUILocaleCode();
            LangString label = entity.getLabel(code);

            if (label instanceof LangString) {
                text = label.getText();
            }
        }

        return text;
    }


    /**
     * Obtains the currently active locale code of the graphical user
     * interface. This method may return "default" if the interface
     * locale was not set explicitly.
     *
     * @return          Locale code
     */
    public static String getGUILocaleCode() {
        LocaleInfo locale = LocaleInfo.getCurrentLocale();
        return locale.getLocaleName();
    }


    /**
     * Compares two strings for order. The current GUI locale is used
     * to perform a locale-sensitive comparision of the strings.
     *
     * @param a         First string
     * @param z         Second string
     * @return          Comparision value
     */
    public static int compare(String a, String z) {
        return compare(a, z, getGUILocaleCode());
    }


    /**
     * Compares two entity types for order. The ordinal of the type
     * on the enumeration is used for the comparision.
     *
     * @param a         First entity type
     * @param z         Second entity type
     * @return          Comparision value
     */
    public static int compare(EntityType a, EntityType z) {
        return (a == z) ? 0 : (a != null) ? a.compareTo(z) : 1;
    }


    /**
     * Compares two relation types for order. The ordinal of the type
     * on the enumeration is used for the comparision.
     *
     * @param a         First relation type
     * @param z         Second relation type
     * @return          Comparision value
     */
    public static int compare(RelationType a, RelationType z) {
        return (a == z) ? 0 : (a != null) ? a.compareTo(z) : 1;
    }


    /**
     * Compares two entities for order. First, the entity types are
     * compared and if they are equal their labels are compared using
     * the current GUI locale.
     *
     * @param a         First entity
     * @param z         Second entity
     * @return          Comparision value
     */
    public static int compare(Entity a, Entity z) {
        int result = 0;

        try {
            EntityType at = a.getType();
            EntityType zt = z.getType();
            result = compare(at, zt);

            if (result == 0) {
                String as = getStringFor(a);
                String zs = getStringFor(z);
                result = compare(as, zs);
            }
        } catch (Exception e) {}

        return result;
    }


    /**
     * Compares two entities for order. First, the entity types are
     * compared and if they are equal their labels are compared using
     * the current GUI locale.
     *
     * @param a         First entity
     * @param z         Second entity
     * @return          Comparision value
     */
    public static int compare(Relation a, Relation z) {
        int result = 0;

        try {
            RelationType at = a.getType();
            RelationType zt = z.getType();
            result = compare(at, zt);

            if (result == 0) {
                Entity ae = a.getTarget();
                Entity ze = z.getTarget();
                result = compare(ae, ze);
            }
        } catch (Exception e) {}

        return result;
    }


    /**
     * Sorts a list of entities using the default comparator, which
     * takes into account the entity types and their labels.
     *
     * @param items     Entities to sort
     */
    public static void sortEntites(List<Entity> items) {
        Collections.sort(items, ENTITY_COMPARATOR);
    }


    /**
     * Sorts a list of entity relations using the default comparator,
     * which takes into account the relation type, the target entity
     * types and their labels.
     *
     * @param items     Relations to sort
     */
    public static void sortRelations(List<Relation> items) {
        Collections.sort(items, RELATION_COMPARATOR);
    }


    /**
     * Performs a locale sensitive comparision of two strings. If the
     * browser supports {@code Intl.Collator} it is used, otherwise
     * defaults to a unicode character comparision.
     *
     * @see             Comparable#compareTo(Object)
     * @param a         First string
     * @param z         Second string
     * @param locale    Locale code
     *
     * @return          Comparision value
     */
    public static native int compare(String a, String z, String locale) /*-{
        try {
            return a.localeCompare(z, locale,
                { sensitivity: 'base' });
        } catch (e) {}

        return (a < z) ? -1 : (a > z) ? 1 : 0;
    }-*/;

}
