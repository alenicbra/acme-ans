
package acme.features.member.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.Member;

@GuiController
public class MemberFlightAssignmentController extends AbstractGuiController<Member, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentListCompletedService	completedListService;

	@Autowired
	private MemberFlightAssignmentListPlannedService	plannedListService;

	@Autowired
	private MemberFlightAssignmentShowService			showService;

	@Autowired
	private MemberFlightAssignmentCreateService			createService;

	@Autowired
	private MemberFlightAssignmentUpdateService			updateService;

	@Autowired
	private MemberFlightAssignmentDeleteService			deleteService;

	@Autowired
	private MemberFlightAssignmentPublishService		publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addCustomCommand("completed-list", "list", this.completedListService);
		super.addCustomCommand("planned-list", "list", this.plannedListService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
