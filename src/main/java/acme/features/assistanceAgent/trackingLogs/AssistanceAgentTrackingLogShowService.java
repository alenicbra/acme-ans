
package acme.features.assistanceAgent.trackingLogs;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.IndicatorType;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		AssistanceAgent assistanceAgent;
		TrackingLog trackingLog;

		masterId = super.getRequest().getData("id", int.class);
		trackingLog = this.repository.findOneTrackingLogById(masterId);
		assistanceAgent = trackingLog == null ? null : trackingLog.getClaim().getAssistanceAgent();
		status = trackingLog != null && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int masterId;
		TrackingLog object;

		masterId = super.getRequest().getData("id", int.class);
		object = this.repository.findOneTrackingLogById(masterId);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final TrackingLog object) {
		Dataset dataset;
		Boolean exceptionalCase;
		SelectChoices choicesIndicator;
		Claim claim;

		claim = object.getClaim();
		exceptionalCase = !claim.isDraftMode() && this.repository.countTrackingLogsForExceptionalCase(claim.getId()) == 1;
		choicesIndicator = SelectChoices.from(IndicatorType.class, object.getIndicator());

		dataset = super.unbindObject(object, "lastUpdateMoment", "step", "resolutionPercentage", "draftMode", "resolutionReason", "indicator");
		dataset.put("indicators", choicesIndicator);
		dataset.put("exceptionalCase", exceptionalCase);

		super.getResponse().addData(dataset);
	}

}
