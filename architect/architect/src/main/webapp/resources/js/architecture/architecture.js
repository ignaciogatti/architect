var Topic = 'topic://topic.architecture';
var Queue = 'queue://queue.architecture';

$(document).ready(function() {
	initNewArchitectureDialog();
	initDeleteArchitectureDialog();
	initEditArchitectureDialog();
	
	initConnections();
	tableProperties();
});

function initConnections(){
	org.activemq.Amq.init({
		uri : 'amq',
		logging : true,
		timeout : 45,
		clientId : (new Date()).getTime().toString()
	});
	org.activemq.Synchro.init(USER,"","","architecture","syncArchitecture",true);
}

function initNewArchitectureDialog() {
    $("#dialogAdd").dialog({
    	bgiframe: true,
    	autoOpen: false,
    	width: 600,
    	modal: true,
    	title : 'New Architecture',
    	buttons: {
    		'Accept' : function() {
    			insertArchitecture();
    		}
    	},
        close: function() {
        	$("#dialogAdd").html("");
        }
    });
    
    $("#newArchitectureButton").click(function(){
        $("#dialogAdd").dialog('open');
        $("#dialogAdd").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
        $.get("newArchitecture", function (data) {
            $("#dialogAdd").html(data);
        });
    });
}

function initDeleteArchitectureDialog() {
    $("#dialogDelete").dialog( {
    	bgiframe: true,
    	autoOpen: false,
    	width: 300,
    	modal: true,
    	title : 'Delete Architecture',
    	buttons: {
    		'Accept' : function() {
    			deleteArchitecture();
    		},
    		'Cancel': function(){
    			$(this).dialog('close');
    		}
    	},
    	close: function() {
    	}
    });
}

function displayDeleteDialog(id) {
	$("#dialogDelete").dialog('open');
    $("#idDelete").val(id);
}

function initEditArchitectureDialog() {
    $("#dialogEdit").dialog({
    	bgiframe: true,
    	autoOpen: false,
    	width: 600,
    	modal: true,
    	title : 'Edit Architecture',
    	buttons: {
    		'Accept' : function() {
    			editArchitecture();
    		}
    	},
    	close: function() {
    		$("#dialogEdit").html("");
    	}
	});
}

function displayEditDialog(id) {
	 $("#dialogEdit").dialog('open');
     $("#dialogEdit").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
     $.get("editArchitecture?id="+id, function (data) {
     	$("#dialogEdit").html(data);
     });
}

function insertArchitecture() {
	if ( $("#nombre").val() == "" )
		$("#errorArchitecture").css("display","block");
	else {
		$.ajax({
			type : "GET",
			url : "insertArchitecture",
			data : {
				name : $("#nombre").val(),
				group : $("#group").val()
			},
			success: function(xml) {
				var change = $(xml).find("elementChange")[0];
				if (change!=undefined && change!=null)
					processLocalChange(change);
				if(!showErrorMessages(xml))
					$("#dialogAdd").dialog('close');
			},
			dataType: "xml"
		});
    }
}

function deleteArchitecture() {
    $.ajax({
		type : "GET",
		url : "deleteArchitecture",
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

function editArchitecture() {
	if ( $("#nombre").val() == "" )
		$("#errorArchitecture").css("display","block");
	else {
		$.ajax({
			type : "GET",
			url : "saveArchitecture",
			data : {
				id :  $("#id").val(),
				name : $("#nombre").val(),
				group : $("#group").val()
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

function tableProperties(){
	$(".tableScenarios tbody tr:odd").css("background"," #F6F6F6");
}

function showGritter(){
	$.gritter.add({
		title: "Archietcture Modification",
		text: "Changes applied succesfully",
		image: 'resources/images/gritter-ok.png',
		time: 2000
	});
}

//-------------------- Table Functions ---------------------------

function insertRow(changeNumber, changedElement) {
	var architectureId = changedElement.getAttribute("id");
	var architectureName = changedElement.getAttribute("name");
	try {
		$("#dialogAdd").html("");
		$("#editArchitecture").unbind("click");
		$("#deleteArchitecture").unbind("click");
		$.get('jsp/architecture/ArchitectureTableItem.jsp', {
			id : architectureId,
			name : architectureName
		}, function(data) {
			$(data).appendTo("#tableBody");
		});
		tableProperties();
		showGritter();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}

function deleteRow(changeNumber,changedElement) {
	var arhictetureId = changedElement.getAttribute("id");
	try {
		$(document.getElementById("trArchitecture" + arhictetureId)).remove();
		tableProperties();
		showGritter();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}

function editRow(changeNumber,changedElement) {
	var id = changedElement.getAttribute("id");
	var architectureName = changedElement.getAttribute("name");
	try {
		$(".name" + id).html(architectureName);
		showGritter();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}