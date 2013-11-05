<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>


<h3>Search Tweet Database</h3>

<form class="form-horizontal" role="form" action="<c:url value="/search"/>" method="post">
    <div class="form-group">
        <label for="text" class="col-lg-2 control-label">Tweet</label>

        <div class="col-lg-4">
            <input type="text" class="form-control" id="text" name="text" placeholder="Search for tweets containing this text">
        </div>
    </div>

    <div class="form-group">
        <label for="screenName" class="col-lg-2 control-label">Twitter User</label>

        <div class="col-lg-4">
            <input type="screenName" class="form-control" id="screenName" name="screenName" placeholder="Search for tweets originating from this user">
        </div>
    </div>
    <div class="form-group">
        <div class="col-lg-offset-2 col-lg-10">
            <button type="submit" class="btn btn-primary">Search</button>
            <button type="reset" class="btn btn-default">Reset</button>
        </div>
    </div>
</form>

<c:if test="${! empty tweets}">


    <table class="table table-striped">
        <thead>
        <tr>
            <th>Twitter User</th>
            <th>Tweeted On</th>
            <th>Tweet</th>
        </tr>
        </thead>
        <tbod>


            <c:forEach var="tweet" items="${tweets}">
            <tr>
                <td style="white-space: nowrap">${tweet.screenName}</td>
                <td style="white-space: nowrap"><fmt:formatDate value="${tweet.createdAt}" dateStyle="short" timeStyle="short" type="both"/></td>
                <td>${tweet.text}</td>
            </tr>
            </c:forEach>
            </tbody>
    </table>

</c:if>

