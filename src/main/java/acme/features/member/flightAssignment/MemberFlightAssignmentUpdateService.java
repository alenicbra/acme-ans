
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
public class MemberFlightAssignmentUpdateService extends AbstractGuiService<Member, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private MemberFlightAssignmentRepository repository;

	// AbstractService --------------------------------------------------------


	@Override
	public void authorise() {
		boolean authorization;

		Member fcmLogged;
		FlightAssignment faSelected;

		boolean existingFA = false;
		boolean isFlightAssignmentOwner = false;
		boolean isPublished = false;
		String metodo = super.getRequest().getMethod();
		boolean falseUpdate = false;
		Integer faId;
		boolean validLeg = true;
		boolean validDuty = true;
		boolean validStatus = true;

		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		if (!super.getRequest().getData().isEmpty() && super.getRequest().getData() != null) {
			falseUpdate = true;
			faId = super.getRequest().getData("id", Integer.class);
			if (faId != null) {
				fcmLogged = this.repository.findFlighCrewMemberById(fcmIdLogged);
				faSelected = this.repository.findFlightAssignmentById(faId);
				existingFA = faSelected != null;
				if (existingFA) {
					isFlightAssignmentOwner = faSelected.getMember() == fcmLogged;
					if (metodo.equals("GET"))
						isPublished = !faSelected.isDraftMode();
				}
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

				String currentStatus = super.getRequest().getData("currentStatus", String.class);
				if (currentStatus == null || Arrays.stream(CurrentStatus.values()).noneMatch(cs -> cs.name().equals(currentStatus)) && !currentStatus.equals("0"))
					validStatus = false;

			}
		}

		authorization = isFlightAssignmentOwner && validLeg && validDuty && validStatus && !isPublished && falseUpdate;
		super.getResponse().setAuthorised(authorization);
	}
	@Override
	public void load() {
		FlightAssignment flightAssignment;
		int faIdSolicitud;
		faIdSolicitud = super.getRequest().getData("id", int.class);
		flightAssignment = this.repository.findFlightAssignmentById(faIdSolicitud);
		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		Member fcmLogged = this.repository.findFlighCrewMemberById(fcmIdLogged);
		flightAssignment.setMember(fcmLogged);
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

		// Comprobación de leg no null
		boolean isLegNull;
		isLegNull = flightAssignment.getLeg() != null;
		if (isLegNull) {
			// Comprobación de que el FCM esté AVAILABLE
			boolean fcmAvailable;
			fcmAvailable = flightAssignment.getMember().getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);
			super.state(fcmAvailable, "*", "acme.validation.fcmAvailable-update.message");
			// Comprobación de leg no pasada
			boolean legNotPast;
			legNotPast = flightAssignment.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment());
			super.state(!legNotPast, "legRelated", "acme.validation.legNotPast.message");
			// Comprobación de leg no completada
			boolean legNotCompleted;
			legNotCompleted = flightAssignment.getLeg().getStatus().equals(LegStatus.ON_TIME) || flightAssignment.getLeg().getStatus().equals(LegStatus.DELAYED);
			super.state(legNotCompleted, "legRelated", "acme.validation.legNotCompleted.message");
			// Comprobación de leg no publicada
			boolean legNotPublished;
			legNotPublished = !flightAssignment.getLeg().isDraftMode();
			super.state(legNotPublished, "leg", "acme.validation.legNotPublished.message");

			// Comprobación de leg operada con la aerolínea del FCM
			boolean legFromRightAirline;
			legFromRightAirline = flightAssignment.getLeg().getAircraft().getAirline().equals(fcmLogged.getAirline());
			super.state(legFromRightAirline, "leg", "acme.validation.legFromRightAirline.message");
			// Comprobación de leg asignadas simultáneamente
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

		SelectChoices statuses;

		SelectChoices flightcrewsDuties;

		SelectChoices legChoices;
		List<Leg> legs;

		statuses = SelectChoices.from(CurrentStatus.class, flightAssignment.getCurrentStatus());

		flightcrewsDuties = SelectChoices.from(Duty.class, flightAssignment.getDuty());

		legs = this.repository.findAllLegs();
		legChoices = SelectChoices.from(legs, "label", flightAssignment.getLeg());

		int fcmIdLogged = super.getRequest().getPrincipal().getActiveRealm().getId();
		Member flightCrewMember = this.repository.findFlighCrewMemberById(fcmIdLogged);

		dataset = super.unbindObject(flightAssignment, "duty", "lastUpdatedMoment", "currentStatus", "remarks", "draftMode");
		dataset.put("statuses", statuses);
		dataset.put("duties", flightcrewsDuties);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("member", flightCrewMember);
		dataset.put("FCMname", flightCrewMember.getIdentity().getName() + " " + flightCrewMember.getIdentity().getSurname());
		dataset.put("lastUpdatedMoment", MomentHelper.getCurrentMoment());
		dataset.put("confirmation", false);
		dataset.put("readonly", false);
		super.getResponse().addData(dataset);
	}
}
