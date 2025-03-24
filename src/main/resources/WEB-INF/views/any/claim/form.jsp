<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="any.claim.form.label.description" path="description"/>
	<acme:input-email code="any.claim.form.label.email" path="email"/>
	<acme:input-moment code="any.claim.form.label.registrationMoment" path="registrationMoment"/>
	<acme:input-select code="any.claim.form.label.type" path="type" choices="${types}"/>
	<acme:input-select code="any.claim.form.label.indicator" path="indicator" choices="${indicators}"/>
	<acme:input-select code="any.claim.form.label.flight" path="flight" choices="${flights}"/>
	<acme:input-select code="any.claim.form.label.assistanceAgent" path="assistanceAgent" choices="${assistanceAgents}"/>	

	<jstl:choose>
		<jstl:when test="${_command == 'show'}">
			<acme:button code="any.claim.form.button.tracking-logs" action="/any/tracking-log/list?masterId=${id}"/>
		</jstl:when>		
	</jstl:choose>	
</acme:form>