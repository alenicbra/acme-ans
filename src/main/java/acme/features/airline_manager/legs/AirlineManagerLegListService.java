
package acme.features.airline_manager.legs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@GuiService
public class AirlineManagerLegListService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private AirlineManagerLegRepository repo;


	@Override
	public void authorise() {
		int flightId = super.getRequest().getData("flightId", int.class);
		int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Flight flight = this.repo.findFlightById(flightId);
		Boolean status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class) && flight != null && flight.getManager().getId() == managerId;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int flightId = super.getRequest().getData("flightId", int.class);
		Flight flight = this.repo.findFlightById(flightId);

		Collection<Leg> legs = this.repo.findAllLegByFlight(flight);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg object) {

		Dataset dataset = super.unbindObject(object, "id", "flightNumberNumber", "scheduledDeparture");
		dataset.put("flightDraftMode", object.getFlight().getDraftMode());

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("flightDraftMode", object.getFlight().getDraftMode());
		super.getResponse().addGlobal("flightId", object.getFlight().getId());

	}

}
