<%-- 
    Document   : result
    Created on : May 13, 2012, 7:25:21 AM
    Author     : slaweet
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<table>
    <tr>
        <th>
            your result:
        </th>
        <th>
            correct result:
        </th>
        <th>
            same?
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
            ${result.isCorrect}
        </td>
    </tr>
</table>
