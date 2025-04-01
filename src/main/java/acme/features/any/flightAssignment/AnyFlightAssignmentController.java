
package acme.features.any.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignments.FlightAssignment;

@GuiController
public class AnyFlightAssignmentController extends AbstractGuiController<Any, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyFlightAssignmentListService	createService;

	@Autowired
	private AnyFlightAssignmentShowService	updateService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}

}
