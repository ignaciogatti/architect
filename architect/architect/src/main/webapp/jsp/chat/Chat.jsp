<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="resources/js/jquery/jquery-1.8.3.js"></script>
<script type="text/javascript" src="resources/js/amq/amq_jquery_adapter.js"></script>
<script type="text/javascript" src="resources/js/amq/amq.js"></script>
<script type="text/javascript" src="resources/js/chat/chat.js"></script>
<script type="text/javascript">
	window.onload = function() {
		org.activemq.Amq.init({
			uri : 'amq',
			logging : true,
			timeout : 45,
			clientId : (new Date()).getTime().toString()
		});
		org.activemq.Chat.init();
	};
</script>

<h3>Chat:</h3>
<div id="chat"></div>
<h3>Members:</h3>
<div id="members"></div>
<div id="input">
	<div id="join" class="hidden">
		Username:&nbsp; <input id="username" type="text" />
		<button id="joinB">Join</button>
	</div>
	<div id="joined" class="hidden">
		Chat:&nbsp; <input id="phrase" type="text" />
		<button id="sendB">Send</button>
		<button id="leaveB">Leave</button>
	</div>
</div>