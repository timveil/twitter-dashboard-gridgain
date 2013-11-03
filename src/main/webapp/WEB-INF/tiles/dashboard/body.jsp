<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>

<div class="row">

    <div class="col-mid-4">

        <h3>Popular Hashtags
            <small>last 5 minutes</small>
        </h3>

        <table class="table table-nonfluid table-condensed table-striped">
            <thead>
            <tr>
                <th class="col-mid-2">HashTag</th>
                <th class="col-mid-1">Count</th>
            </tr>
            </thead>
            <tbody id="last5">
            </tbody>
        </table>
    </div>

    <div class="col-mid-4">

        <h3>Popular Hashtags
            <small>last 10 minutes</small>
        </h3>

        <table class="table table-nonfluid table-condensed table-striped">
            <thead>
            <tr>
                <th class="col-mid-2">HashTag</th>
                <th class="col-mid-1">Count</th>
            </tr>
            </thead>
            <tbody id="last10">
            </tbody>
        </table>
    </div>

    <div class="col-mid-4">

        <h3>Popular Hashtags
            <small>last 60 minutes</small>
        </h3>

        <table class="table table-nonfluid table-condensed table-striped">
            <thead>
            <tr>
                <th class="col-mid-2">HashTag</th>
                <th class="col-mid-1">Count</th>
            </tr>
            </thead>
            <tbody id="last60">
            </tbody>
        </table>
    </div>

</div>


<script id="template" type="text/x-jquery-tmpl">
    <tr>
        <td>\${hashtag}</td>
        <td>\${count}</td>
    </tr>
</script>

<script type="text/javascript">
    var socket = $.atmosphere;

    var request = new $.atmosphere.AtmosphereRequest();
    request.transport = 'websocket';
    request.url = '<c:url value="/twitter/concurrency"/>';
    request.contentType = "application/json";
    request.fallbackTransport = 'streaming';

    request.onMessage = function (response) {
        buildTemplate(response);
    };

    var subSocket = socket.subscribe(request);

    function buildTemplate(response) {

        if (response.state = "messageReceived") {

            var data = response.responseBody;

            if (data) {

                try {
                    var result = $.parseJSON(data);

                    console.log(result);

                    $("#last5").empty();

                    $("#template").tmpl(result).appendTo("#last5");


                } catch (error) {
                    console.log("An error ocurred: " + error);
                }
            } else {
                console.log("response.responseBody is null - ignoring.");
            }
        }
    }

</script>
