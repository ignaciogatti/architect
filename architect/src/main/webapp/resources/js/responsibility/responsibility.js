var Topic = 'topic://topic.responsibility';
var Queue = 'queue://queue.responsibility';

$(document).ready(function() {
	initNewResponsibilityDialog();
	initDeleteResponsibilityDialog();
	initEditResponsibilityDialog();
	initResponsibilityDetailsDialog();
	initEditScenariosDialog();
	initEditDependenciesDialog();
	
    $("button, input:submit").button();
	
	initConnections();
	tableProperties();
	$(".tableScenarios").tablesorter({widthFixed: true, headers: {5: {sorter: false }}});
});

function initConnections(){
	org.activemq.Amq.init({
		uri : 'amq',
		logging : true,
		timeout : 45,
		clientId : (new Date()).getTime().toString()
	});
	org.activemq.Synchro.init(USER,GROUP,ARCHITECTURE,"responibility","syncResponsibilities",false);
}

function tableProperties(){
	$(".tableScenarios tbody tr:odd").css("background", " #F6F6F6");
	$(".tableScenarios tbody tr").mouseover(function() {
		$(this).css("background", "#e5f5ff");
	});
	$(".tableScenarios tbody tr").mouseout(function() {
		$(".tableScenarios tbody tr:odd").css("background", "#F6F6F6");
		$(".tableScenarios tbody tr:even").css("background", "#FFFFFF");
	});
}

function initNewResponsibilityDialog() {
	$("#dialogNew").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'New Responsibility',
		buttons : {
			'Accept' : function() {
				insertReponsibility();
			}
		},
		close : function() {
			$("#dialogNew").html("");
		}
	});
	
	$("#newResponsibility").click(function() {
		$("#dialogNew").dialog('open');
	    $("#dialogNew").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
	    $.get("newResponsibility", function (data) {
	    	$("#dialogNew").html(data);
	    });
	});
}

function initDeleteResponsibilityDialog() {
	 $("#dialogDelete").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 300,
		modal : true,
		title : 'Delete Responsibility',
		buttons : {
			'Accept' : function() {
				deleteResponsibility();
			},
			'Cancel' : function() {
				$("#dialogDelete").dialog('close');
			}
		},
		close : function() {
		}
	});
}

function initEditResponsibilityDialog() {
	  $("#dialogEdit").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Edit Responsibility',
		buttons : {
			'Accept' : function() {
				editResponsibility();
			}
		},
		close : function() {
			$("#dialogEdit").html("");
		}
	});
}

function initResponsibilityDetailsDialog() {
	  $("#dialogDetail").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Responsibility Details',
		buttons : {
			'Accept' : function() {
				$("#dialogDetail").dialog('close');
			}
		},
		close : function() {
			$("#dialogDetail").html("");
		}
	});
}

function initEditScenariosDialog(){
	$("#dialogEditScenarios").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Edit Scenarios',
		buttons : {
			'Accept' : function() {
				editScenarios();
			}
		},
		close : function() {
			$("#dialogEditScenarios").html("");
		}
	});
	$("#multiselectorScenarios").multiselect({searchable: false, sortable: false});
}

function initEditDependenciesDialog() {
	$("#dialogEditDependencies").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Edit Dependencies',
		buttons : {
			'Accept' : function() {
				editDependencies();
			}
		},
		close : function() {
			$("#dialogEditDependencies").html("");
		}
	});
	$("#multiselectorDependencies").multiselect({searchable: false, sortable: false});
}

function insertReponsibility() {
	var name = $("#name").val();
	if (name == "")
		$(".errorName").css("display", "block");
	var module = $("#moduleForNewResp").val();
	if (module == "")
		$(".errorModuleSelector").css("display", "block");
	else {
		$.ajax({
			type : "POST",
			url : "insertResponsibility",
			data : {
				name : name,
				description : $("#description").val(),
				module : module,
				executionTime : $("#executionTimeForNewResp").val(),
				complexity : $("#complexityForNewResp").val()
			},
			success: function(xml) {
				var change = $(xml).find("elementChange")[0];
				if (change!=undefined && change!=null)
					processLocalChange(change);
				if(!showErrorMessages(xml))
					$("#dialogNew").dialog('close');
			},
			dataType: "xml"
		});
	}
}

function deleteResponsibility() {
	$.ajax({
		type : "GET",
		url : "deleteResponsibility",
		data : {
			id : $("#idDelete").val()
		},
		success: function(xml) {
			var change = $(xml).find("elementChange")[0];
			if (change!=undefined && change!=null)
				processLocalChange(change);
			if(!showErrorMessages(xml))
				$("#dialogDelete").dialog('close');
		},
		dataType: "xml"
	});
}

