
package acme.entities.aircrafts;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import org.checkerframework.common.aliasing.qual.Unique;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Aircraft", indexes = {
	@Index(columnList = "registrationNumber", unique = true), @Index(columnList = "airline_id"), @Index(columnList = "active"), @Index(columnList = "model")
})
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	private String				model;

	@Mandatory
	@Unique
	@ValidString(max = 50)
	private String				registrationNumber;

	@Mandatory
	private Integer				capacity;

	@Mandatory
	@ValidNumber(min = 2000, max = 50000)
	private Integer				cargoWeight;

	@Mandatory
	private boolean				active;

	@Optional
	@ValidString(max = 255)
	private String				details;

	@ManyToOne(optional = false)
	@Mandatory
	@Valid
	private Airline				airline;

}
