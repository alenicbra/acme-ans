<%--
- menu.jsp
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
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:menu-bar>
	<acme:menu-left>
		<acme:menu-option code="master.menu.anonymous" access="isAnonymous()">
			<acme:menu-suboption code="master.menu.alejandro.favourite-link" action="http://www.apple.com/"/>
			<acme:menu-suboption code="master.menu.pablo.favourite-link" action="http://www.ayesa.com/"/>
			<acme:menu-suboption code="master.menu.josemanuel.favourite-link" action="http://www.youtube.com/"/>
			<acme:menu-suboption code="master.menu.ramon.favourite-link" action="http://www.adidas.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.administrator" access="hasRealm('Administrator')">
			<acme:menu-suboption code="master.menu.administrator.list-user-accounts" action="/administrator/user-account/list"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-initial" action="/administrator/system/populate-initial"/>
			<acme:menu-suboption code="master.menu.administrator.populate-db-sample" action="/administrator/system/populate-sample"/>			
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.shut-system-down" action="/administrator/system/shut-down"/>
			<acme:menu-separator/>
			<acme:menu-suboption code="master.menu.administrator.list.claim" action="/administrator/claim/list"/>
			<acme:menu-suboption code="master.menu.administrator.list.airport" action="/administrator/airport/list"/>
			<acme:menu-suboption code="master.menu.administrator.list.airline" action="/administrator/airline/list"/>
			<acme:menu-suboption code="master.menu.administrator.list.aircraft" action="/administrator/aircraft/list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.assistance-agent" access="hasRealm('AssistanceAgent')">
			<acme:menu-suboption code="master.menu.assistance-agent.claim-completed" action="/assistance-agent/claim/list-completed"/>
			<acme:menu-suboption code="master.menu.assistance-agent.claim-undergoing" action="/assistance-agent/claim/list-undergoing"/>
			<acme:menu-suboption code="master.menu.assistance-agent.dashboard" action="/assistance-agent/assistance-agent-form/show"/>
	  	</acme:menu-option>

		<acme:menu-option code="master.menu.provider" access="hasRealm('Provider')">
			<acme:menu-suboption code="master.menu.provider.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>

		<acme:menu-option code="master.menu.consumer" access="hasRealm('Consumer')">
			<acme:menu-suboption code="master.menu.consumer.favourite-link" action="http://www.example.com/"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.airline-manager" access="hasRealm('AirlineManager')">
			<acme:menu-suboption code="master.menu.airline-manager.my-flights" action="/airline-manager/flight/list"/>
    </acme:menu-option>
      
		<acme:menu-option code="master.menu.member" access="hasRealm('Member')">
			<acme:menu-suboption code="master.menu.member.flight-assignment.plannedList" action="/member/flight-assignment/planned-list"/>
			<acme:menu-suboption code="master.menu.member.flight-assignment.completedList" action="/member/flight-assignment/completed-list"/>
		</acme:menu-option>
		
		<acme:menu-option code="master.menu.customer" access="hasRealm('Customer')">
			<acme:menu-suboption code="master.menu.customer.booking.list" action="/customer/booking/list"/>
			<acme:menu-suboption code="master.menu.customer.passenger.list" action="/customer/passenger/list"/>
		</acme:menu-option>
		
	</acme:menu-left>

	<acme:menu-right>		
		<acme:menu-option code="master.menu.user-account" access="isAuthenticated()">
			<acme:menu-suboption code="master.menu.user-account.general-profile" action="/authenticated/user-account/update"/>
			<acme:menu-suboption code="master.menu.user-account.become-assistance-agent" action="/authenticated/assistance-agent/create" access="!hasRealm('AssistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.assistance-agent-profile" action="/authenticated/assistance-agent/update" access="hasRealm('AssistanceAgent')"/>
			<acme:menu-suboption code="master.menu.user-account.become-provider" action="/authenticated/provider/create" access="!hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.provider-profile" action="/authenticated/provider/update" access="hasRealm('Provider')"/>
			<acme:menu-suboption code="master.menu.user-account.become-consumer" action="/authenticated/consumer/create" access="!hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.consumer-profile" action="/authenticated/consumer/update" access="hasRealm('Consumer')"/>
			<acme:menu-suboption code="master.menu.user-account.become-member" action="/authenticated/member/create" access="!hasRealm('Member')"/>
			<acme:menu-suboption code="master.menu.user-account.member-profile" action="/authenticated/member/update" access="hasRealm('Member')"/>
		</acme:menu-option>
	</acme:menu-right>
</acme:menu-bar>

