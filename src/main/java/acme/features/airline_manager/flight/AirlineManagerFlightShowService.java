
package acme.features.airline_manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightShowService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repo;


	@Override
	public void authorise() {
		boolean status;
		int masterId;

		final Principal principal = super.getRequest().getPrincipal();
		final int managerId = principal.getActiveRealm().getId();

		masterId = super.getRequest().getData("id", int.class);
		Flight flight = this.repo.findOneById(masterId);
		status = flight != null && flight.getManager().getId() == managerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repo.findOneById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Flight object) {

		Dataset dataset;

		dataset = super.unbindObject(object, "id", "tag", "indication", "cost", "description", "draftMode");
		dataset.put("destination", object.destination() != null ? object.destination().getIataCode() : "none");
		dataset.put("origin", object.origin() != null ? object.origin().getIataCode() : "none");
		dataset.put("arrival", object.scheduledArrival() != null ? object.scheduledArrival() : "none");
		dataset.put("departure", object.scheduledDeparture() != null ? object.scheduledDeparture() : "none");
		dataset.put("layovers", object.totalLayovers());
		super.getResponse().addData(dataset);
	}

}
