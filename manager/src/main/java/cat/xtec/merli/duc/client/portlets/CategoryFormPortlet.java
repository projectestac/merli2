package cat.xtec.merli.duc.client.portlets;

import javax.inject.Inject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.*;
import org.semanticweb.owlapi.model.*;

import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.duc.client.LocaleUtils;
import cat.xtec.merli.duc.client.widgets.*;
import cat.xtec.merli.duc.client.dialogs.*;
import cat.xtec.merli.duc.client.services.*;
import static cat.xtec.merli.duc.client.portlets.DucPortletState.*;

import cat.xtec.merli.domain.taxa.Category;
import cat.xtec.merli.duc.client.portlets.forms.CategoryForm;


/**
 * Category editor portlet.
 */
@Portlet(
  title =   "Category editor",
  tooltip = "Learning category editor",
  id =      "xtec.duc.CategoryForm"
)
public class CategoryFormPortlet extends DucPortlet
    implements SelectionHandler<OWLEntity> {

    /** Composite widget for this portlet */
    private final CategoryForm form = new CategoryForm();

    /** RPC service for this portlet */
    private final CategoryServiceAsync service = GWT.create(CategoryService.class);

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("templates/FormPortletToolbar.ui.xml")
    interface Binder extends UiBinder<ToolBar, CategoryFormPortlet> {}

    /** Toolbar for this portlet */
    private ToolBar toolbar = binder.createAndBindUi(this);

    /** Current project identifier */
    private String project;

    /** Current entity identifier */
    private IRI iri;

    /** Label for the current entity */
    @UiField EntityLabel label;

    /** Buffer state indicator */
    @UiField Label indicator;

    /** Store class action */
    @UiField Button store;

    /** Remove class action */
    @UiField MenuItem remove;

    /** Reset form action */
    @UiField MenuItem reset;


    /**
     * Constructs a new category editor portlet.
     *
     * @param selection     Selection model
     * @param project       Project identifier
     */
    @Inject
    public CategoryFormPortlet(SelectionModel selection, ProjectId project) {
        super(selection, project);
        addSelectionHandler(this);
        initMenuCommands();
        attachHandlers();
        add(toolbar);
        add(form);
    }


    /**
     * Called when a selection change request is received.
     *
     * Fetches the corresponding category object for the entity if the
     * selected item is an OWL class or sets the portlet's state as
     * waiting if no class is currently selected on this portlet.
     *
     * @param event     Selection event
     */
    @Override
    public void onSelection(SelectionEvent<OWLEntity> event) {
        OWLEntity entity = event.getSelectedItem();

        if (entity instanceof OWLClass) {
            setViewState(STATE_WORKING);
            setSelectedEntity(entity);
            fetch(entity);
        } else if (entity == null) {
            setSelectedEntity(null);
            setViewState(STATE_WAITING);
        } else if (getSelectedEntity() == null) {
            setViewState(STATE_WAITING);
        }
    }


    /**
     * Fetches an object for the given entity from the server.
     *
     * @param entity        OWL entity pointer
     */
    private void fetch(OWLEntity entity) {
        this.iri = entity.getIRI();
        this.project = getProjectId();
        service.fetch(project, iri, fetchCallback);
    }


    /**
     * Transforms the properties of the given entity. This convenience
     * method currently sorts the entity relations befor editing.
     *
     * @param category      Object to transform
     */
    private void transform(Category category) {
        LocaleUtils.sortEntites(category.getParents());
        LocaleUtils.sortEntites(category.getKeywords());
    }


    /**
     * Refreshes this portlet's view properties.
     */
    private void refreshView() {
        indicator.setVisible(form.isDirty());
    }


    /**
     * Saves the current form if it has changes.
     *
     * @param event     Click event
     */
    @UiHandler("store")
    protected void onSaveClick(ClickEvent event) {
        if (form.isDirty() == true) {
            Category object = form.flush();

            service.persist(project, iri, object, asynch(() -> {
                form.edit(object);
                refreshView();
            }));
        }
    }


    /**
     * Removes the current entity from the ontology.
     */
    protected void onRemoveCommand() {
        RemoveDialog.confirm(confirm -> {
            if (confirm == true) {
                service.remove(project, iri, asynch(() -> {
                    setViewState(STATE_WAITING);
                }));
            }
        });
    }


    /**
     * Resets the current form to its initial state.
     */
    protected void onResetCommand() {
        form.reset();
        form.scrollToTop();
        refreshView();
    }


    /**
     * Initializes the commands associated with the menus.
     */
    private void initMenuCommands() {
        reset.setScheduledCommand(() -> onResetCommand());
        remove.setScheduledCommand(() -> onRemoveCommand());
    }


    /**
     * Registers the global event handlers.
     */
    private void attachHandlers() {
        attachHandler(form.addChangeHandler(e -> refreshView()));
    }


    /**
     * Invokes a runnable inside a new asynchronous callback. The
     * runnable will be called on a success response; otherwise an
     * error dialog will be shown to the user.
     *
     * @param runnable      Runnable to execute on success
     * @retun               New callback instance
     */
    private AsyncCallback<Void> asynch(Runnable runnable) {
        return new AsyncCallback<Void>() {

            @Override public void onSuccess(Void value) {
                runnable.run();
            }

            @Override public void onFailure(Throwable caught) {
                ErrorDialog.show();
            }

        };
    }


    /**
     * Reusable RPC callback. When a response is received successfully,
     * starts the edition of the object.
     */
    private AsyncCallback<Category> fetchCallback = new AsyncCallback<Category>() {

        /** {@inheritDoc} */
        @Override public void onSuccess(Category category) {
            if (hasType(category, Category.class)) {
                transform(category);
                form.edit(category);
                form.scrollToTop();
                label.setEntity(category);
                refreshView();
                setViewState(STATE_EDITING);
            } else {
                setViewState(STATE_WAITING);
            }
        }

        /** {@inheritDoc} */
        @Override public void onFailure(Throwable caught) {
            refreshView();
            setViewState(STATE_FAILURE);
            ErrorDialog.show();
        }

        /** Checks if the entity belongs to a type group */
        private boolean hasType(Category entity, Class<?> group) {
            EntityType type = entity.getType();
            return type != null && group == type.group();
        }
    };

}
