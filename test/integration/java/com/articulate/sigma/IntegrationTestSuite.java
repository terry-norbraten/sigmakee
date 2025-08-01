package com.articulate.sigma;

import com.articulate.sigma.VerbNet.IntegrationVerbNetTestSuite;
import com.articulate.sigma.nlg.IntegrationNLGTestSuite;
import com.articulate.sigma.parsing.IntegrationParsingTestSuite;
import com.articulate.sigma.trans.IntegrationTransTestSuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    IntegrationSigmaTestSuite.class,
    IntegrationVerbNetTestSuite.class,
    IntegrationNLGTestSuite.class,
    IntegrationParsingTestSuite.class,
    IntegrationTransTestSuite.class
})
public class IntegrationTestSuite extends IntegrationTestBase {

}