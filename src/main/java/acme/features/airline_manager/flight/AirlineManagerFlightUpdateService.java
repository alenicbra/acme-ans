
package acme.features.airline_manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightUpdateService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repo;


	@Override
	public void authorise() {
		Boolean status;
		int managerId;
		int masterId;

		final Principal principal = super.getRequest().getPrincipal();
		managerId = principal.getActiveRealm().getId();
		masterId = super.getRequest().getData("id", int.class);
		Flight flight = this.repo.findOneById(masterId);

		status = flight != null && principal.hasRealmOfType(AirlineManager.class) && flight.getManager().getId() == managerId && flight.getDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int id = super.getRequest().getData("id", int.class);
		Flight object = this.repo.findOneById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Flight object) {

		super.bindObject(object, "tag", "indication", "cost", "description");
	}

	@Override
	public void validate(final Flight object) {
	}

	@Override
	public void perform(final Flight object) {

		this.repo.save(object);
	}

	@Override
	public void unbind(final Flight object) {

		Dataset dataset;

		dataset = super.unbindObject(object, "tag", "indication", "cost", "description");
		super.getResponse().addData(dataset);
	}

}
