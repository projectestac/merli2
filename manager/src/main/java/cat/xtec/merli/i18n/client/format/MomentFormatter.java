package cat.xtec.merli.i18n.client.format;

import java.util.Date;
import edu.stanford.bmir.protege.web.client.ui.TimeFormatter;


/**
 * Utility methods to convert dates to strings on the client. This
 * implementation is a wrapper around the native moment.js library.
 */
public final class MomentFormatter extends TimeFormatter {

    /**
     * Converts the given timestamp to a human-readable time string.
     * Returns a time-ago string if the time stamp is within the current
     * month; otherwise returns a short date string representation.
     *
     * @param timestamp     Unix timestamp
     * @return              Localized string
     */
    @Override
    public String toTimeAgo(long timestamp) {
        Moment month = Moment.moment().startOf("month");

        return month.isAfter(timestamp) ?
            Moment.moment(timestamp).format("LL") :
            Moment.moment(timestamp).fromNow();
    }


    /**
     * Converts the given date to a locale aware human-readable date
     * string without the time part.
     *
     * @param date          Date object
     * @return              Localized string
     */
    @Override
    public String toFullDate(Date date) {
        return Moment.moment(date.getTime()).format("LL");
    }

}
