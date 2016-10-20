var Topic = 'topic://topic.scenario';
var Queue = 'queue://queue.scenario';

$(document).ready(function(){
	initNewScenarioDialog();
	initDeleteScenarioDialog();
	initDetailsScenarioDialog();
	initScenarioRespDialog();
	initEditScenarioDialog();
	
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
	org.activemq.Synchro.init(USER,GROUP,ARCHITECTURE,"scenario","syncScenarios",false);
}

function initNewScenarioDialog() {
    $("#dialogNew").dialog({
    	bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'New Scenario',
		buttons : {
			'Accept' : function() {
				insertScenario();
			}
		},
		close : function() {
			$("#dialogNew").html("");
		}
	});
    
	$("#newScenarioButton").click(function() {
		$("#dialogNew").dialog('open');
		$("#dialogNew").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
		$.get("newScenario", function(data) {
			$("#dialogNew").html(data);
		});
	});
}

function initScenarioRespDialog() {
	$("#dialogResp").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Scenario Responsibilities',
		buttons : {
			'Accept' : function() {
				$(this).dialog('close');
			}
		},
		close : function() {
			$("#dialogResp").html("");
		}
	});
}

function initDetailsScenarioDialog() {
	$("#dialogDetail").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Scenario Details',
		buttons : {
			'Accept' : function() {
				$(this).dialog('close');
			}
		},
		close : function() {
			$("#dialogDetail").html("");
		}
	});
}

function initDeleteScenarioDialog(){
    $("#dialogDelete").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 300,
		modal : true,
		title : 'Delete Scenario',
		buttons: {
			'Accept' : function() {
				deleteScenario();
			},
			'Cancel' : function() {
				$(this).dialog('close');
			}
		},
		close : function() {
		}
	});
}

function initEditScenarioDialog() {
	  $("#dialogEdit").dialog({
	      bgiframe: true,
	      autoOpen: false,
	      width: 600,
	      modal: true,
	      title : 'Edit Scenario',
	      buttons: {
	          'Accept' : function(){
	              editScenario();
	          }
	      },
	      close: function(){
	          $("#dialogEdit").html("");
	      }
	  });
	}

function displayRespDialog(id){
	$("#dialogResp").dialog('open');
    $("#dialogResp").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("viewScenarioResponsibilities?id="+id, function (data) {
    	$("#dialogResp").html(data);
    });
}

function displayDetailsDialog(id) {
	$("#dialogDetail").dialog('open');
    $("#dialogDetail").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("viewScenario?id="+id, function (data) {
    	$("#dialogDetail").html(data);
    });
}

function displayEditDialog(id){
	$("#dialogEdit").dialog('open');
	$("#dialogEdit").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
	$.get("editScenario?id="+id, function (data) {
    	$("#dialogEdit").html(data);
    });
}

function displayDeleteDialog(id) {
	$("#dialogDelete").dialog('open');
    $("#idDelete").val(id);
}

function tableProperties() {
	$(".tableScenarios tbody tr:odd").css("background", " #F6F6F6");
	$(".tableScenarios tbody tr").mouseover(function() {
		$(this).css("background", "#e5f5ff");
	});
	$(".tableScenarios tbody tr").mouseout(function() {
		$(".tableScenarios tbody tr:odd").css("background", "#F6F6F6");
		$(".tableScenarios tbody tr:even").css("background", "#FFFFFF");
	});
}

function deleteScenario() {
	$.ajax({
		type : "GET",
		url : "deleteScenario",
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

function insertScenario() {
	var name = $("#name").val();
	if (name == "")
		$(".errorName").css("display", "block");
	else { 
		$.ajax({
			type : "POST",
			url : "insertScenario",
			data : {
				name : name,
				description : $("#description").val(),
				priority : $("#priority").val(),
				type : $("#type").val(),
				stimulus : $("#stimulus").val(),
				source : $("#source").val(),
				enviroment : $("#enviroment").val(),
				artifact : $("#artifact").val(),
				response : $("#response").val(),
				measure : $("#measure").val()
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

function editScenario() {
	var name = $("#name").val();
    if ( name == "" )
        $(".errorName").css("display","block");
    else { 
		$.ajax({
			type : "POST",
			url : "saveScenario",
			data : {
				id :  $("#id").val(),
				name : name,
				description : $("#description").val(),
				priority : $("#priority").val(),
				type : $("#type").val(),
				stimulus : $("#stimulus").val(),
				source : $("#source").val(),
				enviroment : $("#enviroment").val(),
				artifact : $("#artifact").val(),
				response : $("#response").val(),
				measure : $("#measure").val()
			},
			success: function(xml) {
				var change = $(xml).find("elementChange")[0];
				if (change!=undefined && change!=null)
					processLocalChange(change);
				if(!showErrorMessages(xml))
					$("#dialogEdit").dialog('close');
			},
			dataType: "xml"
		});
	}
}

// -------------------- Table Functions ---------------------------

function insertRow(changeNumber, changedElement) {
	var scenarioId = changedElement.getAttribute("id");
	var scenarioName = changedElement.getAttribute("name");
	var scenarioType = changedElement.getElementsByTagName("qualityAttribute")[0].getAttribute("name");
	var scenarioPriority = changedElement.getAttribute("priority");
	var scenarioResponse = changedElement.getAttribute("response");
	var scenarioMeasure = changedElement.getAttribute("measure");
	if (scenarioResponse=="1") {
		scenarioResponse = "Hours";
	} else if (scenarioResponse=="0") {
		scenarioResponse = "Milliseconds";
	}
		
	try {
		$.get('jsp/scenario/ScenarioTableItem.jsp', {
			id : scenarioId,
			name : scenarioName,
			type : scenarioType,
			priority : scenarioPriority,
			response : scenarioResponse,
			measure : scenarioMeasure
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
	var scenarioId = changedElement.getAttribute("id");
	try {
		$(document.getElementById("tr" + scenarioId)).remove();
		tableProperties();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}

function editRow(changeNumber,changedElement) {
	var scenarioId = changedElement.getAttribute("id");
	var scenarioName = changedElement.getAttribute("name");
	var scenarioType = changedElement.getElementsByTagName("qualityAttribute")[0].getAttribute("name");
	var scenarioPriority = changedElement.getAttribute("priority");
	var scenarioResponse = changedElement.getAttribute("response");
	var scenarioMeasure = changedElement.getAttribute("measure");
	if (scenarioResponse=="1") {
		scenarioResponse = "Hours";
	} else if (scenarioResponse=="0") {
		scenarioResponse = "Milliseconds";
	}
	
	try {
		$(".name" + scenarioId).html(scenarioName);
		$(".type" + scenarioId).html(scenarioType);
		$(".priority" + scenarioId).html(
				"<img style='vertical-align: middle' src='resources/images/icons/"
						+ scenarioPriority + ".gif'/> " + scenarioPriority);
		$(".response" + scenarioId).html(scenarioMeasure + " " + scenarioResponse);
		$.gritter.add({
			title : "Edit Scenario",
			text : "Changes Applied.",
			image : 'resources/images/gritter-ok.png',
			time : 2000
		});
		return changeNumber;
	} catch (x) {
		return -1;
	}
}