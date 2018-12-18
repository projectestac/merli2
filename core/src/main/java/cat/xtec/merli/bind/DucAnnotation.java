package cat.xtec.merli.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation assertion axiom.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DucAnnotation {

    /**
     * Axiom predicate.
     */
    DucVocabulary value();

}
