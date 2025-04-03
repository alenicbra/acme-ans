
package acme.features.airline_manager.flight;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerFlightCreateService extends AbstractGuiService<AirlineManager, Flight> {

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
		final int id = super.getRequest().getPrincipal().getActiveRealm().getId();

		Flight object = new Flight();
		object.setManager(this.repo.findOneManagerById(id));
		object.setDraftMode(true);

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
