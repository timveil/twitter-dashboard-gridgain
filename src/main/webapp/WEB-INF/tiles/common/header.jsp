<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>

<tiles:importAttribute/>

<div class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="http://www.gridgain.com" target="_blank"><img src="<c:url value="/static/image/gridgain_cube_48x48.png"/>"/> GridGain Twitter Demo</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li <c:if test="${navigation == 0}">class="active"</c:if>> <a href="<c:url value="/dashboard"/>">Dashboard</a></li>
                <li <c:if test="${navigation == 1}">class="active"</c:if>><a href="<c:url value="/search"/>">Search</a></li>
                <li <c:if test="${navigation == 2}">class="active"</c:if>><a href="<c:url value="/ingest"/>">Admin</a></li>
            </ul>
        </div>
        <!--/.nav-collapse -->
    </div>
</div>
