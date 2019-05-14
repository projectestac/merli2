package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.ui.TreeItem;
import org.semanticweb.owlapi.model.IRI;
import cat.xtec.merli.domain.taxa.Entity;


/**
 * Represents an entity on a tree.
 */
public class EntityTreeItem extends TreeItem {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-EntityTreeItem";

    /** This tree item's widget */
    private final EntityLabel label = new EntityLabel();

    /** This tree item's entity IRI identifier */
    private IRI iri = null;


    /**
     * Constructs a new tree node for the given entity.
     *
     * @param iri       Entity IRI identifier
     * @param entity    Entity instance
     */
    public EntityTreeItem(IRI iri, Entity entity) {
        super();
        setWidget(label);
        setEntity(iri, entity);
        getElement().addClassName(STYLE_NAME);
    }


    /**
     * Obtains the IRI for this item.
     *
     * @return          IRI instance
     */
    public IRI getIRI() {
        return this.iri;
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
     * @param iri       Enity IRI identifier
     * @param entity    Entity instance
     */
    public void setEntity(IRI iri, Entity entity) {
        this.iri = iri;
        label.setEntity(entity);
        setUserObject(entity);
    }


    /**
     * Adds a child item to this node.
     *
     * @param node      Entity instance
     * @return          The new item
     */
    public TreeItem addItem(IRI iri, Entity node) {
        TreeItem item = new EntityTreeItem(iri, node);
        this.addItem(item);

        return item;
    }

}
