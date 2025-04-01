
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airlines.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@Mandatory
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	private String				employeeCode;

	@Mandatory
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	@Automapped
	private String				phoneNumber;

	@Mandatory
	@ValidString(max = 255)
	@Automapped
	private String				languageSkills;

	@Mandatory
	@Automapped
	@Enumerated(EnumType.STRING)
	private AvailabilityStatus	availabilityStatus;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				salary;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 90)
	private Integer				yearsOfExperience;


	private enum AvailabilityStatus {
		AVAILABLE, ON_VACATION, ON_LEAVE
	}

	//------------ Relationships ---------------


	@Valid
	@Mandatory
	@ManyToOne(optional = false)
	private Airline airline;

}
