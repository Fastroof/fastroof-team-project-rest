<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="/WEB-INF/jspf/head.jspf"%>
    <title>FTPR:Check</title>
    <link href="<spring:url value="css/index.css"/>" rel="stylesheet">
</head>
<body class="bg-light">
<%@include file="/WEB-INF/jspf/header.jspf"%>

<main class="bg-white">

    <div class="album py-5 bg-light">
        <div class="container">
            <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">

                <c:forEach var="request" items="${requests}">
                    <div class="col" id="request-${request.id}">
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <p class="card-text mb-1"><c:out value="${request.ownerName}"/></p>

                                <div class="d-flex justify-content-between align-items-center">
                                    <a type="button" href="/auth/approve?id=<c:out value="${request.id}"/>" class="btn btn-sm btn-outline-secondary">Прийняти</a>
                                    <a type="button" href="/auth/decline?id=<c:out value="${request.id}"/>" class="btn btn-sm btn-outline-secondary">Відхилити</a>

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