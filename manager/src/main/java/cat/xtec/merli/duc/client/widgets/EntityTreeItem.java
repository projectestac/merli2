package cat.xtec.merli.duc.client.widgets;

import java.util.List;
import com.google.gwt.user.client.ui.TreeItem;
import cat.xtec.merli.domain.taxa.Entity;


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
    public TreeItem addItem(Entity node) {
        TreeItem item = new EntityTreeItem(node);
        this.addItem(item);

        return item;
    }


    /**
     * Adds multiple child items to this node.
     *
     * @param nodes     List of entity graph vertices
     */
    public void addTreeItems(List<Entity> nodes) {
        for (Entity node : nodes) {
            addItem(node);
        }
    }

}
