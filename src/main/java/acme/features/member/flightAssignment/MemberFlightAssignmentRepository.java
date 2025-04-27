
package acme.features.member.flightAssignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.entities.legs.LegStatus;
import acme.realms.Member;

@Repository
public interface MemberFlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.member.id = :id")
	Collection<FlightAssignment> findFlightAssignmentByMemberId(int id);

	@Query("SELECT f FROM FlightAssignment f WHERE f.leg.status = ?1 AND f.member.id = ?2")
	Collection<FlightAssignment> findCompletedFlightAssignmentByMemberId(LegStatus legStatus, Integer member);

	@Query("SELECT f FROM FlightAssignment f WHERE f.leg.status in ?1 AND f.member.id = ?2")
	Collection<FlightAssignment> findPlannedFlightAssignmentByMemberId(Collection<LegStatus> statuses, Integer member);

	@Query("SELECT l FROM Leg l")
	Collection<Leg> findAllLegs();

	@Query("SELECT l FROM Leg l WHERE l.id = ?1")
	Leg findLegById(int id);

	@Query("SELECT m FROM Member m")
	Collection<Member> findAllMembers();

	@Query("SELECT m FROM Member m WHERE m.id = :id")
	Member findMemberById(int id);

	@Query("SELECT al FROM ActivityLog al WHERE al.flightAssignment.id = ?1")
	Collection<ActivityLog> findActivityLogsByAssignmentId(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.leg.id = ?1")
	Collection<FlightAssignment> findAllFlightAssignmentByLegId(int id);
}
