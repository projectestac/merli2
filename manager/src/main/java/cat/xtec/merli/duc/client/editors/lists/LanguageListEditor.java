package cat.xtec.merli.duc.client.editors.lists;

import com.google.gwt.editor.client.Editor;
import cat.xtec.merli.duc.client.editors.voc.LanguageEditor;
import cat.xtec.merli.domain.voc.Language;


/**
 * Editor for a dynamic list of formats.
 */
public class LanguageListEditor extends DynamicListEditor<Language, LanguageEditor> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-LanguageListEditor";


    /**
     * Editor constructor.
     */
    public LanguageListEditor() {
        super();
        addStyleName(STYLE_NAME);
    }


    /**
     * Creates a new subeditor instance.
     *
     * @return          A new editor instance
     */
    @Editor.Ignore
    protected LanguageEditor createSubeditor() {
        return new LanguageEditor();
    }

}
