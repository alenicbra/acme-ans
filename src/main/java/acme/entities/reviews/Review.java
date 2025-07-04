
package acme.entities.reviews;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Review", indexes = {
	@Index(columnList = "name"), @Index(columnList = "subject"), @Index(columnList = "postedMoment"), @Index(columnList = "score"), @Index(columnList = "recommended")
})
public class Review extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(max = 50)
	private String				name;

	@Mandatory
	@ValidMoment
	@Temporal(TemporalType.TIMESTAMP)
	private Date				postedMoment;

	@Mandatory
	@ValidString(max = 50)
	private String				subject;

	@Mandatory
	@ValidString(max = 255)
	private String				text;

	@Optional
	@ValidNumber(min = 0., max = 10.)
	private Double				score;

	@Optional
	private boolean				recommended;

}
