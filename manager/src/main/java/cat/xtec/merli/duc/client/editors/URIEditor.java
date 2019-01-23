package cat.xtec.merli.duc.client.editors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.UID;
import cat.xtec.merli.duc.client.widgets.InputBox;


/**
 * Universal Resource Identifer editor.
 */
public class URIEditor extends Composite implements Editor<UID> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-URIEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("URIEditor.ui.xml")
    interface Binder extends UiBinder<Widget, URIEditor> {}

    /** Identifier IRI */
    @UiField InputBox string;


    /**
     * Constructs a new URI editor.
     */
    public URIEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        setStylePrimaryName(STYLE_NAME);
    }


    /**
     * Sets the placeholder attribute for this editor.
     *
     * @param value     Placeholder value
     */
    public void setPlaceholder(String value) {
        string.setPlaceholder(value);
    }

}
