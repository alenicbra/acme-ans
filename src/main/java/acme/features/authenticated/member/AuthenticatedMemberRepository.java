
package acme.features.authenticated.member;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.Member;

@Repository
public interface AuthenticatedMemberRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findUserAccountById(int id);

	@Query("select m from Member m where m.userAccount.id = :id")
	Member findMemberByUserAccountId(int id);

}
