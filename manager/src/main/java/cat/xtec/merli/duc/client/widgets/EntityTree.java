package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import org.semanticweb.owlapi.model.IRI;
import cat.xtec.merli.domain.taxa.Entity;


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
    public TreeItem addItem(IRI iri, Entity node) {
        TreeItem item = new EntityTreeItem(iri, node);
        this.addItem(item);

        return item;
    }

}
