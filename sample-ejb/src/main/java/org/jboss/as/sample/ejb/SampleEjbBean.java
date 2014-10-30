package org.jboss.as.sample.ejb;

import java.security.Principal;

import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

@Stateless
@Remote(SampleEjb.class) 
public class SampleEjbBean implements SampleEjb {
    private static final Logger LOGGER = Logger.getLogger(SampleEjbBean.class);

    @Resource
    SessionContext context;

    @Override
    public String getJBossNodeName() {
        return System.getProperty("jboss.node.name");
    }

    @Override
    public String invoke(String text) {
        Principal caller = context.getCallerPrincipal();
        LOGGER.info("[" + caller.getName() + "] " + text);
        return "sample[" + caller.getName() + "]@" + getJBossNodeName();
    }


}
