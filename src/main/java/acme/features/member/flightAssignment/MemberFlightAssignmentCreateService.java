
package acme.features.member.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.CurrentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.Member;

@GuiService
public class MemberFlightAssignmentCreateService extends AbstractGuiService<Member, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		FlightAssignment fa;
		Member member;

		member = (Member) super.getRequest().getPrincipal().getActiveRealm();

		fa = new FlightAssignment();
		fa.setDraftMode(true);
		fa.setMember(member);
		fa.setLastUpdatedMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(fa);
	}

	@Override
	public void bind(final FlightAssignment fa) {
		super.bindObject(fa, "duty", "lastUpdatedMoment", "currentStatus", "remarks", "draftMode", "member", "leg");
	}

	@Override
	public void validate(final FlightAssignment fa) {
		;
	}

	@Override
	public void perform(final FlightAssignment fa) {
		assert fa != null;
		this.repository.save(fa);
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