function editResponsibility(){
	var name = $("#name").val();
	if (name == "")
		$(".errorName").css("display", "block");
	else {
		$.ajax({
			type : "POST",
			url : "saveResponsibility",
			data : {
				id :  $("#id").val(),
				name : name,
				description : $("#description").val(),
				module : $("#module").val() == -1 ? "" : $("#module").val(),
				executionTime : $("#executionTime").val(),
				complexity : $("#complexity").val()
			},
			success: function(xml) {
				var change = $(xml).find("elementChange")[0];
				if (change!=undefined && change!=null)
					processLocalChange(change);
				if(!showErrorMessages(xml)) {
					$("#dialogEdit").html("");
					$("#dialogEdit").dialog('close');
				}
			},
			dataType: "xml"
		});
	}
}

function editScenarios() {
	var scenarios = $("#scenarios").val();
	if (scenarios != null && scenarios != "")
		scenarios = scenarios.join(",");
	else
		scenarios = "";
	$.ajax({
		type : "POST",
		url : "saveScenarios",
		data : {
			id : $("#responsibilityId").val(),
			scenarios : scenarios
		},
		success: function(xml) {
			if(!showErrorMessages(xml)) {
				$("#dialogEditScenarios").dialog('close');
				$("#dialogEditScenarios").html("");
				$.gritter.add({
					title : "Responsibility scenarios' changed.",
					text : "Changes Applied Successfully.",
					image : 'resources/images/gritter-ok.png',
					time : 2000
				});
			}
		}
	});
}

function displayResponsibilityDetailsDialog(id) {
	$("#dialogDetail").dialog('open');
    $("#dialogDetail").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("responsibilityDetails?id="+id, function (data) {
    	$("#dialogDetail").html(data);
    });
}

function displayEditResponsibilityDialog(id) {
	$("#dialogEdit").dialog('open');
    $("#dialogEdit").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("editResponsibilityDialog?id="+id, function (data) {
    	$("#dialogEdit").html(data);
    });
}

function displayDeleteResponsibilityDialog(id) {
	$("#dialogDelete").dialog('open');
    $("#idDelete").val(id);
}

function displayDependenciesDialog(id) {
	$("#dialogEditDependencies").dialog('open');
	$("#dialogEditDependencies").html('<img src="resources/images/loading.gif" style="margin-top : 50px" />');
    $.get("editDependenciesDialog?id="+id, function(data){
       $("#dialogEditDependencies").html(data);
   });
}

function displayScenariosDialog(id) {
	$("#dialogEditScenarios").dialog('open');
    $("#dialogEditScenarios").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("editScenariosDialog?id="+id, function(data){
        $("#dialogEditScenarios").html(data);
    });
}

// -------------------- Table Functions ---------------------------

function insertRow(changeNumber, changedElement) {
	var responsibilityId = changedElement.getAttribute("id");
	var responsibilityName = changedElement.getAttribute("name");
	var responsibilityComplexity = changedElement.getAttribute("complexity");
	var responsibilityExecutionTime = changedElement.getAttribute("executionTime");
	var responsibilityModule = changedElement.getElementsByTagName("module");
	var module = "";
	if (responsibilityModule!=undefined && responsibilityModule.length>0)
		module = responsibilityModule[0].getAttribute("name");
	try {
		$.get('jsp/responsibility/ResponsibilityTableItem.jsp', {
			id : responsibilityId,
			name : responsibilityName,
			module : module,
			complexity : responsibilityComplexity,
			executionTime : responsibilityExecutionTime
		}, function(data) {
			$(data).appendTo("#tableBody");
		});
		tableProperties();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}

function deleteRow(changeNumber,changedElement) {
	var responsibilityId = changedElement.getAttribute("id");
	try {
		$(document.getElementById("tr" + responsibilityId)).remove();
		tableProperties();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}

function editRow(changeNumber,changedElement) {
	var responsibilityId = changedElement.getAttribute("id");
	var responsibilityName = changedElement.getAttribute("name");
	var responsibilityComplexity = changedElement.getAttribute("complexity");
	var responsibilityExecutionTime = changedElement.getAttribute("executionTime");
	var responsibilityModule = changedElement.getElementsByTagName("module");
	var module = "";
	if (responsibilityModule!=undefined && responsibilityModule.length>0)
		module = responsibilityModule[0].getAttribute("name");
	try {
		$(".name" + responsibilityId).html(responsibilityName);
		if (responsibilityModule==undefined || responsibilityModule==null)
			responsibilityModule = "";
		$(".module" + responsibilityId).html(module);
		$(".complexity" + responsibilityId).html(responsibilityComplexity);
		$(".executionTime" + responsibilityId).html(responsibilityExecutionTime);
		$.gritter.add({
			title : "Responsibility Edited",
			text : "Changes Applied.",
			image : 'resources/images/gritter-ok.png',
			time : 2000
		});
		return changeNumber;
	} catch (x) {
		return -1;
	}
}