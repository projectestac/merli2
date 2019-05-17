package cat.xtec.merli.duc.client.portlets.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.taxa.Category;
import cat.xtec.merli.duc.client.editors.taxa.CategoryEditor;


/**
 * Controller for the category editor.
 */
public class CategoryForm extends ScrollPanel
    implements HasChangeHandlers {

    /** UiBinder instance */
    private static Binder binder = GWT.create(Binder.class);

    /** Bean editor driver instance */
    private Driver driver = GWT.create(Driver.class);

    /** UiBinder interface */
    @UiTemplate("CategoryForm.ui.xml")
    interface Binder extends UiBinder<Widget, CategoryForm> {}

    /** Bean editor driver interface */
    interface Driver extends SimpleBeanEditorDriver<Category, CategoryEditor> {}

    /** Current entity */
    private Category category = new Category();

    /** Category editor */
    @UiField CategoryEditor editor;

    /** Actual form panel */
    @UiField FormPanel form;


    /**
     * Creates a new category form widget.
     */
    public CategoryForm() {
        super();
        setWidget(binder.createAndBindUi(this));
        driver.initialize(editor);
    }


    /**
     * Checks if the form has unsaved changes.
     *
     * @return          True if dirty
     */
    public boolean isDirty() {
        return driver.isDirty();
    }


    /**
     * Fills the form with the given object data.
     *
     * @param category      Object to edit
     */
    public void edit(Category category) {
        this.category = category;
        driver.edit(category);
    }


    /**
     * Saves the form data.
     */
    public Category flush() {
        return driver.flush();
    }


    /**
     * Resets the form data.
     */
    public void reset() {
        driver.edit(category);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return form.addDomHandler(handler, ChangeEvent.getType());
    }

}
