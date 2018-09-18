This is a simple demo of [JASPIC](https://jcp.org/en/jsr/detail?id=196) (JSR 196)]

JASPIC stands for *Java Authentication Service Provider Interface* and seeks to define a standard interface 
by which authentication modules may be integrated with containers 
and such that these modules may establish the authentication identities used by containers.

Concretly it defines how an athenticated module is integrated into a Java EE container. 
It is part of the platform since Java EE 6 (2009).

Othe security-related specifications are:

* [JAAS](): *Java Authentication and Authorization Service* which is part of Java SE since J2SDK 1.4 (2002). 
Among other things, JAAS defines the core concepts of Java Security: Subject (such as a person, an entity, a service), Principal (the id of subject: such as a name, a SSN) and Credential (security-related attributes of a subject).
* [JACC](https://jcp.org/en/jsr/detail?id=115) (JSR 115): *Java Authorization Contract for Containers* which seeks 
to define a contract between containers and authorization service providers 
that will result in the implementation of providers for use by containers
* more recently [Java EE Security]() (JSR 375): the goal of this JSR is to improve the Java EE platform by ensuring the 
Security API aspect is useful in the modern cloud/PaaS application paradigm

This demo project is inspired by a DZOne article [Using JASPIC to Secure a Web Application in GlassFish](https://dzone.com/articles/using-jaspic-secure-web).

Thanks it his author Andy Overton!

It is is made of 2 Maven modules:

1. JaspicSamDemo: a secured access module made of one class implementing the ServerAuthModule interface. It is deliberatly a very basic implementation with no delegation to any real authentication mechanism,
1. JaspicAppDemo: a basic secured application made of one Servlet and one EJB.

It has been tested with Java 8 and Payara 5.183 and should work with Glassfish 5.

How to:

1. Generate JaspciSamDemo.jar and copy it to the lib directory of you application server by running maven install (do not forget to adjust payara.home property in your pom.xml)
1. Restart Payara (or Glassfish),
1. Configure it as a secure module using the Admin Console,
1. Deploy the JaspicAppDemo and test it using URL such as:

    * http://localhost:8080/JaspicAppDemo/SecuredServlet?user=jefrajames&group=admin
    * http://localhost:8080/JaspicAppDemo/SecuredServlet?user=jefrajames&group=standard
    * http://localhost:8080/JaspicAppDemo/SecuredServlet?user=who&group=other
