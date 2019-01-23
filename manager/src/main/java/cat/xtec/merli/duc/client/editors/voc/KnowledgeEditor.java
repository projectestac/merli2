package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.Knowledge;


/**
 * Knowledge type {@code ListBoxEditor} editor.
 */
public class KnowledgeEditor extends ListBoxEditor<Knowledge> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-KnowledgeEditor";


    /**
     * Constructs a Knowledge editor.
     */
    public KnowledgeEditor() {
        super(Knowledge.class);
        setStylePrimaryName(STYLE_NAME);
    }

}