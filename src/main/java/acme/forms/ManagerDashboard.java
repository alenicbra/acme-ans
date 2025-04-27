
package acme.forms;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.client.components.datatypes.Money;
import acme.entities.airports.Airport;
import acme.entities.legs.LegStatus;

public class ManagerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Double						rankingExperience;
	Integer						yearsToRetire;
	Double						ratioOnTimeLegs;
	Double						ratioDelayedLegs;
	Airport						mostPopularAirport;
	Airport						lessPopularAirport;
	Map<LegStatus, Integer>		numberOfLegs;
	Money						averageCostFlights;
	Money						minimumCostFlights;
	Money						maximumCostFlights;
	Money						standardDeviationCostFlights;

}
