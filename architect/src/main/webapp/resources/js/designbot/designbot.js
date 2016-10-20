var Topic = 'topic://topic.designBot';
var Queue = 'queue://queue.designBot';

$(document).ready(function() {
	initNewDesignBotDialog();
	initDeleteDesignBotDialog();
	initEditDesignBotDialog();
	initDesignBotDetailsDialog();
	
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
	org.activemq.Synchro.init(USER,GROUP,ARCHITECTURE,"designBot","syncDesignBot",false);
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

function initNewDesignBotDialog() {
	$("#dialogNew").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'New Design Bot',
		buttons : {
			'Accept' : function() {
				insertDesignBot();
			}
		},
		close : function() {
			$("#dialogNew").html("");
		}
	});
	
	$("#newDesignBot").click(function() {
		$("#dialogNew").dialog('open');
	    $("#dialogNew").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
	    $.get("newDesignBot", function (data) {
	    	$("#dialogNew").html(data);
	    });
	});
}

function initDeleteDesignBotDialog() {
	 $("#dialogDelete").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 300,
		modal : true,
		title : 'Delete Design Bot',
		buttons : {
			'Accept' : function() {
				deleteDesignBot();
			},
			'Cancel' : function() {
				$("#dialogDelete").dialog('close');
			}
		},
		close : function() {
		}
	});
}

function initEditDesignBotDialog() {
	  $("#dialogEdit").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Edit Design Bot',
		buttons : {
			'Accept' : function() {
				editDesignBot();
			}
		},
		close : function() {
			$("#dialogEdit").html("");
		}
	});
}

function initDesignBotDetailsDialog() {
	  $("#dialogDetail").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Design Bot Details',
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

function insertDesignBot() {
	var qualityAttribute = $("#qualityAttribute").val();
	var name = $("#name").val();
	if (name == "")
		$(".errorName").css("display", "block");
	else {
		$.ajax({
			type : "POST",
			url : "insertDesignBot",
			data : {
				name : name,
				qualityAttribute : qualityAttribute
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

function deleteDesignBot() {
	$.ajax({
		type : "GET",
		url : "deleteDesignBot",
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

function editDesignBot(){
	var tactics = $("#tactics").val();
	if (tactics != null && tactics != "")
		tactics = tactics.join(",");
	else
		tactics = "";
	var qualityAttribute = $("#qualityAttribute").val();
	var name = $("#name").val();
	if (name == "")
		$(".errorName").css("display", "block");
	else {
		$.ajax({
			type : "POST",
			url : "saveDesignBot",
			data : {
				id :  $("#id").val(),
				name : name,
				qualityAttribute : qualityAttribute,
				tactics :tactics
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

function displayDesignBotDetailsDialog(id) {
	$("#dialogDetail").dialog('open');
    $("#dialogDetail").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("designBotDetails?id="+id, function (data) {
    	$("#dialogDetail").html(data);
    });
}

function displayEditDesignBotDialog(id) {
	$("#dialogEdit").dialog('open');
    $("#dialogEdit").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("editDesignBot?id="+id, function (data) {
    	$("#dialogEdit").html(data);
    });
}

function displayDeleteDesignBotDialog(id) {
	$("#dialogDelete").dialog('open');
    $("#idDelete").val(id);
}

// -------------------- Table Functions ---------------------------

function insertRow(changeNumber, changedElement) {
	var designBotId = changedElement.getAttribute("id");
	var designBotName = changedElement.getAttribute("name");
	var qualityAttribute = changedElement.getElementsByTagName("qualityAttribute")[0].getAttribute("name");
	try {
		$.get('jsp/designbots/DesignBotTableItem.jsp', {
			id : designBotId,
			name : designBotName,
			qualityAttribute : qualityAttribute
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
	var designBotId = changedElement.getAttribute("id");
	try {
		$(document.getElementById("tr" + designBotId)).remove();
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