package cat.xtec.merli.duc.client.portlets.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.duc.client.editors.lom.ResourceEditor;


/**
 * Controller for the resource editor.
 */
public class ResourceForm extends ScrollPanel
    implements HasChangeHandlers {

    /** UiBinder instance */
    private static Binder binder = GWT.create(Binder.class);

    /** Bean editor driver instance */
    private Driver driver = GWT.create(Driver.class);

    /** UiBinder interface */
    @UiTemplate("ResourceForm.ui.xml")
    interface Binder extends UiBinder<Widget, ResourceForm> {}

    /** Bean editor driver interface */
    interface Driver extends SimpleBeanEditorDriver<Resource, ResourceEditor> {}

    /** Current entity */
    private Resource resource = new Resource();

    /** Resource editor */
    @UiField ResourceEditor editor;

    /** Actual form panel */
    @UiField FormPanel form;



    /**
     * Creates a new resource form widget.
     */
    public ResourceForm() {
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
     * @param resource      Object to edit
     */
    public void edit(Resource resource) {
        this.resource = resource;
        driver.edit(resource);
    }


    /**
     * Saves the form data.
     */
    public Resource flush() {
        return driver.flush();
    }


    /**
     * Resets the form data.
     */
    public void reset() {
        driver.edit(resource);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return form.addDomHandler(handler, ChangeEvent.getType());
    }

}
