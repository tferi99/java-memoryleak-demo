<%@ page import="org.ftoth.javamemoryleakdemo.util.SystemUtil" %>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%
    String processInfo = SystemUtil.getProcessInfo();
%>

<html lang="en">
<head>
    <!-- Access the bootstrap Css like this,
        Spring boot will handle the resource mapping automcatically -->
    <link rel="stylesheet" type="text/css" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/webjars/font-awesome/5.1.0/css/fontawesome.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/webjars/font-awesome/5.1.0/css/solid.css" rel="stylesheet">

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
        <span class="navbar-brand">Java Memory Leak Test - <%=processInfo%></span>
        <div class="collapse navbar-collapse"></div>

        <form class="form-inline my-2 my-lg-0" action="/stat">
            <input type="hidden" name="redirect" value="/openstream_init">
            <button class="btn btn-success my-2 my-sm-0 mr-2" type="submit">Status</button>
        </form>

        <form class="form-inline my-2 my-lg-0" action="/gc">
            <input type="hidden" name="gc" value="1">
            <input type="hidden" name="redirect" value="/openstream_init">
            <button class="btn btn-warning my-2 my-sm-0" type="submit">GC</button>
        </form>
    </nav>

    <div>
    </div>

    <div class="container-fluid">
        <h3>Streams</h3>
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Open a stream but don't close <span class="badge badge-primary badge-pill"  data-toggle="tooltip" data-placement="right" title="number of memory leaks">${openStreamCount} + ${leakCount} (${leakSize} MB)</span></h5>
                <form id="form" action="/openstream">
                    <div class="form-group form-check">
                        <input class="form-check-input" type="checkbox" id="store" name="store" ${storeChecked}>
                        <label class="form-check-label" for="store" data-toggle="tooltip" data-placement="right" title="to generate extra memory leak">store to static, too</label>
                    </div>
                    <div class="form-group form-check">
                        <input class="form-check-input" type="checkbox" id="gc" name="gc" ${gcChecked}>
                        <label class="form-check-label" for="gc" data-toggle="tooltip" data-placement="right" title="GC after creating memory leak">GC</label>
                    </div>

                    <input type="hidden" name="redirect" value="/">
                    <button id="btnSubmit" type="submit" class="btn btn-primary" onclick="onSubmit()">Submit</button>
                    <button type="button" class="btn btn-danger" onclick="onCancel()">Cancel</button>
                </form>
            </div>
        </div>
    </div>

    <div class="container-fluid mt-3">
        <div class="loader mx-auto" style="display: none"></div>
    </div>
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

    function onCancel() {
        location.href = '/';
    }
</script>

</body>

</html>
