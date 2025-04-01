<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="authenticated.member.form.label.employeeCode" path="employeeCode"/>
	<acme:input-textbox code="authenticated.member.form.label.phoneNumber" path="phoneNumber"/>
	<acme:input-textbox code="authenticated.member.form.label.languageSkills" path="languageSkills"/>
	<acme:input-textbox code="authenticated.member.form.label.availabilityStatus" path="availabilityStatus"/>
	<acme:input-textbox code="authenticated.member.form.label.salary" path="salary"/>
	<acme:input-textbox code="authenticated.member.form.label.yearsOfExperience" path="yearsOfExperience"/>
	
	<jstl:if test="${_command == 'create'}">
		<acme:submit code="authenticated.member.form.button.create" action="/authenticated/member/create"/>
	</jstl:if>
	
	<jstl:if test="${_command == 'update'}">
		<acme:submit code="authenticated.member.form.button.update" action="/authenticated/member/update"/>
	</jstl:if>
</acme:form>
