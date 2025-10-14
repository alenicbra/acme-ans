<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="acme" uri="http://acme-framework.org/" %>


<div class="container" style="margin-top: 2rem;">
    <!-- Título de la sección -->
    <div class="text-center mb-4">
        <h2>
            <fmt:message key="client.dashboard.form.title.general-indicators"/>
        </h2>
    </div>

    <!-- Tabla de indicadores generales -->
    <table class="table table-sm table-bordered">
        <!-- Indicadores de destinos y gasto -->
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.last-five-destinations-last-year"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${lastFiveDestinations != null}">
                        <acme:print value="${lastFiveDestinations}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.spent-money-last-year"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${spentMoneyLastYear != null}">
                        <acme:print value="${spentMoneyLastYear}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        
        <!-- Indicadores de reservas -->
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.economy-bookings"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${economyBookings != null}">
                        <acme:print value="${economyBookings}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.business-bookings"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${businessBookings != null}">
                        <acme:print value="${businessBookings}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        
        <!-- Indicadores de costes de reservas -->
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-count-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingCountCost != null}">
                        <acme:print value="${bookingCountCost}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-average-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingAverageCost != null}">
                        <acme:print value="${bookingAverageCost}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-minimum-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingMinimumCost != null}">
                        <acme:print value="${bookingMinimumCost}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-maximum-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingMaximumCost != null}">
                        <acme:print value="${bookingMaximumCost}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-deviation-cost"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingDeviationCost != null}">
                        <acme:print value="${bookingDeviationCost}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        
        <!-- Indicadores de pasajeros -->
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-count-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingCountPassengers != null}">
                        <acme:print value="${bookingCountPassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-average-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingAveragePassengers != null}">
                        <acme:print value="${bookingAveragePassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-minimum-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingMinimumPassengers != null}">
                        <acme:print value="${bookingMinimumPassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-maximum-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingMaximumPassengers != null}">
                        <acme:print value="${bookingMaximumPassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
        <tr>
            <th scope="row">
                <fmt:message key="client.dashboard.form.label.booking-deviation-passengers"/>
            </th>
            <td>
                <jstl:choose>
                    <jstl:when test="${bookingDeviationPassengers != null}">
                        <acme:print value="${bookingDeviationPassengers}"/>
                    </jstl:when>
                    <jstl:otherwise>
                        <acme:print value="N/A"/>
                    </jstl:otherwise>
                </jstl:choose>
            </td>
        </tr>
    </table>
    
    <!-- Gráfico de Reservas (Distribución: Económicas y de Negocios) -->
    <jstl:choose>
        <jstl:when test="${economyBookings != 0 || businessBookings != 0}">
            <h3>Bookings Distribution</h3>
<div class="dashboard-chart" style="height: 500px;">
    <canvas id="chartBookings"></canvas>
</div>
<script type="text/javascript">
    document.addEventListener("DOMContentLoaded", function() {
        var data = {
            labels: ["Economy", "Business"], // Cambiadas las etiquetas
            datasets: [{
                data: [
                    <jstl:out value="${economyBookings}"/>,
                    <jstl:out value="${businessBookings}"/>
                ],
                backgroundColor: [
                    'rgba(174, 214, 241, 0.7)',
                    'rgba(245, 203, 167, 0.7)'
                ],
                borderColor: [
                    'rgba(174, 214, 241, 1)',
                    'rgba(245, 203, 167, 1)'
                ],
                borderWidth: 1
            }]
        };
        var ctx = document.getElementById("chartBookings").getContext("2d");
        new Chart(ctx, {
            type: "doughnut",
            data: data,
            options: {
                responsive: true,
                maintainAspectRatio: false, // Permite que el alto del contenedor determine el tamaño
                title: {
                    display: true,
                    text: "Bookings Distribution"
                }
            }
        });
    });
</script>
        </jstl:when>
        <jstl:otherwise>
            <h3>
                <fmt:message key="client.dashboard.form.label.bookings.distribution"/>
            </h3>
            <acme:print value="N/A"/>
        </jstl:otherwise>
    </jstl:choose>
        
    
</div>