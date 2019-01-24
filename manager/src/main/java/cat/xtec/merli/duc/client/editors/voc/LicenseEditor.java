package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.License;


/**
 * The state or condition of a learning object (LOMv1.0).
 */
public class LicenseEditor extends ListBoxEditor<License> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-LicenseEditor";


    /**
     * Constructs a License editor.
     */
    public LicenseEditor() {
        super(License.class);
        setStylePrimaryName(STYLE_NAME);
    }

}
