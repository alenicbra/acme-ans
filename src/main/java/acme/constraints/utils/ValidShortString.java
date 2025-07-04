
package acme.constraints.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Size;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {})
@ReportAsSingleViolation
@Size(max = 50)
public @interface ValidShortString {

	String message() default "{acme.validation.text.long.fifty}";

	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
