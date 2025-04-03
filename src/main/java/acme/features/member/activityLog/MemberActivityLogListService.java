
package acme.features.member.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.Member;

@GuiService
public class MemberActivityLogListService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state -------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		int masterId;
		int memberId;
		FlightAssignment fa;
		boolean status;

		masterId = super.getRequest().getData("masterId", int.class);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		fa = this.repository.findFlightAssignmentById(masterId);

		status = fa.getMember().getId() == memberId;
		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Collection<ActivityLog> als;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		als = this.repository.findActivityLogsByAssignmentId(masterId);
		super.getBuffer().addData(als);
	}

	@Override
	public void unbind(final ActivityLog al) {
		Dataset dataset;
		int masterId;

		dataset = super.unbindObject(al, "registrationMoment", "typeOfIncident", "severityLevel");
		masterId = super.getRequest().getData("masterId", int.class);

		super.addPayload(dataset, al, "draftMode");
		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addData(dataset);
	}

}
