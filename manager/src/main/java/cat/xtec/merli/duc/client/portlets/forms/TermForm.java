package cat.xtec.merli.duc.client.portlets.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.taxa.Term;
import cat.xtec.merli.duc.client.editors.taxa.TermEditor;


/**
 * Controller for the term editor.
 */
public class TermForm extends ScrollPanel
    implements HasChangeHandlers {

    /** UiBinder instance */
    private static Binder binder = GWT.create(Binder.class);

    /** Bean editor driver instance */
    private Driver driver = GWT.create(Driver.class);

    /** UiBinder interface */
    @UiTemplate("TermForm.ui.xml")
    interface Binder extends UiBinder<Widget, TermForm> {}

    /** Bean editor driver interface */
    interface Driver extends SimpleBeanEditorDriver<Term, TermEditor> {}

    /** Current entity */
    private Term term = new Term();

    /** Term editor */
    @UiField TermEditor editor;

    /** Actual form panel */
    @UiField FormPanel form;


    /**
     * Creates a new term form widget.
     */
    public TermForm() {
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
     * @param term      Object to edit
     */
    public void edit(Term term) {
        this.term = term;
        driver.edit(term);
    }


    /**
     * Saves the form data.
     */
    public Term flush() {
        return driver.flush();
    }


    /**
     * Resets the form data.
     */
    public void reset() {
        driver.edit(term);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return form.addDomHandler(handler, ChangeEvent.getType());
    }

}
