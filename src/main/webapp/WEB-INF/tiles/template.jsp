<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <tiles:insertAttribute name="meta"/>

    <style>
            /* Sticky footer styles
       -------------------------------------------------- */

        html,
        body {
            height: 100%;
            /* The html and body elements cannot have any padding or margin. */
        }

            /* Wrapper for page content to push down footer */
        #wrap {
            min-height: 100%;
            height: auto !important;
            height: 100%;
            /* Negative indent footer by its height */
            margin: 0 auto -60px;
            /* Pad bottom by footer height */
            padding: 0 0 60px;
        }

            /* Set the fixed height of the footer here */
        #footer {
            height: 60px;
            background-color: #f5f5f5;
        }


            /* Custom page CSS
            -------------------------------------------------- */
            /* Not required for template or sticky footer method. */

        #wrap > .container {
            padding: 60px 15px 0;
        }
        .container .credit {
            margin: 20px 0;
        }

        #footer > .container {
            padding-left: 15px;
            padding-right: 15px;
        }

        code {
            font-size: 80%;
        }
    </style>
</head>
<body>

<div id="wrap">

    <tiles:insertAttribute name="header"/>

    <div class="container">
        <div class="page-header">
            <h1>some title</h1>
        </div>
        <tiles:insertAttribute name="body"/>
    </div>


</div>



<div id="footer">
    <div class="container">
        <tiles:insertAttribute name="footer"/>
    </div>
</div>


<script src="//code.jquery.com/jquery.js"></script>

<!-- Latest compiled and minified JavaScript -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.0/js/bootstrap.min.js"></script>

</body>
</html>