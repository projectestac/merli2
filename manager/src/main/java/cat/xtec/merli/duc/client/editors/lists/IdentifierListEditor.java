package cat.xtec.merli.duc.client.editors.lists;

import com.google.gwt.editor.client.Editor;
import cat.xtec.merli.duc.client.editors.type.IdentifierEditor;
import cat.xtec.merli.domain.type.Identifier;


/**
 * Editor for a dynamic list of object identifiers.
 */
public class IdentifierListEditor extends DynamicListEditor<Identifier, IdentifierEditor> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-IdentifierListEditor";


    /**
     * Editor constructor.
     */
    public IdentifierListEditor() {
        super();
        addStyleName(STYLE_NAME);
    }


    /**
     * Creates a new subeditor instance.
     *
     * @return          A new editor instance
     */
    @Editor.Ignore
    protected IdentifierEditor createSubeditor() {
        return new IdentifierEditor();
    }

}
