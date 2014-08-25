<spring:htmlEscape defaultHtmlEscape="true" />
<ul id="menu">
	<li class="first"><a
		href="${pageContext.request.contextPath}/admin"><spring:message
				code="admin.title.short" /></a></li>

	<li
		<c:if test='<%= request.getRequestURI().contains("/manage") %>'>class="active"</c:if>>
		<a
		href="${pageContext.request.contextPath}/module/icchange/pharmacy/manage.form"><spring:message
				code="icchange.pharmacy.manage" /></a>
	</li>
	
	<!-- Add further links here -->

	<openmrs:message var="pageTitle" code="Pharmacy Module" scope="page" />

</ul>
<h2>
	<spring:message code="icchange.pharmacy.title" />
</h2>
