package cat.xtec.merli.theme.client;

import com.google.gwt.user.client.Element;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Main entry point for the XTEC theme module.
 */
public class DucThemeEntryPoint implements EntryPoint {

    /** Theme name */
    private static String THEME_NAME = "duc-theme";


    /**
     * {@inheritDoc}
     */
    public void onModuleLoad() {
        Element body = RootPanel.getBodyElement();
        body.addClassName(THEME_NAME);
    }

}
