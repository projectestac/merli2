package cat.xtec.merli.duc.client.editors.type;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.duc.client.editors.lists.LeafEditorWidget;
import cat.xtec.merli.duc.client.editors.voc.ContributorRoleEditor;
import cat.xtec.merli.domain.type.Contribution;


/**
 * Contribution to the state of a learning object.
 */
public class ContributionEditor extends Composite
    implements LeafEditorWidget<Contribution> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ContributionEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("ContributionEditor.ui.xml")
    interface Binder extends UiBinder<Widget, ContributionEditor> {}

    /** Type of contribution */
    @UiField ContributorRoleEditor role;

    /** List of contributors */
    @UiField ContactEditor entity;

    /** Date of the contribution */
    @UiField TimePointEditor timePoint;


    /**
     *
     */
    public ContributionEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Contribution getValue() {
        return null; // TODO:
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Contribution value) {
        // TODO:
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<Contribution> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Contribution> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

}
