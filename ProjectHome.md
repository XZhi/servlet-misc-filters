# Introduction #
This project is **intended** to offer some more or less common Java Servlet filters to do some typical tasks as session management, logging, validations and such using Servlet API 3.0.

Think of it as a library with simple classes which are just plain boring to write when you have to work with Java web-apps. That way you can focus on the real thing.

If I recieve enough feedback (or even help!) the project could be extended to include some less common type of filters. Just no warranties!

On 10th October 2012 the project contains following working filters:

**LoggingFilter:** This is a classic logging filter for your application. It can log every request's parameter, session information associated with incoming requests, cookies and other miscellaneous information. It can be configured to log only the information you need.

**CookieCheckFilter:** This filter checks incoming request's cookie list and compares against a preconfigured list of valid cookies. If any cookie is missing (or there's more cookies than expected) the filter can be told to log the event and let the request go or redirect automatically to any page you want.

**NoCacheFilter:** This filter will allow you to set the no-cache header to every incoming request. Useful when working with AJAX requests.

**SessionFixationFilter:** This filter will detect any attempt to hijack any user's session using the 'session fixation' attack pattern. It has various levels of tolerance to suspicious behavior, allowing certain behaviors if needed (for example allow session id to be passed as query string).

The project contains some other WIP filters:

**CompressionFilter:**

**CrossSiteScriptingFilter:**

**MaintenanceFilter:**

### Notes ###
At this moment the project is not ready for download, so for now you can only do SVN checkouts.

Please note that the full SVN source code contains an Eclipse project with some junk I'm using for testing purposes, not just the 'good stuff'. Ignore those if you do a full checkout.

Also note that the project runs under Apache Tomcat 7 and it's configured to use servlet API 3.0, so no pre-7 Tomcats for running the filters. I guess some parts of the project may work on older versions but some other won't.