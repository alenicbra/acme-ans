
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AssistanceAgent extends AbstractRole {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	// The first two or three letters correspond to their initials
	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	private String				employeeCode;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				spokenLanguages;

	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	@Mandatory
	private Date				moment;

	@ValidString(max = 255)
	@Optional
	@Automapped
	private String				briefBio;

	@ValidMoney
	@Automapped
	@Optional
	private Money				salary;

	@ValidUrl
	@Optional
	@Automapped
	private String				link;

	// Relationships -------------------------------------------------------------

	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Airline				airline;

}
