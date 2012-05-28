/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var XMLSolver = {
    // setting here
    serverUrl: 'http://localhost:8080/XMLExercises/',
    evaluatorServletName: 'result/',
    taskServletName: 'task/',
    types: {
          "none": "Choose type",
          "xquery": "XQuery",
          "xslt": "XSLT",
          "xpath": "XPath",
          "xmlschema": "XML Schema",
          "dtd": "DTD"
    },
    init: function() {
        this.moveNumber = 0;
        this.lastTime = -1;
        $(".rand").click(function() {XMLSolver.loadTask()});
        $(".send").click(function() {XMLSolver.sendResult()});
        //alert($('task').value);
        if (location.hash.indexOf("/")!= -1) {
            var taskArray = location.hash.split("/");
            if (taskArray.length == 2 || taskArray[2].length == 0) {
                this.loadTask(taskArray[1]);
            }else if (taskArray.length == 3) {
                this.loadTask(taskArray[1],taskArray[2]);
            }
        }
        this.initTypeSelect();
    },
    initTypeSelect: function() {
        var typeSelect = "";
        for(var i in this.types) {
            typeSelect += '<option '+ (this.type == i ? 'selected' : '') +
                ' value="'+ i +'" >' + this.types[i] + '</option>';
        }
        $("#type").html(typeSelect);
        
    },
    loadTask: function(type, id) {
        if (type === undefined && this.type !== undefined) {
            type = this.type;
        }
        if (type == "none" || type === undefined) {
            return;
        }
        this.type = type;
        var queryData = [];
        if (id !== undefined) {
            queryData.push("id=" + id);
        }
        var query = queryData.join('&');
        $.get(this.serverUrl + this.taskServletName + type, query, function(data) {
            XMLSolver.taskLoaded(data);
        });
        $("#tasktext").html("Loading...");
        $("#xmldata").html("Loading...");
        $('#result').html("");
        $("#solution").html("");
        $(".rand").removeAttr("disabled");
        $(".send").removeAttr("disabled");
    },
    taskLoaded: function(task) {
        //alert(task)
        this.task = eval("(" + task + ")");
        this.tabs(this.task.data)
        $("#tasktext").html(this.task.text.replace(/\n/g, "\n<br>"));
        if (this.task.htmlOutput && this.task.htmlOutputAsString) {
            $("#htmloutput").html(this.task.htmlOutput);
            $("#stringoutput").html(this.task.htmlOutputAsString.replace(/\n/g, "\n<br>").replace(/\t/g, "\t&nbsp;&nbsp;&nbsp;&nbsp;"));
        } else {
            $("#htmloutput").html("");
            $("#stringoutput").html("");
        }
        var cursorPosition = this.task.initSolution.indexOf("CURSOR");
        $("#solution").val(this.task.initSolution.replace("CURSOR", ""));
        $("#solution").focus();
        setCaretToPos(document.getElementById("solution"), cursorPosition);
        location.hash = "/" + this.type + "/" + this.task.id;
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
    sendResult: function() {
        var time = (new Date()).getTime()/1000;
        if (this.lastTime != -1 && time < this.lastTime + 5) {
            $("#result").html("<b>You have to wait at least 5 seconds between two attempts.</b>"); 
            return
        }
        this.lastTime = time;
        $('#result').html("Evaluating...");
        var queryData = {
            "id": this.task.id,
            "userSolution": $("#solution").val().replace("\n", " \n")
        }
        this.logInfo = $("#solution").val();
        this.moveNumber++;

        $.post(this.serverUrl + this.evaluatorServletName + this.type, queryData, function(data) {
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
