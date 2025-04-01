
package acme.features.any.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;

@GuiService
public class AnyFlightAssignmentListService extends AbstractGuiService<Any, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyFlightAssignmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		final boolean status = true;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> objects;

		objects = this.repository.findAllPublishedFlightAssignments();
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final FlightAssignment fa) {
		Dataset dataset;

		dataset = super.unbindObject(fa, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "airline");

		super.getResponse().addData(dataset);
	}

}
