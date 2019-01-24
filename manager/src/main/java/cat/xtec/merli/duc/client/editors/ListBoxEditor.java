package cat.xtec.merli.duc.client.editors;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.duc.client.editors.lists.LeafEditorWidget;
import cat.xtec.merli.duc.client.messages.DucConstants;
import cat.xtec.merli.duc.client.widgets.SelectBox;


/**
 * A leaf value editor for a list of enumeration constants. This editor
 * shows the values as a select box.
 */
public class ListBoxEditor<E extends Enum<E>> extends SimplePanel
    implements LeafEditorWidget<E>, Focusable {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ListBoxEditor";

    /** Editor panel */
    protected Panel wrapper = new FlowPanel();

    /** Drop down */
    protected SelectBox options = new SelectBox();

    /** Enumeration constants */
    protected E[] constants;

    /** Current value */
    protected E value = null;


    /**
     * Constructs a list box editor initialized with the given
     * enumeration class constants.
     *
     * @param type          Enumeration class
     */
    public ListBoxEditor(Class<E> type) {
        this(type.getEnumConstants());
    }


    /**
     * Constructs a list box editor initialized with the given
     * enumeration constants.
     *
     * @param constants     Enumeration constants
     */
    public ListBoxEditor(E[] constants) {
        this.constants = constants;

        setWidget(wrapper);
        initListBoxes(constants);
        attachHandlers();
        wrapper.setStylePrimaryName(STYLE_NAME);
        options.setPlaceholder(DucConstants.getText("CHOOSE_AN_OPTION"));
        options.addChangeHandler(e -> setValue(getSelectedValue()));
    }


    /**
     * Returns the selected value on the list box.
     *
     * @return      Enumeration constant or null
     */
    public E getSelectedValue() {
        String name = options.getSelectedValue();

        for (E object : this.constants) {
           if (object.name().equals(name))
               return object;
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public E getValue() {
        return value;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(E value) {
        this.value = value;
        options.clearSelection();

        if (value == null) {
            return;
        }

        String name = value.name();

        for (int i = 0; i < options.getItemCount(); i++) {
            if (name.equals(options.getValue(i))) {
                options.setSelectedIndex(i);
                break;
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccessKey(char key) {
        options.setAccessKey(key);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus(boolean focused) {
        options.setFocus(focused);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getTabIndex() {
        return options.getTabIndex();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setTabIndex(int index) {
        options.setTabIndex(index);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addSelectionHandler(SelectionHandler<E> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<E> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }


    /**
     * Initializes the set of select boxes for the given constants.
     *
     * @param constants      Enumeration constants
     */
    protected void initListBoxes(E[] constants) {
        for (E constant : constants) {
            String name = constant.name();
            String text = DucConstants.getText(name);

            options.addItem(text, name);
        }

        wrapper.add(options);
        setValue(null);
    }


    /**
     * Registers the event handlers.
     */
    private void attachHandlers() {
        options.addChangeHandler(event -> {
            E value = getSelectedValue();
            SelectionEvent.fire(this, value);
        });

        options.addBlurHandler(e -> {
            E value = getSelectedValue();

            if (value != this.value) {
                this.value = value;
                ValueChangeEvent.fire(this, value);
            }
        });
    }

}
