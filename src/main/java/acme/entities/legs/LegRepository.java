
package acme.entities.legs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.entities.flights.Flight;

public interface LegRepository extends JpaRepository<Leg, Integer> {

	@Query("Select l From Leg l Where l.flight = :flight Order by l.scheduledDeparture ASC Limit 1")
	Leg findFirstByFlight(@Param("flight") Flight flight);

	@Query("Select l From Leg l Where l.flight = :flight Order by l.scheduledDeparture DESC Limit 1")
	Leg findLastByFlight(@Param("flight") Flight flight);

	@Query("Select COUNT(l) From Leg l Where l.flight = :flight")
	long getLayoversByFlight(@Param("flight") Flight flight);
}
