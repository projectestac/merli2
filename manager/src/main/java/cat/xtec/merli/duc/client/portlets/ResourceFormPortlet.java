package cat.xtec.merli.duc.client.portlets;

import javax.inject.Inject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.*;

import cat.xtec.merli.domain.lom.Resource;
import cat.xtec.merli.domain.taxa.EntityType;
import cat.xtec.merli.duc.client.portlets.forms.ResourceForm;
import cat.xtec.merli.duc.client.services.DucService;
import cat.xtec.merli.duc.client.services.DucServiceAsync;
import cat.xtec.merli.duc.client.widgets.EntityLabel;
import cat.xtec.merli.duc.client.widgets.ToolBar;
import static cat.xtec.merli.duc.client.portlets.DucPortletState.*;


/**
 * Learning object editor portlet.
 */
@Portlet(
  title =   "Resource editor",
  tooltip = "Learning object editor",
  id =      "xtec.duc.ResourceForm"
)
public class ResourceFormPortlet extends DucPortlet
    implements SelectionHandler<OWLEntity> {

    /** Composite widget for this portlet */
    private final ResourceForm form = new ResourceForm();

    /** RPC service for this portlet */
    private final DucServiceAsync service = GWT.create(DucService.class);

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("templates/FormPortletToolbar.ui.xml")
    interface Binder extends UiBinder<ToolBar, ResourceFormPortlet> {}

    /** Toolbar for this portlet */
    private ToolBar toolbar = binder.createAndBindUi(this);

    /** Label for the current entity */
    @UiField EntityLabel label;

    /** Buffer state indicator */
    @UiField Label indicator;

    /** Store class action */
    @UiField Button store;

    /** Export class action */
    @UiField MenuItem export;

    /** Remove class action */
    @UiField MenuItem remove;

    /** Reset form action */
    @UiField MenuItem reset;


    /**
     * Constructs a new resource editor portlet.
     *
     * @param selection     Selection model
     * @param project       Project identifier
     */
    @Inject
    public ResourceFormPortlet(SelectionModel selection, ProjectId project) {
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
     * Fetches the corresponding resource object for the entity if the
     * selected item is an OWL class or sets the portlet's state as
     * waiting if no class is currently selected on this portlet.
     *
     * @param event     Selection event
     */
    @Override
    public void onSelection(SelectionEvent<OWLEntity> event) {
        OWLEntity entity = event.getSelectedItem();

        if (entity instanceof OWLNamedIndividual) {
            setViewState(STATE_WORKING);
            setSelectedEntity(entity);
            fetchResource(entity);
        } else if (entity == null) {
            setSelectedEntity(null);
            setViewState(STATE_WAITING);
        } else if (getSelectedEntity() == null) {
            setViewState(STATE_WAITING);
        }
    }


    /**
     * Fetches the resource object for the given entry. The object is
     * fetched asynchronously from the server.
     *
     * @param entity        OWL entity pointer
     */
    private void fetchResource(OWLEntity entity) {
        String id = getProjectId();
        String iri = String.valueOf(entity.getIRI());
        service.fetchResource(id, iri, callback);
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
     * Reusable RPC callback. When a response is received successfully,
     * starts the edition of the object.
     */
    private AsyncCallback callback = new AsyncCallback<Resource>() {

        /** {@inheritDoc} */
        @Override public void onSuccess(Resource resource) {
            if (hasType(resource, Resource.class)) {
                form.edit(resource);
                form.scrollToTop();
                label.setEntity(resource);
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
        private boolean hasType(Resource entity, Class<?> group) {
            EntityType type = entity.getType();
            return type != null && group == type.group();
        }

    };

}
