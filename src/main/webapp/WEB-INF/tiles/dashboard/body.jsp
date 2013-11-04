<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>

<fmt:formatDate var="startTimeFormatted" value="${sessionScope.startTime}" type="both" dateStyle="short" timeStyle="short"/>

<h3>Twitter Data
    <small>as of ${startTimeFormatted}</small>
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

<div class="row">
    <div class="col-lg-4">

        <h4>Top Tweeters
            <small>since ${startTimeFormatted}</small>
        </h4>

        <table class="table table-nonfluid table-condensed table-striped">
            <thead>
            <tr>
                <th class="col-lg-2">Screen Name</th>
                <th class="col-lg-1">Count</th>
            </tr>
            </thead>
            <tbody id="topTweets">
            </tbody>
        </table>
    </div>

    <div class="col-lg-8">&nbsp;
    </div>
</div>

<script id="hashTagTemplate" type="text/x-jquery-tmpl">
    <tr>
        <td>\${hashTag}</td>
        <td>\${count}</td>
    </tr>
</script>


<script id="topTweetsTemplate" type="text/x-jquery-tmpl">
    <tr>
        <td>\${screenName}</td>
        <td>\${count}</td>
    </tr>
</script>

<script type="text/javascript">

    $(document).ready(function () {

        getStreamingData('<c:url value="/counts/lastFive"/>', '#last5', "#hashTagTemplate");
        getStreamingData('<c:url value="/counts/lastFifteen"/>', '#last15', "#hashTagTemplate");
        getStreamingData('<c:url value="/counts/lastSixty"/>', '#last60', "#hashTagTemplate");
        getStreamingData('<c:url value="/counts/topTweets"/>', '#topTweets', "#topTweetsTemplate");

    });

    function getStreamingData(url, divId, templateId) {

        var socket = $.atmosphere;

        var request = new $.atmosphere.AtmosphereRequest();
        request.transport = 'websocket';
        request.url = url;
        request.contentType = "application/json";
        request.fallbackTransport = 'streaming';

        request.onMessage = function (response) {
            buildTemplate(response, divId, templateId);
        };

        socket.subscribe(request);


    }

    function buildTemplate(response, divId, templateId) {

        if (response.state = "messageReceived") {

            var data = response.responseBody;

            if (data) {

                try {
                    var result = $.parseJSON(data);

                    console.log("result for divId [" + divId + "]  " + result);

                    $(divId).empty();

                    $(templateId).tmpl(result).appendTo(divId);


                } catch (error) {
                    console.log("An error occurred: " + error);
                }
            } else {
                console.log("response.responseBody is null - ignoring.");
            }
        }
    }


</script>
