
package acme.features.administrator.airlines;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airlines.Airline;
import acme.entities.airlines.AirlineType;

@GuiService
public class AdministratorAirlineCreateService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		Administrator administrator = (Administrator) super.getRequest().getPrincipal().getActiveRealm();
		boolean status = super.getRequest().getPrincipal().hasRealm(administrator);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airline airline;
		Date foundationMoment;

		foundationMoment = MomentHelper.getCurrentMoment();

		airline = new Airline();
		airline.setName("");
		airline.setIataCode("");
		airline.setWebsite("");
		airline.setFoundationMoment(foundationMoment);
		airline.setAirlineType(AirlineType.STANDARD);
		airline.setEmail("");
		airline.setPhoneNumber("");

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {
		super.bindObject(airline, "name", "iataCode", "website", "airlineType", "foundationMoment", "email", "phoneNumber");
	}

	@Override
	public void validate(final Airline airline) {
		super.state(super.getRequest().getData("confirmation", boolean.class), "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(AirlineType.class, airline.getAirlineType());

		dataset = super.unbindObject(airline, "name", "iataCode", "website", "airlineType", "foundationMoment", "email", "phoneNumber");
		dataset.put("confirmation", false);
		dataset.put("airlineTypes", choices);

		super.getResponse().addData(dataset);
	}
}
