package cat.xtec.merli.duc.client.editors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.type.Contact;
import cat.xtec.merli.duc.client.widgets.InputBox;


/**
 * A user business card.
 */
public class ContactEditor extends Composite implements Editor<Contact> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-ContactEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("ContactEditor.ui.xml")
    interface Binder extends UiBinder<Widget, ContactEditor> {}

    /** User identifier */
    @UiField InputBox userId;

    /** Company or individual name */
    @UiField InputBox name;


    /**
     *
     */
    public ContactEditor() {
        initWidget(binder.createAndBindUi(this));
        setStylePrimaryName(STYLE_NAME);
    }

}
