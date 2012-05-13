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

<style type="text/css">
textarea {width:400px; height: 200px}
h4 {margin: 5px;padding: 0px}
.data {width: 340px; float:right;}
.rect, .data { border:1px solid #ccc;  margin-bottom: 20px; padding: 5px}
.send {cursor: pointer}
#wraper { text-align:left; width:800px; margin: 20px auto 0 auto;}
</style>

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
  <textarea id="solution"></textarea>
  <button class="send">Spusť program</button>
  </div>

  <div class="data" > 
   <h4>Testovací soubory:</h4>
   <div id='xmldata'></div>
  </div>
  <div class="data" >
    <h4 >Výstup:</h4>
    <div id="result"></div>
  </div>
<br style="clear:both;">

  </div>
</body>
</html>