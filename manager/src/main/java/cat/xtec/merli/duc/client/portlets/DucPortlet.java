package cat.xtec.merli.duc.client.portlets;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.LegacyHandlerWrapper;
import com.google.gwt.event.logical.shared.*;
import edu.stanford.bmir.protege.web.client.portlet.PortletUi;
import edu.stanford.bmir.protege.web.client.portlet.WebProtegePortletPresenter;
import edu.stanford.bmir.protege.web.shared.event.WebProtegeEventBus;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import static cat.xtec.merli.duc.client.portlets.DucPortletState.*;
import cat.xtec.merli.duc.client.events.Debouncer;


/**
 * This is the base class for all the DUC portlets.
 */
public abstract class DucPortlet extends FlowPanel
    implements HasInitializeHandlers, HasSelectionHandlers<OWLEntity>,
    WebProtegePortletPresenter {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-Portlet";

    /** Selection events debounce milliseconds */
    public static final int DEBOUNCE_DELAY = 250;

    /** Current view state of this portlet */
    private DucPortletState state = STATE_WORKING;

    /** Parent GUI of this portlet */
    private PortletUi ui;

    /** Project to which this portlet belongs */
    private ProjectId project;

    /** Selection model of the application */
    private SelectionModel selection;

    /** Selected OWL entity instance */
    private OWLEntity selectedEntity = null;

    /** Global event handler registrations */
    private List<HandlerRegistration> handlers = new ArrayList<>();


    /**
     * Constructs a new portlet.
     *
     * @param selection     Selection model
     * @param project       Project identifier
     */
    public DucPortlet(SelectionModel selection, ProjectId project) {
        super();
        this.selection = selection;
        this.project = project;
        this.attachHandlers();
        this.setStylePrimaryName(STYLE_NAME);
    }


    /**
     * This method is invoked when the portlet is initialized. The default
     * implementation attaches this portlet to the assigned widget and fires
     * the initialization events.
     */
    @Override
    public final void start(PortletUi ui, WebProtegeEventBus bus) {
        this.ui = ui;
        this.ui.setWidget(this);
        this.ui.asWidget().addStyleName(STYLE_NAME + "Container");
        this.setViewState(STATE_WORKING);

        InitializeEvent.fire(this);
    }


    /**
     * This method is invoked when the portlet gets removed from the
     * interface. The default implementation removes any global event
     * handlers registered on the portlet.
     */
    @Override
    public final void dispose() {
        this.removeHandlers();
    }


    /**
     * @deprecated      Use {@link #setViewState} instead
     */
    @Override @Deprecated
    public final void setBusy(boolean value) {
        setViewState(STATE_WORKING);
    }


    /**
     * Returns the hash identifier of the project to which this portlet
     * is attached.
     *
     * @return      Project hash
     */
    public String getProjectId() {
        return project.getId();
    }


    /**
     * Returns the selected entity if it exists.
     *
     * @return          OWL entity instance or null
     */
    public OWLEntity getSelectedEntity() {
        return selectedEntity;
    }


    /**
     * Sets the selected entity without emiting events. To set the entity
     * and broadcast the change see {@link #emitSelectedEntity}.
     *
     * @param entity    OWL entity to select
     */
    public void setSelectedEntity(OWLEntity entity) {
        this.selectedEntity = entity;
    }


    /**
     * Sets the selected entity and broadcasts it to the application.
     *
     * The emissions are debounced, meaning that only the last received
     * request will be processed if two ore more emissions are requested
     * with a delay between them shorter than {@code DEBOUNCE_DELAY}.
     *
     * @param entity    OWL entity to select
     */
    public void emitSelectedEntity(OWLEntity entity) {
        emitDebouncer.debounce(DEBOUNCE_DELAY, entity);
    }


    /**
     * Returns the current state of the portlet.
     *
     * @return              Current state
     */
    public DucPortletState getViewState() {
        return state;
    }


    /**
     * Sets the state of the portlet. Setting the state changes the
     * apparence and behaviour of the portlet.
     *
     * @param newState      New state
     */
    public void setViewState(DucPortletState newState) {
        if (ui instanceof PortletUi == false) {
            throw new IllegalStateException(
                "The portlet has not been initialized");
        }

        if (newState instanceof DucPortletState == false) {
            throw new NullPointerException(
                "The portlet state cannot be null");
        }

        ui.asWidget().setStyleName("duc-" + state.name(), false);
        ui.asWidget().setStyleName("duc-" + newState.name(), true);

        ui.setBusy(newState == STATE_WORKING);
        ui.setForbiddenVisible(newState == STATE_FORBIDDEN);
        ui.setNothingSelectedVisible(newState == STATE_WAITING);

        this.state = newState;
    }


    /**
     * Returns if this portlet's container widget is attached to the
     * browser's document. If the portlet's container was not initialized
     * this method returns false.
     *
     * @see             Widget#isAttached
     * @return          True if the container is attached
     */
    public boolean isContainerAttached() {
        return (ui instanceof PortletUi) ?
            ui.asWidget().isAttached() : false;
    }


    /**
     * Returns if the given object is equal to the selected entity.
     *
     * @param entity        OWL entity instance
     * @return              True if the objects are equivalent
     */
    public boolean equalsSelectedEntity(OWLEntity entity) {
        return (selectedEntity == null) ?
            selectedEntity == entity :
            selectedEntity.equals(entity);
    }


    /**
     * Adds an initialization event handler.
     *
     * @param handler   Event handler
     * @return          Handler registration
     */
    @Override
    public HandlerRegistration addInitializeHandler(InitializeHandler handler) {
        return addHandler(handler, InitializeEvent.getType());
    }


    /**
     * Adds a selection request event handler to the widget.
     *
     * A selection event will be fired if the selected entity changes and
     * this portlet's container is attached to the browser's document. Note
     * that the selection request event may have been broadcasted from other
     * classes on the application.
     *
     * @see             SelectionModel
     * @param handler   Event handler
     * @return          Handler registration
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<OWLEntity> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }


    /**
     * Attaches a handler registration to this portlet. The registered
     * handlers will be removed automatically when the portlet is disposed.
     *
     * @param registration  Handler registration
     */
    public void attachHandler(HandlerRegistration registration) {
        handlers.add(registration);
    }


    /**
     * Fires a selection change event if the portlet is attached to the
     * DOM and the entity is not already selected on this widget. Use
     * {@link #emitSelectionChange} instead of this method if you want to
     * notify the change to the application.
     *
     * @param entity    OWL entity to emit
     */
    private void fireSelectionChange(OWLEntity entity) {
        if (isContainerAttached() && !equalsSelectedEntity(entity)) {
            SelectionEvent.fire(this, entity);
        }
    }


    /**
     * Notifies a selection change to the application. This differs from
     * the method {@link #fireSelectionChange} in that the selection change
     * is broadcast to all the classes listening on the selection model.
     *
     * @param entity    OWL entity to emit
     */
    private void emitSelectionChange(OWLEntity entity) {
        if (entity instanceof OWLEntity) {
            setSelectedEntity(entity);
            selection.setSelection(entity);
        } else {
            setSelectedEntity(null);
            selection.clearSelection();
        }
    }


    /**
     * Registers the global event handlers.
     */
    private void attachHandlers() {
        attachHandler(addInitializeHandler(e -> {
            OWLEntity entity = selection.getSelection().orElse(null);
            SelectionEvent.fire(this, entity);
        }));

        attachHandler(addAttachHandler(e -> {
            OWLEntity entity = selection.getSelection().orElse(null);
            fireDebouncer.debounce(DEBOUNCE_DELAY, entity);
        }));

        attachHandler(new LegacyHandlerWrapper(
            selection.addSelectionChangedHandler(e -> {
                OWLEntity entity = selection.getSelection().orElse(null);
                fireDebouncer.debounce(DEBOUNCE_DELAY, entity);
            })
        ));
    }


    /**
     * Removes the global handler registrations.
     */
    private void removeHandlers() {
        for (HandlerRegistration registration : handlers) {
            registration.removeHandler();
        }
    }


    /**
     * Debounces selection change events.
     */
    private Debouncer<OWLEntity> fireDebouncer = new Debouncer<OWLEntity>() {
        @Override public void process(OWLEntity entity) {
            fireSelectionChange(entity);
        }
    };


    /**
     * Debounces selection broadcast events.
     */
    private Debouncer<OWLEntity> emitDebouncer = new Debouncer<OWLEntity>() {
        @Override public void process(OWLEntity entity) {
            emitSelectionChange(entity);
        }
    };

}
