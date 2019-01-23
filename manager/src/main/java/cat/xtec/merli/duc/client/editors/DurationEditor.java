package cat.xtec.merli.duc.client.editors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.editor.client.*;
import com.google.gwt.uibinder.client.*;
import com.google.gwt.user.client.ui.*;

import cat.xtec.merli.domain.Duration;
import cat.xtec.merli.duc.client.widgets.InputBox;


/**
 * A user business card.
 */
public class DurationEditor extends Composite
    implements ValueAwareEditor<Duration> {

    /** Primary CSS style for this widget */
    public static final String STYLE_NAME = "duc-DurationEditor";

    /** UiBinder instance */
    private static final Binder binder = GWT.create(Binder.class);

    /** UiBinder interface */
    @UiTemplate("DurationEditor.ui.xml")
    interface Binder extends UiBinder<Widget, DurationEditor> {}

    /** Duration hours */
    @Editor.Ignore
    @UiField LongBox hours;

    /** Duration minutes */
    @Editor.Ignore
    @UiField IntegerBox minutes;

    /** Duration seconds */
    @Editor.Ignore
    @UiField IntegerBox seconds;

    /** Duration hours label */
    @Editor.Ignore
    @UiField Label hoursLabel;

    /** Duration minutes label */
    @Editor.Ignore
    @UiField Label minutesLabel;

    /** Duration seconds label */
    @Editor.Ignore
    @UiField Label secondsLabel;

    /** Human redable duration */
    @Editor.Ignore
    @UiField InputBox text;

    /** Delegate for this editor */
    private EditorDelegate<Duration> delegate;

    /** Duration object */
    private Duration value = null;


    /**
     * Construsts a duration editor.
     */
    public DurationEditor() {
        Widget widget = binder.createAndBindUi(this);

        initWidget(widget);
        initFields(widget);
        setStylePrimaryName(STYLE_NAME);
    }


    /**
     * Obtains the current value.
     *
     * @return      Duration object or null
     */
    public Duration getValue() {
        return this.value;
    }


    /**
     * {@inheritDoc}
     */
    public void setValue(Duration value) {
        if (value instanceof Duration) {
            this.value = value;
            this.hours.setValue(value.toHours());
            this.minutes.setValue(value.toMinutesPart());
            this.seconds.setValue(value.toSecondsPart());
            this.updateText();
        } else {
            this.value = new Duration();
            this.hours.setValue(0L);
            this.minutes.setValue(0);
            this.seconds.setValue(0);
            this.updateText();
        }
    }


    /**
     * {@inheritDoc}
     */
    public void flush() {
        /* TODO */
    }


    /**
     * {@inheritDoc}
     */
    public void setDelegate(EditorDelegate<Duration> delegate) {
        this.delegate = delegate;
    }


    /**
     * {@inheritDoc}
     */
    public void onPropertyChange(String... paths) {}


    /**
     * Updates the dirty state of the editor delegate.
     */
    private void updateDirtyState() {
        /* TODO */
    }


    /**
     *
     */
    protected void initFields(Widget widget) {
        hours.addBlurHandler(e -> updateValue());
        minutes.addBlurHandler(e -> updateValue());
        seconds.addBlurHandler(e -> updateValue());
        hoursLabel.addClickHandler(e -> focusInput(e, hours));
        minutesLabel.addClickHandler(e -> focusInput(e, minutes));
        secondsLabel.addClickHandler(e -> focusInput(e, seconds));
        text.addFocusHandler(e -> focusInput(e, hours));
    }


    /**
     *
     */
    private void focusInput(DomEvent event, ValueBox input) {
        input.setFocus(true);
        input.selectAll();
        event.preventDefault();
        event.stopPropagation();
    }


    /**
     *
     */
    private void updateValue() {
        long value = 0L;

        hours.setValue(hours.getValue());
        minutes.setValue(minutes.getValue());
        seconds.setValue(seconds.getValue());

        value += asLong(seconds) * Duration.SECOND_SECONDS;
        value += asLong(minutes) * Duration.MINUTE_SECONDS;
        value += asLong(hours) * Duration.HOUR_SECONDS;

        this.value = new Duration(value);
        this.updateText();
    }


    private void updateText() {
        hours.setValue(value.toHours());
        minutes.setValue(value.toMinutesPart());
        seconds.setValue(value.toSecondsPart());

        StringBuilder sb = new StringBuilder();

        if (hours.getValue() != null && hours.getValue() > 0) {
            sb.append(hours.getText());
            sb.append(" ");
            sb.append(hoursLabel.getText());
            sb.append(", ");
        }

        if (minutes.getValue() != null && minutes.getValue() > 0) {
            sb.append(minutes.getText());
            sb.append(" ");
            sb.append(minutesLabel.getText());
            sb.append(", ");
        }

        if (seconds.getValue() != null && seconds.getValue() > 0) {
            sb.append(seconds.getText());
            sb.append(" ");
            sb.append(secondsLabel.getText());
        }

        text.setText(sb.toString());
    }


    /**
     *
     */
    private long asLong(LongBox field) {
        Long value = field.getValue();
        return (value == null) ? 0L : value;
    }


    /**
     *
     */
    private long asLong(IntegerBox field) {
        Integer value = field.getValue();
        return (value == null) ? 0L : (long) value;
    }

}
