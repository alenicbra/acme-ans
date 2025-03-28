
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.legs.Leg;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@Repository
public interface AssistanceAgentClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.assistanceAgent.id = :id")
	public Collection<Claim> findManyClaimsByMasterId(int id);

	@Query("select c from Claim c where c.id = :id")
	public Claim findOneClaimById(int id);

	@Query("select l from Leg l")
	public Collection<Leg> findAllLegs();

	@Query("select a from AssistanceAgent a where a.id = :id")
	public AssistanceAgent findOneAssitanceAgentById(int id);

	@Query("select l from Leg l where l.id = :id")
	public Leg findOneLegById(int id);

	@Query("select t from TrackingLog t where t.claim.id = :id")
	public Collection<TrackingLog> findManyTrackingLogsByClaimId(int id);

}
