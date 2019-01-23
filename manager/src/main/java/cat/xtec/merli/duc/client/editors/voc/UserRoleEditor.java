package cat.xtec.merli.duc.client.editors.voc;

import cat.xtec.merli.duc.client.editors.CheckBoxEditor;
import cat.xtec.merli.domain.voc.UserRole;


/**
 *
 */
public class UserRoleEditor extends CheckBoxEditor<UserRole> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-UserRoleEditor";


    /**
     * Constructs a userrole editor.
     */
    public UserRoleEditor() {
        super(UserRole.class);
        setStylePrimaryName(STYLE_NAME);
    }

}