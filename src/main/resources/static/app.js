var stompClient = null;
var playerId,roomId ,matchId;

function connect() {
    var socket = new SockJS('/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/reply',function(result){
            // 单人通知
            var resp = JSON.parse(result.body)
            switch(resp.type){
                case 'createRoom':
                    createRoomBack(resp.msg);
                    break;
                case 'joinRoom':
                    updateRoomBack(resp.msg);
                    break;
                case 'updateRoom':
                    updateRoomBack(resp.msg);
                    break;
                case 'stepBegin':
                    stepBegin(resp.msg.step);
                    break;
                case 'stepEnd':
                    stepEnd(resp.msg.step,resp.msg.nextStep);
                    break;
                default:
                    console.error("不能处理的消息~");
            }
        })
        stompClient.subscribe('/topic', function (greeting) {
            // 全局通知
            console.log(greeting);
            var resp = JSON.parse(greeting.body)
            switch(resp.type){
                case 'apply':
                    applyBack(resp.msg);
                    break;
                default:
                    console.error("不能处理的全局消息~");
            }
        });

    });
}

function createRoomBack(msg){
    stompClient.send("/app/joinRoom/"+msg.roomId);
}
function updateRoomBack(msg){
    console.log("房间有变动");
    if(msg.room){
        var room = msg.room;
        $("#roomId").text(room.id);
        roomId = $("#roomId").text();
        matchId = room.matchId;
        $("#roomPlayers").text(JSON.stringify(room.playerIds));
        $("#apply").show();
        $("#createRoom").hide();
        if(room.status == 'fighting'){
            $("#fighting").show();
            $("#apply").hide();
            $('<hr><p>人员齐备，比赛开始</p>').appendTo("#msg");
        }
    }
}

function setConnected(b){
    $("#status").text(b?"在线":"离线")
    $("#createRoom").show();
}
function applyBack(msg) {
    if(roomId){
        return ;
    }
    if (confirm("是否接受"+msg.playerId+"的邀请？"))
    {
        stompClient.send("/app/joinRoom/"+msg.roomId);
    }
}
function applyAnswer(msg) {
    if(roomId != msg.roomId){
        return ;
    }
    addTOMsg('<p>'+msg.playerId+':'+msg.answer+'</p>');
}

function stepBegin(step){
    var optionHtml = "";
    for (var obj in step.options) {
        var a = step.options[obj]
        optionHtml+= ('<input type="button" data-v="'+obj+'" value="'+a+'">')
    }
    addTOMsg('<p>'+step.question+'<br>'+optionHtml+'</p>');
}
function stepEnd(step,nextStep){
    var resultHtml = "";
    var playerAnswer = step.playerAnswer;
    for(var playerId in playerAnswer){
        var result = playerAnswer[playerId];
        var answer = step.options[result.answer]
        resultHtml +=('<p>用户'+playerId+'的回答:'+answer+'['+(result.right?'对':'错')+']</p>');
    }
    addTOMsg(resultHtml+'<hr>');
    if(nextStep){
        stepBegin(nextStep);
    }else{
        addTOMsg('<hr><p>比赛结束</p>');
    }
}

function addTOMsg(html){
    $(html).appendTo("#msg");
    $("#msg").scrollTop($("#msg")[0].scrollHeight);
}

$(function () {
    playerId = $("#playerId").text();
    connect();
    $( "#createRoom" ).click(function() {
        stompClient.send("/app/createRoom",{},JSON.stringify({playerId:playerId}));
    });
    $( "#apply" ).click(function() {
        stompClient.send("/app/apply",{},JSON.stringify({roomId:roomId}));
    });
    $("#msg").on("click",'input',function () {
        var v = $(this).data('v');
        stompClient.send("/app/answer",{},JSON.stringify({roomId:roomId,matchId:matchId,answer:v}));
        $("#msg input").hide();
    })
});