
var user;

$(

    function () {

        $("#url").text(document.domain);
        $("#login").submit(
            function (e) {
                e.preventDefault();
                webSocket.send("login " + $("#login input[name=id]").val() + " " + $("#login input[name=password]").val());
            }
        );
        $("#signup").submit(
            function (e) {
                e.preventDefault();
                if (!($("#signup input[name=password]").val() == $("#signup input[name=passwordrepeat]").val())) {
                    $("#signup #notice").text("비밀번호가 일치하지 않습니다.");
                    return;
                }
                webSocket.send("signup " + $("#signup input[name=id]").val() + " " + $("#signup input[name=name]").val() + " " + $("#signup input[name=password]").val());
            }
        );


        webSocket = new WebSocket("ws://" + location.host + "/LoalLink");
        webSocket.onopen = function () {
            webSocket.send("connect");
        };
        webSocket.onclose = function () {
            alert("서버와 연결이 종료되었습니다.");
        };
        webSocket.onmessage = function (b) {
            var a = JSON.parse(b.data);
            switch (a.type) {
                case "signup":
                    if (a.data == "allow") {
                        $("#signup #notice").text("");
                        hidesignup();
                    } else if (a.data == "iderr") {
                        $("#signup #notice").text("이미 존재하는 아이디입니다.");
                    } else if (a.data == "deny") {
                        $("#signup #notice").text("가입 요청이 거부되었습니다.");
                    }
                    break;
                case "login":
                    if (a.data == "allow") {
                        $("#login #notice").text("");
                        $("#user").text("안녕하세요, " + a.name + " 님!");
                        hidelogin();
                    } else if (a.data == "deny") {
                        $("#login #notice").text("아이디 또는 비밀번호가 일치하지 않습니다.");
                    }
                    break;
                case "singer":
                    $("#singer").text(a.data);
                    break;
                case "title":
                    $("#title,#navtitle").text(a.data);
                    break;
                case "album":
                    $("#album").text(a.data);
                    break;
                case "lyric":
                    $("#lyric").text(a.data);
                    break;
                case "artwork":
                    a.data = a.data.replace(/(\r\n|\n|\r)/gm, "");
                    $("#image").attr('src', 'data:image/png;base64,' + a.data);
                    $(".navimg").css('background-image', 'url(data:image/png;base64,' + a.data + ')');
                    break;
            }
        }
    });

function hidesignup() {
    $("#signup-form").hide();
}

function showsignup() {
    $("#signup-form").show();
}


function hidelogin() {
    $("#login-form").hide();
}

function showlogin() {
    $("#login-form").show();
}

function showlyric() {
    $("#lyric-form").show();
}

function hidelyric() {
    $("#lyric-form").hide();
}

function nextsong() {
    webSocket.send("nextsong");
}
function backsong() {
    webSocket.send("backsong");
}
