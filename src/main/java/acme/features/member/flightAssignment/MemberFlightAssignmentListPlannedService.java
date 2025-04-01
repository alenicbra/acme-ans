
package acme.features.member.flightAssignment;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.LegStatus;
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
		Collection<FlightAssignment> fas;
		Collection<LegStatus> statuses = List.of(LegStatus.ON_TIME, LegStatus.DELAYED);
		int memberId;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		fas = this.repository.findPlannedFlightAssignmentByMemberId(statuses, memberId);

		super.getBuffer().addData(fas);

	}

	@Override
	public void unbind(final FlightAssignment fa) {
		Dataset dataset;

		dataset = super.unbindObject(fa, "duty", "lastUpdatedMoment", "currentStatus");
		super.addPayload(dataset, fa, "remarks", "draftMode", "member.identity.fullName", "leg.Status");

		super.getResponse().addData(dataset);
	}

}
