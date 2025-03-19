
package acme.features.flights;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class flightShowService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private flightRepository repo;


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
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "tag", "indication", "cost", "description");
		dataset.put("destination", object.destination().getIataCode());
		dataset.put("origin", object.origin().getIataCode());
		dataset.put("arrival", object.scheduledArrival());
		dataset.put("departure", object.scheduledDeparture());
		dataset.put("layovers", object.totalLayovers());
		super.getResponse().addData(dataset);
	}

}
