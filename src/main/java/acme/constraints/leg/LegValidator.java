
package acme.constraints.leg;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.SpringHelper;
import acme.entities.legs.Leg;
import acme.features.airline_manager.legs.AirlineManagerLegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result = true;

		if (leg == null)
			return true;

		AirlineManagerLegRepository legRepository = SpringHelper.getBean(AirlineManagerLegRepository.class);

		if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
			boolean isDepartureAfterArrival = leg.getScheduledDeparture().after(leg.getScheduledArrival());
			super.state(context, isDepartureAfterArrival, "scheduledDeparture", "acme.validation.leg.negative-duration");
		}

		if (leg.getDepartureAirport() != null && leg.getArrivalAirport() != null) {
			boolean areAirportsEquals = leg.getDepartureAirport().equals(leg.getArrivalAirport());
			super.state(context, !areAirportsEquals, "arrivalAirport", "acme.validation.leg.sameAirports.message");
		}

		if (leg.getFlightNumber() != null) {
			boolean repeatedFlightNumber = legRepository.findByFlightNumber(leg.getFlightNumber(), leg.getId()).isPresent();

			super.state(context, !repeatedFlightNumber, "flightNumber", "acme.validation.leg.flightNumber.unique");
		}

		if (leg.getFlightNumber() != null && leg.getAircraft() != null) {
			String iata = leg.getAircraft().getAirline().getIataCode();
			boolean correctIata = leg.getFlightNumber().contains(iata);

			super.state(context, correctIata, "flightNumber", "acme.validation.leg.flightNumber.iata");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
