package ca.hedlund.jpraat.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interface used to denote a wrapped C++ method.  It's required that a method 
 * with this annotation include a _wrapped suffix and throw a PraatException.  Code generation can be done
 * using the apt tool.
 * 
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Wrapped {
	
}
