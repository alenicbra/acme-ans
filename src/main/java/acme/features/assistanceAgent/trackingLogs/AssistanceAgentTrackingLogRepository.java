
package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AssistanceAgentTrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.claim.assistanceAgent.id = :id")
	public Collection<TrackingLog> findManyTrackingLogsByMasterId(int id);

	@Query("select c from Claim c where c.id = :id")
	public Claim findOneClaimById(int id);
}
