
package acme.features.member.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.CurrentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.Member;

@GuiService
public class MemberFlightAssignmentShowService extends AbstractGuiService<Member, FlightAssignment> {

	// Internal state -------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -----------------------------------------


	@Override
	public void authorise() {
		boolean status;
		FlightAssignment fa;
		Member member;

		fa = this.repository.findFlightAssignmentById(super.getRequest().getData("id", int.class));
		member = fa == null ? null : fa.getMember();
		status = super.getRequest().getPrincipal().hasRealm(member) || fa != null && fa.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment fa;
		int id;

		id = super.getRequest().getData("id", int.class);
		fa = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(fa);
	}

	@Override
	public void unbind(final FlightAssignment fa) {
		Dataset dataset;
		SelectChoices dutyChoice;
		SelectChoices currentStatusChoice;

		SelectChoices legChoice;
		Collection<Leg> legs;

		SelectChoices memberChoice;
		Collection<Member> members;

		dutyChoice = SelectChoices.from(Duty.class, fa.getDuty());
		currentStatusChoice = SelectChoices.from(CurrentStatus.class, fa.getCurrentStatus());

		legs = this.repository.findAllLegs();
		legChoice = SelectChoices.from(legs, "id", fa.getLeg());

		members = this.repository.findAllMembers();
		memberChoice = SelectChoices.from(members, "id", fa.getMember());

		dataset = super.unbindObject(fa, "duty", "lastUpdatedMoment", "currentStatus", "remarks", "draftMode", "leg", "member");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("leadDuty", Duty.LEAD_ATTENDANT);
		dataset.put("legChoice", legChoice);
		dataset.put("memberChoice", memberChoice);

		super.getResponse().addData(dataset);
	}

}
