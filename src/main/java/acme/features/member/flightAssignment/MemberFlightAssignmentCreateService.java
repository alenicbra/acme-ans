
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
		boolean status;
		int legId;
		Leg leg;
		Collection<Leg> validLegs;
		int memberId;
		int airlineId;
		if (super.getRequest().getMethod().equals("GET"))
			status = true;
		else {
			memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
			airlineId = this.repository.findAirlineIdByMemberId(memberId);
			validLegs = this.repository.findLegsByAirlineId(airlineId);
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);
			status = legId == 0 || leg != null && validLegs.contains(leg);
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment fa;
		Member member;

		member = (Member) super.getRequest().getPrincipal().getActiveRealm();

		fa = new FlightAssignment();
		fa.setDraftMode(true);
		fa.setMember(member);
		fa.setDuty(Duty.PILOT);
		fa.setCurrentStatus(CurrentStatus.PENDING);

		super.getBuffer().addData(fa);
	}

	@Override
	public void bind(final FlightAssignment fa) {
		int legId;

		legId = super.getRequest().getData("leg", int.class);
		Leg leg = this.repository.findLegById(legId);
		super.bindObject(fa, "duty", "currentStatus", "remarks");
		fa.setDraftMode(true);
		fa.setLeg(leg);
		fa.setLastUpdatedMoment(MomentHelper.getCurrentMoment());
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

		int memberId;
		int airlineId;

		dutyChoice = SelectChoices.from(Duty.class, fa.getDuty());
		currentStatusChoice = SelectChoices.from(CurrentStatus.class, fa.getCurrentStatus());

		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airlineId = this.repository.findAirlineIdByMemberId(memberId);
		legs = this.repository.findLegsByAirlineId(airlineId);
		legChoice = SelectChoices.from(legs, "flightNumberNumber", fa.getLeg());

		dataset = super.unbindObject(fa, "duty", "lastUpdatedMoment", "currentStatus", "remarks", "draftMode", "leg", "member");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);

		super.getResponse().addData(dataset);
	}

}
