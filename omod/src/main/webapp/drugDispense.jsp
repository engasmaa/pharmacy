<%@ include file="/WEB-INF/template/include.jsp"%>

<openmrs:require privilege="View Patients" otherwise="/login.htm" redirect="/findPatient.htm" />
<c:set var="OPENMRS_VIEWING_PATIENT_ID" scope="request" value="${patient.patientId}" />
<openmrs:globalProperty var="enablePatientName" key="dashboard.showPatientName" defaultValue="false" />

<%@ include file="/WEB-INF/template/header.jsp"%>
<%@ include file="template/localMenuHeader.jsp"%>

<h2>
	<openmrs:message code="icchange.pharmacy.dispense" />
</h2>

<br />

<openmrs:htmlInclude file="/scripts/easyAjax.js" />
<openmrs:htmlInclude file="/scripts/jquery/dataTables/css/dataTables.css" />
<openmrs:htmlInclude file="/scripts/jquery/dataTables/js/jquery.dataTables.min.js" />
<openmrs:htmlInclude file="/scripts/jquery-ui/js/jquery-ui-1.7.2.custom.min.js" />
<link href="<openmrs:contextPath/>/scripts/jquery-ui/css/<spring:theme code='jqueryui.theme.name' />/jquery-ui.custom.css"
	type="text/css" rel="stylesheet" />
<openmrs:htmlInclude file="/scripts/flot/jquery.flot.js" />
<openmrs:htmlInclude file="/scripts/flot/jquery.flot.multiple.threshold.js" />
<%-- /end file imports for portlets --%>

<c:if test="${patient.voided}">
	<div id="patientDashboardVoided" class="retiredMessage">
		<div>
			<openmrs:message code="Patient.voidedMessage" />
		</div>
	</div>
</c:if>

<c:if test="${patient.dead}">
	<div id="patientDashboardDeceased" class="retiredMessage">
		<div>
			<openmrs:message code="Patient.patientDeceased" />
			<c:if test="${not empty patient.deathDate}">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<openmrs:message code="Person.deathDate" />: <openmrs:formatDate
					date="${patient.deathDate}" />
			</c:if>
			<c:if test="${not empty patient.causeOfDeath}">
				&nbsp;&nbsp;&nbsp;&nbsp;
				<openmrs:message code="Person.causeOfDeath" />: <openmrs:format
					concept="${patient.causeOfDeath}" />
				<c:if test="${not empty causeOfDeathOther}"> 
					  &nbsp;:&nbsp;<c:out value="${causeOfDeathOther}"></c:out>
				</c:if>
			</c:if>
		</div>
	</div>
</c:if>

<c:if test="${!patient.dead && !patient.voided}">
	<openmrs:portlet url="patientHeader" id="pharmacyPatientdHeader" patientId="${patient.patientId}" />
	
	<openmrs:htmlInclude file="/dwr/engine.js" />
	<openmrs:htmlInclude file="/dwr/util.js" />
	<openmrs:htmlInclude file="/dwr/interface/DWRPharmacyOrderService.js" />
	
	<openmrs:htmlInclude file="/moduleResources/icchange/pharmacy/js/view/DrugOrderListView.js" />
	<openmrs:htmlInclude file="/moduleResources/icchange/pharmacy/js/view/DrugOrderView.js" />
	<openmrs:htmlInclude file="/moduleResources/icchange/pharmacy/js/PharmacyOrder.js" />
	
	<div id="drugOrdersSection" class="drugOrders">
	
	
	</div>
	
	
	<div id="regimenPortletCurrent">
		<div id="uhTheyCannotSeeme" style="display: none;">
			<c:forEach var="drugOrder" items="${druglist}">
				<div id="hiddenPOrder${drugOrder.orderId}" style="margin-left: 50px;">
					<c:if test="${not empty pharmacyordermap[drugOrder.orderId]}">
					<table>
						<thead>
							<tr>
								<th>id</th>
								<th>order id</th>
								<th>patient id</th>
							</tr>
						</thead>
	
						<tbody>
							<c:forEach var="pOrder" items="${pharmacyordermap[drugOrder.orderId]}">
								<tr>
									<td>${pOrder.id}</td>
									<td>${pOrder.drugOrder.orderId}</td>
									<td>${pOrder.patient}</td>
									<td><input id="porder_${pOrder.id}" type="button" value="Edit" /></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					</c:if>
					<c:if test="${empty pharmacyordermap[drugOrder.orderId]}">
						<p>There is no patient-drug history.</p>
					</c:if>
				</div>
			</c:forEach>
		</div>
		<table class="regimenCurrentTable">
			<thead>
				<tr class="regimenCurrentHeaderRow">
					<th style="nowrap: true; width: 500px" class="regimenCurrentDrugOrderedHeader"><openmrs:message code="Order.item.ordered" /></th>
					<th class="regimenCurrentDrugFrequencyHeader"><openmrs:message code="DrugOrder.frequency" /></th>
					<th class="regimenCurrentDrugDateStartHeader"><openmrs:message code="general.dateStart" /></th>
					<th class="regimenCurrentDrugInstructionsHeader">Dose</th>
				</tr>
			</thead>
			<c:forEach var="drugOrder" items="${druglist}">
				<tbody>
					<tr class="regimenCurrentDrugRow">
						<td class="regimenCurrentDrugEmptyData">${drugOrder.drug.name}</td>
						<td class="regimenCurrentDrugFrequencyData">${drugOrder.frequency}</td>
						<td class="regimenCurrentDrugStartDateData"><openmrs:formatDate date="${drugOrder.startDate}" type="medium" /></td>
						<td class="regimenCurrentDrugDoseData">${drugOrder.dose} ${drugOrder.units}</td>
						<td class="regimenCurrentDrugVoidData"><input id="show_${drugOrder.orderId}" type="button" value="more"
							onClick="handleClickOnMe(${drugOrder.orderId})"/></td>
						<td class="regimenCurrentDrugVoidData"><input id="dorder_${drugOrder.orderId}" type="button" value="dispense" />
						</td>
					</tr>
					<tr>
						<td colspan="6" id="pordertd${drugOrder.orderId}"></td>
					</tr>
				</tbody>
			</c:forEach>
		</table>
	</div>
