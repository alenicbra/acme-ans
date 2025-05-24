
package acme.features.airline_manager.flight;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flights.Flight;
import acme.entities.legs.Leg;
import acme.realms.AirlineManager;

@Repository
public interface AirlineManagerFlightRepository extends AbstractRepository {

	@Query("Select f From Flight f Where f.manager.id = :id")
	Collection<Flight> findAllByManagerId(@Param("id") int id);

	@Query("Select f From Flight f Where f.id = :id")
	Flight findOneById(@Param("id") int id);

	@Query("Select am From AirlineManager am Where am.id = :id")
	AirlineManager findOneManagerById(@Param("id") int id);

	@Query("Select f From Flight f")
	Collection<Flight> findAllFlights();

	@Query("SELECT l FROM Leg l WHERE l.flight = :flight")
	Collection<Leg> getLegsByFlight(@Param("flight") Flight flight);

	@Query("SELECT l FROM Leg l WHERE l.flight.id = :flightId")
	Collection<Leg> getLegsByFlightId(@Param("flightId") int flightId);

}
