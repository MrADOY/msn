var email = "pietrzak.aurelien@gmail.com";
var newMessages = $("#newMessages");

function connect() {
		socket = new SockJS('http://localhost:5001/ws');
		stompClient = Stomp.over(socket);
		stompClient.connect({ 'userID' : email }, stompSuccess, stompFailure);
	}

	function stompSuccess(frame) {
		console.log("Your WebSocket connection was successfuly established!");
		stompClient.subscribe('/topic/' + email + '.public.messages', privateMessages);
	}

	function stompFailure(error) {
		console.log("Lost connection to WebSocket! Reconnecting in 10 seconds...");
		disableInputMessage();
	    setTimeout(connect, 1000000);
	}

	function privateMessages(message) {
    console.log("Nouveau message ");
		var instantMessage = JSON.parse(message.body);
		appendPrivateMessage(instantMessage);
	}

	function appendPrivateMessage(instantMessage) {
    newMessages
				.append("<p>" + instantMessage.destinataire + ": " + instantMessage.emetteur +  " msg" + ":" + instantMessage.message  + "</p>");
	}

	function inputMessageIsEmpty() {
		return inputMessage.val() == "";
	}
	function sendTo(e) {
		spanSendTo.text(e.toElement.textContent);
		inputMessage.focus();
	}

connect();
