/*
 * Copyright 2018 jefrajames.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package lab.jefrajames.jaspicappdemo;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

/**
 * This example illustrates that the role is propagated to EJB
 * 
 * @author jefrajames
 */
@Stateless
@RolesAllowed({"admin", "standard"})
public class SecuredEjb {
    
    @Resource
    private SessionContext ctx;

    public String hello() {
        return "Hello from EJB to " + ctx.getCallerPrincipal().getName() + ", " + (ctx.isCallerInRole("admin")?"admin":"standard") + " group";
    }
}
