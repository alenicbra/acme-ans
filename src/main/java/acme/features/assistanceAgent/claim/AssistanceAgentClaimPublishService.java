
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.ClaimType;
import acme.entities.claims.IndicatorType;
import acme.entities.legs.Leg;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;

		masterId = super.getRequest().getData("id", int.class);
		claim = this.repository.findOneClaimById(masterId);
		status = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int masterId;
		Claim object;

		masterId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneClaimById(masterId);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Claim object) {
		assert object != null;

		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findOneLegById(legId);

		super.bindObject(object, "registrationMoment", "email", "description", "leg", "indicator", "type");
		object.setLeg(leg);
	}

	@Override
	public void validate(final Claim object) {
		super.state(this.repository.allTrackingLogsPublishedByClaimId(object.getId()), "*", "assistanceAgent.claim.form.error.all-tracking-logs-published");
		if (!super.getBuffer().getErrors().hasErrors("indicator")) {
			boolean bool1;
			boolean bool2;

			bool1 = object.getIndicator() == IndicatorType.IN_PROGRESS && this.repository.findMaxResolutionPercentageByClaimId(object.getId()) < 100;
			bool2 = object.getIndicator() != IndicatorType.IN_PROGRESS && this.repository.findMaxResolutionPercentageByClaimId(object.getId()) == 100;

			super.state(bool1 || bool2, "indicator", "assistanceAgent.claim.form.error.indicator-in-progress");
		}
	}

	@Override
	public void perform(final Claim object) {
		assert object != null;

		object.setDraftMode(false);

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

		dataset = super.unbindObject(object, "registrationMoment", "email", "description", "draftMode", "leg", "indicator", "type");
		dataset.put("legs", choices);
		dataset.put("indicators", choicesIndicator);
		dataset.put("types", choicesType);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
