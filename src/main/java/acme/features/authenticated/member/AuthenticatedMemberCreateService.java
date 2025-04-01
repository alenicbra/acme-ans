
package acme.features.authenticated.member;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.Member;

@GuiService
public class AuthenticatedMemberCreateService extends AbstractGuiService<Authenticated, Member> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedMemberRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Member.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Member object;
		int userAccountId;
		UserAccount userAccount;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findUserAccountById(userAccountId);

		object = new Member();
		object.setUserAccount(userAccount);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Member object) {
		assert object != null;

		super.bindObject(object, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience", "airline");
	}

	@Override
	public void validate(final Member object) {
		assert object != null;
	}

	@Override
	public void perform(final Member object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Member object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "employeeCode", "phoneNumber", "languageSkills", "availabilityStatus", "salary", "yearsOfExperience");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
