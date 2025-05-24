
package acme.features.assistanceAgent.trackingLogs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.IndicatorType;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		status = trackingLog != null && trackingLog.isDraftMode() && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

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
	public void bind(final TrackingLog object) {
		super.bindObject(object, "lastUpdateMoment", "step", "resolutionPercentage", "resolutionReason", "indicator");
	}

	@Override
	public void validate(final TrackingLog object) {
		if (!super.getBuffer().getErrors().hasErrors("indicator")) {
			boolean bool1;
			boolean bool2;
			bool1 = object.getIndicator() == IndicatorType.IN_PROGRESS && object.getResolutionPercentage() < 100;
			bool2 = object.getIndicator() != IndicatorType.IN_PROGRESS && object.getResolutionPercentage() == 100;
			super.state(bool1 || bool2, "indicator", "assistanceAgent.claim.form.error.indicator-in-progress");
		}
		if (!super.getBuffer().getErrors().hasErrors("resolutionReason")) {
			boolean isInProgress = object.getIndicator() == IndicatorType.IN_PROGRESS;

			boolean valid = isInProgress && !Optional.ofNullable(object.getResolutionReason()).map(String::strip).filter(s -> !s.isEmpty()).isPresent()
				|| !isInProgress && Optional.ofNullable(object.getResolutionReason()).map(String::strip).filter(s -> !s.isEmpty()).isPresent();

			super.state(valid, "resolutionReason", "assistanceAgent.claim.form.error.resolution-reason-not-null");
		}
	}

	@Override
	public void perform(final TrackingLog object) {
		Claim claim;

		claim = this.repository.findOneClaimById(object.getClaim().getId());
		claim.setIndicator(object.getIndicator());

		object.setLastUpdateMoment(MomentHelper.getCurrentMoment());

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrackingLog object) {
		Dataset dataset;

		SelectChoices choicesIndicator;

		choicesIndicator = SelectChoices.from(IndicatorType.class, object.getIndicator());

		dataset = super.unbindObject(object, "lastUpdateMoment", "step", "resolutionPercentage", "draftMode", "resolutionReason", "indicator");
		dataset.put("indicators", choicesIndicator);

		super.getResponse().addData(dataset);
	}

}
