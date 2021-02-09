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
        <span class="navbar-brand">Thread Test</span>
        <div class="collapse navbar-collapse"></div>

        <form class="form-inline my-2 my-lg-0" action="/memstat">
            <input type="hidden" name="redirect" value="/thread_init">
            <button class="btn btn-success my-2 my-sm-0 mr-2" type="submit">Memory status</button>
        </form>

        <form class="form-inline my-2 my-lg-0" action="/threadstat">
            <input type="hidden" name="redirect" value="/thread_init">
            <button class="btn btn-success my-2 my-sm-0 mr-2" type="submit">Thread status</button>
        </form>

        <form class="form-inline my-2 my-lg-0" action="/gc">
            <input type="hidden" name="gc" value="1">
            <input type="hidden" name="redirect" value="/thread_init">
            <button class="btn btn-warning my-2 my-sm-0" type="submit">GC</button>
        </form>
    </nav>

    <div>
    </div>

    <div class="container-fluid">
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Start thread(s) <span class="badge badge-primary badge-pill" data-toggle="tooltip" data-placement="right" title="number of threds (and allocated size in MB)">${threadCountTotal} (${threadAllocMbTotal} MB)</span></h5>
                <form id="form" action="/thread">
                    <div class="form-row">
                        <div class="form-group col-md-4">
                            <label for="threadCount">Threads</label>
                            <input type="number" id="threadCount" name="threadCount" class="form-control" value="${threadCount}"  min="1">
                        </div>
                        <div class="form-group col-md-4">
                            <label for="mb">MB</label>
                            <input type="number"id="mb" name="mb" class="form-control" value="${mb}" min="0">
                        </div>
                        <div class="form-group col-md-4">
                            <label for="threadMaxAgeSecs">Max age (secs)</label>
                            <input type="number"id="threadMaxAgeSecs" name="threadMaxAgeSecs" class="form-control" value="${threadMaxAgeSecs}" min="0">
                        </div>
                    </div>
                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="waitMsecs">With msecs before allocating next 1 MB</label>
                            <input type="number" id="waitMsecs" name="waitMsecs" class="form-control" value="${waitMsecs}"  min="1">
                        </div>
                        <div class="form-group col-md-6">
                            <label for="txt">Content (random if empty)</label>
                            <input  type="text" id="txt" name="txt" class="form-control" value="${txt}">
                        </div>
                    </div>
                    <div >
                        <span style="color: red">${error}</span>
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

        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Thread memory release lock</h5>

                <label class="switch">
                    <input type="checkbox" id="releaseLock" ${memReleaseLock}>
                    <span class="slider round"></span>
                </label>
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
        $('[data-toggle="tooltip"]').tooltip();

        $('#releaseLock').change(function() {
            setLocked(this.checked);
        });
    })

    function setLocked(locked) {
        console.log("########## CHANGE: " + locked);

        $.ajax({
            type: 'GET',
            url: '/threadmemlocked',
            data: {'locked': locked},
            success: function (result) {
                console.log('threadmemlocked - ok');
            },
            error: function (result) {
                console.error('threadmemlocked - error');
            }
        });
    }

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
