package cat.xtec.merli.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 */
@Target({ ElementType.TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DucRelation {

    /**
     * Axiom predicate.
     */
    DucVocabulary value() default DucVocabulary.RELATION;

}
