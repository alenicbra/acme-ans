
package acme.features.assistanceAgent.trackingLogs;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Claim claim;
		AssistanceAgent assistanceAgent;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findOneClaimById(masterId);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		status = claim != null && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<TrackingLog> objects;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findManyTrackingLogsByMasterId(masterId);

		super.getBuffer().addData(objects);
	}
}
