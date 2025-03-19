
package acme.features.flights;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class flightListService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private flightRepository repo;


	@Override
	public void authorise() {
		Principal principal = super.getRequest().getPrincipal();
		Boolean status = principal.hasRealmOfType(AirlineManager.class);

		super.getResponse().setAuthorised(status);
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
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "tag");
		super.getResponse().addData(dataset);
	}

}
