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

<script type="text/javascript" src="jquery.js"></script>
<script type="text/javascript" src="client.js"></script>
<script type="text/javascript">


    window.onload = function () {
        XMLSolver.init();
    }
</script>
</head>
<body>
<div id="wraper" style="" >
  <div  id="tasktext" class="rect"></div>

  <div style="width:400px; float:left;">
  <textarea id="solution"><table></table></textarea>
  <button class="send">Spusť program</button>
  </div>

  <div class="data" > 
   <h4>Testovací data:</h4>
   <div id='xmldata'></div>
  </div>
  <div class="rect" >
    <div id="result"></div>
  </div>
<br style="clear:both;">

  </div>
</body>
</html>