package cat.xtec.merli.duc.client.editors.lists;

import com.google.gwt.editor.client.Editor;
import cat.xtec.merli.duc.client.editors.voc.FormatEditor;
import cat.xtec.merli.domain.voc.Format;


/**
 * Editor for a dynamic list of formats.
 */
public class FormatListEditor extends DynamicListEditor<Format, FormatEditor> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-FormatListEditor";


    /**
     * Editor constructor.
     */
    public FormatListEditor() {
        super();
        addStyleName(STYLE_NAME);
    }


    /**
     * Creates a new subeditor instance.
     *
     * @return          A new editor instance
     */
    @Editor.Ignore
    protected FormatEditor createSubeditor() {
        return new FormatEditor();
    }

}
