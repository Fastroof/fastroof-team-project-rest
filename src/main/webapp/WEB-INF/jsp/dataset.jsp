<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta charset="utf-8">
    <meta name="description" content="">
    <meta name="author" content="">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link href='https://fonts.googleapis.com/css?family=Montserrat' rel='stylesheet'>
    <link href='https://fonts.googleapis.com/css?family=Spectral' rel='stylesheet'>
    <link rel="icon" type="image/png" href="https://i.imgur.com/EcMMVHW.png">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-0evHe/X+R7YkIZDRvuzKMRqM+OrBnVFBL6DOitfPri4tjfHxaWutUpFmBp4vmVor" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-pprn3073KE6tl6bjs2QrFaJGz5/SUsLqktiwsUTF55Jfv3qYSDhgCecCxMW52nD2" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.1.1/css/all.min.css" integrity="sha512-KfkfwYDsLkIlwQp6LFnl8zNdLGxu9YAA1QvwINks4PhcElQSvqcyVLLD9aMhXd13uQjoXtEKNosOWaZqXgel0g==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <title>FTPR:Dataset show</title>
</head>
<body>
<main>
    <div class="modal modal-alert d-block bg-secondary py-5" tabindex="-1" role="dialog" id="modalChoice">
        <div class="modal-dialog" role="document">
            <div class="modal-content rounded-3 shadow">
                <div class="modal-body">
                    <div class="p-4 text-center">
                        <img class="mb-2" src="https://i.imgur.com/EcMMVHW.png" alt="" width="48" height="48">
                        <h4 class="mb-3">Інформація про датасет</h4>

                        <div class="row g-3">
                            <div class="col">
                                <label for="name" class="form-label">Назва</label>
                                <input type="text" class="form-control" id="name" name="name" value="<c:out value="${datasetName}"/>" disabled>
                            </div>

                            <div class="col">
                                <label for="tagName" class="form-label">Тег</label>
                                <input type="text" class="form-control" id="tagName" name="tagName" value="<c:out value="${tagName}"/>" disabled>
                            </div>
                        </div>
                    </div>

                    <div class="p-4 pt-0">
                        <h6 class="text-center">Файли</h6>
                        <ul class="list-group">
                            <c:forEach var="file" items="${files}">
                                <li class="list-group-item"><a target="_blank" href="<c:out value="${file.linkToFile}"/>"><c:out value="${file.name}"/></a></li>
                            </c:forEach>
                        </ul>

                    </div>
                </div>
                <div class="modal-footer d-flex justify-content-center p-0">
                    <a href="/" type="button" class="btn btn-lg btn-link fs-6 text-decoration-none m-0 rounded-0">Вийти</a>
                </div>

            </div>
        </div>
    </div>
</main>
</body>
</html>
