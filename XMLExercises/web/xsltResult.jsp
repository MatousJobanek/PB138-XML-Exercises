<%-- 
    Document   : result
    Created on : May 13, 2012, 7:25:21 AM
    Author     : slaweet
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<table>
    <tr>
        <th>
            #
        </th>
        <th>
            Your result
        </th>
        <th>
            Correct result
        </th>
        <th>
        </th>
    </tr>
    <tr>
        <td>
            ${result.userSolution}
        </td>
        <td>
            ${result.correctSolution}
        </td>
        
        <td>
            <c:out value="${result.userSolution}"/>
        </td>
        <td>
            <c:out value="${result.correctSolution}"/>
        </td>
        <td class="${result.isCorrect ? "ok":"nok"}">
            <c:out value='${result.isCorrect ? "OK":"X"}'/>
        </td>
    </tr>
</table>
