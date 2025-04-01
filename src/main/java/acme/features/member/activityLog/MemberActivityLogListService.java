
package acme.features.member.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.Member;

@GuiService
public class MemberActivityLogListService extends AbstractGuiService<Member, ActivityLog> {

	// Internal state -------------------------------------------------------

	@Autowired
	private MemberActivityLogRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<ActivityLog> als;
		int memberId;

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		als = this.repository.findActivityLogByMemberId(memberId);

		super.getBuffer().addData(als);
	}

	@Override
	public void unbind(final ActivityLog al) {
		Dataset dataset;

		dataset = super.unbindObject(al, "registrationMoment", "typeOfIncident", "description", "severityLevel", "draftMode");

		super.getResponse().addData(dataset);
	}

}
