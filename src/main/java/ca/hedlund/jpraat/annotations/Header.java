package ca.hedlund.jpraat.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation for declaring the c/cpp header(s)
 * where a code element is declared.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Header {

	public String value() default "";
	
}
