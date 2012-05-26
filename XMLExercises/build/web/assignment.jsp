<%-- 
    Document   : task
    Created on : May 13, 2012, 7:20:00 AM
    Author     : Matous Jobanek
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
{
 "id": "<c:out value="${Assignment.name}"/>",
 "text": "<c:out value="${Assignment.assignmentText}"/>",
 "type": "<c:out value="${Assignment.type}"/>",
 "htmlOutput": "${Assignment.htmlOutput}",
 "htmlOutputAsString": "<c:out value="${Assignment.htmlOutputAsString}"/>",
 "data": { "0": "<c:out value="${Assignment.xml}"/>"

 }
}
