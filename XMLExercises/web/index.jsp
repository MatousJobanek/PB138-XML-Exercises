<%-- 
    Document   : index
    Created on : May 13, 2012, 7:06:08 AM
    Author     : slaweet
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>

        <link type="text/css" href="css/smoothness/jquery-ui-1.8.20.custom.css" rel="stylesheet" />
        <link rel="stylesheet" href="style.css" type="text/css" media="screen">

        <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="js/client.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.20.custom.min.js"></script>
        <script type="text/javascript">


            window.onload = function () {
                XMLSolver.init();
            }
        </script>
        <title>XML Exercises</title>
    </head>
    <body>
        <div id="wraper" class="ui-corner-all" >
            <div id="header">
                <select onchange="XMLSolver.loadTask(this.options[this.selectedIndex].value)">
                    <option selected value="none" > Choose type </option>
                    <option value="/xquery" > XQuery </option>
                    <option value="/xslt" > XSLT </option>
                    <option value="/xpath" > XPath </option>
                    <option value="/xmlschema" > XML Schema </option>
                    <option value="/dtd" > DTD </option>
                </select>
                <button class="rand">Get random</button>
            </div>
            <div class="data ui-corner-all" > 
                <h4>Testing data:</h4>
                <div id='xmldata'></div>
                <div id='htmloutput'></div>
                <div id='stringoutput'></div>
            </div>

            <div style="width:410px; float:left;" class="rect ui-corner-all">
                <h4>Assignment:</h4>
            <div  id="tasktext" ></div>
                <textarea id="solution"></textarea>
                <button class="send">Run</button>
                <div id='stringoutput'></div>
            </div>

            <div class="rect ui-corner-all" >
                <div id="result"></div>
            </div>
            <br style="clear:both;">

        </div>
    </body>
</html>