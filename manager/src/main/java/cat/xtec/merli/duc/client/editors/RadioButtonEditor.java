package cat.xtec.merli.duc.client.editors;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.editor.client.LeafValueEditor;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.event.dom.client.*;

import cat.xtec.merli.duc.client.messages.DucConstants;


/**
  * A leaf value editor for a list of enumeration constants. This editor
  * shows the values as radio buttons.
 */
public class RadioButtonEditor<E extends Enum<E>> extends SimplePanel
    implements LeafValueEditor<E>, HasChangeHandlers {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-RadioButtonEditor";

    /** Editor panel */
    protected Panel wrapper = new FlowPanel();

    /** Radio button values */
    protected List<RadioButton> options;

    /** Enumeration constants */
    protected E[] constants;


    /**
     * Constructs a radio button editor initialized with the given
     * enumeration class constants.
     *
     * @param type          Enumeration class
     * @param group         Group name
     */
    public RadioButtonEditor(Class<E> type, String group) {
        this(type.getEnumConstants(), group);
    }


    /**
     * Constructs a radio button editor initialized with the given
     * enumeration constants.
     *
     * @param constants     Enumeration constants
     * @param group         Group name
     */
    public RadioButtonEditor(E[] constants, String group) {
        this.constants = constants;
        this.options = new ArrayList<>(constants.length);

        setWidget(wrapper);
        initRadioButtons(constants, group);
        wrapper.setStylePrimaryName(STYLE_NAME);
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
    public E getConstant(String name) {
        for (E constant : constants) {
            if (constant.name().equals(name)) {
                return constant;
            }
        }

        throw new IllegalArgumentException();
    }


    /**
     * {@inheritDoc}
     */
    public E getValue() {
        for (RadioButton option: options) {
            if (option.getValue() == true) {
                String name = option.getFormValue();
                return getConstant(name);
            }
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    public void setValue(E constant) {
        for (RadioButton option: options) {
            option.setValue(false);
        }

        if (constant == null) {
            return;
        }

        String name = constant.name();

        for (RadioButton option: options) {
            if (name.equals(option.getFormValue())) {
                option.setValue(true);
                break;
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
     * Initializes the set of radio buttons for the given constants.
     *
     * @param constants     Enumeration constants
     * @param group         Group name
     */
    protected void initRadioButtons(E[] constants, String group) {
        for (E object : constants) {
            RadioButton option = new RadioButton(group);
            String name = object.name();
            String text = DucConstants.getText(name);

            option.setText(text);
            option.setFormValue(name);

            options.add(option);
            wrapper.add(option);
        }
    }

}
