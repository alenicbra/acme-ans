
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

	@Query("select t from TrackingLog t where t.id = :id")
	public TrackingLog findOneTrackingLogById(int id);

	@Query("select max(t.resolutionPercentage) from TrackingLog t where t.claim.id = :claimId and t.id != :id")
	public Double findMaxResolutionPercentageByClaimId(int id, int claimId);

	@Query("SELECT COUNT(t) FROM TrackingLog t WHERE t.claim.id = :claimId AND t.resolutionPercentage = 100.00")
	public Long countTrackingLogsForExceptionalCase(int claimId);
}
