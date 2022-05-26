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
    <%@include file="/WEB-INF/jspf/header.jspf"%>

    <main class="bg-white">

        <section class="py-5 text-center container">
            <div class="row">
                <div class="col">
                    <div class="input-group rounded">
                        <input id="search-input" type="search" class="form-control rounded-start" placeholder="Пошук за назвою" aria-label="Search" aria-describedby="search-addon" />
                        <button id="search-button" type="button" class="btn btn-dark">
                            <i class="fas fa-search"></i>
                        </button>
                    </div>
                    <div>

                    </div>
                </div>
                <div class="col">
                     ---
                </div>
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
                                        <a type="button" href="/dataset/n/<c:out value="${dataSet.id}"/>?action=show" class="btn btn-sm btn-outline-secondary">Детальніше</a>
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
