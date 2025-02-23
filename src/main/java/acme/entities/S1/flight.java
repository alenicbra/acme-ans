
package acme.entities.S1;

import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class flight extends AbstractEntity {

	/// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@ValidString(max = 50)
	@Mandatory
	@Automapped
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				indication;

	@ValidMoney
	@Mandatory
	@Automapped
	private Money				cost;

	@Optional
	@Automapped
	@ValidString(max = 254)
	private String				description;

}
