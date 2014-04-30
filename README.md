Twitter Dashboard
=================

About
-----

This demo monitors the [Twitter Sample Stream](https://dev.twitter.com/docs/api/1.1/get/statuses/sample) using GridGain's [In-Memory Computing Platform](http://www.gridgain.org/platform/) product.
This publicly available stream of data represents roughly **%1** of all real-time Twitter activity.
The [Twitter Firehose](https://dev.twitter.com/docs/api/1.1/get/statuses/firehose), which includes all activity, is not publicly available.
As a result, this application applies a **multiplier** to incoming events to simulate a significantly faster stream of data.

In addition to GridGain, this demo is built using a number of popular open source projects including:
* Spring Framework - [http://projects.spring.io/spring-framework]()
* Spring Social Twitter - [http://projects.spring.io/spring-social-twitter]()
* Apache Tiles - [http://tiles.apache.org]()
* Atmosphere - [https://github.com/Atmosphere/atmosphere]()
* Twitter Bootstrap - [http://getbootstrap.com]()
* jQuery - [http://jquery.com]()


Pre Requisites
--------------

The following software and accounts are required to run this demo.  Instructions on installing the JDK, Tomcat and Maven can be found on those respective sites.

1. Latest Oracle JDK (1.7.x)
2. Latest Apache Tomcat (7.x)
3. Latest Apache Maven (3.2.x)
4. A Twitter Developer Account.  See [https://dev.twitter.com/]()

Running the Demo
----------------

Once the project has been cloned or downloaded do the following:

1. Update the protocol for the port 8080 connector in Tomcat's `server.xml`

        <Connector port="8080"
            protocol="org.apache.coyote.http11.Http11NioProtocol"
            connectionTimeout="20000"
            redirectPort="8443" />
2.  Copy the `src/main/resources/twitter-dashboard.properties` file to the top level of your user (home) directory and update the file with your twitter developer credentials.
3.  Navigate to the root folder and execute ```mvn package``` to generate a .war file
4.  Deploy war file to tomcat and start tomcat
5.  Navigate to `http://localhost:8080/ingest` to start capturing twitter data

