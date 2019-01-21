package cat.xtec.merli.i18n.client;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.i18n.client.LocaleInfo;

import cat.xtec.merli.i18n.client.format.Moment;


/**
 * Main entry point for the internationalization module.
 */
public class I18nEntryPoint implements EntryPoint {

    /**
     * {@inheritDoc}
     */
    public void onModuleLoad() {
        initMoment();
        updateHTMLLang();
    }


    /**
     * Returns the current locale code of the interface.
     *
     * @return      Locale code string
     */
    private String getLocaleCode() {
        return LocaleInfo.getCurrentLocale().getLocaleName();
    }


    /**
     * Initializes the Moment.js native library.
     */
    private void initMoment() {
        ScriptInjector
            .fromUrl(Moment.LIBRARY_URL)
            .setWindow(ScriptInjector.TOP_WINDOW)
            .setCallback(MomentCallback)
            .inject();
    }


    /**
     * Updates the HTML element's 'lang' attribute to reflect the
     * current locale.
     */
    private void updateHTMLLang() {
        Element document = Document.get().getDocumentElement();
        document.setPropertyString("lang", getLocaleCode());
    }


    /**
     * Moment.js script injector callback. Configures the library
     * after it has been successfully loaded.
     */
    private Callback MomentCallback = new Callback<Void, Exception>() {

        /** {@inheritDoc} */
        public void onSuccess(Void result) {
            Moment.locale(getLocaleCode());
        }

        /** {@inheritDoc} */
        public void onFailure(Exception reason) {
            // Pass
        }
    };

}
