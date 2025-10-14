
package acme.constraints;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.entities.passengers.Passenger;
import acme.entities.passengers.PassengerRep;

public class PassengerValidator extends AbstractValidator<ValidPassenger, Passenger> {

	@Autowired
	private PassengerRep repository;


	@Override
	public boolean isValid(final Passenger passenger, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (passenger == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean uniquePassenger;
			Passenger existingPassenger;

			// Each customer has a series of passengers associated with a unique passport number
			// but it may be possible that for different customers there are passengers associated with the same passport number
			existingPassenger = this.repository.findPassengerByPassportNumberAndCustomer(passenger.getPassportNumber(), passenger.getCustomer().getId());

			uniquePassenger = existingPassenger == null || passenger.getPassportNumber().isBlank() || existingPassenger.equals(passenger);

			super.state(context, uniquePassenger, "passportNumber", "acme.validation.passenger.duplicate-passportNumber.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
