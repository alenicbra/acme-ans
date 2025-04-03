
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
import acme.entities.legs.LegStatus;
import acme.realms.Member;
import acme.realms.Member.AvailabilityStatus;

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
		assert fa != null;
		if (!super.getBuffer().getErrors().hasErrors("duty")) {
			boolean condition_p = true;
			boolean condition_cp = true;
			boolean condition_la = true;

			for (FlightAssignment e_fa : this.repository.findAllFlightAssignmentByLegId(fa.getLeg().getId())) {
				Duty e_duty = e_fa.getDuty();
				if (e_duty.equals(Duty.PILOT) && e_duty.equals(fa.getDuty()))
					condition_p = false;
				else if (e_duty.equals(Duty.COPILOT) && e_duty.equals(fa.getDuty()))
					condition_cp = false;
				else if (e_duty.equals(Duty.LEAD_ATTENDANT) && e_duty.equals(fa.getDuty()))
					condition_la = false;
			}
			super.state(condition_p, "duty", "member.flight-assignment.error.duty.duplicated-pilot");
			super.state(condition_cp, "duty", "member.flight-assignment.error.duty.duplicated-copilot");
			super.state(condition_la, "duty", "member.flight-assignment.error.duty.duplicated-lead-attendant");
		}

		if (!super.getBuffer().getErrors().hasErrors("member")) {
			Member fa_member = fa.getMember();
			super.state(fa_member.getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE), "member", "member.flight-assignment.error.member.availabitity");
		}
		if (!super.getBuffer().getErrors().hasErrors("leg")) {
			Leg fa_leg = fa.getLeg();
			super.state(!fa_leg.getStatus().equals(LegStatus.LANDED), "leg", "member.flight-assignment.error.leg.landed-leg");
		}
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
		legChoice = SelectChoices.from(legs, "flightNumberNumber", fa.getLeg());

		members = this.repository.findAllMembers();
		memberChoice = SelectChoices.from(members, "employeeCode", fa.getMember());

		dataset = super.unbindObject(fa, "duty", "lastUpdatedMoment", "currentStatus", "remarks", "draftMode", "leg", "member");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);
		dataset.put("memberChoice", memberChoice);

		super.getResponse().addData(dataset);
	}

}
