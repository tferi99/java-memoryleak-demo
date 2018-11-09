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
    <div class="collapse navbar-collapse"></div>
    <form class="form-inline my-2 my-lg-0" action="/gc">
        <input type="hidden" name="gc" value="1">
        <input type="hidden" name="redirect" value="/">
        <button class="btn btn-primary my-2 my-sm-0" type="submit">GC</button>
    </form>
</nav>

<ul class="list-group">
    <li class="list-group-item d-flex justify-content-between align-items-center">
        <a href="/memalloc_init">Allocate memory</a>
        <span class="badge badge-primary badge-pill" data-toggle="tooltip" data-placement="right" title="number of memory leaks (and size in MB)">${memAllocLeakCount} (${memAllocLeakSize} MB)</span>
    </li>
    <li class="list-group-item d-flex justify-content-between align-items-center">
        <a href="/openstream_init">Unclosed stream</a>
        <span class="badge badge-primary badge-pill" data-toggle="tooltip" data-placement="right" title="number of memory leaks">${streamCount} + ${streamLeakCount} (${streamLeakSize}MB)</span>
    </li>
    <!--
    <li class="list-group-item d-flex justify-content-between align-items-center">
        <a href="/openconn_init">Unclosed connections</a>
        <span class="badge badge-primary badge-pill" data-toggle="tooltip" data-placement="right" title="number of memory leaks">${streamCount} + ${streamLeakCount} (${streamLeakSize} MB)</span>
    </li>
    -->
</ul>

<script type="text/javascript" src="webjars/popper.js/1.14.4/umd/popper.min.js"></script>
<script type="text/javascript" src="webjars/jquery/3.3.1/jquery.js"></script>
<script type="text/javascript" src="webjars/bootstrap/4.1.3/js/bootstrap.min.js"></script>

<script type="text/javascript">
    $(function () {
        $('[data-toggle="tooltip"]').tooltip()
    })
</script>


</body>

</html>
