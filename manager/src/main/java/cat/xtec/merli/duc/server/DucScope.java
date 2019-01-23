package cat.xtec.merli.duc.server;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.inject.Scope;


/**
 * Main DUC component scope for the Dagger injection framework.
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface DucScope {}
