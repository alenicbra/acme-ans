<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "manager.leg.list.label.id" path= "id"/>
	<acme:list-column code = "manager.leg.list.label.departureAirport" path= "flightNumberNumber"/>
	<acme:list-column code = "manager.leg.list.label.arrivalAirport" path= "scheduledDeparture"/>
</acme:list>

<jstl:if test="${_command == 'list' && flightDraftMode==true}">
	<acme:button code="manager.leg.list.button.create" action="/airline-manager/leg/create?flightId=${flightId}"/>
</jstl:if>	