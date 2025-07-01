
package acme.entities.airports;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Airport", indexes = {
	@Index(columnList = "iataCode", unique = true), @Index(columnList = "name"), @Index(columnList = "city"), @Index(columnList = "country")
})
public class Airport extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	private String				name;

	@Mandatory
	@ValidString(pattern = "^[A-Z]{3}$")
	private String				iataCode;

	@Mandatory
	private OperationalScope	operationalScope;

	@Mandatory
	@ValidString(max = 50)
	private String				city;

	@Mandatory
	@ValidString(max = 50)
	private String				country;

	@Optional
	@ValidUrl
	private String				website;

	@Optional
	@ValidEmail
	private String				email;

	@Optional
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

}
