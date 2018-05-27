function appendMessage(message) {
    $('#messages').append($('<div />').text(message.from + ": " + message.message))
}

function onNewMessage(result) {
    var message = JSON.parse(result.body);
    appendMessage(message);
}

function connectWebSocket() {
    var socket = new SockJS('http://localhost:5001/chatWS');
    stompClient = Stomp.over(socket);
    stompClient.connect({{nom: "Aurelien"}}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', onNewMessage);
    });
}

connectWebSocket();
