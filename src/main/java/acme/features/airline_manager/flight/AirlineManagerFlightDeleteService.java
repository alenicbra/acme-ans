
package acme.features.airline_manager.flight;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private AirlineManagerFlightRepository repo;


	@Override
	public void authorise() {
		Boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);
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
		Collection<Leg> legs = this.repo.getLegsByFlightId(object.getId());
		legs = legs.stream().filter(e -> !e.getDraftMode()).toList();
		super.state(legs.isEmpty(), "*", "acme.validation.flight.delete");
	}

	@Override
	public void perform(final Flight object) {
		Collection<Leg> legs;

		legs = this.repo.getLegsByFlightId(object.getId());

		this.repo.deleteAll(legs);
		this.repo.delete(object);
	}

	@Override
	public void unbind(final Flight object) {

		Dataset dataset;

		dataset = super.unbindObject(object, "tag", "indication", "cost", "description", "draftMode");
		super.getResponse().addData(dataset);
	}

}
