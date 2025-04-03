
package acme.features.airline_manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightListService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repo;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Flight> flights;
		Principal principal;
		int id;

		principal = super.getRequest().getPrincipal();
		id = principal.getActiveRealm().getId();

		flights = this.repo.findAllByManagerId(id);
		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight object) {

		Dataset dataset;

		dataset = super.unbindObject(object, "id", "tag");
		super.getResponse().addData(dataset);
	}

}
