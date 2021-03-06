<%@ page import="org.ftoth.javamemoryleakdemo.controller.JspController" %>
<%@ page import="org.ftoth.javamemoryleakdemo.util.SystemUtil" %>
<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>


<%
    int refreshSecs = 5;
    String refreshHead = JspController.createRefreshHeadCode(request, response, refreshSecs);
    String refreshBody = JspController.createRefreshBodyCode(request, response, refreshSecs);

    String processInfo = SystemUtil.getProcessInfo();
    String jvmVersion = SystemUtil.getJvmVersion();
%>

<html lang="en">
<head>

    <!-- Access the bootstrap Css like this,
        Spring boot will handle the resource mapping automcatically -->
    <link rel="stylesheet" type="text/css" href="webjars/bootstrap/4.1.3/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="/webjars/font-awesome/5.1.0/css/fontawesome.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="/webjars/font-awesome/5.1.0/css/solid.css" rel="stylesheet">
    <!--
    <link rel="stylesheet" type="text/css" href="/webjars/jquery/3.3.1/jquery.min.js" rel="stylesheet">
    -->

    <!--
	<spring:url value="/css/main.css" var="springCss" />
	<link href="${springCss}" rel="stylesheet" />
	 -->
    <c:url value="/css/main.css" var="jstlCss" />
    <link href="${jstlCss}" rel="stylesheet" />

    <%=refreshHead%>
</head>
<body>

<div class="container ml-5 mr-5">

    <nav class="navbar navbar-expand-lg navbar-light bg-light mb-3">
        <span class="navbar-brand">Java Memory Leak Test - <%=processInfo%></span>
        <div class="collapse navbar-collapse"></div>

        <form class="form-inline my-2 my-lg-0" action="/stat">
            <input type="hidden" name="redirect" value="/">
            <button class="btn btn-success my-2 my-sm-0 mr-2" type="submit">Status</button>
        </form>

        <form class="form-inline my-2 my-lg-0" action="/gc">
            <input type="hidden" name="gc" value="1">
            <input type="hidden" name="redirect" value="/">
            <button class="btn btn-warning my-2 my-sm-0" type="submit">GC</button>
        </form>
    </nav>

    <%=refreshBody%>

    <ul class="list-group">
        <li class="list-group-item d-flex justify-content-between align-items-center">
            <a href="/memalloc_init">Allocate/free memory</a>
            <span class="badge badge-primary badge-pill" data-toggle="tooltip" data-placement="right" title="number of memory leaks (and size in MB)">${memAllocLeakCount} (${memAllocLeakSize} MB)</span>
        </li>
        <li class="list-group-item d-flex justify-content-between align-items-center">
            <a href="/openstream_init">Unclosed stream</a>
            <span class="badge badge-primary badge-pill" data-toggle="tooltip" data-placement="right" title="number of memory leaks">${streamCount} + ${streamLeakCount} (${streamLeakSize}MB)</span>
        </li>
        <li class="list-group-item d-flex justify-content-between align-items-center">
            <a href="/thread_init">Threads</a>
            <span class="badge badge-primary badge-pill" data-toggle="tooltip" data-placement="right" title="number of threads/allocated memory">${threadCount} (${threadLeakSize}MB)</span>
        </li>
        <!--
        <li class="list-group-item d-flex justify-content-between align-items-center">
            <a href="/openconn_init">Unclosed connections</a>
            <span class="badge badge-primary badge-pill" data-toggle="tooltip" data-placement="right" title="number of memory leaks">${streamCount} + ${streamLeakCount} (${streamLeakSize} MB)</span>
        </li>
        -->
    </ul>

    <h4 class="mt-5">Environment</h4>
    <table class="table">
        <thead>
            <tr>
                <th>Name</th>
                <th>Value</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>JVM</td>
                <td><%=jvmVersion%></td>
            </tr>
        </tbody>
    </table>

</div>

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
