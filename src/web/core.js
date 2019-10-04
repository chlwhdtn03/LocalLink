$(

    function () {
        
        $("#url").text(document.domain);
        $("#login").submit(
            function(e) { 

                e.preventDefault();
                $("#login-form").hide();

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
                case "singer":
                    $("#singer").text(a.data);
                    break;
                case "title":
                    $("#title,#navtitle").text(a.data);
                    break;
                case "album":
                    $("#album").text(a.data);
                    break;
                case "artwork":
                    $("#image").attr('src', 'data:image/png;base64,' + a.data);
                    $(".navimg").css('background-image', 'url(data:image/png;base64,' + a.data + ')');
                    break;
            }
        }
    });
function nextsong() {
    webSocket.send("nextsong");
}
function backsong() {
    webSocket.send("backsong");
}

