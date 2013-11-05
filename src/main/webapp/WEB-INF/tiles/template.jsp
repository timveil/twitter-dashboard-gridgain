<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="/WEB-INF/tiles/common/taglibs.jsp" %>

<tiles:importAttribute/>

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

        .table-nonfluid {
            width: auto;
        }

        .counter {
            font-size: 200%;
            font-weight: bold;
            color: #CCC;
        }

        .navbar-brand {
            padding-left: 15px;
            padding-right: 15px;
            padding-top: 5px;
            padding-bottom: 5px;
        }


            /*
             * Callouts
             *
             * Not quite alerts, but custom and helpful notes for folks reading the docs.
             * Requires a base and modifier class.
             */

            /* Common styles for all types */
        .bs-callout {
            margin: 20px 0;
            padding: 20px;
            border-left: 3px solid #eee;
        }
        .bs-callout h4 {
            margin-top: 0;
            margin-bottom: 5px;
        }
        .bs-callout p:last-child {
            margin-bottom: 0;
        }

            /* Variations */
        .bs-callout-danger {
            background-color: #fdf7f7;
            border-color: #eed3d7;
        }
        .bs-callout-danger h4 {
            color: #b94a48;
        }
        .bs-callout-warning {
            background-color: #faf8f0;
            border-color: #faebcc;
        }
        .bs-callout-warning h4 {
            color: #c09853;
        }
        .bs-callout-info {
            background-color: #f4f8fa;
            border-color: #bce8f1;
        }
        .bs-callout-info h4 {
            color: #3a87ad;
        }

    </style>
</head>
<body>

<div id="wrap">

    <tiles:insertAttribute name="header"/>

    <div class="container">
        <c:if test="${! empty title}">
            <div class="page-header">
                <h1>${title}</h1>
            </div>
        </c:if>
        <tiles:insertAttribute name="body"/>
    </div>


</div>


<div id="footer">
    <div class="container">
        <tiles:insertAttribute name="footer"/>
    </div>
</div>


</body>
</html>