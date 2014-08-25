<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Patients" otherwise="/login.htm" redirect="/findPatient.htm" />

<%@ include file="/WEB-INF/template/header.jsp"%>

<%@ include file="template/localMenuHeader.jsp"%>

<h2><openmrs:message code="Patient.search"/></h2>	

<br />

<openmrs:portlet id="findPatient" url="findPatient" parameters="size=full|hideAddNewPatient=true|postURL=drugDispense.form|showIncludeVoided=false|viewType=shortEdit" />

<%@ include file="/WEB-INF/template/footer.jsp"%>