package cat.xtec.merli.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Method used to instantiate a class from a string.
 *
 * The annotated method must return an instance of the class where it
 * is defined and must take as parameters a value string and optionally
 * a locale tag string.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DucCreator {}
