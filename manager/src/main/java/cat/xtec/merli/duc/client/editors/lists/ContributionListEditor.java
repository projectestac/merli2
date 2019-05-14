package cat.xtec.merli.duc.client.editors.lists;

import com.google.gwt.editor.client.Editor;
import cat.xtec.merli.duc.client.editors.type.ContributionEditor;
import cat.xtec.merli.domain.type.Contribution;


/**
 * Editor for a dynamic list of formats.
 */
public class ContributionListEditor
    extends DynamicListEditor<Contribution, ContributionEditor> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ContributionListEditor";


    /**
     * Editor constructor.
     */
    public ContributionListEditor() {
        super();
        addStyleName(STYLE_NAME);
    }


    /**
     * Creates a new subeditor instance.
     *
     * @return          A new editor instance
     */
    @Editor.Ignore
    protected ContributionEditor createSubeditor() {
        return new ContributionEditor();
    }

}
