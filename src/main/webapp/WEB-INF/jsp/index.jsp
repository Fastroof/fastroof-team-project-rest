<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jspf/head.jspf"%>
    <title>FTPR:Main</title>
    <link href="<spring:url value="css/index.css"/>" rel="stylesheet">
</head>
<body class="bg-light">
    <header class="p-3 text-white bg-dark">
        <div class="container">
            <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
                <a href="/" class="d-flex align-items-center mb-2 mb-lg-0 text-white text-decoration-none">
                    <img class="bi me-2" width="32" height="32" src="<spring:url value="logo/logo.svg"/>" alt="">
                </a>

                <ul class="nav col-12 col-lg-auto me-lg-auto mb-2 justify-content-center mb-md-0">
                    <li><a href="/" class="nav-link px-2 text-white">Головна</a></li>
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

    <main class="bg-white">

        <section class="py-5 text-center container">

            <div class="input-group rounded">
                <input id="search-input" type="search" class="form-control rounded-start" placeholder="Пошук за назвою" aria-label="Search" aria-describedby="search-addon" />
                <button id="search-button" type="button" class="btn btn-dark">
                    <i class="fas fa-search"></i>
                </button>
            </div>

        </section>

        <div class="album py-5 bg-light">
            <div class="container">
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">

                    <c:forEach var="dataSet" items="${dataSets}">
                        <div class="col" id="data-set-${dataSet.name.replace(' ','').toLowerCase()}">
                            <div class="card shadow-sm">
                                <div class="card-body">
                                    <p class="card-text mb-1"><c:out value="${dataSet.name}"/></p>
                                    <p class="mb-1"><small class="text-secondary">Кількість файлів: <c:out value="${dataSet.fileCount}"/></small></p>
                                    <div class="mb-3">
                                        <small class="text-secondary">Тег: </small>
                                        <span class="badge bg-dark mr-2 text-wrap text-break"><c:out value="${dataSet.tagName}"/></span>&nbsp;
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <a type="button" href="/datasets/n/<c:out value="${dataSet.id}"/>" class="btn btn-sm btn-outline-secondary">Детальніше</a>
                                        <small class="text-muted d-inline-block text-truncate" style="max-width: 10em;"><c:out value="${dataSet.ownerName}"/></small>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>

                </div>
            </div>
        </div>
    </main>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
    <script type="text/javascript">
        jQuery(function() {
            const allDataSets = [];
            $("div[id^='data-set-']").each(function () {
                allDataSets.push($(this).attr('id').replace('data-set-',''));
            });

            $("#search-button").on('click', () => {
                const value = $("#search-input").val().toLowerCase().replace(' ','');
                allDataSets.forEach(element => {
                    const name = '#data-set-' + element;
                    if (element.includes(value)) {
                        $(name).show();
                    } else {
                        $(name).hide();
                    }
                })
            });
        });
    </script>
</body>
</html>
