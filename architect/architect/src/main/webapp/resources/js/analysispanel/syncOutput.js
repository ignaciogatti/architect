var amq = org.activemq.Amq;

var LOCAL_CHANGES= [];

org.activemq.SyncOutput = function() {
	return {
		init: function(user,group,architecture,elementType,syncURL,generalSection) {
			requestOutput(syncURL);
		}
	};
}();

function requestOutput(url) {
	setTimeout(function() {
		$.ajax({
			type : "GET",
			url : url,
			data: {
			},
			dataType : "xml",
			complete : requestOutput(url),
			timeout : 5000, 	// 5 seg
			success : function(data) {
				processResponse(data);
			}
		});
	}, 3000); 	// 3 seg
}

function processResponse(data) {
	var errors = data.getElementsByTagName("parsererror");
	if (errors.length<=0) {
		var analysisResults = data.getElementsByTagName("analysisResults");
		if (analysisResults.length>0)
			processAnalysisResults(analysisResults[0]);
	}
}

function processAnalysisResults(analysisResults) {
	var actualArchitectureResults = analysisResults.getElementsByTagName("actualArchitectureResult")[0];
	var designBotResultsList = analysisResults.getElementsByTagName("designBotResult");
	for (var i = 0; i < designBotResultsList.length; i++) {
		updateArchitectureAnalysisResults(designBotResultsList[i],actualArchitectureResults);
	}
}

function updateArchitectureAnalysisResults(designBotResults,actualArchitectureResults) {
	var architectureAnalysis = designBotResults.getElementsByTagName("architectureAnalysis")[0];
	var architectureAnalysisId = architectureAnalysis.getAttribute("id");
	var architectureAnalysisResults = designBotResults.getElementsByTagName("architectureResults")[0];

	updateTableAnalysisResultsColumn(architectureAnalysisId);
	updateTableAnalysisResultsRows(architectureAnalysisId, architectureAnalysisResults,actualArchitectureResults);
	updateApplyOptions(architectureAnalysisId);
}

function updateTableAnalysisResultsColumn(architectureAnalysisId) {
	if (architectureAnalysisId == 9999){
		if ($('#columnDBotNegotiation').length <= 0) {
			$("#tableAnalysisResultsColumns").append('<th id="columnDBotNegotiation"> Negotiation Results </th>');
		}
		else
			$('#columnDBotNegotiation').html("Negotiation Results");
	}
	else{
		if ($('#columnDBot'+ architectureAnalysisId).length <= 0) {
			$("#tableAnalysisResultsColumns").append('<th id="columnDBot' + architectureAnalysisId + '"> Analysis ' + architectureAnalysisId + '</th>');
		} else {
			$('#columnDBot'+ architectureAnalysisId).html("Analysis " + architectureAnalysisId);
		}
	}
}

function updateTableAnalysisResultsRows(architectureAnalysisId, architectureAnalysisResults,actualArchitectureResults) {
	var newScenarioResults = architectureAnalysisResults.getElementsByTagName("ScenarioResults");
	for (var i = 0; i < newScenarioResults.length; i++) {
		
		var newScenarioId = newScenarioResults[i].getElementsByTagName("scenario")[0].getAttribute("id");
		var newScenarioCost = newScenarioResults[i].getAttribute("scenarioCost");
		var scenarioMeasure = actualArchitectureResults.getElementsByTagName("scenario")[i].getAttribute("measure");
		
		var actualScenarioResults = actualArchitectureResults.getElementsByTagName("ScenarioResults");
		var actualScenario;
		var actualScenarioCost = 0;
		for (var j = 0; j < actualScenarioResults.length; j++){
			var scenario = actualScenarioResults[j].getElementsByTagName("scenario")[0];
			if (scenario.getAttribute("id") == newScenarioId){
				actualScenario = scenario;
				actualScenarioCost = actualScenarioResults[i].getAttribute("scenarioCost");
			}
		}
		
		var fieldContent = "";
		if (parseFloat(newScenarioCost) > parseFloat(actualScenarioCost)){
			fieldContent = '<img src="resources/images/arrow_up.png" width="12" height="14" />';
		}
		else if (parseFloat(newScenarioCost) < parseFloat(actualScenarioCost)){
			fieldContent = '<img src="resources/images/arrow_down.png" width="12" height="14" />';
		}
		else{
			fieldContent = '<img src="resources/images/equals.png" width="12" height="14" />';
		}
		
		var costoEscenario = newScenarioCost;
		if (parseFloat(costoEscenario) > parseFloat("9999")){
			costoEscenario = "--";
		}
		if (parseFloat(newScenarioCost) >= parseFloat(scenarioMeasure)){
			fieldContent = fieldContent + '<img src="resources/images/circle_no.png" width="12" height="12" />' + costoEscenario;
		} else{
			fieldContent = fieldContent + '<img src="resources/images/circle_ok.png" width="12" height="12" />' + costoEscenario;
		}
		
		if ($("#td" + newScenarioId + "-" + architectureAnalysisId).length <= 0) {
			$("#trAnalysisResult"+newScenarioId).append('<td id="td' + newScenarioId + "-" + architectureAnalysisId + '" class="a-left results-margin">' + fieldContent + '</td>');
		} else {
			$("#td" + newScenarioId + "-" + architectureAnalysisId).html(fieldContent);
		}
		
	}
}

function updateApplyOptions(architectureAnalysisId) {
	if (architectureAnalysisId == 9999){
		if ($('#optionNegotiation').length <= 0) {
			$("#designBotToApply").append('<option id="optionNegotiation" value="' + architectureAnalysisId + '"> Negotiation Results</option>');	
		}
		else
			$('#optionNegotiation').html("Negotiation Results");
	}
	else {
		if ($('#option'+ architectureAnalysisId).length <= 0) {
			$("#designBotToApply").append('<option id="option' + architectureAnalysisId + '" value="' + architectureAnalysisId + '"> Analysis ' + architectureAnalysisId + '</option>');	
		} else {
			$('#option'+ architectureAnalysisId).html("Analysis " + architectureAnalysisId);
		}
	}
	$("#viewChanges").removeClass("hidden");
	$("#applyChanges").removeClass("hidden");
	$("#designBotToApply").removeClass("hidden");
}