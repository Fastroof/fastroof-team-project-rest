<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jspf/head.jspf"%>
    <title>FTPR:Check</title>
    <style>
        .form-control-dark {
            border-color: var(--bs-gray);
        }
        .form-control-dark:focus {
            border-color: #fff;
            box-shadow: 0 0 0 .25rem rgba(255, 255, 255, .25);
        }

        .text-small {
            font-size: 85%;
        }

        .dropdown-toggle {
            outline: 0;
        }

        .btn {
            text-transform: unset !important;
        }
    </style>
</head>
<body class="bg-light">
<%@include file="/WEB-INF/jspf/header.jspf"%>

<main class="bg-white">
    <h5>Запити на додавання даних</h5>
    <div class="album py-5 bg-light">
        <div class="container">
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">

                <c:forEach var="add_request" items="${addRequests}">
                    <div class="col" id="request-${add_request.id}">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <p class="card-text mb-1"><c:out value="${add_request.ownerName}"/></p>
                                <p class="card-text mb-1"><a target="_blank" href="<c:out value="${add_request.linkToFile}"/>"><c:out value="${add_request.name}"/></a></p>

                                <div class="d-flex justify-content-between align-items-center">
                                    <a type="button" href="/data/approve_add?id=<c:out value="${add_request.id}"/>" class="btn btn-sm btn-outline-success">Прийняти</a>
                                    <a type="button" href="/data/decline_add?id=<c:out value="${add_request.id}"/>" class="btn btn-sm btn-outline-danger">Відхилити</a>

                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

            </div>
        </div>
    </div>
    <h5>Запити на редагування даних</h5>
    <div class="album py-5 bg-light">
        <div class="container">
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">

                <c:forEach var="edit_request" items="${editRequests}">
                    <div class="col" id="request-${edit_request.id}">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <p class="card-text mb-1"><c:out value="${edit_request.ownerName}"/></p>
                                <p class="card-text mb-1"><a target="_blank" href="<c:out value="${edit_request.linkToFile}"/>"><c:out value="${edit_request.name}"/></a></p>

                                <div class="d-flex justify-content-between align-items-center">
                                    <a type="button" href="/data/approve_edit?id=<c:out value="${edit_request.id}"/>" class="btn btn-sm btn-outline-success">Прийняти</a>
                                    <a type="button" href="/data/decline_edit?id=<c:out value="${edit_request.id}"/>" class="btn btn-sm btn-outline-danger">Відхилити</a>

                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>

            </div>
        </div>
    </div>
</main>
</body>
</html>