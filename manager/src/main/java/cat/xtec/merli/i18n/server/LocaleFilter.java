package cat.xtec.merli.i18n.server;

import java.io.IOException;
import java.util.Locale;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import javax.servlet.*;


/**
 * Internationalization web filter. This filter stores into the current
 * session the locale that was set on the client so it can be accessed
 * from the server side.
 */
@WebFilter(
  filterName =  "LocaleFilter",
  urlPatterns = "/WebProtege.jsp"
)
public class LocaleFilter implements Filter {

    /** Default locale of the interface */
    public static final String DEFAULT_LOCALE = "en";


    /**
     * {@inheritDoc}
     */
    public void init(FilterConfig config) throws ServletException {}


    /**
     * {@inheritDoc}
     */
    public void destroy() {}


    /**
     * {@inheritDoc}
     */
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {

        // Store the locale on the HTTP session object

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession();

        Locale locale = getCurrentLocale(request);
        session.setAttribute("locale", locale);

        // Chain the next filter

        chain.doFilter(request, response);
    }


    /**
     * Returns a locale object for the current request. If no locale
     * was specified on the request's {@code locale} parameter, a locale
     * for the default language will be returned.
     *
     * @see         I18nConstants.DEFAULT_LOCALE
     * @return      Locale object
     */
    private Locale getCurrentLocale(ServletRequest request) {
        final String code = request.getParameter("locale");
        return Locale.forLanguageTag(code != null ? code : DEFAULT_LOCALE);
    }

}
