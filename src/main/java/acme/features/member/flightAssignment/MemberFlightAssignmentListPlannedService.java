
package acme.features.member.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.Member;

@GuiService
public class MemberFlightAssignmentListPlannedService extends AbstractGuiService<Member, FlightAssignment> {

	// Internal state -------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> plannedFlightAssignments;
		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();

		plannedFlightAssignments = this.repository.findPlannedFlightAssignmentByFlightCrewMemberId(fcmIdLogged);

		super.getBuffer().addData(plannedFlightAssignments);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "duty", "lastUpdatedMoment", "currentStatus");

		super.getResponse().addData(dataset);
	}

}
