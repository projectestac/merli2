package cat.xtec.merli.duc.client.events;

import com.google.gwt.user.client.Timer;


/**
 * Debounced task sheduler. This class can be used to schedule tasks that
 * will exececuted in the future unless a new task is requested before its
 * execution; in which case the current task is canceled and the new task
 * sheduled for execution.
 */
public abstract class Debouncer<T> {

    /** Next object to process */
    private T object;

    /** Task sheduler */
    private Timer timer = new Timer() {
        public void run() {
            process(object);
        }
    };


    /**
     * Schedules a task that will be processed after the given delay
     * unless a new task is scheduled in between.
     *
     * @param delay     Time to wait
     * @param object    Next object to process
     */
    public void debounce(int delay, T object) {
        this.object = object;
        timer.schedule(delay);
    }


    /**
     * This method will be invoked to process an object.
     *
     * @param object    Object to process
     */
    public abstract void process(T object);

}
