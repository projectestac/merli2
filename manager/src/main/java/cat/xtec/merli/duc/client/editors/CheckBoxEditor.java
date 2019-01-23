package cat.xtec.merli.duc.client.editors;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.duc.client.messages.DucConstants;


/**
 * An editor for a list of enumeration constants. This editor shows the
 * values as check boxes.
 */
public class CheckBoxEditor<E extends Enum<E>> extends SimplePanel
    implements ValueAwareEditor<List<E>>, HasChangeHandlers {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-CheckBoxEditor";

    /** Editor panel */
    protected Panel wrapper = new FlowPanel();

    /** Check box values */
    protected List<CheckBox> options;

    /** Enumeration constants */
    protected E[] constants;

    /** Selected values list */
    private List<E> values;

    /** Editor delegate */
    private EditorDelegate delegate;


    /**
     * Constructs a check box editor initialized with the given
     * enumeration class constants.
     *
     * @param type          Enumeration class
     */
    public CheckBoxEditor(Class<E> type) {
        this(type.getEnumConstants());
    }


    /**
     * Constructs a check box editor initialized with the given
     * enumeration constants.
     *
     * @param constants        Enumeration constants
     */
    public CheckBoxEditor(E[] constants) {
        this.constants = constants;
        this.options = new ArrayList<>(constants.length);
        this.values = new ArrayList<>(constants.length);

        setWidget(wrapper);
        initCheckBoxes(constants);
        wrapper.setStylePrimaryName(STYLE_NAME);
        addChangeHandler(e -> delegate.setDirty(isDirty()));
    }


    /**
     * {@inheritDoc}
     */
    public void setValue(List<E> values) {
        this.values = values;

        for (CheckBox option : options) {
            String name = option.getFormValue();
            option.setValue(contains(name));
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        values.clear();

        for (CheckBox option : options) {
            if (option.getValue() == true) {
                String name = option.getFormValue();
                values.add(getConstant(name));
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return wrapper.addDomHandler(handler, ChangeEvent.getType());
    }


    /**
     * Given an option name returns the enumeration constant
     * that has the given name.
     *
     * @param name          Enumeration name
     * @return              Enumeration constant
     *
     * @throws IllegalArgumentException     If no enumeration
     *      constant exists with the given name
     */
    protected E getConstant(String name) {
        for (E constant : constants) {
            if (constant.name().equals(name)) {
                return constant;
            }
        }

        throw new IllegalArgumentException();
    }


    /**
     * Checks if the current values list contains the given item.
     *
     * @param name          Enumeration name
     * @return              True or false
     */
    protected boolean contains(String name) {
        for (E value : this.values) {
            if (value.name().equals(name)) {
                return true;
            }
        }

        return false;
    }


    /**
     * Checks if the current editor values have changed from
     * their initial state.
     *
     * @return      True if dirty
     */
    protected boolean isDirty() {
        for (CheckBox option : options) {
            String name = option.getFormValue();
            boolean checked = values.contains(name);

            if (checked != option.getValue()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Initializes the set of checkboxes for the given values.
     *
     * @param values        Enumeration constants
     */
    protected void initCheckBoxes(E[] constants) {
        for (E object : constants) {
            String name = object.name();
            String text = DucConstants.getText(name);
            CheckBox option = new CheckBox(text);

            option.setFormValue(name);
            option.setTitle(text);
            options.add(option);
            wrapper.add(option);
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onPropertyChange(String... paths) {}


    /** {@inheritDoc} */
    @Override
    public void setDelegate(EditorDelegate<List<E>> delegate) {
        this.delegate = delegate;
    }

}
