
package acme.entities.aircrafts;

import javax.persistence.Entity;

import org.checkerframework.common.aliasing.qual.Unique;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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

}
