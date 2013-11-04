<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>

<h3>Twitter Data
    <small>as of <fmt:formatDate value="${sessionScope.startTime}" type="both" dateStyle="short" timeStyle="long"/></small>
</h3>

<div class="row">

    <div class="col-lg-4">

        <h4>Popular Hashtags
            <small>last 5 minutes</small>
        </h4>

        <table class="table table-nonfluid table-condensed table-striped">
            <thead>
            <tr>
                <th class="col-lg-2">HashTag</th>
                <th class="col-lg-1">Count</th>
            </tr>
            </thead>
            <tbody id="last5">
            </tbody>
        </table>
    </div>

    <div class="col-lg-4">

        <h4>Popular Hashtags
            <small>last 15 minutes</small>
        </h4>

        <table class="table table-nonfluid table-condensed table-striped">
            <thead>
            <tr>
                <th class="col-lg-2">HashTag</th>
                <th class="col-lg-1">Count</th>
            </tr>
            </thead>
            <tbody id="last15">
            </tbody>
        </table>
    </div>

    <div class="col-lg-4">

        <h4>Popular Hashtags
            <small>last 60 minutes</small>
        </h4>

        <table class="table table-nonfluid table-condensed table-striped">
            <thead>
            <tr>
                <th class="col-lg-2">HashTag</th>
                <th class="col-lg-1">Count</th>
            </tr>
            </thead>
            <tbody id="last60">
            </tbody>
        </table>
    </div>

</div>


<script id="template" type="text/x-jquery-tmpl">
    <tr>
        <td>\${hashTag}</td>
        <td>\${count}</td>
    </tr>
</script>

<script type="text/javascript">

    $(document).ready(function () {

        getStreamingData('<c:url value="/counts/lastFive"/>', '#last5');
        getStreamingData('<c:url value="/counts/lastFifteen"/>', '#last15');
        getStreamingData('<c:url value="/counts/lastSixty"/>', '#last60');

    });

    function getStreamingData(url, divId) {

        var socket = $.atmosphere;

        var request = new $.atmosphere.AtmosphereRequest();
        request.transport = 'websocket';
        request.url = url;
        request.contentType = "application/json";
        request.fallbackTransport = 'streaming';

        request.onMessage = function (response) {
            buildTemplate(response, divId);
        };

        socket.subscribe(request);


    }

    function buildTemplate(response, divId) {

        if (response.state = "messageReceived") {

            var data = response.responseBody;

            if (data) {

                try {
                    var result = $.parseJSON(data);

                    console.log("result for divId [" + divId + "]  " + result);

                    $(divId).empty();

                    $("#template").tmpl(result).appendTo(divId);


                } catch (error) {
                    console.log("An error occurred: " + error);
                }
            } else {
                console.log("response.responseBody is null - ignoring.");
            }
        }
    }


</script>
