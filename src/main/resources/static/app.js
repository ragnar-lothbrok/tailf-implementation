var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/log');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected to server: ' + frame);
        stompClient.subscribe('/topic/log', function (greeting) {
            showGreeting(greeting.body);
            sendData();
        });
    });
}

function disconnect() {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected from Server.");
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}


function sendData() {
    var result = {
      title: "hello",
      sortOrder: "desc",
    };
    console.log(JSON.stringify(result));
    stompClient.send("/app/log", {
      priority: 9
    }, JSON.stringify(result));
  }

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#connect" ).click(function() { connect(); });
    $( "#disconnect" ).click(function() { disconnect(); });
    $( "#send" ).click(function() { sendName(); });
});