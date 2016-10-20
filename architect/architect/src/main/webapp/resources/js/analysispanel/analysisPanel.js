var Topic = 'topic://topic.architectureAnalysis';
var Queue = 'queue://queue.architectureAnalysis';

$(document).ready(function() {
	initNewArchitectureAnalysisDialog();
	initDeleteArchitectureAnalysisDialog();
	initEditArchitectureAnalysisDialog();
	initAppliedChanges();
	initRunFullAnalysisDialog();
	
    $("button, input:submit").button();

	initPortlets();
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
	org.activemq.Synchro.init(USER,GROUP,ARCHITECTURE,"architectureAnalysis","syncArchitectureAnalysis",false);
	org.activemq.SyncOutput.init(USER,GROUP,ARCHITECTURE,"architectureAnalysis","syncOutput",false);
}

function initPortlets() {
	$(".outputLogButton").click(function() {
		$("#console").toggle();
		$(".masLog").toggle();
		$(".menosLog").toggle();
	});
	$(".outputButton").click(function() {
		$(".solutionResults").toggle();
		$(".masSol").toggle();
		$(".menosSol").toggle();
	});
}

function toggleItemBlock(tacticId,changeId) {
	$("#closed" + tacticId + "-"+ changeId).toggle();
	$("#collapsed" + tacticId + "-"+ changeId).toggle();
	$("#itemBlock" + tacticId + "-"+ changeId).toggle();
}

function toggleTacticBlock(id) {
	$("#closedTactic" + id).toggle();
	$("#collapsedTactic" + id).toggle();
	$("#tacticBlock" + id).toggle();
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

function initNewArchitectureAnalysisDialog() {
	$("#dialogNew").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'New Architecture Analysis',
		buttons : {
			'Accept' : function() {
				insertArchitectureAnalysis();
			}
		},
		close : function() {
			$("#dialogNew").html("");
		}
	});
	
	$("#newArchitectureAnalysis").click(function() {
		$("#dialogNew").dialog('open');
	    $("#dialogNew").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
	    $.get("newArchitectureAnalysis", function (data) {
	    	$("#dialogNew").html(data);
	    });
	});
}

function initDeleteArchitectureAnalysisDialog() {
	 $("#dialogDelete").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 300,
		modal : true,
		title : 'Delete Architecture Analysis',
		buttons : {
			'Accept' : function() {
				deleteArchitectureAnalysis();
			},
			'Cancel' : function() {
				$("#dialogDelete").dialog('close');
			}
		},
		close : function() {
		}
	});
}

function initEditArchitectureAnalysisDialog() {
	  $("#dialogEdit").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Edit Architecture Analysis',
		buttons : {
			'Accept' : function() {
				editArchitectureAnalysis();
			}
		},
		close : function() {
			$("#dialogEdit").html("");
		}
	});
}

function initAppliedChanges() {
	$("#dialogChanges").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'View Changes and Apply',
		buttons : {
			'Apply' : function() {
				applyChangesDesignBot();
			}
		},
		close : function() {
			$("#dialogChanges").html("");
		}
	});
}


function initRunFullAnalysisDialog() {
	$("#dialogRunFullAnalysis").dialog({
		bgiframe : true,
		autoOpen : false,
		width : 600,
		modal : true,
		title : 'Run Full Analysis',
		buttons : {
			'Accept' : function() {
				$("#dialogRunFullAnalysis").hide();
				runFullAnalysis();
				$("#dialogRunFullAnalysis").dialog('close');
			}
		},
		close : function() {
			$("#dialogRunFullAnalysis").html("");
		}
	});
	
	$("#fullAnalysis").click(function() {
		$("#dialogRunFullAnalysis").dialog('open');
	    $("#dialogRunFullAnalysis").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
	    $("#dialogRunFullAnalysis").html(
	    		'<div class="dialogContainer"><div class="itemDialog">'+
	    			'<div id="algorithmRadios" style="margin-top: 20px;">'+
	    				'<input type="radio" id="negotiationRadio" name="negotiationRadio" value="true"/>'+
	    				'<label for="negotiationRadio">Run with negotiation?</label>'+
	    			'</div>'+	
	    		'</div></div>'
	    		);
	});
}


