package cat.xtec.merli.parser;

import java.lang.FunctionalInterface;
import cat.xtec.merli.parser.DucProperty;


/**
 * An operation that consumes a property along with its target object
 * an the value of the property for the target. Here a property may
 * be seen as the meaning of a value on a target object.
 */
@FunctionalInterface
public interface DucConsumer {

    /**
     * Performs an operation on the given arguments.
     *
     * @param property          Property instance
     * @param target            Target of the property
     * @param value             Value of the property
     */
    public void accept(DucProperty property, Object target, Object value);

}
