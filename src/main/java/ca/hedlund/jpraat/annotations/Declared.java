package ca.hedlund.jpraat.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The c/cpp file which declares this element
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Declared {

	public String value() default "";
	
}
