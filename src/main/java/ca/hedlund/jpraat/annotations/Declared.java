package ca.hedlund.jpraat.binding.jna;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The c/cpp file which declares this element
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Declared {

	public String value() default "";
	
}
