package cat.xtec.merli.duc.client.portlets;

import javax.inject.Inject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.*;

import cat.xtec.merli.domain.taxa.Category;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.duc.client.LocaleUtils;
import cat.xtec.merli.duc.client.portlets.forms.CategoryForm;
import cat.xtec.merli.duc.client.services.CategoryService;
import cat.xtec.merli.duc.client.services.CategoryServiceAsync;
import cat.xtec.merli.duc.client.widgets.EntityLabel;
import cat.xtec.merli.duc.client.widgets.ToolBar;
import static cat.xtec.merli.duc.client.portlets.DucPortletState.*;


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
            fetchCategory(entity);
        } else if (entity == null) {
            setSelectedEntity(null);
            setViewState(STATE_WAITING);
        } else if (getSelectedEntity() == null) {
            setViewState(STATE_WAITING);
        }
    }


    /**
     * Fetches the category object for the given entry. The object is
     * fetched asynchronously from the server.
     *
     * @param entity        OWL entity pointer
     */
    private void fetchCategory(OWLEntity entity) {
        IRI iri = entity.getIRI();
        String id = getProjectId();
        service.fetch(id, iri, callback);
    }


    /**
     * Saves the current form if it has changes.
     *
     * @param event     Click event
     */
    @UiHandler("store")
    protected void onSaveClick(ClickEvent event) {
        if (form.isDirty()) {
            form.store();
            refreshView();
        }
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
    }


    /**
     * Registers the global event handlers.
     */
    private void attachHandlers() {
        attachHandler(form.addChangeHandler(e -> refreshView()));
    }


    /**
     * Refreshes this portlet's view properties.
     */
    private void refreshView() {
        indicator.setVisible(form.isDirty());
    }


    /**
     * Transforms the properties of the given entity. This convenience
     * method currently sorts the entity relations befor editing.
     *
     * @param category      Object to transform
     */
    private void transformCategory(Category category) {
        LocaleUtils.sortEntites(category.getParents());
        LocaleUtils.sortEntites(category.getKeywords());
    }


    /**
     * Reusable RPC callback. When a response is received successfully,
     * starts the edition of the object.
     */
    private AsyncCallback<Category> callback = new AsyncCallback<Category>() {

        /** {@inheritDoc} */
        @Override public void onSuccess(Category category) {
            if (hasType(category, Category.class)) {
                transformCategory(category);
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
        }

        /** Checks if the entity belongs to a type group */
        private boolean hasType(Category entity, Class<?> group) {
            EntityType type = entity.getType();
            return type != null && group == type.group();
        }
    };

}
