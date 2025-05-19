
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

		boolean haveALeg = object.totalLayovers() >= 0;
		super.state(haveALeg, "*", "acme.validation.flight.no-legs.message");

		Collection<Leg> legs = this.repo.getLegsByFlightId(object.getId());
		for (Leg leg : legs)
			if (leg.getAircraft() != null) {
				boolean isAircraftActive = leg.getAircraft().isActive();
				super.state(isAircraftActive, "*", "acme.validation.flight.aircraft-under-maintenance.message");
				boolean isLegPublished = !leg.isDraftMode();
				super.state(isLegPublished, "*", "acme.validation.flight.leg-not-published.message");
				break;
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

		dataset = super.unbindObject(object, "id", "tag", "indication", "cost", "description", "draftMode");
		dataset.put("destination", object.destination() != null ? object.destination().getIataCode() : "none");
		dataset.put("origin", object.origin() != null ? object.origin().getIataCode() : "none");
		dataset.put("arrival", object.scheduledArrival() != null ? object.scheduledArrival() : "none");
		dataset.put("departure", object.scheduledDeparture() != null ? object.scheduledDeparture() : "none");
		dataset.put("layovers", object.totalLayovers());
		super.getResponse().addData(dataset);
	}

}
