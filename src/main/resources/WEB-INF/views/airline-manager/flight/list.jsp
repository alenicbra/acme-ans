<%@page %>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code = "manager.flight.list.label.id" path= "id"/>
	<acme:list-column code = "manager.flight.list.label.tag" path= "tag"/>
</acme:list>

<jstl:if test="${_command == 'list'}">
	<acme:button code="manager.flight.list.button.create" action="/airline-manager/flight/create"/>
</jstl:if>