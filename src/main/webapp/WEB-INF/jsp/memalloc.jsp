<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
    <!-- Access the bootstrap Css like this,
        Spring boot will handle the resource mapping automcatically -->
    <link rel="stylesheet" type="text/css" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" />

    <!--
	<spring:url value="/css/main.css" var="springCss" />
	<link href="${springCss}" rel="stylesheet" />
	 -->
    <c:url value="/css/main.css" var="jstlCss" />
    <link href="${jstlCss}" rel="stylesheet" />
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light mb-3">
    <span class="navbar-brand">Java Memory Leak Test</span>
    <a class="nav-link" href="/">Home</a>
</nav>

<div>
</div>

<div class="container-fluid">
    <div class="card">
        <div class="card-body">
            <h5 class="card-title">Loading to static/singleton <span class="badge badge-primary badge-pill"  data-toggle="tooltip" data-placement="right" title="number of memory leaks (and size in MB)">${leakCount} (${leakSize})</span></h5>
            <form id="form" action="/memalloc">
                <div class="form-group">
                    <label for="mb">MB</label>
                    <input id="mb" name="mb" class="form-control" value="${mb}">
                </div>
                <div class="form-group form-check">
                    <input class="form-check-input" type="checkbox" id="store" name="store" ${storeChecked}>
                    <label class="form-check-label" for="store" data-toggle="tooltip" data-placement="right" title="to generate memory leak">store to static</label>
                </div>
                <div class="form-group form-check">
                    <input class="form-check-input" type="checkbox" id="gc" name="gc" ${gcChecked}>
                    <label class="form-check-label" for="gc" data-toggle="tooltip" data-placement="right" title="GC after creating memory leak">GC</label>
                </div>
                <button id="btnSubmit" type="submit" class="btn btn-primary" onclick="onSubmit()">Submit</button>
            </form>
        </div>
    </div>
</div>

<div class="container-fluid mt-3">
    <div class="loader mx-auto" style="display: none"></div>
</div>


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
</script>

</body>

</html>
