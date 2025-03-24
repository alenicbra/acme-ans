
package acme.features.any.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claims.Claim;
import acme.entities.claims.IndicatorType;

@GuiService
public class AnyClaimShowService extends AbstractGuiService<Any, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Claim> objects;
		objects = this.repository.findAllCompletedClaims(IndicatorType.IN_PROGRESS);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbindObject(object, "registrationMoment", "email", "description", "type", "indicator", "link", "draftMode", "flight", "assistanceAgent");

		super.getResponse().addData(dataset);
	}
}
