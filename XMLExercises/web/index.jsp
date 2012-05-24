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

        <link rel="stylesheet" href="style.css" type="text/css" media="screen">
        <link type="text/css" href="jquery-ui-1.8.20.custom.css" rel="stylesheet" />

        <script type="text/javascript" src="jquery.js"></script>
        <script type="text/javascript" src="client.js"></script>
        <script type="text/javascript" src="jquery-ui-1.8.20.custom.min.js"></script>
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
                    <option value="xquery" > XQuery </option>
                    <option value="xslt" > XSLT </option>
                    <option value="xpath" > XPath </option>
                    <option value="xmlshema" > XML Schema </option>
                    <option value="dtd" > DTD </option>
                </select>
                <button class="rand">Get random</button>
            </div>
            <div  id="tasktext" class="rect ui-corner-all"></div>
            <div class="data" > 
                <div id='xmldata'></div>
            </div>

            <div style="width:400px; float:left;">
                <textarea id="solution"><table></table></textarea>
                <button class="send">Run</button>
            </div>

            <div class="rect ui-corner-all" >
                <div id="result"></div>
            </div>
            <br style="clear:both;">

        </div>
    </body>
</html>