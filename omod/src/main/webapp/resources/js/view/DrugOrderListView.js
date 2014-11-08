/*
img.displayed {
    display: block;
    margin-left: auto;
    margin-right: auto 
}
*/

var pharmacy_html_constants = {
	img_full:"<img width=\"30px\" class=\"drugOrderStatus\" style='display: block;margin-left: auto;margin-right: auto;' src=\"/openmrs/moduleResources/icchange/pharmacy/images/green.png\" />",
	img_half:"<img width=\"30px\" class=\"drugOrderStatus\" style='display: block;margin-left: auto;margin-right: auto;' src=\"/openmrs/moduleResources/icchange/pharmacy/images/yellow.png\" />",
	img_none:"<img width=\"30px\" class=\"drugOrderStatus\" style='display: block;margin-left: auto;margin-right: auto;' src=\"/openmrs/moduleResources/icchange/pharmacy/images/none.png\" />",
	img_unknown:"<img width=\"30px\" class=\"drugOrderStatus\" style='display: block;margin-left: auto;margin-right: auto;' src=\"/openmrs/moduleResources/icchange/pharmacy/images/unknown.png\" />",
	
	order_set:"sets",
	order_date: "date",
	order_active: "active",
	
};

function createDrugOrderView (order, mode) {

	
	var self = { maxName:50, order:order, mode:mode, changeExpandResume:function() {console.log("will fetch order " + self.order.id)} }
	var img;
	
	switch (self.order.dispenseStatus) {
	case "full":
		img = pharmacy_html_constants.img_full;
		break;
	case "partial":
		img = pharmacy_html_constants.img_half;
		break;
	case "none":
		img = pharmacy_html_constants.img_none;
		break;
	default:
		img = pharmacy_html_constants.img_unknown;
	}
	
	var ret = 
		'<table id="%id" class="drugOrderBox" width="100%" >'.replace(/%id/g, "orderTable" + self.order.id) + 
			'<tr id="%id" class="drugOrderResumed%extra" >'.replace(/%extra/g, "").replace(/%id/g, "orderTr" + self.order.id) + 
				'<td class="drugOrderTitle, drugOrderDrugName">' +
					(order.drugName.length >  self.maxName ? self.order.drugName.substring(0, self.maxName - 3) + "..." : self.order.drugName) +  
				'</td>' +
				'<td class="drugOrderTitle, drugOrderPrescriber">' +
					"Prescribed by" + self.order.ordererName + " in " + self.order.creationDate + 
				'</td>' +	
				'<td class="drugOrderTitle, drugOrderStatus">' +
					order.status + img +
				'</td>' +	
			'</tr>' +
			'<tr class="drugOrderExtended%extra" style="display:none">'.replace(/%extra/g, "") +
			'</tr>' +
		'</table>';


	$j("table.regimenCurrentTable").append(ret);
	$j("#orderTr" + self.order.id).click(self.changeExpandResume); 
}



/*

majorSetHeader
majorNoSetHeader
majorPrescriptionStausHeader
 
 * */


var viewMode = {
	major:"",
	minor:"",
	sets:[],
	prescriptionsStatus:[],
	dispenseStatus:[]
};

function createHeaders(viewMode) {
	
	var configs = [];
	var ret = [];
	
	switch (viewMode.major) {
	case "set":
		viewMode.sets.forEach(function(set){
			configs.push({
				c:"majorSetHeader",
				t:set,
			})
		});
		configs.push({
			c:"majorNoSetHeader",
			t:"Others",
		});
		break;
	case "precription":
		viewMode.prescriptionsStatus.forEach(function(status){
			configs.push({
				c:"majorPrescriptionStatusHeader",
				t:status,
			})
		});
		configs.push({
			c:"majorPrescriptionNoStatusHeader",
			t:"Others",
		});
		break;
	case "dispense":
		viewMode.dispenseStatus.forEach(function(status){
			configs.push({
				c:"majorDispenseStatusHeader",
				t:status,
			})
		});
		configs.push({
			c:"majorDispenseNoStatusHeader",
			t:"Others",
		});
		break;
	default:
		configs.push({
			c:"noMajorHeader",
			t:"",
		});
	}

	configs.forEach(function(config){
		ret.push({
			html:"<thead class=\"%c\"><tr class=\"%c\"><th class=\"%c\"> %t </th></tr></thead>".replace(/%c/g, config.c).replace(/%t/g, config.t),
			body_html:"<tbody>"+"</tbody>"
		}); 
	});
	
	return ret;
}




function DrugOrderViewHeader (order, mode) {
	
	var self = this;

	this.maxName = 50;
	this.order = order;
	this.mode = mode;

	this.createView  = function () {
			
		//should not check for null, but rather order status, whether filled, not filled, or ongoing
		if (self.order == null) {
	
			
		} else {
			var img = pharmacy_html_constants.img_unknown;
		} 
		
		
		var ret = 
			'<table id="%id" class="drugOrderBox" width="100%" >'.replace(/%id/g, "orderTable" + order.id) + 
				'<tr class="drugOrderResumed%extra">'.replace(/%extra/g, "") + 
					'<td class="drugOrderTitle, drugOrderDrugName">' +
						(order.drugName.length >  self.maxName ? order.drugName.substring(0, self.maxName - 3) + "..." : order.drugName) +  
					'</td>' +
					'<td class="drugOrderTitle, drugOrderPrescriber">' +
						"Prescribed by" + order.ordererName + " in " + order.creationDate + 
					'</td>' +	
					'<td class="drugOrderTitle, drugOrderStatus">' +
					'</td>' +	
				'</tr>' +
				'<tr class="drugOrderExtended%extra" style="display:none">'.replace(/%extra/g, "") +
				'</tr>' +
			'</table>';

		
		
		
	}	
}



function DrugOrderView (order, parent) {
	
	var self = this;
	
	this.order = order;
	this.parent = parent

	this.createView  = function (viewMode) {
		
	}
	
}


function DrugOrderListView (patientId, parentDiv) {
	
	var self = this;
	
	this.patientId = patientId;
	this.headers = [];
	this.orders = {}
	this.parent = $j(parentDiv);
	this.viewMode = {major:"active",minor:"sets",order:"date"};

	this.fetchHeaders = function () {
		DWRPharmacyOrderService.getDrugOrdersHeaders(self.patientId, function(data){
			self.headers = data;
			self.render();
		});
	};
	
	this.render = function () {
		
		if (self.headers == null)
			return;

		
		var table = $j("#drugOrdersListTable");
		
		table.children().remove();			
		self.orders = {}
		self.orderHeaders();
		
		self.headers.forEach(function(order) {
			
			table.append(
					
					"<tr><td>" + 
					order.drugName + " " + order.ordererName + " " + order.creationDate + " " +
					
					
					"<img width=\"30px\" class=\"drugOrderStatus\" src=\"/openmrs/moduleResources/icchange/pharmacy/images/green.png\" />"
					+ "</td></tr>");
			
			
			
		});

	};
		

	this.MajorFilter = function (order) {
		switch (self.viewMode.major) {
		case "active":
			if (order.active)
				return true;
			return false;
		case "date":
			break;
		case "sets":
			break;
		default :
			return true;
		}
	}

	
	this.orderHeaders = function () {
		
		switch (self.viewMode.major) {
		case "active":
			break;
		case "date":
			break;
		case "sets":
			break;
		default :
			break;
		}
	}
	
	this.changeView = function (viewMode) {
	
	};
	
}