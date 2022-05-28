<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <%@include file="/WEB-INF/jspf/head.jspf"%>
    <title>FTPR:Main</title>
    <link href="<spring:url value="css/index.css"/>" rel="stylesheet">
    <link href="<spring:url value="css/rangeSlider.css"/>" rel="stylesheet">
</head>
<body class="bg-light">
    <%@include file="/WEB-INF/jspf/header.jspf"%>

    <main class="bg-white">

        <section class="py-4 text-center container">
            <div class="row">
                <div class="col">
                    <h5>Пошук за фільтрами</h5>
                    <p class="form-label mt-2">Назва датасету</p>
                    <div class="input-group rounded">
                        <input id="search-input" type="search" class="form-control rounded-start" aria-label="Search" aria-describedby="search-addon" />
                    </div>
                    <p class="form-label mt-2">Кількість файлів</p>
                    <div>
                        <div id="anchor"></div>
                    </div>
                </div>
                <div class="col">
                    <h5>Пошук за тегами</h5>
                    <h5>
                        <c:forEach var="tagName" items="${tagsNames}">
                            <span id="find-tag-<c:out value="${tagName.replaceAll(' ','')}"/>" class="find-tag-on badge mr-2 text-wrap text-break"><c:out value="${tagName}"/></span>&nbsp;
                        </c:forEach>
                    </h5>
                </div>
            </div>
            <button id="search-button" type="button" class="btn btn-dark mt-3" style="width: fit-content;">Викорастати фільтри та теги</button>
        </section>
        <hr class="m-0">
        <div class="album py-4 bg-light">
            <div class="container">
                <div class="row row-cols-1 row-cols-sm-2 row-cols-md-3 g-3">

                    <c:forEach var="dataSet" items="${dataSets}">
                        <div class="col" files="<c:out value="${dataSet.fileCount}"/>" tag="<c:out value="${dataSet.tagName.replaceAll(' ','')}"/>" data-set-name="<c:out value="${dataSet.name.replace(' ','').toLowerCase()}"/>" id="data-set-<c:out value="${dataSet.id}"/>">
                            <div class="card shadow-sm">
                                <div class="card-body">
                                    <p class="card-text mb-1"><c:out value="${dataSet.name}"/></p>
                                    <p class="mb-1"><small class="text-secondary">Кількість файлів: <c:out value="${dataSet.fileCount}"/></small></p>
                                    <div class="mb-3">
                                        <small class="text-secondary">Тег: </small>
                                        <span class="badge bg-dark mr-2 text-wrap text-break"><c:out value="${dataSet.tagName}"/></span>&nbsp;
                                    </div>
                                    <div class="d-flex justify-content-between align-items-center">
                                        <a type="button" href="/dataset/show/<c:out value="${dataSet.id}"/>" class="btn btn-sm btn-outline-secondary">Детальніше</a>
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
    <script src="<spring:url value="js/rangeSlider.js"/>"></script>
    <script type="text/javascript">
        jQuery(function() {

            $("#anchor").rangeSlider({ skin: 'dark', direction: 'horizontal', type: 'interval', scale: true }, { step: 1, values: [], min: <c:out value="${minFileCount}"/>, max: <c:out value="${maxFileCount}"/> });

            const allDataSets = [];
            $("div[id^='data-set-']").each(function () {
                allDataSets.push([$(this).attr('id').replace('data-set-',''), $(this).attr('data-set-name'), parseInt($(this).attr('files')), $(this).attr('tag')]);
            });

            $("#search-button").on('click', () => {
                const value = $("#search-input").val().toLowerCase().replace(' ','');
                const range = [parseInt($(".slider__tip").attr('data-value')), parseInt($(".slider__tip:last").attr('data-value'))]
                allDataSets.forEach(element => {
                    const name = '#data-set-' + element[0];
                    if (element[1].includes(value)) {
                        if ((range[0] <= element[2] && element[2] <= range[1]) || (range[1] <= element[2] && element[2] <= range[0])) {
                            if ($("#find-tag-"+element[3]).hasClass("find-tag-on")) {
                                $(name).show();
                            } else {
                                $(name).hide();
                            }
                        } else {
                            $(name).hide();
                        }
                    } else {
                        $(name).hide();
                    }
                });
            });

            $("span[id^='find-tag-']").on("click", function () {
                if ($(this).hasClass("find-tag-off")) {
                    $(this).removeClass("find-tag-off");
                    $(this).addClass("find-tag-on");
                } else {
                    $(this).removeClass("find-tag-on");
                    $(this).addClass("find-tag-off");
                }
            });
        });
    </script>
</body>
</html>
