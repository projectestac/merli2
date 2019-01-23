package cat.xtec.merli.duc.client.editors.lists;

import java.util.List;
import java.util.ArrayList;
import com.google.gwt.editor.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.editor.client.CompositeEditor.EditorChain;

import cat.xtec.merli.domain.type.LangString;
import cat.xtec.merli.duc.client.editors.LangStringEditor;
import static cat.xtec.merli.i18n.client.I18nConstants.I18N;


/**
 * An editor for a predefined list of language strings. Notice that the
 * list is not a dynamic, the editable languages are predefined on the
 * i18n's module constant {@code I18nConstants.ACTIVE_LOCALES}.
 */
public class LangStringListEditor extends Composite
    implements CompositeEditor<List<LangString>, LangString, LangStringEditor> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-LangStringListEditor";

    /** Flow panel for the editors */
    private FlowPanel panel = new FlowPanel();

    /** Current editor values */
    private List<LangString> strings;

    /** Language string sub-editors */
    private List<LangStringEditor> editors;


    /**
     * Editor constructor.
     */
    public LangStringListEditor() {
        initWidget(panel);
        initEditors(I18N.ACTIVE_LOCALES());
        setStylePrimaryName(STYLE_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(List<LangString> strings) {
        this.strings = strings;
        this.populateEditors();
    }


    /**
     * Creates the sub-editors for this widget based on the provided
     * array of language codes.
     *
     * @param locales       Language codes array
     */
    protected void initEditors(String[] locales) {
        editors = new ArrayList<>(locales.length);

        for (String code : locales) {
            LangStringEditor editor = new LangStringEditor();
            editor.setValue(LangString.from(null, code));
            editors.add(editor);
            panel.add(editor);
        }
    }


    /**
     * Populates the sub-editors with the current values list. Note that
     * this method only updates the text of the current value found on
     * the editor.
     *
     * @param strings       Language strings
     */
    private void populateEditors() {
        for (LangStringEditor editor : editors) {
            String code = editor.getLocaleCode();
            LangString string = editor.getValue();

            string.setText(getValueFor(code).getText());
            editor.setValue(string);
        }
    }


    /**
     * Returns the current value for the given locale code. The first
     * value found on the list will be returned or a new language string
     * if no value exists for the locale.
     *
     * @param               Locale code
     * @return              Language string
     */
    private LangString getValueFor(String code) {
        for (LangString string : this.strings) {
            if (string instanceof LangString) {
                if (code.equals(string.getLocaleCode())) {
                    return string;
                }
            }
        }

        return LangString.from(null, code);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        editors.forEach(editor -> {
            String code = editor.getLocaleCode();
            String text = editor.getValue().getText();
            LangString string = getValueFor(code);

            string.setText(text);

            if (!strings.contains(string)) {
                strings.add(string);
            }
        });

        strings.removeIf(string -> {
            String text = string.getText();
            return text.trim().isEmpty();
        });
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setEditorChain(EditorChain<LangString, LangStringEditor> chain) {
        for (LangStringEditor editor : editors) {
            LangString string = editor.getValue();

            chain.detach(editor);
            chain.attach(string, editor);
        }
    }


    /** {@inheritDoc} */
    @Override
    public LangStringEditor createEditorForTraversal() {
        return new LangStringEditor();
    }


    /** {@inheritDoc} */
    @Override
    public String getPathElement(LangStringEditor editor) {
        return "[" + editors.indexOf(editor) + "]";
    }


    /** {@inheritDoc} */
    @Override
    public void onPropertyChange(String... paths) {}


    /** {@inheritDoc} */
    @Override
    public void setDelegate(EditorDelegate<List<LangString>> delegate) {}

}
