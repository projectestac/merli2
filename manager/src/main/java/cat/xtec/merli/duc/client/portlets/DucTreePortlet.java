package cat.xtec.merli.duc.client.portlets;

import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasTreeItems;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.ScrollPanel;
import edu.stanford.bmir.protege.web.shared.DataFactory;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import org.semanticweb.owlapi.model.OWLEntity;

import cat.xtec.merli.domain.taxa.Entity;
import cat.xtec.merli.domain.taxa.Vertex;
import cat.xtec.merli.duc.client.services.DucService;
import cat.xtec.merli.duc.client.services.DucServiceAsync;
import cat.xtec.merli.duc.client.widgets.EntityTree;
import cat.xtec.merli.duc.client.widgets.EntityTreeItem;
import static cat.xtec.merli.duc.client.portlets.DucPortletState.*;


/**
 * This is the base class for the tree portlets.
 */
public abstract class DucTreePortlet extends DucPortlet {

    /** Unexpanded internal node placeholder */
    protected static final Label PLACEHOLDER = new Label();

    /** Tree widget for this portlet */
    protected EntityTree tree = new EntityTree();

    /** Container panel for the tree */
    protected ScrollPanel container = new ScrollPanel(tree.asWidget());

    /** RPC service for this portlet */
    protected DucServiceAsync service = GWT.create(DucService.class);


    /**
     * {@inheritDoc}
     */
    public DucTreePortlet(SelectionModel selection, ProjectId project) {
        super(selection, project);
        attachTreeHandlers();
        add(container);
    }


    /**
     * Fetches the children of an OWL entity from the server and sets
     * them as the roots of this portlet's tree.
     *
     * @param iri       An OWL entity IRI identifier
     */
    public void populateRoots(String iri) {
        String id = this.getProjectId();

        setViewState(STATE_WORKING);
        service.fetchChildren(id, iri, callback);
    }


    /**
     * Expands a collapsed tree item. This method fetches the childs of
     * the item asynchronously from the server and replaces its current
     * children with the response.
     *
     * @param item      Tree item instance
     */
    public void expandTreeItem(EntityTreeItem item) {
        String id = this.getProjectId();
        String iri = item.getEntity().getUID().getString();

        service.fetchChildren(id, iri, new AsyncCallback<List<Vertex>>() {
            @Override public void onFailure(Throwable caught) {}
            @Override public void onSuccess(List<Vertex> nodes) {
                setVertices(item, nodes);
            }
        });
    }


    /**
     * Collapses a previously expanded tree item. This method removes from
     * the DOM all the child items of the node.
     *
     * @param item      Tree item instance
     */
    public void collapseTreeItem(EntityTreeItem item) {
        if (item.getChildCount() > 0) {
            item.removeItems();
            item.addItem(PLACEHOLDER);
        }
    }


    /**
     * Broadcasts the given tree item entity to the application.
     * @see #emitSelectedEntity
     *
     * @param item      Tree item instance
     */
    public void emitTreeItem(EntityTreeItem item) {
        Entity entity = item.getEntity();
        String iri = entity.getUID().getString();
        OWLEntity instance = DataFactory.getOWLClass(iri);

        this.emitSelectedEntity(instance);
    }


    /**
     * Adds a new child vertex to a widget.
     *
     * @param widget        Target widget
     * @param node          Node to add
     */
    private void addVertex(HasTreeItems widget, Vertex node) {
        TreeItem item = new EntityTreeItem(node);
        if (!node.isLeaf()) item.addItem(PLACEHOLDER);
        widget.addItem(item);
    }


    /**
     * Set the child vertices of a widget removing any previous
     * vertices set on the widget.
     *
     * @param widget        Target widget
     * @param node          Node to add
     */
    private void setVertices(HasTreeItems widget, List<Vertex> nodes) {
        widget.removeItems();

        for (Vertex node : nodes) {
            addVertex(widget, node);
        }
    }


    /**
     * Attached the event handlers for the entities tree.
     */
    private void attachTreeHandlers() {
        attachHandler(tree.addOpenHandler(event -> {
            TreeItem item = event.getTarget();
            expandTreeItem((EntityTreeItem) item);
        }));

        attachHandler(tree.addCloseHandler(event -> {
            TreeItem item = event.getTarget();
            collapseTreeItem((EntityTreeItem) item);
        }));

        attachHandler(tree.addSelectionHandler(event -> {
            TreeItem item = event.getSelectedItem();
            emitTreeItem((EntityTreeItem) item);
        }));
    }


    /**
     * Reusable RPC callback. When a response is received successfully,
     * sets the root vertices of this portlet's tree.
     */
    private AsyncCallback callback = new AsyncCallback<List<Vertex>>() {

        /** {@inheritDoc} */
        @Override public void onFailure(Throwable caught) {
            setViewState(STATE_FAILURE);
        }

        /** {@inheritDoc} */
        @Override public void onSuccess(List<Vertex> nodes) {
            setVertices(tree, nodes);
            setViewState(STATE_EDITING);
        }
    };

}
