
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
public class AirlineManagerLegListService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repo;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("flightId", int.class);
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Flight flight = this.repo.findFlightById(flightId);
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class) && flight != null && flight.getManager().getId() == managerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.repo.findFlightById(flightId);

		Collection<Leg> legs = this.repo.findAllLegByFlight(flight);

		super.getResponse().addGlobal("flightId", flightId);
		super.getResponse().addGlobal("flightDraftMode", flight.getDraftMode());

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg object) {
		Collection<Aircraft> aircrafts = this.repo.findAllAircraft();
		Collection<Airport> airports = this.repo.findAllAirport();

		SelectChoices aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", object.getAircraft());
		SelectChoices arrivalChoices = SelectChoices.from(airports, "iataCode", object.getArrivalAirport());
		SelectChoices departureChoices = SelectChoices.from(airports, "iataCode", object.getDepartureAirport());
		SelectChoices typeChoices = SelectChoices.from(LegStatus.class, object.getStatus());

		Dataset dataset = super.unbindObject(object, "scheduledDeparture", "scheduledArrival", "draftMode");

		dataset.put("status", typeChoices.getSelected().getKey());
		dataset.put("statuses", typeChoices);

		dataset.put("departure", departureChoices.getSelected().getLabel());
		dataset.put("departures", departureChoices);

		dataset.put("arrival", arrivalChoices.getSelected().getLabel());
		dataset.put("arrivals", arrivalChoices);

		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
		super.getRequest().addGlobal("draftMode", object.getDraftMode());
	}

}
