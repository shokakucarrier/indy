/**
 * Copyright (C) 2011 Red Hat, Inc. (jdcasey@commonjava.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.commonjava.aprox.promote.validate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.commonjava.aprox.audit.ChangeSummary;
import org.commonjava.aprox.model.core.StoreKey;
import org.commonjava.aprox.model.core.StoreType;
import org.commonjava.aprox.promote.conf.PromoteConfig;
import org.commonjava.aprox.promote.model.ValidationRuleSet;
import org.commonjava.aprox.subsys.datafile.DataFile;
import org.commonjava.aprox.subsys.datafile.DataFileManager;
import org.commonjava.aprox.subsys.datafile.change.DataFileEventManager;
import org.commonjava.aprox.subsys.template.ScriptEngine;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by jdcasey on 9/14/15.
 */
public class PromoteValidationsManagerTest
{
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    private ValidationRuleParser parser;

    private DataFileManager fileManager;

    private PromoteValidationsManager promoteValidations;

    private PromoteConfig config;

    @Before
    public void setUp()
            throws Exception
    {
        parser = new ValidationRuleParser( new ScriptEngine(), new ObjectMapper() );
        fileManager = new DataFileManager( temp.newFolder( "data" ), new DataFileEventManager() );
        config = new PromoteConfig();
        config.setEnabled( true );
    }

    @Test
    public void testRuleSetParseAndMatchOnStoreKey()
            throws Exception
    {
        DataFile dataFile = fileManager.getDataFile( "promote/rule-sets/test.json" );
        dataFile.writeString( "{\"name\":\"test\",\"storeKeyPattern\":\".*\"}",
                              new ChangeSummary( ChangeSummary.SYSTEM_USER, "writing test data" ) );

        promoteValidations = new PromoteValidationsManager( fileManager, config, parser );

        ValidationRuleSet ruleSet = promoteValidations.getRuleSetMatching( new StoreKey( StoreType.hosted, "repo" ) );

        assertThat( ruleSet, notNullValue() );
        assertThat( ruleSet.matchesKey( "hosted:repo" ), equalTo( true ) );
    }
}