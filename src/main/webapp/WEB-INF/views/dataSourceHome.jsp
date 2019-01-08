<%--
  Created by IntelliJ IDEA.
  User: jadef
  Date: 01/01/2019
  Time: 23:34
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>IoT Data Retriever</title>
</head>
<body>
<h3>Please, enter the data source details</h3>

<form:form method="POST"
           action="${pageContext.request.contextPath}/addDataSource" modelAttribute="dataSource">
    <table>
        <tr>
            <td><form:label path="name">Name</form:label></td>
            <td><form:input path="name" /></td>
        </tr>
        <tr>
            <td><form:label path="latitude">Latitude</form:label></td>
            <td><form:input path="latitude" /></td>
        </tr>
        <tr>
            <td><form:label path="longitude">
                Longitude</form:label></td>
            <td><form:input path="longitude" /></td>
        </tr>
        <tr>
            <td><input type="submit" value="Submit" /></td>
        </tr>
    </table>
</form:form>

</body>
</html>
