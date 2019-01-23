package cat.xtec.merli.duc.client.dialogs;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;


/**
 *
 */
public class ConfirmDialog {

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("ConfirmDialog.ui.xml")
    interface Binder extends UiBinder<DialogBox, ConfirmDialog> {}

    /** Dialog box */
    private DialogBox dialog;

    /** Confirm button */
    @UiField Button confirm;

    /** Cancel button */
    @UiField Button cancel;


    /**
     *
     */
    protected ConfirmDialog() {
        dialog = binder.createAndBindUi(this);
        dialog.setGlassEnabled(true);
        RootPanel.get().add(dialog);
    }


    /**
     *
     */
    public static void confirm(DialogCallback callback) {
        new ConfirmDialog().show(callback);
    }


    /**
     *
     */
    protected void show(DialogCallback callback) {
        confirm.addClickHandler(e -> {
            dialog.hide();
            callback.onResponse(true);
        });

        cancel.addClickHandler(e -> {
            dialog.hide();
            callback.onResponse(false);
        });

        dialog.center();
    }


    /**
     * Logs an object to the browser's console.
     */
    protected native static void log(Object o) /*-{
        console.log(o.toString());
    }-*/;

}
