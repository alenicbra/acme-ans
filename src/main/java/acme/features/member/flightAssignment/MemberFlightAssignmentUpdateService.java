
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
public class MemberFlightAssignmentUpdateService extends AbstractGuiService<Member, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentRepository repository;

	// AbstractService --------------------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightAssignment fa;
		int memberId;

		masterId = super.getRequest().getData("id", int.class);
		fa = this.repository.findFlightAssignmentById(masterId);
		memberId = fa == null ? null : super.getRequest().getPrincipal().getActiveRealm().getId();
		status = fa != null && fa.isDraftMode() && fa.getMember().getId() == memberId;

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
	public void bind(final FlightAssignment fa) {
		super.bindObject(fa, "duty", "lastUpdatedMoment", "currentStatus", "remarks", "draftMode", "leg", "member");
	}

	@Override
	public void validate(final FlightAssignment fa) {
		boolean status;
		status = fa.getDuty().equals(Duty.LEAD_ATTENDANT);
		super.state(status, "*", "member.flight-assignment.update.correct-duty");
	}

	@Override
	public void perform(final FlightAssignment fa) {
		assert fa != null;
		fa.setLastUpdatedMoment(MomentHelper.getCurrentMoment());
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
