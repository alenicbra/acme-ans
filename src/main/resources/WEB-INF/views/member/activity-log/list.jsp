<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="member.activity-log.list.label.registrationMoment" path="registrationMoment" width="20%"/>
	<acme:list-column code="member.activity-log.list.label.typeOfIncident" path="typeOfIncident" width="20%"/>
	<acme:list-column code="member.activity-log.list.label.description" path="description" width="20%"/>
	<acme:list-column code="member.activity-log.list.label.severityLevel" path="severityLevel" width="20%"/>
	<acme:list-column code="member.activity-log.list.label.draftMode" path="draftMode" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="member.activity-log.list.button.create" action="/member/activity-log/create"/>
</jstl:if>