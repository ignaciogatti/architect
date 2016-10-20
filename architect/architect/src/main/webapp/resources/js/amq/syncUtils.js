/**
 * The next variables need to be defined:
 * 	LAST_CHANGE_NUMBER
 * 	Topic
 * 	Queue
 * The next functions need to be defined:
 * 	insertRow(changeNumber,changedElement)
 * 	deleteRow(changeNumber,changedElement)
 * 	editRow(changeNumber,changedElement)
 */

var amq = org.activemq.Amq;

var LOCAL_CHANGES= [];

org.activemq.Synchro = function() {
	
	var listenerHandler = function(message) {
		var change = message.parentElement;
		processLocalChange(change);
	};

	return {
		init: function(user,group,architecture,elementType,syncURL,generalSection) {
			if (generalSection==true) {
				addGeneralListener(elementType, listenerHandler);
				sendMessagetoQueue('<message type="ping" user="' + user + '"/>');
			} else {
				addListener(group,architecture,elementType, listenerHandler);
				sendMessagetoQueue('<message type="ping" user="' + user + ' group=' + group + ' architecture=' + architecture +'"/>');
			}
			requestSync(syncURL);
		}
	};
}();

function sendMessagetoQueue(message) {
	amq.sendMessage(Queue, message);
}

function addGeneralListener(service,handler) {
	amq.addListener(service, Topic, handler);
}

function addListener(groupId,architectureId,service,handler) {
	var selector = "JMSCorrelationID='G" + groupId + "A" + architectureId + "'";
	amq.addListener(service, Topic, handler, selector);
}

function removeListener(service) {
	amq.removeListener(service, Topic);
}

function requestSync(url) {
	setTimeout(function() {
		$.ajax({
			type : "GET",
			url : url,
			data: {lastChangeNumber : LAST_CHANGE_NUMBER},
			dataType : "xml",
			complete : requestSync(url),
			timeout : 10000, 	// 10 seg
			success : function(data) {
				processChanges(data);
			}
		});
	}, 5000); 	// 5 seg
}

function processChanges(data) {
	var elementChangeList = data.getElementsByTagName("change");
	for (var i = 0; i < elementChangeList.length; i++) {
		var change = elementChangeList[i];
		processLocalChange(change);
	}
}

function processLocalChange(change) {
	var changeType = change.getElementsByTagName("change_type")[0].textContent;
	var changeNumber = change.getElementsByTagName("change_number")[0].textContent;
	var oldElement = change.getElementsByTagName("oldElement")[0];
	var newElement = change.getElementsByTagName("newElement")[0];
	
	if ($.inArray(changeNumber, LOCAL_CHANGES)!=-1) {
		return -1;
	}
	var result = -1;
	
	switch (changeType) {
		case 'INSERT' : {
			result = insertRow(changeNumber,newElement);
			break;
		}
		case 'DELETE' : {
			result = deleteRow(changeNumber,oldElement);
			break;
		}
		case 'UPDATE' : {
			result = editRow(changeNumber,newElement);
			break;
		}
	};
	if (!(result==undefined) && (result>0)) {
		LOCAL_CHANGES.push(result);
		if (result==(LAST_CHANGE_NUMBER+1))
			LAST_CHANGE_NUMBER=result;
	}
}