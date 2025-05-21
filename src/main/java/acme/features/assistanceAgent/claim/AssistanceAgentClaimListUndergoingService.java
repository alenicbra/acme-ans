
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.IndicatorType;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListUndergoingService extends AbstractGuiService<AssistanceAgent, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		AssistanceAgent assistanceAgent;
		boolean status;

		assistanceAgent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();
		status = super.getRequest().getPrincipal().hasRealm(assistanceAgent);
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Claim> objects;
		int masterId;

		masterId = super.getRequest().getPrincipal().getActiveRealm().getId();
		objects = this.repository.findManyClaimsUndergoingByMasterId(masterId, IndicatorType.IN_PROGRESS);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		String published;
		Dataset dataset;

		Collection<TrackingLog> tlogs;
		IndicatorType value;

		tlogs = this.repository.findManyTrackingLogsByClaimId(object.getId());
		value = tlogs.stream().map(t -> t.getIndicator()).filter(t -> t.equals(IndicatorType.ACCEPTED) || t.equals(IndicatorType.DENIED)).findFirst().orElse(IndicatorType.IN_PROGRESS);
		object.setIndicator(value);

		dataset = super.unbindObject(object, "type", "indicator");
		published = !object.isDraftMode() ? "✓" : "x";
		dataset.put("published", published);
		dataset.put("leg", object.getLeg().getFlightNumberNumber());

		super.getResponse().addData(dataset);
	}
}
