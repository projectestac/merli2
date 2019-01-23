package cat.xtec.merli.duc.client.editors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.domain.voc.Language;
import cat.xtec.merli.duc.client.widgets.InputBox;
import cat.xtec.merli.duc.client.widgets.StringLabel;
import cat.xtec.merli.duc.client.messages.DucConstants;


/**
 * Leaf value editor for a language string.
 */
public class LangStringEditor extends Composite
    implements LeafValueEditor<LangString>, HasChangeHandlers, Focusable {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-LangStringEditor";

    /** Nil string value */
    private static final LangString NIL_VALUE = new LangString();

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("LangStringEditor.ui.xml")
    interface Binder extends UiBinder<Widget, LangStringEditor> {}

    /** Textual representation */
    @UiField InputBox text;

    /** Language representation */
    @UiField StringLabel language;

    /** Current language string value */
    private LangString string = NIL_VALUE;


    /**
     * Construsts a language string editor.
     */
    public LangStringEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
        text.addChangeHandler(e -> commitChanges());
    }


    /**
     * Obtains this editor language code.
     *
     * @return      Language code
     */
    public String getLocaleCode() {
        return string.getLocaleCode();
    }


    /**
     * Obtains the current language string value.
     *
     * The returned value will be a new {@code LangString} if the text
     * changed or {@code null} if it did not change and the input value
     * was also {@code null}. Note also that the value will be commited
     * only after the editor's form control is blured.
     *
     * @return      Language string or null
     */
    @Override
    public LangString getValue() {
        return (string != NIL_VALUE) ? string : null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(LangString value) {
        this.string = (value != null) ? value : NIL_VALUE;
        this.refreshView();
    }


    /**
     * Sets the placeholder attribute for this editor.
     *
     * @param value     Placeholder value
     */
    public void setPlaceholder(String value) {
        text.setPlaceholder(value);
    }


    /**
     * Sets the text value of this editor.
     *
     * @param value     String value
     */
    public void setText(String value) {
        text.setText(value);
    }


    /**
     * Sets the read-only mode of the editor.
     *
     * @param value     Read only state
     */
    public void setReadOnly(boolean value) {
        text.setReadOnly(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setAccessKey(char key) {
        text.setAccessKey(key);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setFocus(boolean focused) {
        text.setFocus(focused);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getTabIndex() {
        return text.getTabIndex();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setTabIndex(int index) {
        text.setTabIndex(index);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return text.addChangeHandler(handler);
    }


    /**
     * Checks if the current language string text is distinct from the
     * input box text. That is, if the text changed and thus must be
     * commited.
     *
     * @return          True if the values are distinct
     */
    protected boolean hasChanges() {
        String previous = string.getText();
        String current = text.getText();

        return !current.equals(previous);
    }


    /**
     * Sets the language value of this editor.
     *
     * @param value     Language value
     */
    protected void setLanguage(Language value) {
        language.setText(value);
    }


    /**
     * Refreshes this editor's view properties.
     */
    protected void refreshView() {
        String text = string.getText();
        Language language = string.getLanguage();
        String name = DucConstants.getText(language);

        this.setText(text);
        this.setLanguage(language);
        this.setPlaceholder(name);
        this.setTitle(name);
    }


    /**
     * Commits the changes of the editor. If the form values are distinct
     * from the current language string values this method creates a new
     * {@code LangString} instance and sets it as the current value.
     */
    private void commitChanges() {
        if (hasChanges() == true) {
            string = LangString.from(string);
            string.setText(text.getText());
        }
    }

}
