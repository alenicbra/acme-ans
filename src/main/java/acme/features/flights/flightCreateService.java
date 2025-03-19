
package acme.features.flights;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class flightCreateService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private flightRepository repo;


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
		assert object != null;

		super.bindObject(object, "tag", "indication", "cost", "description");
	}

	@Override
	public void validate(final Flight object) {
		assert object != null;
	}

	@Override
	public void perform(final Flight object) {
		assert object != null;

		this.repo.save(object);
	}

	@Override
	public void unbind(final Flight object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "tag", "indication", "cost", "description");
		super.getResponse().addData(dataset);
	}

}
