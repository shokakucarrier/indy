package org.commonjava.aprox.autoprox.conf;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.commonjava.aprox.core.model.StoreKey;
import org.commonjava.aprox.core.model.StoreType;
import org.junit.Test;

public class DefaultAutoProxConfigurationTest
{

    @Test
    public void parseBriefConstituentNotation()
    {
        final DefaultAutoProxConfiguration config = new DefaultAutoProxConfiguration( "http://foo.com" );

        final String list =
            "<repoName, >deployPoint, +group, repository:repoName2, deploy_point:deployPoint2, group:group2, foo, f:, :foobar";

        config.setExtraGroupConstituentsString( list );

        final List<StoreKey> constituents = config.getExtraGroupConstituents();
        assertThat( constituents, notNullValue() );
        assertThat( constituents.size(), equalTo( 6 ) );

        int i = 0;
        assertThat( constituents.get( i++ ), equalTo( new StoreKey( StoreType.repository, "repoName" ) ) );
        assertThat( constituents.get( i++ ), equalTo( new StoreKey( StoreType.deploy_point, "deployPoint" ) ) );
        assertThat( constituents.get( i++ ), equalTo( new StoreKey( StoreType.group, "group" ) ) );
        assertThat( constituents.get( i++ ), equalTo( new StoreKey( StoreType.repository, "repoName2" ) ) );
        assertThat( constituents.get( i++ ), equalTo( new StoreKey( StoreType.deploy_point, "deployPoint2" ) ) );
        assertThat( constituents.get( i++ ), equalTo( new StoreKey( StoreType.group, "group2" ) ) );
    }

}
