
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

	@Query("select c from Claim c where c.assistanceAgent.id = :id")
	public Collection<Claim> findManyClaimsByMasterId(int id);

	@Query("select c from Claim c where c.indicator != :type")
	public Collection<Claim> findAllCompletedClaims(IndicatorType type);

	@Query("select l from Leg l")
	public Collection<Leg> findAllLegs();

}
