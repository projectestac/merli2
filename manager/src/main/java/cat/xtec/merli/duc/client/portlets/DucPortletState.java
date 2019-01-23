package cat.xtec.merli.duc.client.portlets;

/**
 * Possible view states of a portlet. This states define the behaviour
 * and appearence of a portlet on a given instance.
 */
public enum DucPortletState {

    /** Busy performing a task */
    STATE_WORKING,

    /** Waiting for user input */
    STATE_WAITING,

    /** Showing the edition form */
    STATE_EDITING,

    /** Permissions error */
    STATE_FORBIDDEN,

    /** Unexpected error */
    STATE_FAILURE

}
