
package acme.features.administrator.claim;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.entities.claims.Claim;

@Service
public class AdministratorClaimListService extends AbstractGuiService<Administrator, Claim> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		Collection<Claim> objects;
		objects = this.repository.findAllPublishedClaims();
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Claim object) {
		String published;
		Dataset dataset;

		dataset = super.unbindObject(object, "type", "indicator");
		published = !object.isDraftMode() ? "✓" : "x";
		dataset.put("published", published);
		dataset.put("leg", object.getLeg().getFlightNumberNumber());
		dataset.put("assistanceAgent", object.getAssistanceAgent().getEmployeeCode());
	}

}
