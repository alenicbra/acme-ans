
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssistanceAgentDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	Double						ratioOfClaimsStoredSuccessfully;
	Double						ratioOfClaimsRejected;

	Map<String, Integer>		topThreeMonthsHighestNumberOfClaims;

	Double						avgNumberOfLogsClaimsHave;
	Double						minNumberOfLogsClaimsHave;
	Double						maxNumberOfLogsClaimsHave;
	Double						devNumberOfLogsClaimsHave;

	Double						avgNumberOfLogsClaimsAssistedDuringLastMonth;
	Double						minNumberOfLogsClaimsAssistedDuringLastMonth;
	Double						maxNumberOfLogsClaimsAssistedDuringLastMonth;
	Double						devNumberOfLogsClaimsAssistedDuringLastMonth;
}
