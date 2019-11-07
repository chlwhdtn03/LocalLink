
var isUploaded;
var isFileSize = false;
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
        $("#file-upload").submit(
            function (e) {
                e.preventDefault();
                if(!isUploaded) {
                    alert("업로드된 파일이 없습니다.");
                    return;
                }
                if(!isFileSize) {
                    alert("파일의 용량이 너무 큽니다.");
                    return;
                }

                var date = new Date();
                var random = Math.floor(Math.random() * 100) + 1;
                var id = date.getTime();
                id = id + "-" + random;
                var filearray = new Array();
                webSocket.send("transfer head " + id);
                for(var i = 0; i < $("#files")[0].files.length; i++) {
                    filearray[i] = $("#files")[0].files[i].name;
                    console.log(filearray[i]);
                }
                var count = 0;
                for(var i = 0; i < $("#files")[0].files.length; i++) {
                    getBase64($("#files")[0].files[i], function(e) {
                        webSocket.send("transfer " + id + " " + e.target.result + " " + filearray[count]);
                        count++;
                        if(count == filearray.length) {
                            webSocket.send("transfer end " + id);
                        }
                    });
                }

 
                alert("정상적으로 업로드 되었습니다.");
                /* 업로드 이후 Input 초기화 */
                var agent = navigator.userAgent.toLowerCase();
                if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
                    // ie 일때 input[type=file] init.
                    $("#files").replaceWith( $("#files").clone(true) );
                    $("#parentCreateGroupFullName").val("");
                } else {
                    //other browser 일때 input[type=file] init.
                    $("#files").val("");
                    $("#parentCreateGroupFullName").val("");
                }
                $(".upload-name").val(undefined);
                $("#file-warning").text("");
                isFileSize = false;
                isUploaded = false;
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
        $("#files").on('change', function() {
                        
            if($(this)[0].files.length == 1) {
                $(".upload-name").val($(this)[0].files[0].name);
                isUploaded = true;
            } else if($(this)[0].files.length == 0)  {
                $(".upload-name").val(undefined);
                isUploaded = false;
            } else {
                $(".upload-name").val($(this)[0].files.length + "개의 파일");
                isUploaded = true;
            }
            var totalsize = 0;
            for(var i = 0; i < $(this)[0].files.length; i++) {
                totalsize += $(this)[0].files[i].size;
            }
            var Unit = ["B", "KB", "MB", "GB", "TB", "PB", "EB"];
            var i;
            for(i = 0; totalsize / 1024 >= 1; i++) {
                totalsize /= 1024;
                totalsize = totalsize.toFixed(2);
            }
            if(i > 2) {
                isFileSize = false;
                $("#file-warning").text("파일을 업로드 할 수 없습니다. " + totalsize + Unit[i]);
                $("#file-warning")[0].style.color = "red";
                return;
            } 
            if (i == 2) {
                if(totalsize > 55) {
                    isFileSize = false;
                    $("#file-warning").text("파일을 업로드 할 수 없습니다. " + totalsize + Unit[i]);
                    $("#file-warning")[0].style.color = "red";
                    return;
                }
            }
            isFileSize = true;
            $("#file-warning").text(totalsize + Unit[i]);
            $("#file-warning")[0].style.color = "black";

        });


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
                        $("#user").text(a.name + " 님!");
                        user = a.name;
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

function getBase64(file, onLoadCallback) {
    var reader = new FileReader();
    reader.onload = onLoadCallback;
    reader.readAsDataURL(file);
}

function music() {
    $(".music").show();
    $(".file").hide();
    var li = document.getElementsByTagName("li");
    li[0].className = "active";
    li[1].className = undefined;
}

function transfer() {
    $(".music").hide();
    $(".file").show();
    var li = document.getElementsByTagName("li");
    li[0].className = undefined;
    li[1].className = "active";
}

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
