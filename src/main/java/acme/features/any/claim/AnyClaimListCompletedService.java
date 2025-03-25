
package acme.features.any.claim;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.IndicatorType;

@GuiService
public class AnyClaimListCompletedService extends AbstractGuiService<Any, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Collection<Claim> claims;

		masterId = super.getRequest().getData("masterId", int.class);
		claims = this.repository.findManyClaimsByMasterId(masterId);
		status = !claims.isEmpty() && claims.stream().allMatch(c -> !c.getIndicator().equals(IndicatorType.IN_PROGRESS));

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<Claim> objects;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		objects = this.repository.findManyClaimsByMasterId(masterId).stream().filter(c -> !c.getIndicator().equals(IndicatorType.IN_PROGRESS)).toList();

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		String published;
		Dataset dataset;

		dataset = super.unbindObject(object, "employeeCode", "leg", "moment");
		published = !object.isDraftMode() ? "✓" : "x";
		dataset.put("published", published);

		super.getResponse().addData(dataset);
	}
}
