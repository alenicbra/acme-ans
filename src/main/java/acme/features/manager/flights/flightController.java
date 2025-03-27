
package acme.features.manager.flights;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flights.Flight;
import acme.realms.AirlineManager;

@GuiController
public class flightController extends AbstractGuiController<AirlineManager, Flight> {

	@Autowired
	private flightCreateService		createService;

	@Autowired
	private flightDeleteService		deleteService;

	@Autowired
	private flightListService		listService;

	@Autowired
	private flightPublishService	publishService;

	@Autowired
	private flightShowService		showService;

	@Autowired
	private flightUpdateService		updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("show", this.showService);
		super.addCustomCommand("list-mine", "list", this.listService);
		super.addCustomCommand("publish", "update", this.publishService);
	}
}
