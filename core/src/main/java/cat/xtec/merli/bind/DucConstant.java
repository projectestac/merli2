package cat.xtec.merli.bind;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marks an enumeration constant as providing a predicate for a
 * relation. This annotation may be used for a field which bind
 * type is annotated as {@code DucPredicate}.
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DucConstant {

    /** Default predicate */
    DucVocabulary value() default DucVocabulary.NOTHING;

}
