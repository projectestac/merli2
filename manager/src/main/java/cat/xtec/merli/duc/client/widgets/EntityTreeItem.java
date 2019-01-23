package cat.xtec.merli.duc.client.widgets;

import java.util.List;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TreeItem;

import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.domain.taxa.Vertex;
import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.voc.Language;


/**
 * Represents an entity on a tree.
 */
public class EntityTreeItem extends TreeItem {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-EntityTreeItem";

    /** This tree item's widget */
    private final EntityLabel label = new EntityLabel();


    /**
     * Constructs a new tree node from the given entity.
     */
    public EntityTreeItem(Entity entity) {
        super();
        setWidget(label);
        setEntity(entity);
        getElement().addClassName(STYLE_NAME);
    }


    /**
     * Constructs a new tree node from the given vertex.
     */
    public EntityTreeItem(Vertex node) {
        this(node.getEntity());
    }


    /**
     * Obtains the entity associated with this item.
     *
     * @return          Entity instance
     */
    public Entity getEntity() {
        return (Entity) getUserObject();
    }


    /**
     * Sets the entity associated with this item.
     *
     * @param entity    Entity instance
     */
    public void setEntity(Entity entity) {
        label.setEntity(entity);
        setUserObject(entity);
    }


    /**
     * Adds a child item to this node.
     *
     * @param node      Entity graph vertex
     * @return          The new item
     */
    public TreeItem addItem(Vertex node) {
        TreeItem item = new EntityTreeItem(node);
        this.addItem(item);

        return item;
    }


    /**
     * Adds multiple child items to this node.
     *
     * @param nodes     List of entity graph vertices
     */
    public void addTreeItems(List<Vertex> nodes) {
        for (Vertex node : nodes) {
            addItem(node);
        }
    }

}
