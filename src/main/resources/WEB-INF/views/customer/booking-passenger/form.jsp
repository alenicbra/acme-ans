<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<jstl:choose>
		<jstl:when test="${_command == 'create'}">
			<acme:input-select code="customer.booking-passenger.form.label.passenger" path="passenger" choices="${passengers}"/>
			<acme:submit code="customer.booking-passenger.form.button.create" action="/customer/booking-passenger/create?bookingId=${booking.id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'show'}">
			<acme:input-textbox code="customer.booking-passenger.form.label.passenger" path="passengerName" readonly="true"/>
			<jstl:if test="${booking.draftMode == true}">
				<acme:submit code="customer.booking-passenger.form.button.delete" action="/customer/booking-passenger/delete"/>
			</jstl:if>
		</jstl:when>
	</jstl:choose>
</acme:form>