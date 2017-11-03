var stompClient = null;
var roomId ,matchId;
var countDownTimeOut, count;

function connect() {
    var socket = new SockJS('/socket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/user/queue/reply',function(result){
            // 单人通知
            var resp = JSON.parse(result.body);
            switch(resp.type){
                case 'createRoom':
                    createRoomBack(resp.msg);
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
                case 'matchEnd':
                    matchEnd(resp.msg.matchResult);
                    break;
                default:
                    console.error("不能处理的消息~");
            }
        });
        stompClient.subscribe('/topic', function (greeting) {
            // 全局通知
            console.log(greeting);
            var resp = JSON.parse(greeting.body);
            switch(resp.type){
                case 'apply':
                    applyBack(resp.msg);
                    break;
                default:
                    console.error("不能处理的全局消息~");
            }
        });

    },
        function (error) {
            addTOMsg("<p> 连接服务器丢，请刷新页面" + error + "</p>");
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
        roomId = room.id;
        matchId = room.matchId;
        $("#roomPlayers").text(JSON.stringify(room.playerAccounts));
        if(room.playerAccounts.length < room.maxPlayerNum){
            $("#apply").show();
            $("#exitRoom").show();
        }
        $("#createRoom").hide();
        if(room.status == 'fighting'){
            $("#fighting").show();
            $("#apply").hide();
        }
    }else{
        $("#roomId").text("未进入房间");
        $("#roomPlayers").text("未进入房间");
        roomId = null;
        matchId = null;
        $("#createRoom").show();
        $("#exitRoom").hide();
        $("#apply").hide();
        $("#msg").html("");
    }
}

function setConnected(b){
    $("#status").text(b ? "在线" : "离线");
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


function stepBegin(step){
    $("#exitRoom").hide();
    var optionHtml = "";
    for (var obj in step.options) {
        var a = step.options[obj];
        optionHtml+= ('<input type="button" data-v="'+obj+'" value="'+a+'">')
    }
    addTOMsg('<p>'+step.question+'['+step.score+'分]<br>'+optionHtml+'</p>');
    count = 15;
    countDownTimeOut = setInterval(function () {
        count--;
        $("#countDown").text(count);
        if (count <= 0) {
            stompClient.send("/app/answer", {}, JSON.stringify({roomId: roomId, matchId: matchId}));
            clearInterval(countDownTimeOut);
        }
    }, 1000)
}
function stepEnd(step,nextStep){
    var resultHtml = "";
    var playerAnswer = step.playerAnswer;
    for(var playerId in playerAnswer){
        var result = playerAnswer[playerId];
        var answer = step.options[result.answer] || '跳过';
        resultHtml +=('<p>用户'+playerId+'的回答:'+answer+'['+(result.right?'对':'错')+']</p>');
    }
    addTOMsg(resultHtml+'<hr>');
    if(nextStep){
        stepBegin(nextStep);
    }else{
        addTOMsg('<p>比赛结束,正在统计总得分。。</p>');
    }
}
function matchEnd(matchResult){
    var resultHtml = "";
    var winPlayer = "";
    for(var playerId in matchResult){
        var result = matchResult[playerId];
        resultHtml +=('<p>用户'+playerId+'的总得分:'+result.score+'</p>');
        if(result.win){
            winPlayer = playerId;
        }
    }
    addTOMsg(resultHtml+'<p>获胜的是：'+winPlayer+'</p>');
    $("#exitRoom").show();
}

function addTOMsg(html){
    $(html).appendTo("#msg");
    $("#msg").scrollTop($("#msg")[0].scrollHeight);
}

$(function () {
    connect();
    $( "#createRoom" ).click(function() {
        stompClient.send("/app/createRoom",{});
    });
    $( "#exitRoom" ).click(function() {
        stompClient.send("/app/exitRoom/"+roomId,{});
    });
    $( "#apply" ).click(function() {
        stompClient.send("/app/apply",{},JSON.stringify({roomId:roomId}));
        $("#msg").html("");
    });
    $("#msg").on("click",'input',function () {
        var v = $(this).data('v');
        clearInterval(countDownTimeOut);
        stompClient.send("/app/answer",{},JSON.stringify({roomId:roomId,matchId:matchId,answer:v}));
        $("#msg input").hide();
    })
});