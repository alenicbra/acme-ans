<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="member.flight-assignment.list.label.duty" path="duty" width="20%"/>
	<acme:list-column code="member.flight-assignment.list.label.lastUpdatedMoment" path="lastUpdatedMoment" width="20%"/>
	<acme:list-column code="member.flight-assignment.list.label.currentStatus" path="currentStatus" width="20%"/>
	<acme:list-payload path="payload"/>
</acme:list>

<jstl:if test="${_command == 'list-planned'}">
	<acme:button code="member.flight-assignment.list.button.create" action="/member/flight-assignment/create"/>
</jstl:if>