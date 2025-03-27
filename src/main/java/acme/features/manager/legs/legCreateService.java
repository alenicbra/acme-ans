
package acme.features.manager.legs;

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
public class legCreateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private LegRepository repo;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("flightId", int.class);
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Flight flight = this.repo.findFlightById(flightId);
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class) && flight != null && flight.getManager().getId() == managerId && !flight.getDraftMode();

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

		int departureId = super.getRequest().getData("departureAirport", int.class);
		int arrivalId = super.getRequest().getData("arrivalAirport", int.class);
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
	public void validate(final Leg object) {
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

		super.getResponse().addData(dataset);

	}

}
