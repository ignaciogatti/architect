$(document).ready(function() {
	$("#statesTitle").click(function() {
		$("#statesBody").toggle("slow");
	});
	$(".house").click(function() {
		$(".architectureOptions").toggle("slow");
	});
	$(".resp").click(function() {
		$(".respOptions").toggle("slow");
	});
	$(".scen").click(function() {
		$(".scenOptions").toggle("slow");
	});
	$(".tact").click(function() {
		$(".tactOptions").toggle("slow");
	});
	$(".design").click(function() {
		$(".designOptions").toggle("slow");
	});
	
	$.get("sidebar/listArchitectures", function(data) {
		$(".architectureOptions").html(data);
	});
	$.get("sidebar/listScenarios", function(data) {
		$(".scenOptions").html(data);
	});
	$.get("sidebar/listResponsibilities", function(data) {
		$(".respOptions").html(data);
	});
	$.get("sidebar/listDesignBots", function(data) {
		$(".designOptions").html(data);
	});
	refreshArchitectureStates();
	initSidebarDialogs();
});

function refreshArchitectureStates() {
	$("#applyUndo").addClass("hidden");
	$.get("sidebar/listArchitectureStates", function(data) {
		$("#statesBody").html(data);
		$("#applyUndo").removeClass("hidden");
	});
}

function applyUndo() {
	var change = $('#changes').val();
	$.ajax({
		type : "POST",
		url : "undoChanges",
		data : {
			change : change
		},
		success: function(xml) {
			var change = $(xml).find("elementChange")[0];
			processLocalChange(change);
		},
		dataType: "xml"
	});
}

function initSidebarDialogs() {
	$("#dialogDetailResp").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Responsibility Details',
		buttons : {
			'Accept' : function() {
				$(this).dialog('close');
			}
		},
		close : function() {
			$("#dialogDetailResp").html("");
		}
	});

	$("#dialogDetailScen").dialog({
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
			$("#dialogDetailScen").html("");
		}
	});

	$("#dialogDetailDesign").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Design Bot Details',
		buttons : {
			'Accept' : function() {
				$(this).dialog('close');
			}
		},
		close : function() {
			$("#dialogDetailDesign").html("");
		}
	});
}