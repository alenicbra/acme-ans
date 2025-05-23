
package acme.features.assistanceAgent.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.IndicatorType;
import acme.forms.AssistanceAgentForm;
import acme.realms.AssistanceAgent;

@GuiService
public class AssistanceAgentDashboardShowService extends AbstractGuiService<AssistanceAgent, AssistanceAgentForm> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		AssistanceAgentForm dashboard = new AssistanceAgentForm();

		dashboard.setRatioOfClaimsStoredSuccessfully(this.repository.ratioOfClaimsStoredSuccessfully(IndicatorType.ACCEPTED));
		dashboard.setRatioOfClaimsRejected(this.repository.ratioOfClaimsRejected(IndicatorType.DENIED));

		List<Integer> topMonths = this.repository.topThreeMonthsHighestNumberOfClaims(Pageable.ofSize(3));
		dashboard.setTopThreeMonthsHighestNumberOfClaims(topMonths);

		dashboard.setAvgNumberOfLogsClaimsHave(0.0);
		dashboard.setMinNumberOfLogsClaimsHave(0.0);
		dashboard.setMaxNumberOfLogsClaimsHave(0.0);
		dashboard.setDevNumberOfLogsClaimsHave(0.0);

		dashboard.setAvgNumberOfLogsClaimsAssistedDuringLastMonth(0.0);
		dashboard.setMinNumberOfLogsClaimsAssistedDuringLastMonth(0.0);
		dashboard.setMaxNumberOfLogsClaimsAssistedDuringLastMonth(0.0);
		dashboard.setDevNumberOfLogsClaimsAssistedDuringLastMonth(0.0);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final AssistanceAgentForm object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "ratioOfClaimsStoredSuccessfully", "ratioOfClaimsRejected", "topThreeMonthsHighestNumberOfClaims", "avgNumberOfLogsClaimsHave", "minNumberOfLogsClaimsHave", "maxNumberOfLogsClaimsHave",
			"devNumberOfLogsClaimsHave", "avgNumberOfLogsClaimsAssistedDuringLastMonth", "minNumberOfLogsClaimsAssistedDuringLastMonth", "maxNumberOfLogsClaimsAssistedDuringLastMonth", "devNumberOfLogsClaimsAssistedDuringLastMonth");

		super.getResponse().addData(dataset);
	}
}
