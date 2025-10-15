
package acme.features.member.flightAssignment;

import java.util.Arrays;
import java.util.List;

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

		// Comprobacion de leg
		String metodo = super.getRequest().getMethod();
		boolean authorised;

		boolean validLeg = true;
		boolean validDuty = true;
		boolean fakeUpdate = true;

		if (super.getRequest().hasData("id")) {
			Integer id = super.getRequest().getData("id", Integer.class);
			if (id != 0)
				fakeUpdate = false;
		}

		if (metodo.equals("POST")) {
			Integer legId = super.getRequest().getData("leg", Integer.class);

			if (legId == null)
				validLeg = false;
			else {
				Leg leg = this.repository.findLegById(legId);
				if (leg == null && legId != 0)
					validLeg = false;
			}

			String duty = super.getRequest().getData("duty", String.class);
			if (duty == null || Arrays.stream(Duty.values()).noneMatch(tc -> tc.name().equals(duty)) && !duty.equals("0"))
				validDuty = false;

		}

		authorised = validLeg && validDuty && fakeUpdate;

		super.getResponse().setAuthorised(authorised);

	}

	@Override
	public void load() {
		FlightAssignment flightAssignment;
		flightAssignment = new FlightAssignment();

		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		Member flightCrewMember = this.repository.findFlighCrewMemberById(fcmIdLogged);

		flightAssignment.setDraftMode(true);
		flightAssignment.setMember(flightCrewMember);
		flightAssignment.setCurrentStatus(CurrentStatus.PENDING);

		super.getBuffer().addData(flightAssignment);
	}

	@Override
	public void bind(final FlightAssignment flightAssignment) {
		Member flightCrewMember;

		int legId;
		Leg leg;

		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		flightCrewMember = this.repository.findFlighCrewMemberById(fcmIdLogged);

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(flightAssignment, "duty", "currentStatus", "remarks");
		flightAssignment.setLastUpdatedMoment(MomentHelper.getCurrentMoment());
		flightAssignment.setMember(flightCrewMember);
		flightAssignment.setLeg(leg);
	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {

		Member fcmLogged;
		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		fcmLogged = this.repository.findFlighCrewMemberById(fcmIdLogged);

		boolean isLegNull;
		isLegNull = flightAssignment.getLeg() != null;
		if (isLegNull) {

			// Comprobación de leg no pasada
			boolean legNotPast;
			legNotPast = flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment());
			super.state(!legNotPast, "leg", "acme.validation.legNotPast.message");

			// Comprobación de leg no completada
			boolean legNotCompleted;
			legNotCompleted = flightAssignment.getLeg().getStatus().equals(LegStatus.ON_TIME) || flightAssignment.getLeg().getStatus().equals(LegStatus.DELAYED);
			super.state(legNotCompleted, "leg", "acme.validation.legNotCompleted.message");

			// Comprobación de leg no publicada
			boolean legNotPublished;
			legNotPublished = !flightAssignment.getLeg().isDraftMode();
			super.state(legNotPublished, "leg", "acme.validation.legNotPublished.message");

			// Comprobación de leg operada con la aerolínea del FCM
			boolean legFromRightAirline;
			legFromRightAirline = flightAssignment.getLeg().getAircraft().getAirline().equals(fcmLogged.getAirline());
			super.state(legFromRightAirline, "leg", "acme.validation.legFromRightAirline.message");

			// Comprobación de que el FCM esté AVAILABLE
			boolean fcmAvailable;
			fcmAvailable = flightAssignment.getMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
			super.state(fcmAvailable, "*", "acme.validation.fcmAvailable-create.message");

			// Comprobación de leg asignadas al fcm no sea a la vez que otra
			boolean legCompatible = true;

			List<Leg> legByFCM = this.repository.findLegsByFlightCrewMemberId(flightAssignment.getMember().getId(), flightAssignment.getId()).stream().toList();
			for (Leg l : legByFCM)
				if (this.legIsCompatible(flightAssignment.getLeg(), l)) {
					legCompatible = false;
					super.state(legCompatible, "leg", "acme.validation.legCompatible.message");
					break;
				}
		}

		boolean confirmation;
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");

	}

	private boolean legIsCompatible(final Leg finalLeg, final Leg legToCompare) {
		boolean departureCompatible = MomentHelper.isInRange(finalLeg.getScheduledDeparture(), legToCompare.getScheduledDeparture(), legToCompare.getScheduledArrival());
		boolean arrivalCompatible = MomentHelper.isInRange(finalLeg.getScheduledArrival(), legToCompare.getScheduledDeparture(), legToCompare.getScheduledArrival());
		return departureCompatible && arrivalCompatible;
	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		SelectChoices flightcrewsDuties;

		SelectChoices legChoices;
		List<Leg> legs;

		flightcrewsDuties = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		legs = this.repository.findAllLegs();
		legChoices = SelectChoices.from(legs, "label", flightAssignment.getLeg());

		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		Member flightCrewMember = this.repository.findFlighCrewMemberById(fcmIdLogged);

		dataset = super.unbindObject(flightAssignment, "duty", "lastUpdatedMoment", "remarks", "draftMode");
		dataset.put("currentStatus", CurrentStatus.PENDING);
		dataset.put("member", flightCrewMember);
		dataset.put("FCMname", flightCrewMember.getIdentity().getName() + " " + flightCrewMember.getIdentity().getSurname());
		dataset.put("duties", flightcrewsDuties);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("lastUpdatedMoment", MomentHelper.getCurrentMoment());

		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);

	}

}
