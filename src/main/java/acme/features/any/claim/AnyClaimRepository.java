
package acme.features.any.claim;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claims.Claim;
import acme.entities.claims.IndicatorType;
import acme.entities.legs.Leg;

@Repository
public interface AnyClaimRepository extends AbstractRepository {

	@Query("select c from Claim c where c.assistanceAgent.id = :id and c.indicator != :type")
	public Collection<Claim> findManyClaimsCompletedByMasterId(int id, IndicatorType type);

	@Query("select c from Claim c where c.indicator != :type")
	public Collection<Claim> findAllCompletedClaims(IndicatorType type);

	@Query("select l from Leg l")
	public Collection<Leg> findAllLegs();

}
