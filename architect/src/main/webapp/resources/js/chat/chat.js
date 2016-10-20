var amq = org.activemq.Amq;
var chatTopic = 'topic://topic.chat';
var chatQueue = 'queue://queue.chat';

org.activemq.Chat = function() {
	var last = '';

	var user = null;

	var chat, join, joined, phrase, members, username = null;

	var chatHandler = function(message) {
		var type = message.getAttribute('type');
		var from = message.getAttribute('from');

		switch (type) {
			// Incoming chat message
			case 'chat' : {
				var text = message.childNodes[0].data;

				if (from == last) from = '...';
				else {
					last = from;
					from += ':';
				}

				chat.innerHTML += '<span class=\'from\'>' + from + '&nbsp;</span><span class=\'text\'>' + text + '</span><br/>';
				break;
			}

			// Incoming ping request, add the person's name to your list.
			case 'ping' : {
				members.innerHTML += '<span class="member">' + from + '</span><br/>';
				break;
			}

			// someone new joined the chatroom, clear your list and
			// broadcast your name to all users.
			case 'join' : {
				members.innerHTML = '';
				if (user != null)
					sendMessagetoQueue('<message type="ping" from="' + user + '"/>');
				chat.innerHTML += '<span class="alert"><span class="from">' + from + '&nbsp;</span><span class="text">has joined the room!</span></span><br/>';
				break;
			}

			// Screw you guys, I'm going home...
			// When I (and everyone else) receive a leave message, we broadcast
			// our own names in a ping in order to update everyone's list.
			// todo: Make this more efficient by simply removing the person's name from the list.
			case 'leave': {
				members.innerHTML = '';
				chat.innerHTML += '<span class="alert"><span class="from">' + from + '&nbsp;</span><span class="text">has left the room!</span></span><br/>';

				// If we are the one that is leaving...
				if (from == user) {
				// switch the input form
					join.className = '';
					joined.className = 'hidden';
					username.focus();

					user = null;
					removeListener('chat');
				}
				if (user != null)
					sendMessagetoQueue('<message type="ping" from="' + user + '"/>');
				break;
			}
		}

		chat.scrollTop = chat.scrollHeight - chat.clientHeight;
	};

	var getKeyCode = function (ev) {
		var keyc;
		if (window.event) keyc = window.event.keyCode;
		else keyc = ev.keyCode;
		return keyc;
	};

	var addEvent = function(obj, type, fn) {
		if (obj.addEventListener)
			obj.addEventListener(type, fn, false);
		else if (obj.attachEvent) {
			obj["e"+type+fn] = fn;
			obj[type+fn] = function() { obj["e"+type+fn]( window.event ); };
			obj.attachEvent( "on"+type, obj[type+fn] );
		}
	};

	var initEventHandlers = function() {
		addEvent(username, 'keyup', function(ev) {
			var keyc = getKeyCode(ev);
			if (keyc == 13 || keyc == 10) {
				org.activemq.Chat.join();
				return false;
			}
			return true;
		});

		addEvent(document.getElementById('joinB'), 'click', function() {
			org.activemq.Chat.join();
			return true;
		});

		addEvent(phrase, 'keyup', function(ev) {
			var keyc = getKeyCode(ev);

			if (keyc == 13 || keyc == 10) {
				var text = phrase.value;
				phrase.value = '';
				org.activemq.Chat.chat(text);
				return false;
			}
			return true;
		});

		addEvent(document.getElementById('sendB'), 'click', function() {
			var text = phrase.value;
			phrase.value = '';
			org.activemq.Chat.chat(text);
		});

		addEvent(document.getElementById('leaveB'), 'click', function() {
			org.activemq.Chat.leave();
			return false;
		});
	};

	return {
		join: function() {
			var name = username.value;
			if (name == null || name.length == 0) {
				alert('Please enter a username!');
			} else {
				user = name;

				addListener('chat', chatHandler);
				join.className = 'hidden';
				joined.className = '';
				phrase.focus();

				sendMessagetoQueue('<message type="join" from="' + user + '"/>');
			}
		},

		leave: function() {
			sendMessagetoQueue('<message type="leave" from="' + user + '"/>');
		},

		chat: function(text) {
			if (text != null && text.length > 0) {
				// TODO more encoding?
				text = text.replace('<', '&lt;');
				text = text.replace('>', '&gt;');

				sendMessagetoQueue('<message type="chat" from="' + user + '">' + text + '</message>');
			}
		},

		init: function() {
			join = document.getElementById('join');
			joined = document.getElementById('joined');
			chat = document.getElementById('chat');
			members = document.getElementById('members');
			username = document.getElementById('username');
			phrase = document.getElementById('phrase');

			if (join.className == 'hidden' && joined.className == 'hidden') {
				join.className = '';
				joined.className = 'hidden';
				username.focus();
			}

			initEventHandlers();
		}
	};
}();

function sendMessagetoQueue(message) {
	amq.sendMessage(chatQueue, message);
}

function addListener(service,handler) {
	var selector = "JMSCorrelationID='12345678'";
	amq.addListener(service, chatTopic, handler, selector);
}

function removeListener(service) {
	amq.removeListener(service, chatTopic);
}









