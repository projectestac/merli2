package cat.xtec.merli.duc.client.portlets;

import javax.inject.Inject;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;
import edu.stanford.bmir.protege.web.shared.selection.SelectionModel;
import edu.stanford.webprotege.shared.annotations.Portlet;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLEntity;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.uibinder.client.*;

import cat.xtec.merli.duc.client.widgets.ToolBar;


/**
 * A portlet that displays a selectable tree of vocabulary terms.
 */
@Portlet(
  title =   "Vocabularies",
  tooltip = "Vocabulary terms tree",
  id =      "xtec.duc.TermTree"
)
public class TermTreePortlet extends DucTreePortlet
    implements InitializeHandler, SelectionHandler<OWLEntity> {

    /** IRI of the root element of this portlet's tree */
    public static final String ROOT_ID = "http://merli.xtec.cat/DUC#Concept";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("templates/TreePortletToolbar.ui.xml")
    interface Binder extends UiBinder<ToolBar, TermTreePortlet> {}

    /** Toolbar for this portlet */
    private ToolBar toolbar = binder.createAndBindUi(this);

    /** Create class action */
    @UiField Button create;

    /** Search classes action */
    @UiField Button search;

    /** Refresh tree action */
    @UiField MenuItem refresh;


    /**
     * {@inheritDoc}
     */
    @Inject
    public TermTreePortlet(SelectionModel selection, ProjectId project) {
        super(selection, project);
        addInitializeHandler(this);
        addSelectionHandler(this);
        initMenuCommands();
        insert(toolbar, 0);
    }


    /**
     * Called after the portlet has been initialized. Fetches the
     * root nodes from the server and populates the tree with them.
     *
     * @param event     Initialization event
     */
    @Override
 	public void onInitialize(InitializeEvent event) {
 	    populateRoots(ROOT_ID);
 	}


    /**
     * Called when a selection change request is received.
     *
     * Clears the selected tree item if the received entity is an OWL
     * class that is not equal to the currently selected OWL entity.
     *
     * @param event     Selection event
     */
    @Override
    public void onSelection(SelectionEvent<OWLEntity> event) {
        OWLEntity entity = event.getSelectedItem();

        if (entity instanceof OWLClass) {
            if (!equalsSelectedEntity(entity)) {
                tree.setSelectedItem(null);
                setSelectedEntity(null);
            }
        }
    }


    /**
     * Shows the class creation dialog to the user.
     *
     * @param event     Click event
     */
    @UiHandler("create")
    protected void onCreateClick(ClickEvent event) {

    }


    /**
     * Shows the class search dialog to the user.
     *
     * @param event     Click event
     */
    @UiHandler("search")
    protected void onSearchClick(ClickEvent event) {

    }


    /**
     * Initializes the commands associated with the menus.
     */
    private void initMenuCommands() {
        refresh.setScheduledCommand(() -> {
            populateRoots(ROOT_ID);
        });
    }

}