</c:if>

<div id="drugOrdersListDiv">
	<div id="drugOrdersListDivTop">
		Major Group
		<select>
			<option value="pstatus">Prescription Status</option>
  			<option value="dstatus">Dispensing Status</option>
			<option value="none">none</option>
		</select>
		<div id="drugOrdersListSortOptions">
			<input type="checkbox" id="drugOrdersListSplitBySet" name="splitOptions" value="bySet">Split by drug Set<br>
			<input type="checkbox" id="drugOrdersListSplitByFilled" name="splitOptions" value="Car">Split by filled in
		</div>
		<input id="druoOrderSortButton" type="button" value="sort" />
	</div>
	<table id="drugOrdersListTable">
	</table>
</div>



<script> 
	<!-- // begin
	
	var drugOrdersHeaders = [<c:forEach var="d" items="${drugOrderHeaders}">${d},</c:forEach>];
	
	var drugordersListView;
	
	$j(document).ready(function () {
		//drugordersListView = new DrugOrderListView(${patient.id}, "drugOrdersList");
		
	});
	
	
	var porderCellFuncs = [
		function (data) {return "1";},
		function (data) {return "2";}
	];
	
	var pharmacyorders; 
	var drugorders;
	
    var pharmacyorders = {  
            <c:forEach items="${pharmacyordermap}" var="item" varStatus="loop">  
              ${item.key}: '${item.value}' ${not loop.last ? ',' : ''}  
            </c:forEach>  
          }; 
	
    function handleMore2 (orderId) {
    	<c:forEach var="drugOrder" items="${druglist}">
			hideDiv("hiddenPOrder${drugOrder.orderId}");
		</c:forEach>
		showDiv("hiddenPOrder"+orderId);
    }
    
    function pleaseHideMeFromTheUser (orderId) {
		//var dest = document.getElementById("pordertd"+orderId);
		var dest = document.getElementById("uhTheyCannotSeeme");
		var orig = document.getElementById("hiddenPOrder"+orderId);
		//dest.removeChild(orig);
		dest.appendChild(orig);

		document.getElementById("show_"+orderId).value="more";
		document.getElementById("show_"+orderId).onClick="handleMore(orderId);";
    }
    
    
    function handleClickOnMe (orderId) {
    	var button = document.getElementById("show_"+orderId);
    	var dest = null;
    	var orig = null;
    	
    	if (button.value === "more") {
    		button.value = "less";
    		dest = document.getElementById("pordertd"+orderId);
    		orig = document.getElementById("hiddenPOrder"+orderId);    		
    	} else {
    		dest = document.getElementById("uhTheyCannotSeeme");
    		orig = document.getElementById("hiddenPOrder"+orderId);
    		button.value = "more";
    	}
		dest.appendChild(orig);
    }
    
    
	function handleMore (orderId) {
		//var pname = "hiddenPOrder";
		//var sname = "hiddenPOrderInfo";
		//var pelem = document.getElementById(pname);
		//var selem = document.getElementById(sname);
		//var tableName = "porderinfo";	
		//var telem = document.getElementById(tableName);
		
		//showHideDiv(pname);
		
		
	 	//while (telem.hasChildNodes())
	 	//	telem.removeChild(telem.firstChild);
		
	 	//dwr.util.removeAllRows(tableName);
	 			
		//dwr.util.addRows(tableName, ["", ""], porderCellFuncs, {
		//	cellCreator:function(options) {
		//	    var td = document.createElement("td");
		//	    return td;
		//	},
		//	escapeHtml:false
		//});
		
		//showDiv(pname);
		
		
		var dest = document.getElementById("pordertd"+orderId);
		var orig = document.getElementById("hiddenPOrder"+orderId);
		dest.appendChild(orig);
		
		document.getElementById("show_"+orderId).value="less";
		document.getElementById("show_"+orderId).onClick="pleaseHideMeFromTheUser(orderId);";
	}
	
	
		function handlePharmacyOrder(orderId) {
			DWRPharmacyOrderService.createPharmacyOrder(orderId, ${patient.patientId});
		}
		
		//<!-- add style="display: none;" to <div id="patientSubheader" class="box" > -->
		window.onload = function() {			
			//patientSubheader
			
			var elem = document.getElementById("patientVisitsSubheader");
			elem.style.display = "none";
		    //var telem = document.getElementById("patientHeaderObs");
		    //pharmacyorders = ${pharmacyordermap};
		    //drugorders = ${druglist};
		    var nelem = document.getElementById("patientHeaderObsRegimen");
			nelem.parentNode.removeChild(nelem);
		};
	
	// end --> 
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>