<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <!-- Access the bootstrap Css like this,
        Spring boot will handle the resource mapping automcatically -->
    <link rel="stylesheet" type="text/css" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/webjars/font-awesome/5.1.0/css/fontawesome.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/webjars/font-awesome/5.1.0/css/solid.css" rel="stylesheet">

    <script type="text/javascript" src="webjars/popper.js/1.14.4/umd/popper.min.js"></script>
    <!--
	<spring:url value="/css/main.css" var="springCss" />
	<link href="${springCss}" rel="stylesheet" />
	 -->
    <c:url value="/css/main.css" var="jstlCss" />
    <link href="${jstlCss}" rel="stylesheet" />
</head>
<body>

<div class="container ml-5 mr-5">
    <nav class="navbar navbar-expand-lg navbar-light bg-light mb-3">
        <a class="nav-link" href="/"><i class="fas fa-home"></i></a>
        <span class="navbar-brand">Java Memory Leak Test</span>
        <div class="collapse navbar-collapse"></div>

        <form class="form-inline my-2 my-lg-0" action="/memstat">
            <input type="hidden" name="redirect" value="/memalloc_init">
            <button class="btn btn-success my-2 my-sm-0 mr-2" type="submit">Memory status</button>
        </form>

        <form class="form-inline my-2 my-lg-0" action="/threadstat">
            <input type="hidden" name="redirect" value="memalloc_init">
            <button class="btn btn-success my-2 my-sm-0 mr-2" type="submit">Thread status</button>
        </form>

        <form class="form-inline my-2 my-lg-0" action="/gc">
            <input type="hidden" name="gc" value="1">
            <input type="hidden" name="redirect" value="/memalloc_init">
            <button class="btn btn-warning my-2 my-sm-0" type="submit">GC</button>
        </form>
    </nav>

    <div>
    </div>

    <div class="container-fluid">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Allocate memory <span class="badge badge-primary badge-pill"  data-toggle="tooltip" data-placement="right" title="number of memory leaks (and size in MB)">${leakCount} (${leakSize} MB)</span></h5>
                <form id="form" action="/memalloc">
                    <div class="form-group">
                        <label for="mb">MB</label>
                        <input  type="number" id="mb" name="mb" class="form-control" value="${mb}" min="0">
                    </div>
                    <div class="form-group">
                        <label for="txt">Content (random if empty)</label>
                        <input  type="text" id="txt" name="txt" class="form-control" value="${txt}">
                    </div>
                    <div class="form-group form-check">
                        <input class="form-check-input" type="checkbox" id="store" name="store" ${storeChecked}>
                        <label class="form-check-label" for="store" data-toggle="tooltip" data-placement="right">generate memory leak</label>
                    </div>
                    <div class="form-group form-check">
                        <input class="form-check-input" type="checkbox" id="gc" name="gc" ${gcChecked}>
                        <label class="form-check-label" for="gc" data-toggle="tooltip" data-placement="right">GC after allocation</label>
                    </div>

                    <input type="hidden" name="redirect" value="/">
                    <div class="form-row">
                        <div class="col-6">
                            <button id="btnSubmit" type="submit" class="btn btn-primary" onclick="onSubmit()">Submit</button>
                            <button type="button" class="btn btn-danger" onclick="onCancel()">Cancel</button>
                        </div>
                        <div class="col-6 text-right">
                            <div class="form-check mb-2">
                                <input class="form-check-input" type="checkbox" id="stayHere" name="stayHere" ${stayHere}>
                                <label class="form-check-label" for="stayHere">stay here</label>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <div class="container-fluid mt-3">
        <div class="loader mx-auto" style="display: none"></div>
    </div>
</div>

<It allocates specified amount of MB memory by creating 1 KB random strings and adding to a

<script type="text/javascript" src="webjars/popper.js/1.14.4/umd/popper.min.js"></script>
<script type="text/javascript" src="webjars/jquery/3.3.1/jquery.js"></script>
<script type="text/javascript" src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>

<script type="text/javascript">
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })

    function onSubmit() {
        $('#form').submit();
        $('#btnSubmit').attr('disabled', true);
        $('.loader').show();
    }

    function onCancel() {
        location.href = '/';
    }
</script>

</body>

</html>
