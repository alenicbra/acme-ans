
package acme.features.any.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.claims.IndicatorType;
import acme.entities.legs.Leg;

@GuiService
public class AnyClaimShowService extends AbstractGuiService<Any, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> objects;
		objects = this.repository.findAllCompletedClaims(IndicatorType.IN_PROGRESS);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		Dataset dataset;

		Collection<Leg> legs;
		SelectChoices choices;
		SelectChoices choicesIndicator;
		SelectChoices choicesType;

		legs = this.repository.findAllLegs();

		choices = SelectChoices.from(legs, "code", object.getLeg());
		choicesType = SelectChoices.from(ClaimType.class, object.getType());
		choicesIndicator = SelectChoices.from(IndicatorType.class, object.getIndicator());

		dataset = super.unbindObject(object, "registrationMoment", "email", "description", "link", "draftMode");
		dataset.put("legs", choices.getSelected().getKey());
		dataset.put("leg", choices);
		dataset.put("indicators", choicesIndicator.getSelected().getKey());
		dataset.put("indicator", choices);
		dataset.put("type", choicesType.getSelected().getKey());
		dataset.put("types", choicesType);

		super.getResponse().addData(dataset);
	}
}
