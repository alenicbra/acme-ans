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
	<acme:input-moment code="administrator.trackingLog.form.label.lastUpdateMoment" path="lastUpdateMoment" readonly="true"/>
	<acme:input-textbox code="administrator.trackingLog.form.label.step" path="step" readonly="true"/>
	<acme:input-double code="administrator.trackingLog.form.label.resolutionPercentage" path="resolutionPercentage" readonly="true"/>
	<acme:input-textbox code="administrator.trackingLog.form.label.resolutionReason" path="resolutionReason" readonly="true"/>
	<acme:input-select code="administrator.trackingLog.form.label.indicator" path="indicator" choices="${indicators}"/>
</acme:form>