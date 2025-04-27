
package acme.features.airline_manager.legs;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.google.common.base.Optional;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircrafts.Aircraft;
import acme.entities.airports.Airport;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;

@Repository
public interface AirlineManagerLegRepository extends AbstractRepository {

	Leg findFirstLegByFlightIdOrderByScheduledDeparture(Integer flightId);

	Leg findFirstLegByFlightIdOrderByScheduledDepartureDesc(Integer flightId);

	@Query("SELECT COUNT(l) FROM Leg l WHERE l.flight = :flight")
	long getLayoversByFlight(@Param("flight") Flight flight);

	@Query("SELECT f FROM Flight f Where f.id = :id")
	Flight findFlightById(@Param("id") int id);

	@Query("Select a From Aircraft a Where a.id = :id")
	Aircraft findAircraftById(@Param("id") int id);

	@Query("Select a From Airport a Where a.id = :id")
	Airport findAirportById(@Param("id") int id);

	@Query("Select a From Aircraft a")
	Collection<Aircraft> findAllAircraft();

	@Query("Select a From Airport a")
	Collection<Airport> findAllAirport();

	@Query("Select l From Leg l Where l.id = :id")
	Leg findLegById(@Param("id") int id);

	@Query("Select l From Leg l Where l.flight = :flight")
	Collection<Leg> findAllLegByFlight(@Param("flight") Flight flight);

	@Query("SELECT l FROM Leg l WHERE l.flightNumberNumber = :flightNumber AND l.id != :legId")
	Optional<Leg> findByFlightNumber(String flightNumber, int legId);
}
