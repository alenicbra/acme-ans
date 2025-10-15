
package acme.features.member.flightAssignment;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.Member;

@Repository
public interface MemberFlightAssignmentRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa")
	List<FlightAssignment> findAllFlightAssignments();

	@Query("select fa from FlightAssignment fa where fa.member.id = :id and (fa.leg.status = acme.entities.legs.LegStatus.LANDED or fa.leg.status = acme.entities.legs.LegStatus.CANCELLED) ")
	Collection<FlightAssignment> findCompletedFlightAssignmentByFlightCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.member.id = :id and (fa.leg.status = acme.entities.legs.LegStatus.DELAYED or fa.leg.status = acme.entities.legs.LegStatus.ON_TIME)")
	Collection<FlightAssignment> findPlannedFlightAssignmentByFlightCrewMemberId(int id);

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select fcm from Member fcm where fcm.id = :id")
	Member findFlighCrewMemberById(int id);

	@Query("select l from Leg l where l.id = :id")
	Leg findLegById(int id);

	@Query("select l from Leg l")
	List<Leg> findAllLegs();

	@Query("SELECT DISTINCT fa.leg FROM FlightAssignment fa WHERE fa.member.id = :memberId and fa.id != :faId ")
	List<Leg> findLegsByFlightCrewMemberId(int memberId, int faId);

	@Query("select fa from FlightAssignment fa where fa.duty =  acme.entities.flightAssignments.Duty.PILOT and fa.leg.id = :idLeg and fa.draftMode = false")
	List<FlightAssignment> findPilotInLeg(int idLeg);

	@Query("select fa from FlightAssignment fa where fa.duty =  acme.entities.flightAssignments.Duty.CO_PILOT and fa.leg.id = :idLeg and fa.draftMode = false")
	List<FlightAssignment> findCoPilotInLeg(int idLeg);

	@Query("select al from ActivityLog al where al.flightAssignment.id = :id")
	List<ActivityLog> findActivityLogsByFAId(int id);
}
