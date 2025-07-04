
package acme.features.assistanceAgent.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.AssistanceAgentForm;
import acme.realms.AssistanceAgent;

@GuiController
public class AssistanceAgentDashboardController extends AbstractGuiController<AssistanceAgent, AssistanceAgentForm> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AssistanceAgentDashboardShowService showService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
