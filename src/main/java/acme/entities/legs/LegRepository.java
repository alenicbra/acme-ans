
package acme.entities.legs;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;

public interface LegRepository extends JpaRepository<Leg, Integer> {

	@Query("SELECT l FROM Leg l WHERE l.flight = :flight ORDER BY l.scheduledDeparture ASC")
	Leg findFirstByFlight(@Param("flight") Flight flight);

	@Query("SELECT l FROM Leg l WHERE l.flight = :flight ORDER BY l.scheduledDeparture ASC")
	Leg findLastByFlight(@Param("flight") Flight flight);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight = :flight")
	long getLayoversByFlight(@Param("flight") Flight flight);

	@Query("SELECT f FROM Flight f Where f.id = :id")
	Flight findFlightById(@Param("id") int id);

	@Query("Select a From Aircraft a Where a.id = :id")
	Aircraft findAircraftById(@Param("id") int id);

	@Query("Select a From Airport a Where a.id = :id")
	Airport findAirportById(@Param("id") int id);

	@Query("Select l From Leg l Where l.id = :id")
	Leg findLegById(@Param("id") int id);

	@Query("Select l From Leg l Where l.flight = :flight")
	Collection<Leg> findAllLegByFlight(@Param("flight") Flight flight);
}
