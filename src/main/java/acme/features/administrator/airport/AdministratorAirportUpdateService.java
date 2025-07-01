
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airports.Airport;
import acme.entities.airports.OperationalScope;

@GuiService
public class AdministratorAirportUpdateService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportRepository repository;


	@Override
	public void authorise() {
		Airport airport;
		int id;
		Administrator administrator;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(id);
		administrator = (Administrator) super.getRequest().getPrincipal().getActiveRealm();
		status = airport != null && super.getRequest().getPrincipal().hasRealm(administrator);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airport airport;
		int id;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(id);

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airport airport) {
		super.state(super.getRequest().getData("confirmation", boolean.class), "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "name", "iataCode", "operationalScope", "city", "country", "website", "email", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("operationalScopes", choices);

		super.getResponse().addData(dataset);
	}

}
