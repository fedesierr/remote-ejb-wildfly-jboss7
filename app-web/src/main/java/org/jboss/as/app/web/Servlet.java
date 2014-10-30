/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.app.web;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.as.sample.ejb.SampleEjb;

@WebServlet(urlPatterns = "/*")
public class Servlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(Servlet.class.getName());
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
       
        LOGGER.info("Servlet is called " + new Date());

        response.setContentType("html");
        write(response, "<h1>Example Servlet to show how EJB's can be invoked</h1>");
       
        try {
            final InitialContext iCtx = getContext();

            write(response, "<h2>Invoke SampleEjb on different server (JBoss 7.1.1)</h2>");
            try {
            	SampleEjb proxy = (SampleEjb) lookup(response, iCtx, "ejb:/sample-ejb/SampleEjbBean!org.jboss.as.sample.ejb.SampleEjb");
                
                if (proxy != null) {
                    write(response, "Invocation method getJBossNodeName SampleEjb return => " + proxy.getJBossNodeName() + "<br/>");
                    write(response, "Invocation method invoke SampleEjb return => " + proxy.getJBossNodeName() + "<br/>");
                } else {
                	write(response, "SampleEjb is null <br/>");
                }
            } catch (Exception n) {
                LOGGER.log(Level.SEVERE, "Failed to invoke SampleEjb", n);
                write(response, "Failed to invoke SampleEjb<br/>");
                write(response, n.getMessage());
            }
            
        } catch (NamingException e) {
            write(response, "<h2>Failed to initialize InitialContext</h2>");
            write(response, e.getMessage());
        }

        write(response, "<br/><br/><br/><p><i>All invocations are successful</i></p>");
        write(response, "<p>Finish at " + new Date() + "</p>");
    }

    private static void write(HttpServletResponse writer, String message) {

        try {
            writer.getWriter().write(message + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static InitialContext getContext() throws NamingException {
        Properties jndiProperties = new Properties();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        return new InitialContext(jndiProperties);
    }

    private Object lookup(HttpServletResponse response, InitialContext ic, String name) {
        try {
            Object proxy = ic.lookup(name);
            ic.close();
            if (proxy == null) {
                write(response, "lookup(" + name + ") returns no proxy object");
            }
            return proxy;
        } catch (NamingException e) {
            write(response, "Failed to lookup(" + name + ")");
            return null;
        }
    }
}
