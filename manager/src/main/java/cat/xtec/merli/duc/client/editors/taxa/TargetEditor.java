package cat.xtec.merli.duc.client.editors.taxa;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.editor.client.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.duc.client.editors.lists.LeafEditorWidget;
import cat.xtec.merli.duc.client.messages.DucConstants;
import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.duc.client.widgets.EntityLabel;
import cat.xtec.merli.duc.client.widgets.EntityBox;


/**
 * A target entity editor. This editor allows selecting entities to
 * use in a relation.
 */
public class TargetEditor extends Composite
    implements LeafEditorWidget<Entity>, Focusable {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-TargetEditor";

    /** Nil entity value */
    private static final Entity NIL_VALUE = new Entity();

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("TargetEditor.ui.xml")
    interface Binder extends UiBinder<Widget, TargetEditor> {}

    /** Entity input box */
    @UiField EntityBox input;

    /** Current entity value */
    private Entity entity = NIL_VALUE;


    /**
     * Construsts an entity editor.
     */
    public TargetEditor() {
        this(EntityType.values());
    }


    /**
     * Constructs an entity editor restricted to the given entity
     * class. Only entities of the given class will be selectable.
     *
     * @param type          Entity class
     */
    public <T extends Entity> TargetEditor(Class<T> type) {
        this(EntityType.valuesFor(type));
    }


    /**
     * Constructs an entity editor restricted to the given set of
     * enumeration constants. Only entities of the given types will
     * be selectable.
     *
     * @param values        Enumeration constants
     */
    public TargetEditor(EntityType[] values) {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
        addStyleName("duc-EDIT_BOX");
        attachHandlers();
    }


    /**
     * Obtatins the current entity value.
     *
     * The returned value will be a new {@code Entity} instance if the
     * selected entity changed or {@code null} if no entity was selected.
     * Note also that the value will be commited only after the editor's
     * form control is blured.
     *
     * @return      Entity instance or null
     */
    @Override
    public Entity getValue() {
        return (entity != NIL_VALUE) ? entity : null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Entity value) {
        this.entity = (value != null) ? value : NIL_VALUE;
        this.refreshView();
    }


    /**
     * Sets the read-only mode of the editor.
     *
     * @param value     Read only state
     */
    public void setReadOnly(boolean value) {
        input.setReadOnly(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccessKey(char key) {
        input.setAccessKey(key);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus(boolean focused) {
        input.setFocus(focused);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getTabIndex() {
        return input.getTabIndex();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setTabIndex(int index) {
        input.setTabIndex(index);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<Entity> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Entity> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }


    /**
     * Refreshes this editor's view properties.
     */
    protected void refreshView() {
        input.setValue(entity);
        setTitle(getTextFor(entity.getType()));
        updateStyleNames(entity);
    }


    /**
     * Update this editor CSS styles for the given entity.
     *
     * @param entity        Entity instance or null
     */
    protected void updateStyleNames(Entity entity) {
        Entity value = (entity != null) ? entity : NIL_VALUE;
        String styleName = EntityLabel.getStyleNameFor(value);

        setStyleName(STYLE_NAME);
        addStyleName(styleName);
    }


    /**
     * Returns an human-readable text for the given entity type.
     *
     * @return      A string or null if the type was null
     */
    protected String getTextFor(EntityType type) {
        return (type != null) ? DucConstants.getText(type) : null;
    }


    /**
     * Registers the event handlers.
     */
    private void attachHandlers() {
        input.addSelectionHandler(event -> {
            Entity value = event.getSelectedItem();
            updateStyleNames(value);
            SelectionEvent.fire(this, value);
        });

        input.addValueChangeHandler(event -> {
            Entity value = event.getValue();
            setValue(value);
            ValueChangeEvent.fire(this, value);
        });
    }

}
