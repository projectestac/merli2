package cat.xtec.merli.duc.client.dialogs;

/**
 * Callback invoked on a dialog response.
 */
public interface ConfirmCallback {

    /**
     * Invoked when the user responds to a dialog.
     *
     * @param confirm       True if confirmed
     */
    void onResponse(boolean confirm);

}
