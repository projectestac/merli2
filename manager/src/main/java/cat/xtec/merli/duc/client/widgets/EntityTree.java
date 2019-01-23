package cat.xtec.merli.duc.client.widgets;

import java.util.List;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import cat.xtec.merli.domain.taxa.Vertex;


/**
 * A tree widget to represent entity graphs.
 */
public class EntityTree extends Tree {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-EntityTree";


    /**
     * Constructs an empty tree.
     */
    public EntityTree() {
        setStylePrimaryName(STYLE_NAME);
        setAnimationEnabled(true);
    }


    /**
     * Adds an item to the root of the tree.
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
     * Adds multiple items to the root of the tree.
     *
     * @param nodes     List of entity graph vertices
     */
    public void addTreeItems(List<Vertex> nodes) {
        for (Vertex node : nodes) {
            addItem(node);
        }
    }

}
