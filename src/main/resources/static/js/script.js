let turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
const turn = "";
let gameOn = false;
let lastMove;


function playerTurn(turn, id) {
    if (gameOn) {
        const spotTaken = $("#" + id).text();


        if (spotTaken !== '#') return;


        console.log('last', lastMove)
        if (lastMove === playerType) return alert("Нельзя ходить 2 раза подряд, дождитесь хода соперника!");


        makeAMove(playerType, id.split("_")[0], id.split("_")[1]);

    }
}

function makeAMove(type, xCoordinate, yCoordinate) {
    $.ajax({
        url: url + "/game/gameplay",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify({
            "type": type,
            "coordinateX": xCoordinate,
            "coordinateY": yCoordinate,
            "gameId": gameId,
            "login": login
        }),
        success: function (data) {
            gameOn = false;
            // lastMove = data.lastMove;
            displayResponse(data, type);
        },
        error: function (error) {
            console.log(error);
        }
    })
}

function displayResponse(data) {
    console.log('display')
    let board = data.board;

    lastMove = data.lastMove;

    for (let i = 0; i < board.length; i++) {
        for (let j = 0; j < board[i].length; j++) {

            if (board[i][j] === 1) {
                turns[i][j] = 'X';
            } else if (board[i][j] === 2) {
                turns[i][j] = 'O';
            }

            let id = i + "_" + j;
            $("#" + id).text(turns[i][j]);
        }
    }


    if (data.winner != null) {
        if (data.winner === "X" || data.winner === "O") {
            alert("Победитель: " + data.winner);
            location.reload();
        } else alert("Ничья :) ");
        location.reload();
    }
    gameOn = true;
}


$(".tic").click(function () {
    var slot = $(this).attr('id');
    playerTurn(turn, slot);
});

function reset() {
    turns = [["#", "#", "#"], ["#", "#", "#"], ["#", "#", "#"]];
    $(".tic").text("#");
}

$("#reset").click(function () {
    reset();
});


