<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>

<h3>Administration</h3>

<div class="bs-callout bs-callout-warning">
    <h4>Be Careful!</h4>
    <p>The Twitter Stream will remain open for fixed period of time as specified by the lifespan entered below.  Given the amount of data that will be consumed, take care to ensure that your server configuration can handle the load.</p>
</div>

<form class="form-horizontal" role="form" action="<c:url value="/ingest"/>" method="post">
    <div class="form-group">
        <label for="duration" class="col-lg-2 control-label">Twitter Stream Lifespan</label>

        <div class="col-lg-4">
            <input type="number" min="0" class="form-control" id="duration" name="duration" placeholder="Lifespan of Twitter Stream in minutes ">
        </div>
    </div>
    <div class="form-group">
        <div class="col-lg-offset-2 col-lg-10">
            <button type="submit" class="btn btn-primary">Capture Twitter Data</button>
            <button type="reset" class="btn btn-default">Reset</button>
        </div>
    </div>
</form>

