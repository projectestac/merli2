package cat.xtec.merli.duc.client.editors.taxa;

import java.util.Objects;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.duc.client.editors.lists.LeafEditorWidget;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.Relation;
import cat.xtec.merli.domain.taxa.RelationType;


/**
 * Represents a relationship with an entity.
 */
public class RelationEditor extends Composite
    implements LeafEditorWidget<Relation> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-RelationEditor";

    /** Nil relation value */
    private static final Relation NIL_VALUE = new Relation();

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("RelationEditor.ui.xml")
    interface Binder extends UiBinder<Widget, RelationEditor> {}

    /** Target entity */
    @UiField TargetEditor target;

    /** Relation type */
    @UiField RelationTypeEditor type;

    /** Current relation */
    private Relation relation = NIL_VALUE;

    /** Entity class restriction */
    private Class<? extends Entity> group;


    /**
     * Creates a new relation editor.
     */
    public RelationEditor() {
        this(Entity.class);
    }


    /**
     * Creates a new relation editor restricted to the given entity
     * type. Only relations for provided entity type will be selectable.
     *
     * @param group         Entity class
     */
    public <T extends Entity> RelationEditor(Class<T> group) {
        this.group = group;

        initWidget(binder.createAndBindUi(this));
        setStylePrimaryName(STYLE_NAME);
        attachHandlers();
    }


    /**
     * Constructs a new target entity editor.
     */
    @UiFactory
    public TargetEditor makeEditorForTarget() {
        return new TargetEditor(group);
    }


    /**
     * Constructs a new relation type editor.
     */
    @UiFactory
    public RelationTypeEditor makeEditorForType() {
        return new RelationTypeEditor(group);
    }


    /**
     * Obtains the current relation value.
     *
     * The returned value will be a new {@code Relation} if the target
     * or type have changed or {@code null} if no relation type or target
     * was selected. Note that the value will be commited only after the
     * form controls are blured.
     *
     * @return      Relation instance or null
     */
    @Override
    public Relation getValue() {
        return (relation != NIL_VALUE) ? relation : null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Relation value) {
        this.relation = (value != null) ? value : NIL_VALUE;
        this.refreshView();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<Relation> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Relation> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }


    /**
     * Returns if the current form values are valid. The form values are
     * valid if none of them are {@code null}.
     */
    protected boolean isValidRelation() {
        return (type.getValue() != null) && (target.getValue() != null);
    }


    /**
     * Checks if the current relation values have changed. That is,
     * if the values shown on the form controls are distinct from
     * the current editor values.
     *
     * @return          True if the values are distinct
     */
    protected boolean hasChanges() {
        return hasTypeChanges() || hasTargetChanges();
    }


    /**
     * Checks if the target entity has changed since the last time
     * it was commited.
     *
     * @return          True if the entity has changed
     */
    protected boolean hasTargetChanges() {
        Entity previous = relation.getTarget();
        Entity current = target.getValue();

        return !Objects.equals(previous, current);
    }


    /**
     * Checks if the relation type has changed since the last time
     * it was commited.
     *
     * @return          True if the type has changed
     */
    protected boolean hasTypeChanges() {
        RelationType previous = relation.getType();
        RelationType current = type.getValue();

        return !Objects.equals(previous, current);
    }


    /**
     * Refreshes this editor's view properties.
     */
    protected void refreshView() {
        type.setValue(relation.getType());
        target.setValue(relation.getTarget());
        refreshEditors(relation.getType());
    }


    /**
     * Updates the state of this relation entity editor. This method is
     * used to prevent the editor from getting the input focus until
     * a value is choosen for the relation type.
     *
     * @param value     Relation type value
     */
    private void refreshEditors(RelationType value) {
        boolean disable = (value == null);

        target.setReadOnly(disable);
        target.setTabIndex(disable ? -1 : 0);
    }


    /**
     * Commits the changes of the editor.
     *
     * If the form values are distinct from the current relation values
     * this method creates a new {@code Relation} instance and sets it as
     * the current value.
     */
    private void commitChanges() {
        if (isValidRelation() == false) {
            relation = NIL_VALUE;
            ValueChangeEvent.fire(this, null);
        } else if (hasChanges() == true) {
            relation = prepareRelation();
            ValueChangeEvent.fire(this, relation);
        }
    }


    /**
     * Creates a new relation from the current editor values.
     *
     * @return          New relation instance
     */
    private Relation prepareRelation() {
        Relation relation = new Relation();

        relation.setType(type.getValue());
        relation.setTarget(target.getValue());

        return relation;
    }


    /**
     * Creates a new relation with the current editor type and
     * the given target.
     *
     * @param target    Entity instance
     * @return          New relation
     */
    private Relation prepareRelation(Entity target) {
        Relation relation = prepareRelation();
        relation.setTarget(target);

        return relation;
    }


    /**
     * Creates a new relation with the current editor target and
     * the given type.
     *
     * @param type      Relation type
     * @return          New relation
     */
    private Relation prepareRelation(RelationType type) {
        Relation relation = prepareRelation();
        relation.setType(type);

        return relation;
    }


    /**
     * Registers the event handlers.
     */
    private void attachHandlers() {
        // Fire selection events whenever a new type or target is
        // selected on the editors. Refresh the target editor state
        // when a type is selected.

        type.addSelectionHandler(event -> {
            RelationType type = event.getSelectedItem();
            refreshEditors(type);

            if (type != null && target.getValue() != null) {
                Relation value = prepareRelation(type);
                SelectionEvent.fire(this, value);
            } else {
                SelectionEvent.fire(this, null);
            }
        });

        target.addSelectionHandler(event -> {
            Entity target = event.getSelectedItem();

            if (target != null && type.getValue() != null) {
                Relation value = prepareRelation(target);
                SelectionEvent.fire(this, value);
            } else {
                SelectionEvent.fire(this, null);
            }
        });

        // Commit the editor values whenever the target or type
        // editor values change (fired on blur).

        type.addValueChangeHandler(event -> {
            commitChanges();
        });

        target.addValueChangeHandler(event -> {
            commitChanges();
        });

        // If the target editor is in read-only mode, return the
        // focus to the type editor

        target.input.addFocusHandler(event -> {
            if (type.getValue() == null) {
                type.setFocus(true);
            }
        });

        // Makes sure the input has the correct tabindex

        addAttachHandler(event -> {
            refreshEditors(relation.getType());
        });
    }

}
