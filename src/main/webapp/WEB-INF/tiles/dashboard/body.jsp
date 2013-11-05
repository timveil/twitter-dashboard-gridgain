<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>

<fmt:formatDate var="startTimeFormatted" value="${sessionScope.startTime}" type="both" dateStyle="short" timeStyle="short"/>

<h3>Twitter Dashboard
    <small>as of ${startTimeFormatted}</small>
</h3>

<div class="bs-callout bs-callout-info">
    <p>This demo ingests the <a href="https://dev.twitter.com/docs/api/1.1/get/statuses/sample" target="_blank">Twitter Sample Stream</a>.  This publicly available stream of data represents roughly <strong>%1</strong> of all real-time Twitter activity.  The Twitter Firehose, which includes all activity, is not publicly available.  As a result, this application applies a <strong>multiplier</strong> to data coming in to simulate a significantly faster stream of data.</p>
    <p>In this example, only the last <strong>1,000,000</strong> tweets are stored in the In-Memory Database.</p>
</div>

<hr/>

<div class="row">

    <div class="col-lg-6">

        <h4>Total Tweets <small>processed by GridGain</small></h4>
        <span id="totalCounter" class="counter"></span>

    </div>

    <div class="col-lg-6">

        <h4>Total Tweets <small>with HashTags</small></h4>
        <span id="totalTags" class="counter"></span>

    </div>
</div>

<hr/>

<div class="row">

    <div class="col-lg-3">

        <h4>Top Hashtags
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

    <div class="col-lg-3">

        <h4>Top Hashtags
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

    <div class="col-lg-3">

        <h4>Top Hashtags
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

    <div class="col-lg-3">

        <h4>Top Tweeters</h4>

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

</div>



<script id="hashTagTemplate" type="text/x-jquery-tmpl">
    <tr>
        <td>\${key}</td>
        <td>\${value}</td>
    </tr>
</script>


<script id="topTweetsTemplate" type="text/x-jquery-tmpl">
    <tr>
        <td><a href="https://twitter.com/\${key}" target="_blank">@\${key}</a></td>
        <td>\${value}</td>
    </tr>
</script>

<script type="text/javascript">

    $(document).ready(function () {
        getStreamingData('<c:url value="/counts/lastFive"/>', '#last5', "#hashTagTemplate");
        getStreamingData('<c:url value="/counts/lastFifteen"/>', '#last15', "#hashTagTemplate");
        getStreamingData('<c:url value="/counts/lastSixty"/>', '#last60', "#hashTagTemplate");
        getStreamingData('<c:url value="/counts/topTweets"/>', '#topTweets', "#topTweetsTemplate");
        getStreamingDataSingle('<c:url value="/counts/totalTweets"/>', '#totalCounter');
        getStreamingDataSingle('<c:url value="/counts/tweetsWithHashTag"/>', '#totalTags');
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

    function getStreamingDataSingle(url, divId) {

        var socket = $.atmosphere;

        var request = new $.atmosphere.AtmosphereRequest();
        request.transport = 'websocket';
        request.url = url;
        request.contentType = "application/json";
        request.fallbackTransport = 'streaming';

        request.onMessage = function (response) {
            if (response.state = "messageReceived") {

                var data = response.responseBody;

                if (data) {

                    try {
                        var result = $.parseJSON(data);

                        console.log("result for divId [" + divId + "]  " + result);

                        $(divId).html(result)


                    } catch (error) {
                        console.log("An error occurred: " + error);
                    }
                } else {
                    console.log("response.responseBody is null - ignoring.");
                }
            }
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
