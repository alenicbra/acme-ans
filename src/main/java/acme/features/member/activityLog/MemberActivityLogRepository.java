
package acme.features.member.activityLog;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.Member;

@Repository
public interface MemberActivityLogRepository extends AbstractRepository {

	@Query("select fa from FlightAssignment fa where fa.id = :id")
	FlightAssignment findFlightAssignmentById(int id);

	@Query("select al from ActivityLog al where al.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("select al from ActivityLog al where al.flightAssignment.id = :faId and al.flightAssignment.member.id = :fcmId")
	Collection<ActivityLog> findOwnedActivityLogsByFAId(int faId, int fcmId);

	@Query("select fcm from Member fcm where fcm.id = :id")
	Member findFlighCrewMemberById(int id);

	@Query("select fa from FlightAssignment fa")
	List<FlightAssignment> findAllFlightAssignments();

	@Query("select al from ActivityLog al")
	List<ActivityLog> findAllActivityLog();
}
