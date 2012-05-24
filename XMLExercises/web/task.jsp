<%-- 
    Document   : task
    Created on : May 13, 2012, 7:20:00 AM
    Author     : slaweet
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
{
 "id": "<c:out value="${task.id}"/>",
 "text": "<c:out value="${task.text}"/>",
 "type": "<c:out value="${task.type}"/>",
 "data": { 
 <c:forEach items="${task.data}" var="file" varStatus="loopStatus">
     ${loopStatus.index == 0 ? "" : ","} "${loopStatus.index}": "<c:out value="${file}"/>"
 </c:forEach>
 }
}
