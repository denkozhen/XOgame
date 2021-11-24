const url = 'http://localhost:8080';
let stompClient;
let gameId;
let playerType;

function connectToSocket(gameId) {

    console.log("Присоединение к игре: ");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("Соединение: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            displayResponse(data);
        })
    })
}

function create_game() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Введите логин");
    } else {
        $.ajax({
            url: url + "/game/start",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'X';
                reset();
                connectToSocket(gameId);
                alert("Вы создали игру с id: " + data.gameId);
                gameOn = true;
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}


function connectToOpponent() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Введите логин");
    } else {
        $.ajax({
            url: url + "/game/connect/ToOpponent",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                gameId = data.gameId;
                playerType = 'O';
                reset();
                connectToSocket(gameId);
                alert("Вы играете с : " + data.firstPlayer.login);
            },
            error: function (error) {
                console.log();
                alert("Игра не найдена");
            }
        })
    }
}
