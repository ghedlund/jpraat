package ca.hedlund.jpraat.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Functions that are not part
 * of original Praat source.
 *
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Custom {
	
}
