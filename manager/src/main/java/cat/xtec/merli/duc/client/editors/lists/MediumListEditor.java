package cat.xtec.merli.duc.client.editors.lists;

import com.google.gwt.editor.client.Editor;
import cat.xtec.merli.duc.client.editors.voc.MediumEditor;
import cat.xtec.merli.domain.voc.Medium;


/**
 * Editor for a dynamic list of physical types.
 */
public class MediumListEditor extends DynamicListEditor<Medium, MediumEditor> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-MediumListEditor";


    /**
     * Editor constructor.
     */
    public MediumListEditor() {
        super();
        addStyleName(STYLE_NAME);
    }


    /**
     * Creates a new subeditor instance.
     *
     * @return          A new editor instance
     */
    @Editor.Ignore
    protected MediumEditor createSubeditor() {
        return new MediumEditor();
    }

}
