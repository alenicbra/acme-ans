
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.claims.IndicatorType;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("id", int.class);
		claim = this.repository.findOneClaimById(masterId);
		status = claim != null && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findOneClaimById(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim object) {
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
