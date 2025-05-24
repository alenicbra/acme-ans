
package acme.features.assistanceAgent.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.IndicatorType;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentClaimListCompletedService extends AbstractGuiService<AssistanceAgent, Claim> {

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
		objects = this.repository.findManyClaimsCompletedByMasterId(masterId, IndicatorType.IN_PROGRESS);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Claim object) {
		String published;
		Dataset dataset;

		dataset = super.unbindObject(object, "type", "indicator");
		published = !object.isDraftMode() ? "✓" : "x";
		dataset.put("published", published);
		dataset.put("leg", object.getLeg().getFlightNumberNumber());

		super.getResponse().addData(dataset);
	}
}
