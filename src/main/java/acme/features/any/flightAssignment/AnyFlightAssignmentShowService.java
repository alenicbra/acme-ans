
package acme.features.any.flightAssignment;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;

@GuiService
public class AnyFlightAssignmentShowService extends AbstractGuiService<Any, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyFlightAssignmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		final FlightAssignment project;

		id = super.getRequest().getData("id", int.class);
		project = this.repository.findFlightAssignmentById(id);
		status = project != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final FlightAssignment fa) {
		Dataset dataset;

		dataset = super.unbindObject(fa, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "airline");

		super.getResponse().addData(dataset);
	}

}
