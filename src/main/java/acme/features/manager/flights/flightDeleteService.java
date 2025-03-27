
package acme.features.manager.flights;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiService
public class flightDeleteService extends AbstractGuiService<AirlineManager, Flight> {

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

		this.repo.delete(object);
	}

	@Override
	public void unbind(final Flight object) {

		Dataset dataset;

		dataset = super.unbindObject(object, "tag", "indication", "cost", "description");
		super.getResponse().addData(dataset);
	}

}
