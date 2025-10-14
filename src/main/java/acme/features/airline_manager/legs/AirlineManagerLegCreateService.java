
package acme.features.airline_manager.legs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repo;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("flightId", int.class);
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Flight flight = this.repo.findFlightById(flightId);
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class) && flight != null && flight.getManager().getId() == managerId && flight.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.repo.findFlightById(flightId);

		Leg object = new Leg();
		object.setFlight(flight);
		object.setDraftMode(true);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Leg object) {

		int departureId = super.getRequest().getData("departure", int.class);
		int arrivalId = super.getRequest().getData("arrival", int.class);
		int aircraftId = super.getRequest().getData("aircraft", int.class);

		Airport departure = this.repo.findAirportById(departureId);
		Airport arrival = this.repo.findAirportById(arrivalId);
		Aircraft aircraft = this.repo.findAircraftById(aircraftId);

		super.bindObject(object, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		object.setAircraft(aircraft);
		object.setArrivalAirport(arrival);
		object.setDepartureAirport(departure);
	}

	@Override
	public void validate(final Leg leg) {

		if (leg.getDepartureAirport() == null) {
			boolean stateDeparture = leg.getDepartureAirport() == null;
			super.state(!stateDeparture, "departure", "acme.validation.leg.null");
		}

		if (leg.getArrivalAirport() == null) {
			boolean stateArrival = leg.getArrivalAirport() == null;
			super.state(!stateArrival, "arrival", "acme.validation.leg.null");
		}

		if (leg.getDepartureAirport() != null && leg.getArrivalAirport() != null) {
			boolean areAirportsEquals = leg.getDepartureAirport().equals(leg.getArrivalAirport());
			super.state(!areAirportsEquals, "*", "acme.validation.leg.sameAirports.message");
		}

		if (leg.getFlightNumber() != null && leg.getAircraft() != null) {
			String iata = leg.getAircraft().getAirline().getIataCode();
			boolean correctIata = leg.getFlightNumber().contains(iata);

			super.state(correctIata, "flightNumber", "acme.validation.leg.flightNumber.iata");
		}

		if (leg.getFlightNumber() != null) {
			boolean repeatedFlightNumber = this.repo.findByFlightNumber(leg.getFlightNumber(), leg.getId()).isPresent();

			super.state(!repeatedFlightNumber, "flightNumber", "acme.validation.leg.flightNumber.unique");
		}

		if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
			boolean isDepartureAfterArrival = leg.getScheduledDeparture().after(leg.getScheduledArrival());
			super.state(!isDepartureAfterArrival, "scheduledDeparture", "acme.validation.leg.negative-duration");
		}
	}

	@Override
	public void perform(final Leg object) {
		this.repo.save(object);
	}

	@Override
	public void unbind(final Leg object) {
		Collection<Aircraft> aircrafts = this.repo.findAllAircraft();
		Collection<Airport> airports = this.repo.findAllAirport();

		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", object.getAircraft());
		SelectChoices arrivalChoices = SelectChoices.from(airports, "iataCode", object.getArrivalAirport());
		SelectChoices departureChoices = SelectChoices.from(airports, "iataCode", object.getDepartureAirport());
		SelectChoices typeChoices = SelectChoices.from(LegStatus.class, object.getStatus());

		Dataset dataset = super.unbindObject(object, "flightNumber", "scheduledDeparture", "scheduledArrival");

		dataset.put("status", typeChoices.getSelected().getKey());
		dataset.put("statuses", typeChoices);

		dataset.put("departure", departureChoices.getSelected().getKey());
		dataset.put("departures", departureChoices);

		dataset.put("arrival", arrivalChoices.getSelected().getKey());
		dataset.put("arrivals", arrivalChoices);

		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		dataset.put("flight", object.getFlight());
		dataset.put("flightId", object.getFlight().getId());

		super.getResponse().addData(dataset);

	}

}
