package org.jboss.as.sample.ejb;

import javax.ejb.Remote;

@Remote
public interface SampleEjb {

  /**
   * Unsecured invocation, will return the name of application, principal and JBoss node.
   * 
   * @param text Simple text written to to the logfile to identify the invocation
   * @return sample[&lt;PrincipalName&gt;]@&lt;jboss.node.name&gt;
   */
    String invoke(String text);

  /**
   * @return The property of jboss.node.name, pattern &lt;host&gt;:&lt;server&gt;
   */
    String getJBossNodeName();


}
