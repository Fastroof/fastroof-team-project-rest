<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jspf/head.jspf"%>
    <title>FTPR:Main</title>
    <link href="<spring:url value="css/header.css"/>" rel="stylesheet">
</head>
<body>
    <header class="p-3 text-white bg-dark">
        <div class="container">
            <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
                <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                    <img class="bi me-2" width="32" height="32" src="<spring:url value="logo/logo.svg"/>" alt="">
                </a>

                <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                    <li><a href="/" class="nav-link px-2 text-secondary">Головна</a></li>
                    <li><a href="/find" class="nav-link px-2 text-white">Знайти датасет</a></li>
                </ul>

                <sec:authorize access="isAnonymous()">
                    <div class="text-end">
                        <a href="/login" type="button" class="btn btn-outline-light me-2">Вхід</a>
                        <a href="/registration" type="button" class="btn btn-warning">Реєстрація</a>
                    </div>
                </sec:authorize>
                <sec:authorize access="isAuthenticated()">
                    <div class="dropdown text-end">
                        <a href="#" class="d-block link-light text-decoration-none dropdown-toggle" id="dropdownUser" data-bs-toggle="dropdown" aria-expanded="false">
                            <sec:authorize access="hasRole('ROLE_1')">
                                <img src="<spring:url value="images/user-icon.png"/>" alt="icon" width="32" height="32" class="rounded-circle">
                            </sec:authorize>
                            <sec:authorize access="hasRole('ROLE_2')">
                                <img src="<spring:url value="images/moderator-icon.png"/>" alt="icon" width="32" height="32" class="rounded-circle">
                            </sec:authorize>
                        </a>
                        <ul class="dropdown-menu text-small" aria-labelledby="dropdownUser">
                            <li><a class="dropdown-item" href="/new/dataset">Новий датасет...</a></li>
                            <li><a class="dropdown-item" href="/my/datasets">Мої датасети</a></li>
                            <sec:authorize access="hasRole('ROLE_1')">
                                <li><a class="dropdown-item" href="/to/moderator">Перейти до модератора</a></li>
                            </sec:authorize>
                            <sec:authorize access="hasRole('ROLE_2')">
                                <li><a class="dropdown-item" href="/data/check">Обробка запиту на данні</a></li>
                                <li><a class="dropdown-item" href="/auth/check">Обробка запиту на підвищення статусу</a></li>
                            </sec:authorize>
                            <li><hr class="dropdown-divider"></li>
                            <li><a class="dropdown-item" href="/logout">Вийти</a></li>
                        </ul>
                    </div>
                </sec:authorize>
            </div>
        </div>
    </header>

</body>
</html>
