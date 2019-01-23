package cat.xtec.merli.duc.client.widgets;

import com.google.gwt.user.client.ui.SuggestOracle;
import cat.xtec.merli.domain.taxa.Entity;


/**
 * A suggestion that contains an entity.
 */
public class EntitySuggestion implements SuggestOracle.Suggestion {

    /** Entity label representation */
    private EntityLabel label;

    /** Suggested entity instance */
    private Entity entity;


    /**
     * Constructs a new suggestion object.
     *
     * @param entity        Suggested entity
     */
    public EntitySuggestion(Entity entity) {
        this.entity = entity;
        this.label = new EntityLabel(entity);
    }


    /**
     * Obtains the suggested entity.
     *
     * @return              Entity instance
     */
    public Entity getEntity() {
        return entity;
    }


    /**
     * {@inheritDoc}
     */
    public String getDisplayString() {
        return label.toString();
    }


    /**
     * {@inheritDoc}
     */
    public String getReplacementString() {
        return label.getText();
    }

}
