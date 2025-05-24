
package acme.features.assistanceAgent.trackingLogs;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.IndicatorType;
import acme.entities.trackingLogs.TrackingLog;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentTrackingLogCreateExceptionalCaseService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentTrackingLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		AssistanceAgent assistanceAgent;
		Claim claim;
		Boolean exceptionalCase;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findOneClaimById(masterId);
		assistanceAgent = claim == null ? null : claim.getAssistanceAgent();
		exceptionalCase = this.repository.countTrackingLogsForExceptionalCase(masterId) == 1;

		status = claim != null && exceptionalCase && super.getRequest().getPrincipal().hasRealm(assistanceAgent);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		TrackingLog object;
		int masterId;
		Claim claim;
		TrackingLog trackingLog;

		masterId = super.getRequest().getData("masterId", int.class);
		claim = this.repository.findOneClaimById(masterId);
		trackingLog = this.repository.findManyTrackingLogsClaimIdAndIndicator(masterId, IndicatorType.IN_PROGRESS).stream().toList().get(0);

		object = new TrackingLog();
		object.setDraftMode(true);
		object.setClaim(claim);
		object.setResolutionPercentage(100.00);
		object.setIndicator(trackingLog.getIndicator());
		object.setLastUpdateMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final TrackingLog object) {
		super.bindObject(object, "lastUpdateMoment", "step", "resolutionPercentage", "resolutionReason", "indicator");
	}

	@Override
	public void validate(final TrackingLog object) {
		if (!super.getBuffer().getErrors().hasErrors("resolutionReason"))
			super.state(Optional.ofNullable(object.getResolutionReason()).map(String::strip).filter(s -> !s.isEmpty()).isPresent(), "resolutionReason", "assistanceAgent.claim.form.error.resolution-reason-not-null");
	}

	@Override
	public void perform(final TrackingLog object) {

		object.setLastUpdateMoment(MomentHelper.getCurrentMoment());

		this.repository.save(object);
	}

	@Override
	public void unbind(final TrackingLog object) {
		Dataset dataset;

		Boolean exceptionalCase;
		Claim claim;

		claim = object.getClaim();
		exceptionalCase = this.repository.countTrackingLogsForExceptionalCase(claim.getId()) == 1;

		dataset = super.unbindObject(object, "lastUpdateMoment", "step", "resolutionPercentage", "resolutionReason", "indicator");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));
		dataset.put("exceptionalCase", exceptionalCase);

		super.getResponse().addData(dataset);
	}

}
