package org.usergrid;


import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.usergrid.persistence.Entity;
import org.usergrid.persistence.EntityRef;
import org.usergrid.persistence.entities.Activity;
import org.usergrid.persistence.entities.Role;
import org.usergrid.services.ServiceAction;
import org.usergrid.services.ServiceManager;
import org.usergrid.services.ServiceRequest;
import org.usergrid.services.ServiceResults;
import org.usergrid.utils.JsonUtils;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.usergrid.services.ServiceParameter.parameters;
import static org.usergrid.services.ServicePayload.batchPayload;
import static org.usergrid.services.ServicePayload.payload;
import static org.usergrid.utils.InflectionUtils.pluralize;

public class ServiceApplication extends CoreApplication
{
    private static final Logger LOG = LoggerFactory.getLogger( ServiceApplication.class );

    protected ServiceManager sm;
    protected ServiceITSetup svcSetup;
    protected boolean svcEnabled = false;


    public ServiceApplication( ServiceITSetup svcSetup )
    {
        super( svcSetup );
        this.svcSetup = svcSetup;
    }


    @Override
    protected void before( Description description ) throws Exception
    {
        super.before( description );
        sm = svcSetup.getSmf().getServiceManager( id );
    }


    public void add( Activity activity )
    {
        this.properties.putAll( activity.getProperties() );
    }


    public ServiceResults testRequest( ServiceAction action, int expectedCount, Object... params ) throws Exception
    {
        return testRequest( action, expectedCount, true, params );
    }


    public ServiceResults testRequest( ServiceAction action, int expectedCount, boolean clear, Object... params )
            throws Exception
    {
        ServiceResults results = invokeService( action, params );
        assertNotNull(results);
        assertEquals( expectedCount, results.getEntities().size() );
        dumpResults( results );

        if ( clear )
        {
            properties.clear();
        }

        return results;
    }


    public ServiceResults invokeService( ServiceAction action, Object... params ) throws Exception
    {
        ServiceRequest request = sm.newRequest( action, parameters( params ), payload( properties ) );

        LOG.info( "Request: {} {}", action, request.toString() );
        dumpProperties( properties );
        ServiceResults results = request.execute();
        assertNotNull( results );
        dumpResults( results );
        return results;
    }


    public void dumpProperties( Map<String, Object> properties )
    {
        if ( properties != null && LOG.isInfoEnabled() )
        {
            LOG.info( "Input:\n {}", JsonUtils.mapToFormattedJsonString( properties ) );
        }
    }


    public void dumpResults( ServiceResults results )
    {
        if ( results != null )
        {
            List<Entity> entities = results.getEntities();
            svcSetup.dump("Results", entities);
        }
    }


    public Entity doCreate( String entityType, String name ) throws Exception
    {
        put( "name", name );

        return testRequest( ServiceAction.POST, 1, pluralize( entityType ) ).getEntity();
    }


    public void createConnection( Entity subject, String verb, Entity noun ) throws Exception
    {
        sm.getEntityManager().createConnection( subject, verb, noun );
    }


    public ServiceResults testBatchRequest( ServiceAction action, int expectedCount, List<Map<String, Object>> batch,
                                            Object... params ) throws Exception
    {
        ServiceRequest request = sm.newRequest( action, parameters( params ), batchPayload( batch ) );
        LOG.info("Request: " + action + " " + request.toString());
        // dump( "Batch", batch );
        ServiceResults results = request.execute();
        assertNotNull(results);
        assertEquals(expectedCount, results.getEntities().size());
        dumpResults(results);
        return results;
    }


    public ServiceResults testDataRequest( ServiceAction action, Object... params ) throws Exception
    {
        ServiceRequest request = sm.newRequest( action, parameters( params ), payload( properties ) );
        LOG.info( "Request: {} {}", action, request.toString() );
        dumpProperties(properties);
        ServiceResults results = request.execute();
        assertNotNull( results );
        assertNotNull( results.getData() );
        // dump( results.getData() );
        return results;
    }


    public Entity createRole( String name, String title, int inactivity ) throws Exception
    {
        return  sm.getEntityManager().createRole( name, title, inactivity );
    }


    public void grantRolePermission( String role, String permission ) throws Exception
    {
        sm.getEntityManager().grantRolePermission( role, permission );
    }


    public void grantUserPermission( UUID uuid, String permission ) throws Exception
    {
        sm.getEntityManager().grantUserPermission( uuid, permission );
    }


    public Set<String> getRolePermissions( String role ) throws Exception
    {
        return sm.getEntityManager().getRolePermissions( role );
    }


    public EntityRef getAlias( String aliasType, String alias ) throws Exception
    {
        return em.getAlias(aliasType, alias);
    }


    public <T extends Entity> T get( EntityRef ref, Class<T> clazz ) throws Exception
    {
        return em.get( ref, clazz );
    }


    public Map<String, Role> getRolesWithTitles( Set<String> roleNames ) throws Exception
    {
        return em.getRolesWithTitles( roleNames );
    }


    public Entity createGroupRole( UUID id, String role, int inactivity ) throws Exception
    {
        return em.createGroupRole( id, role, inactivity );
    }


    public void grantGroupRolePermission( UUID id, String role, String permission ) throws Exception
    {
        em.grantGroupRolePermission( id, role, permission );
    }


    public ServiceManager getSm()
    {
        return sm;
    }
}
