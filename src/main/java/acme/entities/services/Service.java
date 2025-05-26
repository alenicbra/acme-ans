
package acme.entities.services;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Service", indexes = {
	@Index(columnList = "name"), @Index(columnList = "promoCode"), @Index(columnList = "avgDwellTime")
})
public class Service extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	private String				name;

	@Mandatory
	@ValidUrl
	private String				pictureLink;

	@Mandatory
	private Integer				avgDwellTime;

	@Optional
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$")
	private String				promoCode;

	@Optional
	@ValidMoney
	private Money				discountedMoney;

}
