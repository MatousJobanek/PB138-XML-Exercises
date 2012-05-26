/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var XMLSolver = {
    // setting here
    serverUrl: 'http://localhost:8080/XMLExercises/',
    evaluatorServletName: 'result',
    taskServletName: 'task',
    init: function() {
        this.moveNumber = 0;
        this.lastTime = -1;
        $(".rand").click(function() {XMLSolver.loadTask()});
        //alert($('task').value);
    },
    loadTask: function(type) {
        if (type === undefined && this.type !== undefined) {
            type = this.type;
        }
        if (type == "none" || type === undefined) {
            return;
        }
        this.type = type;
        var queryData = [];
        //queryData.push("type=" + type);
        var query = queryData.join('&');
        $.get(this.serverUrl + this.taskServletName + type, query, function(data) {
            XMLSolver.taskLoaded(data);
        });
        $("#tasktext").html("Loading...");
        $("#xmldata").html("Loading...");
        $('#result').html("");
        $("#solution").html("");
    },
    taskLoaded: function(task) {
        //alert(task)
        this.task = eval("(" + task + ")");
        this.tabs(this.task.data)
        $("#tasktext").html(this.task.text.replace(/\n/g, "\n<br>"));
        if (this.task.htmlOutput && this.task.htmlOutputAsString) {
            $("#htmloutput").html(this.task.htmlOutput);
            $("#stringoutput").html(this.task.htmlOutputAsString);
        }
        var cursorPosition = this.task.initSolution.indexOf("CURSOR");
        $("#solution").val(this.task.initSolution.replace("CURSOR", ""));
        $("#solution").focus();
        setCaretToPos(document.getElementById("solution"), cursorPosition);
        $(".send").click(function() {XMLSolver.send()});
    },
    tabs: function(data) {
        var list = "";
        var divs = "";
        for(var i in data) {
            var file = data[i];//.replace(/\n/g, "\n<br>").replace(/ /g, "&nbsp;");
            list += '<li><a href="#tabs-'+i+'">'+(i-0+1)+'</a></li>';
            divs += '<div id="tabs-'+i+'"><textarea readonly >'+file +'</textarea></div>';
        }
    
        $("#xmldata").html("<div id='tabs'><ul>" + list + "</ul>"+ divs+ "</div>");
        $( "#tabs" ).tabs();
    },
    setupServer: function (url, task, evaluator) {
        this.serverUrl = url;
        this.taskServletName = task;
        this.evaluatorServletName = evaluator;
    },
    send: function() {
        var time = (new Date()).getTime()/1000;
        if (this.lastTime != -1 && time < this.lastTime + 5) {
            $("#result").html("<b>You have to wait at least 5 seconds between two attempts.</b>"); 
            return
        }
        this.lastTime = time;
        $('#result').html("Evaluating...");
        var queryData = []
        queryData.push("userSolution=" + $("#solution").val().replace("\n", " \n"));
        //queryData.push("correctSolution=" + this.task.solution);
        //queryData.push("type=" + this.task.type);
        //queryData.push("data=" + this.task.data);
        queryData.push("id=" + this.task.id);

        var query = queryData.join('&');
        this.logInfo = $("#solution").val();
        this.moveNumber++;

        $.get(this.serverUrl + this.evaluatorServletName + this.type, query, function(data) {
            //XMLSolver.tutorLog();
            $('#result').html(data);
            if (data.indexOf('class="nok"') == -1) {
                XMLSolver.win();
            }
        });
    },
    win: function() {
        alert('You won!!');
        /*
        var q = "session_id="+session_id+"&session_hash="+check_hash+"&move_number="+this.moveNumber+"&win=1";
        sendDataToInterface(q);
        after_win();
        */
    },
    tutorLog: function() {
        var q = "session_id="+session_id+"&session_hash="+check_hash+"&move_number="+this.moveNumber+"&move="+this.logInfo;
        sendDataToInterface(q);
    }
}

function setSelectionRange(input, selectionStart, selectionEnd) {
  if (input.setSelectionRange) {
    input.focus();
    input.setSelectionRange(selectionStart, selectionEnd);
  }
  else if (input.createTextRange) {
    var range = input.createTextRange();
    range.collapse(true);
    range.moveEnd('character', selectionEnd);
    range.moveStart('character', selectionStart);
    range.select();
  }
}

function setCaretToPos (input, pos) {
  setSelectionRange(input, pos, pos);
}