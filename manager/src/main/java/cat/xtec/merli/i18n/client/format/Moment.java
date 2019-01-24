package cat.xtec.merli.i18n.client.format;

import jsinterop.annotations.*;


/**
 * Convert dates to strings. This is a wrapper around the native
 * moment.js library.
 */
@JsType(
  isNative = true,
  namespace = JsPackage.GLOBAL
)
public final class Moment {

    /** Moment.js native library path */
    @JsOverlay
    public static final String LIBRARY_URL =
        "https://cdnjs.cloudflare.com/ajax/libs/" +
        "moment.js/2.22.2/moment-with-locales.min.js";


    /**
     * Sets the default language of the library.
     *
     * @param code          Language code
     */
    @JsMethod(namespace = "moment")
    public static native void locale(String code);


    /**
     * Check if a timestamp defines a time that is greater to the
     * current moment.
     *
     * @param timestamp     Unix timestamp
     * @return              If the timestamp is greater
     */
    @JsMethod
    public native boolean isAfter(double timestamp);


    /**
     * Formats the current moment according to the given pattern.
     *
     * @param pattern       Format pattern
     * @return              Localized string
     */
    @JsMethod
    public native String format(String pattern);


    /**
     * Formats the current moment as a time-ago string.
     *
     * @return              Localized string
     */
    @JsMethod
    public native String fromNow();


    /**
     * Creates a new moment instance for the start of the
     * given time unit.
     *
     * @param unit          Time unit string
     * @return              New moment instance
     */
    @JsMethod
    public native Moment startOf(String unit);


    /**
     * Creates a new moment instance for the current time.
     *
     * @return              New moment instance
     */
    @JsMethod(namespace = JsPackage.GLOBAL)
    public static native Moment moment();


    /**
     * Creates a new moment instance for with the given
     * Unix timestamp.
     *
     * @param timestamp     Unix timestamp
     * @return              New moment instance
     */
    @JsMethod(namespace = JsPackage.GLOBAL)
    public static native Moment moment(double timestamp);

}
