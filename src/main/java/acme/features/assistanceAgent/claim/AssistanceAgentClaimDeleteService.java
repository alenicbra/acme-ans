
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
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimDeleteService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		int masterId;
		Claim claim;
		AssistanceAgent assistanceAgent;

		masterId = super.getRequest().getData("id", int.class);
		claim = this.repository.findOneClaimById(masterId);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		boolean status = false;

		if (claim != null)
			if (claim.isDraftMode())
				if (super.getRequest().getPrincipal().hasRealm(assistanceAgent))
					status = true;

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
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findOneLegById(legId);

		super.bindObject(object, "registrationMoment", "email", "description", "leg", "indicator", "type");
		object.setLeg(leg);
	}

	@Override
	public void validate(final Claim object) {
	}

	@Override
	public void perform(final Claim object) {
		Collection<TrackingLog> trackingLogs;

		trackingLogs = this.repository.findManyTrackingLogsByClaimId(object.getId());
		this.repository.deleteAll(trackingLogs);
		this.repository.delete(object);
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

}
