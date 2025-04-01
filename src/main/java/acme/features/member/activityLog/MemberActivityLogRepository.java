
package acme.features.member.activityLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.Member;

@Repository
public interface MemberActivityLogRepository extends AbstractRepository {

	@Query("SELECT al FROM ActivityLog al WHERE al.id = :id")
	ActivityLog findActivityLogById(int id);

	@Query("SELECT m FROM Member m WHERE m.id = :id")
	Member findMemberById(int id);

	@Query("SELECT al FROM ActivityLog al WHERE al.flightAssignment.member.id = :id")
	Collection<ActivityLog> findActivityLogByMemberId(int id);

}
