var Topic = 'topic://topic.dependency';
var Queue = 'queue://queue.dependency';

$(document).ready(function() {
	initNewDependencyDialog();
	initEditDependencyDialog();
	initDeleteDependencyDialog();
	
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
	org.activemq.Synchro.init(USER,GROUP,ARCHITECTURE,"dependency","syncDependencies",false);
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

function initNewDependencyDialog() {
	$("#dialogNew").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'New Dependency',
		buttons : {
			'Accept' : function() {
				insertDependency();
			}
		},
		close : function() {
			$("#dialogNew").html("");
		}
	});
	
	$("#newDependencyButton").click(function() {
		$("#dialogNew").dialog('open');
	    $("#dialogNew").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
	    $.get("newDependency", function (data) {
	    	$("#dialogNew").html(data);
	    });
	});
}

function initEditDependencyDialog() {
	  $("#dialogEdit").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Edit Dependency',
		buttons : {
			'Accept' : function() {
				editDependency();
			}
		},
		close : function() {
			$("#dialogEdit").html("");
		}
	});
}

function initDeleteDependencyDialog() {
	 $("#dialogDelete").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 300,
		modal : true,
		title : 'Delete Dependency',
		buttons : {
			'Accept' : function() {
				deleteDependency();
			},
			'Cancel' : function() {
				$("#dialogDelete").dialog('close');
			}
		},
		close : function() {
		}
	});
}

function insertDependency() {
	$.ajax({
		type : "POST",
		url : "insertDependency",
		data : {
			idChild : $("#child").val(),
			idParent : $("#parent").val(),
			couplingcost : $("#couplingcost").val()
		},
		success : function(xml) {
			var change = $(xml).find("elementChange")[0];
			if (change!=undefined && change!=null)
				processLocalChange(change);
			if(!showErrorMessages(xml))
				$("#dialogNew").dialog('close');
		},
		dataType : "xml"
	});
}

function deleteDependency() {
	$.ajax({
		type : "POST",
		url : "deleteDependency",
		data : {
			idChild : $("#idDeleteChild").val(),
			idParent : $("#idDeleteParent").val()
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

function editDependency(){
	var couplingcost = $("#couplingcost").val();
	if (couplingcost == "")
		$(".errorCouplingcost").css("display", "block");
	else {
		$.ajax({
			type : "POST",
			url : "saveDependency",
			data : {
				idChild :  $("#idChild").val(),
				idParent : $("#idParent").val(),
				couplingcost : couplingcost
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

function displayEditDialog(idChild,idParent) {
	$("#dialogEdit").dialog('open');
    $("#dialogEdit").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("editDependency?idChild="+idChild+"&idParent="+idParent, function (data) {
    	$("#dialogEdit").html(data);
    });
}

function displayDeleteDialog(idChild,idParent) {
	$("#dialogDelete").dialog('open');
    $("#idDeleteChild").val(idChild);
    $("#idDeleteParent").val(idParent);
}

// -------------------- Table Functions ---------------------------

function insertRow(changeNumber, changedElement) {
	var idChild = changedElement.getElementsByTagName("childResponsibility")[0].getAttribute("id");
	var childName = changedElement.getElementsByTagName("childResponsibility")[0].getAttribute("name");
	var idParent = changedElement.getElementsByTagName("parentResponsibility")[0].getAttribute("id");
	var parentName = changedElement.getElementsByTagName("parentResponsibility")[0].getAttribute("name");
	var couplingcost = changedElement.getAttribute("couplingcost");
	try {
		$.get('jsp/dependency/DependencyTableItem.jsp', {
			idChild : idChild,
			idParent : idParent,
			childName : childName,
			parentName : parentName,
			couplingcost : couplingcost
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
	var idChild = changedElement.getElementsByTagName("id")[0].getAttribute("child");
	var idParent = changedElement.getElementsByTagName("id")[0].getAttribute("parent");
	try {
		$(document.getElementById("tr" + idChild + "-" + idParent )).remove();
		tableProperties();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}

function editRow(changeNumber,changedElement) {
	var idChild = changedElement.getElementsByTagName("id")[0].getAttribute("child");
	var idParent = changedElement.getElementsByTagName("id")[0].getAttribute("parent");
	var couplingcost = changedElement.getAttribute("couplingcost");
	try {
		$(".couplingcost-"+ idChild + "-" + idParent).html(couplingcost);
		$.gritter.add({
			title : "Dependency Edited",
			text : "Changes Applied.",
			image : 'resources/images/gritter-ok.png',
			time : 2000
		});
		return changeNumber;
	} catch (x) {
		return -1;
	}
}