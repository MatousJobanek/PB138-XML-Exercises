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
            Your result
        </th>
        <th>
            Correct result
        </th>
        
            <td class="${XSLTResult.isCorrect ? "ok":"nok"}">
            <c:out value='${XSLTResult.isCorrect ? "OK":"X"}'/>
        </td>
        
    </tr>
    <tr>
        <td>
            ${XSLTResult.userHTML}
        </td>
        <td>
            ${XSLTResult.correctHTML}
        </td>
    </tr>
    <tr>
        <td>
            <c:out value="${XSLTResult.userHTMLAsString}"/>
        </td>
        <td>
            <c:out value="${XSLTResult.correctHTMLAsString}"/>
        </td>
    </tr>
</table>