function insertArchitectureAnalysis() {
	$.ajax({
		type : "POST",
		url : "insertArchitectureAnalysis",
		data : {
			scenario : $("#scenario").val(),
			designBot : $("#designBot").val(),
			enable : $("#enable").val()
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

function deleteArchitectureAnalysis() {
	$.ajax({
		type : "GET",
		url : "deleteArchitectureAnalysis",
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

function editArchitectureAnalysis(){
	$.ajax({
		type : "POST",
		url : "saveArchitectureAnalysis",
		data : {
			id : $("#idAnalysis").val(),
			scenario : $("#scenario").val(),
			designBot : $("#designBot").val(),
			enable : $("#enable").val()
		},
		success : function(xml) {
			var change = $(xml).find("elementChange")[0];
			if (change!=undefined && change!=null)
				processLocalChange(change);
			if(!showErrorMessages(xml)) {
				$("#dialogEdit").html("");
				$("#dialogEdit").dialog('close');
			}
		},
		dataType : "xml"
	});
}

function startIndividualAnalysis(id) {
	var checkedRadio = null;
	var radio = document.getElementsByName('algorithmRadio');
	for ( var k = 0; k < radio.length; k++) {
		if (radio[k].checked) {
			checkedRadio = radio[k].value;		
		}
	}
	
	$.ajax({
		type : "POST",
		url : "startIndividualArchitectureAnalysis",
		data : {
			id : id,
			algorithm : checkedRadio
		},
		success : function(response,status,responseObject) {
			if(!showErrorMessages(response)) {			
				if (responseObject!=undefined && responseObject!=null) {
					var responseText = responseObject.responseText;
					if (responseText !=undefined && responseText!=null)
						$.gritter.add({
							title : responseText,
							text : " ",
							image : 'resources/images/gritter-ok.png',
							time : 2000
						});
				}
			}
		},
		dataType : "xml"
	});
}

function runFullAnalysis() {
	$.ajax({
		type : "POST",
		url : "cleanAllAnalisys",
		data : {
		},
		success : function(response,status,responseObject) {
			if(!showErrorMessages(response)) {		
				if (responseObject!=undefined && responseObject!=null) {
					var responseText = responseObject.responseText;
					if (responseText !=undefined && responseText!=null)
						$('#solutionsView').html(responseText);
				}
			}
		},
		dataType : "xml"
	});
	var checkedRadio = null;
	var radio = document.getElementsByName('algorithmRadio');
	for ( var k = 0; k < radio.length; k++) {
		if (radio[k].checked) {
			checkedRadio = radio[k].value;		
		}
	}
	var button = document.getElementById('fullAnalysis');
	button.disabled = true;
	$('#fullAnalysis').button("disable");
	$.ajax({
		type : "POST",
		url : "startFullAnalysis",
		data : {
			algorithm : checkedRadio,
			negotiation: $('#negotiationRadio').is(':checked')
		},
		complete : function(response,status,responseObject) {
			button.disabled = false;
			$('#fullAnalysis').button("enable");
			if(!showErrorMessages(response)) {			
				$.gritter.add({
					title : "Run Full Analysis",
					text : "Analysis Finished",
					image : 'resources/images/gritter-ok.png',
					time : 2000
				});
			}
		},
		dataType : "xml"
	});
	
}

function stopIndividualAnalysis(id) {
	$.ajax({
		type : "POST",
		url : "stopIndividualArchitectureAnalysis",
		data : {
			id : id
		},
		success : function(response,status,responseObject) {
			if(!showErrorMessages(response)) {			
				if (responseObject!=undefined && responseObject!=null) {
					var responseText = responseObject.responseText;
					if (responseText !=undefined && responseText!=null)
						$.gritter.add({
							title : responseText,
							text : " ",
							image : 'resources/images/gritter-ok.png',
							time : 2000
						});
				}
			}
		},
		dataType : "xml"
	});
}

function viewChanges() {
	$("#dialogChanges").dialog('open');
    $("#dialogChanges").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
	$.ajax({
		type : "GET",
		url : "appliedChangesList",
		data : {
			id : $('#designBotToApply').val()
		},
		success : function(data) {
			$("#dialogChanges").html(data);
		},
		dataType : "html"
	});
}

function updateResponsibilityChange(tacticId,changeId) {
	var name = $("#name" + tacticId + "-" + changeId).val();
	var complexity = $("#complexity" + tacticId + "-" + changeId).val();
	$.ajax({
		type : "POST",
		url : "updateResponsibilityChange",
		data : {
			designBotId : $('#idDesignBotChanges').val(),
			tacticId : tacticId,
			changeId : changeId,
			name : name,
			complexity : complexity
		},
		dataType : "html",
		success : function(data) {
			$.gritter.add({
				title : "Responsibility Change Saved.",
				text : " ",
				image : 'resources/images/gritter-ok.png',
				time : 2000
			});
		}
	});
}

function updateDependencyChange(tacticId,changeId) {
	var couplingCost = $("#couplingCost" + tacticId + "-" + changeId).val();
	$.ajax({
		type : "POST",
		url : "updateDependencyChange",
		data : {
			designBotId : $('#idDesignBotChanges').val(),
			tacticId : tacticId,
			changeId : changeId,
			couplingCost : couplingCost,
		},
		dataType : "html",
		success : function(data) {
			$.gritter.add({
				title : "Dependency Change Saved.",
				text : " ",
				image : 'resources/images/gritter-ok.png',
				time : 2000
			});
		}
	});
}

function updateModuleChange(tacticId,changeId) {
	var name = $("#name" + tacticId + "-" + changeId).val();
	$.ajax({
		type : "POST",
		url : "updateModuleChange",
		data : {
			designBotId : $('#idDesignBotChanges').val(),
			tacticId : tacticId,
			changeId : changeId,
			name : name,
		},
		dataType : "html",
		success : function(data) {
			$.gritter.add({
				title : "Module Change Saved.",
				text : " ",
				image : 'resources/images/gritter-ok.png',
				time : 2000
			});
		}
	});
}

function updateScenarioChange(tacticId,changeId) {
	var scenarioPeriod = $("#period" + tacticId + "-" + changeId).val();
	$.ajax({
		type : "POST",
		url : "updateScenarioChange",
		data : {
			designBotId : $('#idDesignBotChanges').val(),
			tacticId : tacticId,
			changeId : changeId,
			scenarioPeriod : scenarioPeriod,
		},
		dataType : "html",
		success : function(data) {
			$.gritter.add({
				title : "Scenario Change Saved.",
				text : " ",
				image : 'resources/images/gritter-ok.png',
				time : 2000
			});
		}
	});
}

function applyChangesDesignBot() {
	$.ajax({
		type : "POST",
		url : "applyChangesDesignBot",
		data : {
			id : $('#idDesignBotChanges').val()
		},
		success : function(response,status,responseObject) {
			if(!showErrorMessages(response)) {			
				$("#dialogChanges").dialog('close');
				if (responseObject!=undefined && responseObject!=null) {
					var responseText = responseObject.responseText;
					if (responseText !=undefined && responseText!=null)
						$.gritter.add({
							title : responseText,
							text : " ",
							image : 'resources/images/gritter-ok.png',
							time : 2000
						});
				}
				
			}
		},
		error : function(data) {
			if (data!=undefined && data!=null) {
				$.gritter.add({
					title : data,
					text : " ",
					image : 'resources/images/gritter-error.png',
					time : 2000
				});
			}
		},
		dataType : "xml"
	});
}

function displayEditArchitectureAnalysisDialog(id) {
	$("#dialogEdit").dialog('open');
    $("#dialogEdit").html('<img src="resources/images/loading.gif" style="margin-left : 180px ; margin-top : 50px" />');
    $.get("editArchitectureAnalysis?id="+id, function (data) {
    	$("#dialogEdit").html(data);
    });
}

function displayDeleteArchitectureAnalysisDialog(id) {
	$("#dialogDelete").dialog('open');
    $("#idDelete").val(id);
}

function blockArchitecture(id,value) {
	$.ajax({
		type : "POST",
		url : "blockArchitecture",
		data : {
			id : id,
			blockValue : value
		},
		success : function(data) {
			if (value) {
				$("#blockArchitecture").addClass("hidden");
				$("#unblockArchitecture").removeClass("hidden");
				$.gritter.add({
					title : " ",
					text : "Architecture blocked.",
					image : 'resources/images/gritter-error.png',
					time : 2000
				});
			} else {
				$("#blockArchitecture").removeClass("hidden");
				$("#unblockArchitecture").addClass("hidden");
				$.gritter.add({
					title : " ",
					text : "Architecture unblocked.",
					image : 'resources/images/gritter-error.png',
					time : 2000
				});
			}
		},
		error : function(data) {
		},
		dataType : "xml"
	});
}

function cleanAllAnalisys() {
	$.ajax({
		type : "POST",
		url : "cleanAllAnalisys",
		data : {
		},
		success : function(response,status,responseObject) {
			if(!showErrorMessages(response)) {		
				if (responseObject!=undefined && responseObject!=null) {
					var responseText = responseObject.responseText;
					if (responseText !=undefined && responseText!=null)
						$('#solutionsView').html(responseText);
						$.gritter.add({
							title : "Analyses cleaned.",
							text : " ",
							image : 'resources/images/gritter-ok.png',
							time : 2000
						});
				}
			}
		},
		dataType : "xml"
	});
}

// -------------------- Table Functions ---------------------------

function insertRow(changeNumber, changedElement) {
	var id = changedElement.getAttribute("id");
	var enable = changedElement.getAttribute("enable");
	var scenario = changedElement.children[1].getAttribute("name");
	var type = changedElement.getElementsByTagName("qualityAttribute")[0].getAttribute("name");
	var dbot = changedElement.getElementsByTagName("designBot")[0].getAttribute("name");
	try {
		$.get('jsp/analysisPanel/ArchitectureAnalysisTableItem.jsp', {
			id : id,
			scenario : scenario,
			type : type,
			dbot : dbot,
			enable : enable
		}, function(data) {
			$(data).appendTo("#tableBodyAnalysis");
		});
		tableProperties();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}

function deleteRow(changeNumber,changedElement) {
	var id = changedElement.getAttribute("id");
	try {
		$(document.getElementById("trArchitectureAnalysis" + id)).remove();
		tableProperties();
		return changeNumber;
	} catch (x) {
		return -1;
	}
}

function editRow(changeNumber,changedElement) {
	var id = changedElement.getAttribute("id");
	var enable = changedElement.getAttribute("enable");
	var scenario = changedElement.children[1].getAttribute("name");
	var type = changedElement.getElementsByTagName("qualityAttribute")[0].getAttribute("name");
	var dbot = changedElement.getElementsByTagName("designBot")[0].getAttribute("name");
	try {
		$(".scenario" + id).html(scenario);
		$(".type" + id).html(type);
		$(".dbot" + id).html(dbot);
		$(".enable" + id).html(enable);
		$.gritter.add({
			title : "Architecture Analysis Edited",
			text : "Changes Applied.",
			image : 'resources/images/gritter-ok.png',
			time : 2000
		});
		return changeNumber;
	} catch (x) {
		return -1;
	}
}