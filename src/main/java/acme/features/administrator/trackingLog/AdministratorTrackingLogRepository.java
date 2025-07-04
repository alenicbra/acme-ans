
package acme.features.administrator.trackingLog;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.legs.Leg;
import acme.entities.trackingLogs.TrackingLog;

@Repository
public interface AdministratorTrackingLogRepository extends AbstractRepository {

	@Query("select t from TrackingLog t where t.draftMode = false")
	public Collection<TrackingLog> findAllPublishedTrackingLogs(); // ✅ Correct

	@Query("select s from TrackingLog s where s.id = :id")
	public TrackingLog findOneTrackingLogById(int id);

	@Query("SELECT l FROM Leg l WHERE l.draftMode = false AND l.flight.draftMode = false")
	public Collection<Leg> findAllLegs();

	@Query("select t from TrackingLog t where t.claim.id = :id")
	public Collection<TrackingLog> findManyTrackingLogsClaimId(int id);

}
