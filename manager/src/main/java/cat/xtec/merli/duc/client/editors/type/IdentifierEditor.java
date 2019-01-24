package cat.xtec.merli.duc.client.editors.type;

import java.util.Objects;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.duc.client.editors.lists.LeafEditorWidget;
import cat.xtec.merli.duc.client.editors.voc.CatalogEditor;
import cat.xtec.merli.duc.client.widgets.InputBox;
import cat.xtec.merli.domain.type.Identifier;
import cat.xtec.merli.domain.voc.Catalog;


/**
 * Represents a catalog identifier of a resource.
 */
public class IdentifierEditor extends Composite
    implements LeafEditorWidget<Identifier> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-IdentifierEditor";

    /** Nil identifier value */
    private static final Identifier NIL_VALUE = new Identifier();

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("IdentifierEditor.ui.xml")
    interface Binder extends UiBinder<Widget, IdentifierEditor> {}

    /** Identifier entry */
    @UiField InputBox entry;

    /** Identifier catalog */
    @UiField CatalogEditor catalog;

    /** Current identifier */
    private Identifier identifier = NIL_VALUE;


    /**
     * Creates a new identifier editor.
     */
    public IdentifierEditor() {
        initWidget(binder.createAndBindUi(this));
        setStylePrimaryName(STYLE_NAME);
        attachHandlers();
    }


    /**
     * Constructs a new entry editor.
     */
    @UiFactory
    public InputBox makeEditorForEntry() {
        return new InputBox();
    }


    /**
     * Constructs a new catalog editor.
     */
    @UiFactory
    public CatalogEditor makeEditorForCatalog() {
        return new CatalogEditor();
    }


    /**
     * Obtains the current identifier value.
     *
     * The returned value will be a new {@code Identifier} if the entry
     * or catalog have changed or {@code null} if no identifier catalog or
     * entry was selected. Note that the value will be commited only after
     * the form controls are blured.
     *
     * @return      Identifier instance or null
     */
    @Override
    public Identifier getValue() {
        return (identifier != NIL_VALUE) ? identifier : null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Identifier value) {
        this.identifier = (value != null) ? value : NIL_VALUE;
        this.refreshView();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<Identifier> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Identifier> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }


    /**
     * Returns if the current form values are valid. The form values are
     * valid if none of them are {@code null}.
     */
    protected boolean isValidIdentifier() {
        return (catalog.getValue() != null && hasEntry());
    }


    /**
     * Returns is the entry editor contains a valid text. That is, it
     * contains a non-empty string after trimming the text.
     *
     * @return          True if the editor has a text value
     */
    protected boolean hasEntry() {
        return !entry.getValue().trim().isEmpty();
    }


    /**
     * Checks if the current identifier values have changed. That is,
     * if the values shown on the form controls are distinct from
     * the current editor values.
     *
     * @return          True if the values are distinct
     */
    protected boolean hasChanges() {
        return hasCatalogChanges() || hasEntryChanges();
    }


    /**
     * Checks if the identifier entry has changed since the last time
     * it was commited.
     *
     * @return          True if the entry changed
     */
    protected boolean hasEntryChanges() {
        String previous = identifier.getEntry();
        String current = entry.getValue();

        return !Objects.equals(previous, current);
    }


    /**
     * Checks if the identifier catalog has changed since the last time
     * it was commited.
     *
     * @return          True if the catalog has changed
     */
    protected boolean hasCatalogChanges() {
        Catalog previous = identifier.getCatalog();
        Catalog current = catalog.getValue();

        return !Objects.equals(previous, current);
    }


    /**
     * Refreshes this editor's view properties.
     */
    protected void refreshView() {
        catalog.setValue(identifier.getCatalog());
        entry.setValue(identifier.getEntry());
        refreshEditors(identifier.getCatalog());
    }


    /**
     * Updates the state of this identifier editor. This method is
     * used to prevent the editor from getting the input focus until
     * a value is choosen for the identifier catalog.
     *
     * @param value     Identifier catalog value
     */
    private void refreshEditors(Catalog value) {
        boolean disable = (value == null);

        entry.setReadOnly(disable);
        entry.setTabIndex(disable ? -1 : 0);
    }


    /**
     * Commits the changes of the editor.
     *
     * If the form values are distinct from the current identifier values
     * this method creates a new {@code Identifier} instance and sets it as
     * the current value.
     */
    private void commitChanges() {
        if (isValidIdentifier() == false) {
            identifier = NIL_VALUE;
            ValueChangeEvent.fire(this, null);
        } else if (hasChanges() == true) {
            identifier = prepareIdentifier();
            ValueChangeEvent.fire(this, identifier);
        }
    }


    /**
     * Creates a new identifier from the current editor values.
     *
     * @return          New identifier instance
     */
    private Identifier prepareIdentifier() {
        Identifier identifier = new Identifier();

        identifier.setCatalog(catalog.getValue());
        identifier.setEntry(entry.getValue());

        return identifier;
    }


    /**
     * Creates a new identifier with the current editor catalog and
     * the given entry.
     *
     * @param entry     String instance
     * @return          New identifier
     */
    private Identifier prepareIdentifier(String entry) {
        Identifier identifier = prepareIdentifier();
        identifier.setEntry(entry);

        return identifier;
    }


    /**
     * Creates a new identifier with the current editor entry and
     * the given catalog.
     *
     * @param catalog   Identifier catalog
     * @return          New identifier
     */
    private Identifier prepareIdentifier(Catalog catalog) {
        Identifier identifier = prepareIdentifier();
        identifier.setCatalog(catalog);

        return identifier;
    }


    private boolean fired = false;

    /**
     * Registers the event handlers.
     */
    private void attachHandlers() {
        // Fire selection events whenever a new catalog is selected
        // and refresh the entry editor state.

        /* TODO: La selecció s'hauria de llençar només si 'entry'
           té algun valor??? Només si té un valor vàlid (clar que
           no puc validar-los tots!) */

        /* TODO: Validar el text de entry? Enviar els canvis quan
           sigui un ID vàlid?? */

        catalog.addSelectionHandler(event -> {
            Catalog catalog = event.getSelectedItem();
            refreshEditors(catalog);

            if (catalog != null && hasEntry()) {
                Identifier value = prepareIdentifier(catalog);
                SelectionEvent.fire(this, value);
            } else {
                SelectionEvent.fire(this, null);
            }
        });

        /* TODO: Llançar 1 cop per KeyUp + 1 a cada blur?
           (Hauria de de-registrar per no creator??)...
           Tampoc s'hauria de llençar si ja té un text?? */

        entry.addKeyUpHandler(event -> {
            if (fired == true) {
                return;
            }

            String text = entry.getText();

            if (catalog != null && !text.trim().isEmpty()) {
                fired = true;
                Identifier value = prepareIdentifier(text);
                SelectionEvent.fire(this, value);
            }
        });

        entry.addChangeHandler(event -> {
            String text = entry.getText();

            if (catalog != null && !text.trim().isEmpty()) {
                Identifier value = prepareIdentifier(text);
                SelectionEvent.fire(this, value);
            } else {
                SelectionEvent.fire(this, null);
            }
        });

        // Commit the editor values whenever the entry or catalog
        // editor values change (fired on blur).

        catalog.addValueChangeHandler(event -> {
            commitChanges();
        });

        entry.addValueChangeHandler(event -> {
            commitChanges();
        });

        // If the entry editor is in read-only mode, return the
        // focus to the catalog editor

        entry.addFocusHandler(event -> {
            if (catalog.getValue() == null) {
                catalog.setFocus(true);
            }
        });

        // Makes sure the input has the correct tabindex

        addAttachHandler(event -> {
            refreshEditors(identifier.getCatalog());
        });
    }

}
