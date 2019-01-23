package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.Language;


/**
 * Language type {@code ListBoxEditor} editor.
 */
public class LanguageEditor extends ListBoxEditor<Language> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-LanguageEditor";


    /**
     * Constructs a Language editor.
     */
    public LanguageEditor() {
        super(Language.class);
        setStylePrimaryName(STYLE_NAME);
    }

}
