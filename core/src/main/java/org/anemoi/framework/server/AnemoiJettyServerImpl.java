package org.anemoi.framework.server;

import jakarta.servlet.http.HttpServlet;
import org.anemoi.framework.server.listener.AnemoiContextListener;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

public final class AnemoiJettyServerImpl {
    private Server server;
    private final int port;
    static final String WEB_ROOT_INDEX = "/web/";
    private static final Logger logger = LoggerFactory.getLogger(AnemoiJettyServerImpl.class);


    public AnemoiJettyServerImpl(int port) {
        this.port = port;
        server = new Server(port);
    }


    public void launch(Class<? extends HttpServlet> mainRequestHandler) throws Exception {
        int minThreads = 10;
        int maxThreads = 200;
        int idleTimeout = 120;


        QueuedThreadPool threadPool = new QueuedThreadPool(maxThreads, minThreads, idleTimeout);
        server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(this.port);
        server.setConnectors(new Connector[]{connector});


        // Create URI for Servlet Context
        URI baseURI = getWebRootResourceURI();


        // Create Servlet Context

        AnemoiContextListener listener = new AnemoiContextListener();

        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.setContextPath("/");
        servletContextHandler.setResourceBase(baseURI.toASCIIString());

        servletContextHandler.addEventListener(listener);

        enableEmbeddedJspSupport(servletContextHandler);


        ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
        holderDefault.setInitOrder(1); // the servlet should be loaded at startup otherwise (0 or less) it will be initialized when the servlet is firstly needed
        holderDefault.setInitParameter("resourceBase", baseURI.toASCIIString());
        holderDefault.setInitParameter("dirAllowed", "true");
        holderDefault.setInitParameter("pathInfoOnly", "true");

        //servletContextHandler.addServlet(holderDefault, "*.css");
        servletContextHandler.addServlet(holderDefault, "/");


        //servletContextHandler.addServlet(holderDefault, "/css/*");

        ServletHolder anemoiCoreRequestHandler = new ServletHolder("anemoiCoreRequestHandler", mainRequestHandler);

        servletContextHandler.addServlet(anemoiCoreRequestHandler, "*.ctl");


        servletContextHandler.setWelcomeFiles(new String[]{"index.jsp", "index.html"});
        server.setHandler(servletContextHandler);


        server.start();


        server.join();
    }


    public void stop() throws Exception {
        server.stop();
        logger.info("Application shutdown");
    }


    private URI getWebRootResourceURI() throws FileNotFoundException, URISyntaxException {
        URL indexURI = this.getClass().getResource(WEB_ROOT_INDEX);
        if (indexURI == null) {
            throw new FileNotFoundException(" web directory not found in the resources , please create the directory!");
        }
        return indexURI.toURI();
    }

    private void enableEmbeddedJspSupport(ServletContextHandler servletContextHandler) throws IOException {
        // Establish Scratch directory for the servlet context (used by JSP compilation)
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");
        logger.info("jsp scratch dir {}", scratchDir.getPath());
        if (!scratchDir.exists()) {
            if (!scratchDir.mkdirs()) {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }
        servletContextHandler.setAttribute("jakarta.servlet.context.tempdir", scratchDir);

        // Set Classloader of Context to be sane (needed for JSTL)
        // JSP requires a non-System classloader, this simply wraps the
        // embedded System classloader in a way that makes it suitable
        // for JSP to use
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], this.getClass().getClassLoader());
        servletContextHandler.setClassLoader(jspClassLoader);

        // Manually call JettyJasperInitializer on context startup
        servletContextHandler.addBean(new EmbeddedJspStarter(servletContextHandler));

        // Create / Register JSP Servlet (must be named "jsp" per spec)
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("scratchdir", scratchDir.toString());
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.8");
        holderJsp.setInitParameter("compilerSourceVM", "1.8");
        holderJsp.setInitParameter("keepgenerated", "true");
        servletContextHandler.addServlet(holderJsp, "*.jsp");


        servletContextHandler.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
    }


}
