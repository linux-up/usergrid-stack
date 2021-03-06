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
package org.usergrid.rest.test.resource;

import org.usergrid.rest.test.resource.app.queue.DevicesCollection;

import java.util.UUID;

/**
 * @author tnine
 * 
 */
public class Connection extends ValueResource {

 
  public Connection(String name, NamedResource parent) {
    super(name, parent);
  }

 
  public EntityResource entity(String deviceName){
    return new EntityResource(deviceName, this);
  }

  public EntityResource entity(UUID entityId){
    return new EntityResource(entityId, this);
  }
  
  public DevicesCollection devices(){
    return new DevicesCollection(this);
  }
  
  public CustomCollection collection(String name){
    return new CustomCollection(name, this);
  }
}
