/*******************************************************************************
 * Copyright 2012 Apigee Corporation
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.usergrid.rest.test.resource.app;

import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;
import org.usergrid.rest.test.resource.CustomCollection;
import org.usergrid.rest.test.resource.NamedResource;
import org.usergrid.rest.test.resource.RootResource;
import org.usergrid.rest.test.resource.ValueResource;
import org.usergrid.rest.test.resource.app.queue.DevicesCollection;
import org.usergrid.rest.test.resource.app.queue.QueuesCollection;

/**
 * @author tnine
 * 
 */
public class Application extends ValueResource {


  /**
   * @param parent
   */
  public Application(String orgName, String appName, RootResource root) {
    super(orgName + SLASH + appName, root);
  }

  

  /**
   * Get the token from management for this username and password
   * 
   * @param username
   * @param password
   * @return
   */
  public String token(String username, String password) {

    String url = String.format("%s/token", url());

    JsonNode node = resource().path(url).queryParam("grant_type", "password").queryParam("username", username)
        .queryParam("password", password)
        .accept(MediaType.APPLICATION_JSON)
        .type(MediaType.APPLICATION_JSON_TYPE).get(JsonNode.class);

    return node.get("access_token").asText();
  }

 
  public UsersCollection users() {
    return new UsersCollection(this);
  }

  public QueuesCollection queues() {
    return new QueuesCollection(this);
  }

  public DevicesCollection devices() {
    return new DevicesCollection(this);
  }

  public CustomCollection collection(String name) {
    return new CustomCollection(name, this);
  }

}
