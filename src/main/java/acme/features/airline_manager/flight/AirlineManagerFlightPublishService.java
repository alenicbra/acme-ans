
package acme.features.airline_manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Principal;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightPublishService extends AbstractGuiService<AirlineManager, Flight> {

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

		if (!super.getBuffer().getErrors().hasErrors()) {
			super.state(object.totalLayovers() >= 1, "layovers", "manager.flights.total-layovers.error");

			Collection<Leg> legs = this.repo.getLegsByFlight(object);
			Boolean status = legs.stream().anyMatch(e -> e.isDraftMode());
			super.state(!status, "legs", "manager.flights.legs.error");
		}
	}

	@Override
	public void perform(final Flight object) {

		object.setDraftMode(false);
		this.repo.save(object);
	}

	@Override
	public void unbind(final Flight object) {

		Dataset dataset;

		dataset = super.unbindObject(object, "tag", "indication", "cost", "description");
		super.getResponse().addData(dataset);
	}

}
