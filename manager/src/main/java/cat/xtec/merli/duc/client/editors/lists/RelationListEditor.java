package cat.xtec.merli.duc.client.editors.lists;

import com.google.gwt.editor.client.Editor;
import cat.xtec.merli.duc.client.editors.taxa.RelationEditor;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.Relation;


/**
 * Editor for a dynamic list of relationships.
 */
public class RelationListEditor extends DynamicListEditor<Relation, RelationEditor> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-RelationListEditor";

    /** Entity class restriction */
    private Class<? extends Entity> group;


    /**
     * Editor constructor.
     */
    public RelationListEditor() {
        this(Entity.class);
    }


    /**
     * Creates a new relation list editor restricted to the given entity
     * type. Only relations for provided entity type will be selectable.
     *
     * @param group         Entity class
     */
    public <T extends Entity> RelationListEditor(Class<T> group) {
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
    protected RelationEditor createSubeditor() {
        return new RelationEditor(group);
    }

}
