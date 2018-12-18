package cat.xtec.merli.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Individual axiom.
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DucIndividual {

    /**
     * Axiom predicate.
     */
    DucVocabulary value() default DucVocabulary.CLASS;

}
