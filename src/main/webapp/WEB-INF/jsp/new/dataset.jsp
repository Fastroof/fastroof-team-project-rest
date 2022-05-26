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
    <title>FTPR:Dataset</title>
    <style>
        .rounded-bottom-right-3 {
            border-radius: 0;
            border-bottom-right-radius: var(--bs-border-radius-lg) !important;
        }
        .plus-file {
            flex-shrink: 0;
        }
        .plus-file:hover {
            opacity: 0.8;
            cursor: pointer;
        }
        .delete-file {
            flex-shrink: 0;
        }
        .delete-file:hover {
            opacity: 0.8;
            cursor: pointer;
        }
    </style>
</head>
<body>
<main>
    <form class="needs-validation" method="post" action="/new/dataset" modelAttribute="datasetFormData" enctype="multipart/form-data">
        <div class="modal modal-alert d-block bg-secondary py-5" tabindex="-1" role="dialog" id="modalChoice">
            <div class="modal-dialog" role="document">
                <div class="modal-content rounded-3 shadow">
                    <div class="modal-body">
                        <div class="p-4 text-center">
                            <img class="mb-2" src="https://i.imgur.com/EcMMVHW.png" alt="" width="48" height="48">
                            <h4 class="mb-3">Створення датасету</h4>

                            <div class="row g-3">
                                <div class="col">
                                    <label for="name" class="form-label">Назва</label>
                                    <input type="text" class="form-control" id="name" name="name" required>
                                </div>

                                <div class="col">
                                    <label for="tagName" class="form-label">Тег</label>
                                    <select class="form-select" id="tagName" name="tagName">
                                        <option value="-">-</option>
                                        <c:forEach var="tagName" items="${tagsNames}" varStatus="loop">
                                            <option value="<c:out value="${tagName}"/>"><c:out value="${tagName}"/></option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>

                        <div class="p-4 pt-0">
                            <h6 class="text-center">Файли</h6>

                            <div id="file-add" class="plus-file d-flex justify-content-center">
                                <span class="fa-stack fa-1x mt-1 plus-file">
                                    <i class="fas fa-square fa-stack-2x text-success"></i>
                                    <i class="fas fa-plus fa-stack-1x fa-inverse"></i>
                                </span>
                            </div>

                        </div>
                    </div>
                    <div class="modal-footer flex-nowrap p-0">
                        <a href="/" type="button" class="btn btn-lg btn-link fs-6 text-decoration-none col-6 m-0 rounded-0" data-bs-dismiss="modal">Вийти</a>
                        <button type="submit" class="btn btn-primary btn-lg fs-6 col-6 m-0 rounded-bottom-right-3 border-start"><strong>Створити</strong></button>
                    </div>

                </div>
            </div>
        </div>
    </form>
</main>

<script src="https://code.jquery.com/jquery-3.6.0.min.js" integrity="sha256-/xUj+3OJU5yExlq6GSYGSHk7tPXikynS7ogEvDej/m4=" crossorigin="anonymous"></script>
<script type="text/javascript">

    jQuery(function() {
        $("#file-add").on("click", () => {
            if ($('input[id^="file-in"]').length === 0) {
                addFileDiv();
            } else {
                const fileIn = $('input[id^="file-in"]:last');
                if (fileIn.val() !== "") {
                    addFileDiv();
                } else {
                    fileIn.css("background-color", "red");
                }
            }
        });

        function addFileDiv() {
            $("#file-add").before(
                '<div id="file-div" class="d-flex align-items-center">'+
                '    <input id="file-in" name="fileIn" class="mt-1 form-control" type="file" required>'+
                '    <span id="file-delete" class="fa-stack fa-1x mt-1 delete-file">'+
                '        <i class="fas fa-square fa-stack-2x text-dark"></i>'+
                '        <i class="fas fa-minus fa-stack-1x fa-inverse"></i>'+
                '    </span>'+
                '</div>'
            );
            $('span[id*="file-delete"]:last').on("click", function () {
                $(this).parent("#file-div").remove();
            });
            $('input[id*="file-in"]:last').on("change", function () {
                if($(this).prop('files')[0].size > 10485760){
                    alert("Файл завеликий! Максимум 10MB");
                    $(this).val("");
                } else {
                    $(this).css("background-color", "");
                }
            });
        }

    });
</script>
</body>
</html>
