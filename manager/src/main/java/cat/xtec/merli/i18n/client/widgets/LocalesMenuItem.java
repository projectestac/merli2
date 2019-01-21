package cat.xtec.merli.i18n.client.widgets;

import java.util.Arrays;
import java.util.List;
import com.google.gwt.http.client.UrlBuilder;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

import static cat.xtec.merli.i18n.client.I18nConstants.I18N;


/**
 * Locale picker widget. Shows a list of languages to the user an sets
 * the application locale when an item is selected on the list.
 */
public class LocalesMenuItem extends MenuItem {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "i18n-LocalesMenu";

    /** Display text for this menu item  */
    private static final String text = I18N.MENUITEM_TEXT();

    /** Locales menu bar */
    private MenuBar menu = new MenuBar(true);


    /**
     * Constructs a new locale picker.
     */
    public LocalesMenuItem() {
        super(SafeHtmlUtils.fromString(text));

        setTitle(text);
        setSubMenu(menu);
        initLocalesMenu(menu);
        addStyleName(STYLE_NAME + "Item");
        menu.addStyleName(STYLE_NAME + "Bar");
    }


    /**
     * Changes the application locale forcing a reload of the
     * application.
     *
     * @param code      Locale code
     */
    public void setLocale(String code) {
        UrlBuilder builder = Window.Location.createUrlBuilder();
        String queryParam = LocaleInfo.getLocaleQueryParam();

        builder.setParameter(queryParam, code);
        Window.Location.replace(builder.buildString());
    }


    /**
     * Returns the code of the active interface locale.
     *
     * @return          Locale code
     */
    public String currentLocale() {
        return LocaleInfo.getCurrentLocale().getLocaleName();
    }


    /**
     * Returns the list of available locales.
     *
     * @return          Locale codes list
     */
    public List<String> availableLocales() {
        return Arrays.asList(LocaleInfo.getAvailableLocaleNames());
    }


    /**
     * Returns a human-readable locale name given a locale code.
     *
     * @param           Locale code
     * @return          Locale name
     */
    public String getLocaleName(String code) {
        return LocaleInfo.getLocaleNativeDisplayName(code);
    }


    /**
     * Populates the locales menu with the currrent supported locales
     * as defined on the {@link I18nConstants.ACTIVE_LOCALES} array.
     *
     * @param menu      Menu bar instance
     */
    private void initLocalesMenu(MenuBar menu) {
        String current = currentLocale();
        List<String> available = availableLocales();

        for (String code : I18N.ACTIVE_LOCALES()) {
            if (!available.contains(code)) {
                continue;
            }

            String name = getLocaleName(code);
            Command command = () -> setLocale(code);

            if (code.equals(current)) {
                this.setText(name);
            }

            menu.addItem(name, command);
        }
    }

}
