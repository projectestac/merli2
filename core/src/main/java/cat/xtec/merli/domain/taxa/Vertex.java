package cat.xtec.merli.domain.taxa;

import java.io.Serializable;


/**
 * Represents an entity node on a graph. This is a convenience object
 * that encapsulates an enity along with its number of children.
 */
public final class Vertex implements Serializable {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** Entity for the node */
    private Entity entity;

    /** Number of children the entity has */
    private int childCount;


    /**
     * Empty constructor.
     */
    public Vertex() {}


    /**
     * Returns the entity for this node.
     */
    public Entity getEntity() {
        return entity;
    }


    /**
     * Sets the entity for this node.
     */
    public void setEntity(Entity entity) {
        this.entity = entity;
    }


    /**
     * Returns the number of children of the entity.
     *
     * @return      Number of childs
     */
    public int getChildCount() {
        return childCount;
    }


    /**
     * Returns the number of children of the entity.
     *
     * @return      Number of childs
     */
    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }


    /**
     * Returns if this node is a leaf; that is, it has no children.
     *
     * @return      Whether it is a leaf or not
     */
    public boolean isLeaf() {
        return childCount == 0;
    }

}
