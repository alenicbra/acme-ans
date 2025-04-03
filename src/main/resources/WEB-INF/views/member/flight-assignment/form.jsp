<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="member.flight-assignment.form.label.duty" path="duty" choices="${dutyChoice}"/>
	<acme:input-moment code="member.flight-assignment.form.label.lastUpdatedMoment" path="lastUpdatedMoment" readonly="true"/>	
	<acme:input-select code="member.flight-assignment.form.label.currentStatus" path="currentStatus" choices="${currentStatusChoice}"/>
	<acme:input-textbox code="member.flight-assignment.form.label.remarks" path="remarks"/>
	<acme:input-checkbox code="member.flight-assignment.form.label.draftMode" path="draftMode"/>
	<acme:input-select code="member.flight-assignment.form.label.member" path="member" choices="${memberChoice}"/>
	<acme:input-select code="member.flight-assignment.form.label.leg" path="leg" choices="${legChoice}"/>

	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:submit code="member.flight-assignment.form.button.return" action="/member/flight-assignment/list"/>
		</jstl:when>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="member.flight-assignment.form.button.update" action="/member/flight-assignment/update"/>
			<acme:submit code="member.flight-assignment.form.button.delete" action="/member/flight-assignment/delete"/>
			<acme:submit code="member.flight-assignment.form.button.activity-log" action="/member/activity-log/list?masterId=${id}"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="member.flight-assignment.form.button.create" action="/member/flight-assignment/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
