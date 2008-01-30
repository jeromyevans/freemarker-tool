package com.blueskyminds.freemarkertool.web.mapper.configuration;

import junit.framework.TestCase;
import com.blueskyminds.freemarkertool.web.actions.mapper.configuration.ActionMapConfiguration;

/**
 * Date Started: 29/01/2008
 * <p/>
 * History:
 */
public class TestMapper extends TestCase {

    private ActionMapConfiguration actionMapConfiguration;

    /**
     * Sets up the fixture, for example, open a network connection.
     * This method is called before a test is executed.
     */
    protected void setUp() throws Exception {
        super.setUp();
        actionMapConfiguration = new SampleActionMapConfiguration();
    }

    public void testMapping() {

        //actionMapConfiguration.getActionMappings()
    }

}
