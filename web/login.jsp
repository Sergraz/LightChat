<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <link href='http://fonts.googleapis.com/css?family=Open+Sans:400,600,800italic&subset=latin,cyrillic,cyrillic-ext' rel='stylesheet' type='text/css'>
    <link href="login.css" rel="stylesheet" type="text/css">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Light Chat Login</title>
</head>
<body>
    <div id="content">

        <c:if test="${message!=null}">
            <div class="error red">
                ${message}
            </div>
        </c:if>
        <span>Добро пожаловать в чат</span>
        <form method="POST" action="login">
            <div class="row">
                <label for="user">Представьтесь:</label>
                <input type="text" name="user" id="user">
            </div>
            <div class="row">
                <label for="color">Выберите цвет текста:</label>
                <select name="color" id="color">
                    <c:forEach items="${colors}" var="color">
                        <option class="${color.name().toLowerCase()}" value="${color.name()}">${color.toString()}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="row enter">
                <input type="submit" value="Войти">
            </div>
        </form>
    </div>
</body>
</html>
