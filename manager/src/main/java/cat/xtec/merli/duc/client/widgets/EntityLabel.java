package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.Element;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.ui.Label;

import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.UID;
import cat.xtec.merli.domain.voc.Language;


/**
 * A label that represents an entity.
 */
public class EntityLabel extends Label {

    /** Primary CSS style for this object */
    public static final String STYLE_NAME = "duc-EntityLabel";

    /** Nil entity value */
    private static final Entity NIL_VALUE = new Entity();

    /** Nil entity label value */
    private static final LangString NIL_TITLE = new LangString();

    /** Entity value */
    private Entity entity;


    /**
     * Creates a new empty label.
     */
    public EntityLabel() {
        this(null);
    }


    /**
     * Creates a label for the given entity;
     *
     * @param entity        Entity instance
     */
    public EntityLabel(Entity entity) {
        super();
        setEntity(entity);
    }


    /**
     * Obtains the entity of this label.
     */
    public Entity getEntity() {
        return entity;
    }


    /**
     * Sets the entity of this label.
     *
     * @param entity        Entity instance
     */
    public void setEntity(Entity entity) {
        this.entity = (entity != null) ? entity : NIL_VALUE;
        this.refreshView(this.entity);
    }


    /**
     * Updates the language attribute of this item. If the provided
     * language is null or equal to the GUI's active locale the 'lang'
     * attribute on the element will be removed; otherwise its value
     * will be set to the provided language code.
     *
     * @param language      Language instance
     */
    private void setLanguage(Language language) {
        Element element = getElement();
        String active = getGUILocaleCode();

        if (language instanceof Language == false) {
            element.removeAttribute("lang");
        } else if (active.equals(language.value())) {
            element.removeAttribute("lang");
        } else {
            String code = language.value();
            element.setAttribute("lang", code);
        }
    }


    /**
     * Obtains the currently active locale code of the graphical user
     * interface. This method may return "default" if the interface
     * locale was not set explicitly.
     *
     * @return      Locale code
     */
    private static String getGUILocaleCode() {
        LocaleInfo locale = LocaleInfo.getCurrentLocale();
        return locale.getLocaleName();
    }


    /**
     * Returns a language string for the given entity.
     *
     * @param entity    Entity instance
     * @return          Language string
     */
    public static LangString getTitleFor(Entity entity) {
        final String code = getGUILocaleCode();
        final LangString title = entity.getLabel(code);

        return (title instanceof LangString) ?
            title : NIL_TITLE;
    }


    /**
     * Returns a style name for the given entity.
     *
     * @param entity    Entity instance
     * @return          Style name
     */
    public static String getStyleNameFor(Entity entity) {
        final EntityType type = entity.getType();
        final UID id = entity.getId();

        if (type instanceof EntityType) {
            return "duc-" + type.name();
        }

        if (id instanceof UID) {
            return "duc-ANY_TYPE";
        }

        return "duc-EDIT_BOX";
    }


    /**
     * Refreshes this widget's view properties.
     *
     * @param entity        Entity instance
     */
    private void refreshView(Entity entity) {
        LangString title = getTitleFor(entity);
        String style = getStyleNameFor(entity);

        setText(title.getText());
        setTitle(title.getText());
        setLanguage(title.getLanguage());
        setStyleName(STYLE_NAME);
        addStyleName(style);
    }

}
