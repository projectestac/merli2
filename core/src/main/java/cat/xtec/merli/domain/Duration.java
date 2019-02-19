package cat.xtec.merli.domain;

import java.io.Serializable;
import javax.xml.bind.annotation.*;
import cat.xtec.merli.bind.*;


/**
 * Represents a time duration in seconds.
 */
@XmlType(name = "duration")
@XmlAccessorType(XmlAccessType.NONE)
public class Duration implements Serializable, Comparable<Duration> {

    /** This class version number */
    static final long serialVersionUID = 1L;

    /** String format pattern */
    private static final String PATTERN =
        "P(\\d*D)?(T(\\d*H)?(\\d*M)?(\\d*S)?)?";

    /** Zero secongs string value */
    public static final String ZERO_STRING = "PT0S";

    /** Number of seconds in a day */
    public static final int DAY_SECONDS = 86400;

    /** Number of seconds in an hour */
    public static final int HOUR_SECONDS = 3600;

    /** Number of seconds in a minute */
    public static final int MINUTE_SECONDS = 60;

    /** Number of seconds in a second */
    public static final int SECOND_SECONDS = 1;

    /** ISO-8601 units */
    private static final int[][] TIME_UNITS = {
        { 'D', DAY_SECONDS },
        { 'H', HOUR_SECONDS },
        { 'M', MINUTE_SECONDS },
        { 'S', SECOND_SECONDS }
    };

    /** String representation */
    protected String string = "P01M";

    /** Seconds representation */
    protected Long seconds = 0L;


    /**
     * Constructs a new empty duration.
     */
    public Duration() {}


    /**
     * Constructs a duration given a number of seconds.
     *
     * @param value         Duration in seconds
     */
    public Duration(long seconds) {
        this.setSeconds(seconds);
    }


    /**
     * Constructs a duration for the given string..
     *
     * @param string        ISO-8601 duration expression
     * @throws IllegalArgumentException
     */
    public Duration(String string) {
        this.setString(string);
    }


    /**
     * Obtains this duration in days.
     *
     * @return              Number of days
     */
    public long toDays() {
        return seconds / DAY_SECONDS;
    }


    /**
     * Obtains this duration in hours.
     *
     * @return              Number of hours
     */
    public long toHours() {
        return seconds / HOUR_SECONDS;
    }


    /**
     * Obtains this duration in minutes.
     *
     * @return              Number of minutes
     */
    public long toMinutes() {
        return seconds / MINUTE_SECONDS;
    }


    /**
     * Obtains this duration in seconds.
     *
     * @return              Number of seconds
     */
    public long toSeconds() {
        return seconds;
    }


    /**
     * Obtains the days part of this duration.
     *
     * @return              Days part or zero
     */
    public long toDaysPart() {
        return toDays();
    }


    /**
     * Obtains the hours part of this duration.
     *
     * @return              Hours part or zero
     */
    public int toHoursPart() {
        long seconds = this.seconds - DAY_SECONDS * toDays();
        return (int) (seconds / HOUR_SECONDS);
    }


    /**
     * Obtains the minutes part of this duration.
     *
     * @return              Minutes part or zero
     */
    public int toMinutesPart() {
        long seconds = this.seconds - HOUR_SECONDS * toHours();
        return (int) (seconds / MINUTE_SECONDS);
    }


    /**
     * Obtains the seconds part of this duration.
     *
     * @return              Seconds part or zero
     */
    public int toSecondsPart() {
        long seconds = this.seconds - MINUTE_SECONDS * toMinutes();
        return (int) (seconds);
    }


    /**
     * Sets the value of this object given a number of seconds.
     *
     * @param value         Duration in seconds
     */
    protected void setSeconds(long value) {
        this.string = toString(value);
        this.seconds = value;
    }


    /**
     * Returns the seconds value of this object.
     *
     * @return              Seconds value
     */
    protected long getSeconds() {
        return seconds;
    }


    /**
     * Returns the string value of this object.
     *
     * @return              String value
     */
    protected String getString() {
        return string;
    }


    /**
     * Sets the value of this object given a string expression.
     *
     * @param value         Duration expression
     */
    @XmlValue()
    protected void setString(String value) {
        this.seconds = toSeconds(value);
        this.string = value;
    }


    /**
     * Returns if the duration is equal to zero.
     *
     * @return              True if the duration is zero
     */
    public boolean isZero() {
        return seconds == 0L;
    }


    /**
     * Parses an ISO-8601 duration expression to produce a new
     * duration object. An ISO duration has the form PnDTnHnMn.nS.
     *
     * @param value     ISO-8601 duration
     */
    @DucCreator()
    public static Duration parse(String value) {
        return new Duration(value);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(Duration o) {
        return seconds.compareTo(o.seconds);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof Duration) {
            Duration duration = (Duration) o;
            return seconds == duration.seconds;
        }

        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return seconds.hashCode();
    }


    /**
     * Returns this object as an ISO-8601 duration string.
     *
     * @return              String representation
     */
    @Override
    public String toString() {
        return toString(seconds);
    }


    /**
     * Convers a number of seconds to an ISO-8601 duration string.
     *
     * @param value         Number of seconds
     * @return              String representation
     */
    private String toString(long value) {
        if (value == 0L) {
            return ZERO_STRING;
        }

        StringBuilder sb = new StringBuilder("P");
        long duration = value;

        for (int[] unit : TIME_UNITS) {
            final int seconds = unit[1];
            final int designator = unit[0];
            final long number = (duration / seconds);

            if (number > 0L) {
                sb.append(number);
                sb.append((char) designator);
                duration -= (number * seconds);
            }

            if (designator == 'D') {
                sb.append('T');
            }

            if (duration <= 0L) {
                break;
            }
        }

        return sb.toString();
    }


    /**
     *
     * @param value     ISO-8601 duration
     * @throws IllegalArgumentException
     */
    private static long toSeconds(String value) {
        long duration = 0L;

        // Supported format is: PnDTnHnMnS

        if (value.length() < 3 || value.charAt(0) != 'P') {
            throw new IllegalArgumentException(
                "Not a valid duration string");
        }

        if (value.matches(PATTERN) == false) {
            throw new IllegalArgumentException(
                "Not a valid duration string");
        }

        // Convert the string value to seconds

        String remainder = value.substring(1).replace("T", "");

        for (int[] unit : TIME_UNITS) {
            final int seconds = unit[1];
            final int designator = unit[0];
            final String regex = String.valueOf((char) designator);

            if (remainder.contains(regex)) {
                String[] parts = remainder.split(regex, 2);

                if (parts[0].length() > 0) {
                    long n = Long.parseLong(parts[0]);
                    duration += seconds * n;
                }

                remainder = parts[1];
            }
        }

        return duration;
    }

}
