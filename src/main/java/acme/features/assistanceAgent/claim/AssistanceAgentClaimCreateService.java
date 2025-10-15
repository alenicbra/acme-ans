
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.claims.IndicatorType;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		AssistanceAgent assistanceAgent;
		boolean status = false;
		boolean bool = true;
		int legId;
		Leg leg;

		if (super.getRequest().getMethod().equals("GET"))
			bool = true;
		else {
			legId = super.getRequest().getData("leg", int.class);
			leg = this.repository.findLegById(legId);

			boolean isLegValid = leg != null;

			if (isLegValid) {
				boolean isLegNotDraft = !leg.isDraftMode();
				if (isLegNotDraft) {
					boolean isFlightNotDraft = !leg.getFlight().getDraftMode();
					bool = isFlightNotDraft;
				} else
					bool = isLegNotDraft;
			}
		}

		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		if (super.getRequest().getPrincipal().hasRealm(assistanceAgent))
			if (bool)
				status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim object;
		AssistanceAgent assistanceAgent;

		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		object = new Claim();
		object.setDraftMode(true);
		object.setAssistanceAgent(assistanceAgent);
		object.setIndicator(IndicatorType.IN_PROGRESS);
		object.setRegistrationMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Claim object) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findOneLegById(legId);

		super.bindObject(object, "registrationMoment", "email", "description", "leg", "indicator", "type");
		object.setLeg(leg);
	}

	@Override
	public void validate(final Claim object) {
		if (!super.getBuffer().getErrors().hasErrors("indicator"))
			super.state(object.getIndicator() == IndicatorType.IN_PROGRESS, "indicator", "assistanceAgent.claim.form.error.indicator-in-progress");

	}

	@Override
	public void perform(final Claim object) {

		object.setRegistrationMoment(MomentHelper.getCurrentMoment());

		this.repository.save(object);
	}

	@Override
	public void unbind(final Claim object) {
		Dataset dataset;

		Collection<Leg> legs;
		SelectChoices choices;
		SelectChoices choicesIndicator;
		SelectChoices choicesType;

		legs = this.repository.findAllLegs();

		choices = SelectChoices.from(legs, "flightNumberNumber", object.getLeg());
		choicesType = SelectChoices.from(ClaimType.class, object.getType());
		choicesIndicator = SelectChoices.from(IndicatorType.class, object.getIndicator());

		dataset = super.unbindObject(object, "registrationMoment", "email", "description", "leg", "indicator", "type");
		dataset.put("legs", choices);
		dataset.put("indicators", choicesIndicator);
		dataset.put("types", choicesType);

		super.getResponse().addData(dataset);
	}

}
