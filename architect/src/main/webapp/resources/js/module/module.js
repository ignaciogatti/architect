var Topic = 'topic://topic.module';
var Queue = 'queue://queue.module';

$(document).ready(function() {
	initNewModuleDialog();
	initDeleteModuleDialog();
	initEditModuleDialog();
	initModuleDetailsDialog();
	
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
	org.activemq.Synchro.init(USER,GROUP,ARCHITECTURE,"module","syncModules",false);
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

function initNewModuleDialog() {
	$("#dialogNew").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'New Module',
		buttons : {
			'Accept' : function() {
				insertModule();
			}
		},
		close : function() {
			$("#dialogNew").html("");
		}
	});
	
	$("#newModule").click(function() {
		$("#dialogNew").dialog('open');
	    $("#dialogNew").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
	    $.get("newModule", function (data) {
	    	$("#dialogNew").html(data);
	    });
	});
}

function initDeleteModuleDialog() {
	 $("#dialogDelete").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 300,
		modal : true,
		title : 'Delete Module',
		buttons : {
			'Accept' : function() {
				deleteModule();
			},
			'Cancel' : function() {
				$("#dialogDelete").dialog('close');
			}
		},
		close : function() {
		}
	});
}

function initEditModuleDialog() {
	  $("#dialogEdit").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Edit Module',
		buttons : {
			'Accept' : function() {
				editModule();
			}
		},
		close : function() {
			$("#dialogEdit").html("");
		}
	});
}

function initModuleDetailsDialog() {
	  $("#dialogDetail").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Module Details',
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

function insertModule() {
	var name = $("#name").val();
	if (name == "")
		$(".errorName").css("display", "block");
	else {
		$.ajax({
			type : "POST",
			url : "insertModule",
			data : {
				name : name,
				description : $("#description").val()
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

function deleteModule() {
	$.ajax({
		type : "GET",
		url : "deleteModule",
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

function editModule(){
	var name = $("#name").val();
	if (name == "")
		$(".errorName").css("display", "block");
	else {
		$.ajax({
			type : "POST",
			url : "saveModule",
			data : {
				id :  $("#id").val(),
				name : name,
				description : $("#description").val()
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

function displayModuleDetailsDialog(id) {
	$("#dialogDetail").dialog('open');
    $("#dialogDetail").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("moduleDetails?id="+id, function (data) {
    	$("#dialogDetail").html(data);
    });
}

function displayEditModuleDialog(id) {
	$("#dialogEdit").dialog('open');
    $("#dialogEdit").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("editModule?id="+id, function (data) {
    	$("#dialogEdit").html(data);
    });
}

function displayDeleteModuleDialog(id) {
	$("#dialogDelete").dialog('open');
    $("#idDelete").val(id);
}

// -------------------- Table Functions ---------------------------

function insertRow(changeNumber, changedElement) {
	var moduleId = changedElement.getAttribute("id");
	var moduleName = changedElement.getAttribute("name");
	try {
		$.get('jsp/module/ModuleTableItem.jsp', {
			id : moduleId,
			name : moduleName
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
	var moduleId = changedElement.getAttribute("id");
	try {
		$(document.getElementById("tr" + moduleId)).remove();
		tableProperties();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}

function editRow(changeNumber,changedElement) {
	var moduleId = changedElement.getAttribute("id");
	var moduleName = changedElement.getAttribute("name");
	try {
		$(".name" + moduleId).html(moduleName);
		$.gritter.add({
			title : "Module Edited",
			text : "Changes Applied.",
			image : 'resources/images/gritter-ok.png',
			time : 2000
		});
		return changeNumber;
	} catch (x) {
		return -1;
	}
}