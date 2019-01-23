package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.ListBoxEditor;
import cat.xtec.merli.domain.voc.ContributorRole;


/**
 * Types of contributions (LOMv1.0).
 */
public class ContributorRoleEditor extends ListBoxEditor<ContributorRole> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ContributorRoleEditor";


    /**
     * Constructs a ContributorRole editor.
     */
    public ContributorRoleEditor() {
        super(ContributorRole.class);
        setStylePrimaryName(STYLE_NAME);
    }

}