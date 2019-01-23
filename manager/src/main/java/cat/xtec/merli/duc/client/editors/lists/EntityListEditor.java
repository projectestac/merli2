package cat.xtec.merli.duc.client.editors.lists;

import com.google.gwt.editor.client.Editor;
import cat.xtec.merli.duc.client.editors.taxa.TargetEditor;
import cat.xtec.merli.domain.taxa.Entity;


/**
 * Editor for a dynamic list of entities.
 */
public class EntityListEditor extends DynamicListEditor<Entity, TargetEditor> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-EntityListEditor";

    /** Entity class restriction */
    private Class<? extends Entity> group;


    /**
     * Editor constructor.
     */
    public EntityListEditor() {
        this(Entity.class);
    }


    /**
     * Creates a new entity list editor restricted to the given entity
     * type. Only entities for the provided type will be selectable.
     *
     * @param group         Entity class
     */
    public <T extends Entity> EntityListEditor(Class<T> group) {
        super();
        this.group = group;
        addStyleName(STYLE_NAME);
    }


    /**
     * Creates a new subeditor instance.
     *
     * @return          A new editor instance
     */
    @Editor.Ignore
    protected TargetEditor createSubeditor() {
        return new TargetEditor(group);
    }

}
