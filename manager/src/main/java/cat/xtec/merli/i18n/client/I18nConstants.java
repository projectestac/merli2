package cat.xtec.merli.i18n.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Constants;
import com.google.gwt.i18n.client.LocalizableResource;


/**
 * Internationalization settings.
 */
@LocalizableResource.Generate(
  fileName = "I18nConstants",
  format =   "com.google.gwt.i18n.server.PropertyCatalogFactory",
  locales =  "default"
)
public interface I18nConstants extends Constants {

    /** Instance of this interface */
    public static final I18nConstants I18N = GWT.create(I18nConstants.class);


    /** List of supported locales on the interface */
    @Key("i18n.active-locales")
    @DefaultStringArrayValue({"en"})
    String[] ACTIVE_LOCALES();

    /** Default menu item text */
    @Key("i18n.menuitem-text")
    @DefaultStringValue("Select language")
    String MENUITEM_TEXT();

}
