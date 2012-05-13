/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

var XMLSolver = {
    // setting here
    serverUrl: 'http://localhost:8080/XMLExercises/',
    evaluatorServletName: 'result.jsp',
    taskServletName: 'task.jsp',
    init: function() {
        this.moveNumber = 0;
        this.lastTime = -1;
        //alert($('task').value);
        var query = '';
        $.get(this.serverUrl + this.taskServletName, query, function(data) {
            XMLSolver.loadTask(data);
        });
    },
    loadTask: function(task) {
        this.task = eval("(" + task + ")");
        $("#xmldata").html(this.task.data);
        $("#tasktext").html(this.task.text);
        $("#solution").focus();
        $(".send").click(function() {XMLSolver.send()});
    },
    setupServer: function (url, task, evaluator) {
        this.serverUrl = url;
        this.taskServletName = task;
        this.evaluatorServletName = evaluator;
    },
    send: function() {
        var time = (new Date()).getTime()/1000;
        if (this.lastTime != -1 && time < this.lastTime + 5) {
            $("#result").html("<b>Ještě neuplynulo 5 vteřin od posledního pokusu.</b>"); 
            return
        }
        this.lastTime = time;
        $('#result').html("Probíhá vyhodnocování..");
        var queryData = []
        queryData.push("userSolution=" + $("#solution").val());
        queryData.push("correctSolution=" + this.task.solution);
        queryData.push("type=" + this.task.type);
        queryData.push("data=" + this.task.data);

        var query = queryData.join('&');
        this.logInfo = $("#solution").val();
        this.moveNumber++;

        $.get(this.serverUrl + this.evaluatorServletName, query, function(data) {
            //XMLSolver.tutorLog();
            if (data.slice(1,4) == "WIN") {
                data = data.slice(4);
                XMLSolver.win()
            }
            $('#result').html(data);
        });
    },
    win: function() {
        alert('správně!!');
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
