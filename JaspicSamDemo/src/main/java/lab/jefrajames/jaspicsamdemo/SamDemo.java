/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab.jefrajames.jaspicsamdemo;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Simple JASPIC Security Access Module. 
 * 
 * The SAM is invoked indirectly by the message processing runtime at the validateRequest and secureResponse interaction points.
 * 
 * The resulted JAR file containing this file should be copied in {payara.home}/glassfish/lib.
 * 
 * The SAM must also be configured as HttpServlet Message Security Provider using the Glassfish Admin Console.
 *
 * @author jefrajames
 */
public class SamDemo implements ServerAuthModule {

    // Acts as a glue between the application and the underlying security services
    // Application-dependant: pop-up window, alternate source ...
    private CallbackHandler callbackHandler;

    private static final Class[] SUPPORTED_MESSAGE_TYPES = new Class[]{HttpServletRequest.class, HttpServletResponse.class};

    @Override
    public void initialize(MessagePolicy mp, MessagePolicy mp1, CallbackHandler ch, Map map) throws AuthException {
        System.out.println("JASPIC AuthModule, initialized called");
        this.callbackHandler = ch;
    }

    @Override
    public Class[] getSupportedMessageTypes() {
        System.out.println("JASPIC AuthModule, getSupportedMessageTypes called");
        return SUPPORTED_MESSAGE_TYPES;
    }

    /**
     * This method doesn't actually do any authentication! It simply takes the
     * user and group, creates callback classes from them and passes them to the
     * callback handler.
     *
     * @param user
     * @param group
     * @param clientSubject
     * @param serverSubject
     */
    private void authenticateUser(String user, String group, Subject clientSubject, Subject serverSubject) {
        System.out.println("JASPIC AuthModule, authenticate internal method called");
        System.out.println("JASPIC AuthModule, listing clientSubject principals: ");
        clientSubject.getPrincipals().stream().forEach(System.out::println);

        // Callback for setting the container's caller (or Remote user) principal.
        // Subject: represents a grouping of related information for a single entity, such as a person
        // Principal: represents the identity of a Subject
        // Subjects may potentially have multiple identities. Each identity is represented as a Principal within the Subject.
        // For instance: a name, an Social Sceurity number
        CallerPrincipalCallback cpc = new CallerPrincipalCallback(clientSubject, user);

        // Callback establishing group principals within the argument subject
        GroupPrincipalCallback gpc = new GroupPrincipalCallback(clientSubject, new String[]{group});

        try {
            callbackHandler.handle(new Callback[]{cpc, gpc});
            System.out.println("JASPIC AuthModule, callbackHandler.handle done");
        } catch (IOException | UnsupportedCallbackException ex) {
            Logger.getLogger(SamDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This is the main method of interest. In order to pass in the user and
     * role I have just added them as servlet request parameters for testing
     * purposes. This method extracts those values and then calls
     * authenticateUser.
     */
    @Override
    public AuthStatus validateRequest(MessageInfo mi, Subject clientSubject, Subject serverSubject) throws AuthException {
        System.out.println("JASPIC AuthModule, validateRequest called");
        HttpServletRequest request = (HttpServletRequest) mi.getRequestMessage();
        String user = request.getParameter("user");
        if ( user==null )
            user = "anonymous"; // Protection against NPE
        String group = request.getParameter("group");
        if ( group==null )
            group = "anonymous"; // Protection against NPE
        System.out.println("JASPIC AuthModule, user=" + user + ", group=" + group);

        authenticateUser(user, group, clientSubject, serverSubject);
        return AuthStatus.SUCCESS;
    }

    @Override
    public AuthStatus secureResponse(MessageInfo mi, Subject sbjct) throws AuthException {
        System.out.println("JASPIC AuthModule, secureResponse called");
        return AuthStatus.SEND_SUCCESS;
    }

    @Override
    public void cleanSubject(MessageInfo mi, Subject sbjct) throws AuthException {
        System.out.println("JASPIC AuthModule, cleanSubject called");
        if (sbjct != null) {
            sbjct.getPrincipals().clear();
        }
    }

}
