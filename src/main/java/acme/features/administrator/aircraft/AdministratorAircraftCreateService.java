
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airlines.Airline;

@GuiService
public class AdministratorAircraftCreateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);
		Administrator administrator = (Administrator) super.getRequest().getPrincipal().getActiveRealm();
		status = airline != null && super.getRequest().getPrincipal().hasRealm(administrator);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Aircraft object;

		object = new Aircraft();
		object.setActive(false);
		object.setCapacity(0);
		object.setCargoWeight(2000);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		Airline airline;
		int airlineId;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "active", "details", "airline");
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		super.state(super.getRequest().getData("confirmation", boolean.class), "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		SelectChoices airlineChoices;
		Dataset dataset;
		Collection<Airline> airlines;

		airlines = this.repository.findAllAirlines();

		airlineChoices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargoWeight", "details", "active");
		dataset.put("confirmation", false);
		dataset.put("airlines", airlineChoices);
		dataset.put("airline", airlineChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
