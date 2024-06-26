package org.anemoi.features.server;

import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

public class EmbeddedJspStarter extends AbstractLifeCycle {

    private JettyJasperInitializer sci;
    private ServletContextHandler context;

    public EmbeddedJspStarter(ServletContextHandler context) {
        this.sci = new JettyJasperInitializer();
        this.context = context;
        StandardJarScanner jarScanner = new StandardJarScanner();
        StandardJarScanFilter jarScanFilter = new StandardJarScanFilter();
        jarScanFilter.setTldScan("taglibs-standard-impl-*");
        jarScanFilter
                .setTldSkip("apache-*,ecj-*,jetty-*,asm-*,javax.servlet-*,javax.annotation-*,taglibs-standard-spec-*");
        jarScanner.setJarScanFilter(jarScanFilter);
        this.context.setAttribute("org.apache.tomcat.JarScanner", jarScanner);
    }

    @Override
    protected void doStart() throws Exception {
        ClassLoader old = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(context.getClassLoader());
        try {
            sci.onStartup(null, context.getServletContext());
            super.doStart();
        } finally {
            Thread.currentThread().setContextClassLoader(old);
        }
    }
}