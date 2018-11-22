<%@page import="ua.com.ncu.portlet.ApiTableau"%>
<%@page import="com.liferay.expando.kernel.model.ExpandoTableConstants"%>
<%@page import="com.liferay.portal.kernel.model.User"%>
<%@page import="com.liferay.expando.kernel.service.ExpandoValueLocalServiceUtil"%>
<%@page import="com.liferay.expando.kernel.model.ExpandoValue"%>

<%@ include file="/init.jsp" %>
<%@ page pageEncoding="UTF-8" %>

 <%-- <script type='text/javascript' src="https://<%= tableauHostname %>/javascripts/api/tableau-2.js"></script> --%> 
 <%
	ApiTableau tableau = new ApiTableau(tableauHostname, 
			tableauSchema, 
			tableauUserName, 
			null, 
			null,
			Boolean.parseBoolean(tableauTrustSSC));
	
	String ticket = tableau.getTicket(tableauUserName, "");
	_log.debug("view ticket : " + ticket);
	
%>

  <c:choose>
	<c:when test="<%= ticket.equals("-1") %>">
		<p style="color:red; font-size: 120%;"><liferay-ui:message key="ticket.1"/> <%= ticket %> !</p>
	</c:when>
	<c:otherwise>
			
<div id="tableauViz<portlet:namespace/>" class="tableauViz" style="width: auto; height: auto;"></div>

<script type="text/javascript" >

var viz<portlet:namespace/>, workbook<portlet:namespace/>, activeSheet<portlet:namespace/>;

/* Liferay.on(
		'allPortletsReady',
		function() {
			console.log("Liferay.on -> ready id = <portlet:namespace/>");
			initializeViz<portlet:namespace/>();
		}
); */
	

function initializeViz<portlet:namespace/>() {
		var placeholderDiv<portlet:namespace/> = document.getElementById("tableauViz<portlet:namespace/>");
		var url<portlet:namespace/> = "<%= tableauHostname %>" + "/trusted/" + "<%= ticket %>" + "/views" + "<%= tableauVisualization %>";
		console.log("view url :: " + url<portlet:namespace/>);
		var options<portlet:namespace/> = {
		//width: "100%",
		//height: "100%",
<%     
		Map<String, String> m = (Map<String, String>) request.getAttribute("tableaFilters");
		for(Map.Entry<String, String> e : m.entrySet()){
			System.out.println(e.getKey() + " :: " + e.getValue());
%>
				"<%= e.getKey()%>" : [<%= e.getValue()%>],
<% 		} %>		
		 hideTabs: <%= tableauHideTabs%>,
		 hideToolbar: <%= tableauHideToolbar%>,
		 onFirstInteractive: function () {
				workbook<portlet:namespace/> = viz<portlet:namespace/>.getWorkbook();
				activeSheet<portlet:namespace/> = workbook<portlet:namespace/>.getActiveSheet();
				//window.setInterval("refreshThat()", ("<%= tableauTimeRefresh %>"*1000));
		 		}
			};
	viz<portlet:namespace/> = new tableau.Viz(placeholderDiv<portlet:namespace/>, url<portlet:namespace/>, options<portlet:namespace/>);
} 

</script>

<script>

window.tableau = window.tableau || {};

if (window.tableau._apiLoaded) { 
	console.log("Liferay.on :: 1");
	//Liferay.on(
	//			'allPortletsReady',
	//			function() {
					initializeViz<portlet:namespace/>();
		//		}
	//	);
} else {
	window.tableau._apiLoaded = true;
	var script = document.createElement("script");  // Creates a new <div> node

	script.onload = function () {
		initializeViz<portlet:namespace/>();
	};

	script.src = "<%= tableauHostname %>/javascripts/api/tableau-2.2.2.js";
	document.body.appendChild(script);
}

</script>

	</c:otherwise>
</c:choose> 