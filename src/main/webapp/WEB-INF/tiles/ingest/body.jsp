<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>


<h3>Configure Ingest</h3>

<form class="form-horizontal" role="form" action="<c:url value="/ingest"/>" method="post">
    <div class="form-group">
        <label for="duration" class="col-lg-2 control-label">Duration</label>

        <div class="col-lg-4">
            <input type="number" class="form-control" id="duration" name="duration" placeholder="Duration of capture in minutes">
        </div>
    </div>
    <div class="form-group">
        <div class="col-lg-offset-2 col-lg-10">
            <button type="submit" class="btn btn-success">Capture Twitter Data</button>
        </div>
    </div>
</form>

