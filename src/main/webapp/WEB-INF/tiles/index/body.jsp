<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>

<form class="form-horizontal" role="form" action="<c:url value="/start"/>" method="post">
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

<table class="table-striped table-bordered">
    <thead>
    <tr>
        <th width="800">Twitter Messages</th>
    </tr>
    </thead>
    <tbody id="twitterMessages">
    <tr id="placeHolder">
        <td>Searching...</td>
    </tr>
    </tbody>
</table>


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

    request.onMessage = function(response){
        buildTemplate(response);
    };

    var subSocket = socket.subscribe(request);

    function buildTemplate(response){

        if(response.state = "messageReceived"){

            var data = response.responseBody;

            if (data) {

                try {
                    var result =  $.parseJSON(data);

                    console.log(result);

                    $( "#template" ).tmpl( result ).hide().prependTo( "#twitterMessages").fadeIn();

                } catch (error) {
                    console.log("An error ocurred: " + error);
                }
            } else {
                console.log("response.responseBody is null - ignoring.");
            }
        }
    }

</script>
