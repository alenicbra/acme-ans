
package acme.entities.bookings;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import acme.client.components.basis.AbstractEntity;
import acme.entities.passengers.Passenger;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookingPassengers extends AbstractEntity {

	// Serialisation version ---------------------

	private static final long	serialVersionUID	= 1L;

	// Relationships ------------------------------
	@ManyToOne(optional = false)
	private Booking				booking;

	@ManyToOne(optional = false)
	private Passenger			passenger;

}
