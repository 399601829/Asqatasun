/*
 * Asqatasun - Automated webpage assessment
 * Copyright (C) 2008-2019  Asqatasun.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: asqatasun AT asqatasun DOT org
 */
package org.asqatasun.rules.rgaa32016;

import org.asqatasun.entity.audit.ProcessResult;
import org.asqatasun.entity.audit.TestSolution;
import org.asqatasun.rules.rgaa32016.test.Rgaa32016RuleImplementationTestCase;

/**
 * Unit test class for the implementation of the rule 6.2.1 of the referential RGAA 3.2016
 *
 * @author jkowalczyk
 */
public class Rgaa32016Rule060201Test extends Rgaa32016RuleImplementationTestCase {

    /**
     * Default constructor
     * @param testName
     */
    public Rgaa32016Rule060201Test (String testName){
        super(testName);
    }

    @Override
    protected void setUpRuleImplementationClassName() {
        setRuleImplementationClassName("org.asqatasun.rules.rgaa32016.Rgaa32016Rule060201");
    }

    @Override
    protected void setUpWebResourceMap() {
        addWebResource("Rgaa32016.Test.06.02.01-2Failed-01");
        addWebResource("Rgaa32016.Test.06.02.01-2Failed-02");
        addWebResource("Rgaa32016.Test.06.02.01-2Failed-03");
        addWebResource("Rgaa32016.Test.06.02.01-2Failed-04");
        addWebResource("Rgaa32016.Test.06.02.01-3NMI-01");
        addWebResource("Rgaa32016.Test.06.02.01-3NMI-02");
        addWebResource("Rgaa32016.Test.06.02.01-4NA-01");
        addWebResource("Rgaa32016.Test.06.02.01-4NA-02");
        addWebResource("Rgaa32016.Test.06.02.01-4NA-03");
        addWebResource("Rgaa32016.Test.06.02.01-4NA-04");

        //06.02.02 testcases
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-2Failed-01",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-2Failed-01.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-2Failed-02",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-2Failed-02.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-2Failed-03",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-2Failed-03.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-2Failed-04",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-2Failed-04.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-3NMI-01",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-3NMI-01.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-3NMI-02",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-3NMI-02.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-3NMI-03",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-3NMI-03.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-3NMI-04",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-3NMI-04.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-3NMI-05",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-3NMI-05.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.02-3NMI-06",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060202/Rgaa32016.Test.06.02.02-3NMI-06.html"));
        
        //06.02.03 testcases
        getWebResourceMap().put("Rgaa32016.Test.06.02.03-2Failed-02",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060203/Rgaa32016.Test.06.02.03-2Failed-02.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.03-2Failed-03",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060203/Rgaa32016.Test.06.02.03-2Failed-03.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.03-2Failed-04",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060203/Rgaa32016.Test.06.02.03-2Failed-04.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.03-3NMI-01",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060203/Rgaa32016.Test.06.02.03-3NMI-01.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.03-3NMI-02",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060203/Rgaa32016.Test.06.02.03-3NMI-02.html"));
        getWebResourceMap().put("Rgaa32016.Test.06.02.03-3NMI-03",
                getWebResourceFactory().createPage(
                getTestcasesFilePath() + "rgaa32016/Rgaa32016Rule060203/Rgaa32016.Test.06.02.03-3NMI-03.html"));

    }

    @Override
    protected void setProcess() {
        //----------------------------------------------------------------------
        //---------------------------2Failed-01---------------------------------
        //----------------------------------------------------------------------
        ProcessResult processResult = processPageTest("Rgaa32016.Test.06.02.01-2Failed-01");
        checkResultIsFailed(processResult, 3,1);
        
        //----------------------------------------------------------------------
        //---------------------------2Failed-02---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32016.Test.06.02.01-2Failed-02");
        checkResultIsFailed(processResult, 3,1);
        
        //----------------------------------------------------------------------
        //---------------------------2Failed-03---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32016.Test.06.02.01-2Failed-03");
        checkResultIsFailed(processResult, 3,1);
        
        //----------------------------------------------------------------------
        //---------------------------2Failed-04---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32016.Test.06.02.01-2Failed-04");
        checkResultIsFailed(processResult, 3,1);
        
        //----------------------------------------------------------------------
        //------------------------------3NMI-01---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32016.Test.06.02.01-3NMI-01");
        checkResultIsPreQualified(processResult,3,1);
        
        //----------------------------------------------------------------------
        //------------------------------3NMI-02---------------------------------
        //----------------------------------------------------------------------
        processResult = processPageTest("Rgaa32016.Test.06.02.01-3NMI-02");
        checkResultIsPreQualified(processResult,3,1);
        
        //----------------------------------------------------------------------
        //------------------------------4NA-01----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.01-4NA-01"),2);
        
        //----------------------------------------------------------------------
        //------------------------------4NA-02----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.01-4NA-02"),3);
        
        //----------------------------------------------------------------------
        //------------------------------4NA-03----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.01-4NA-03"),2);
        
        //----------------------------------------------------------------------
        //------------------------------4NA-04----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.01-4NA-04"),2);        
        
        // other tests about links, scope of other is disjunted from the scope
        // of the current test : All these scopes are not applicable
        
        //----------------------------------------------------------------------
        //---------------------------4NA-6.2.2----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-2Failed-01"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-2Failed-02"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-2Failed-03"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-2Failed-04"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-3NMI-01"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-3NMI-02"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-3NMI-03"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-3NMI-04"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-3NMI-05"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.02-3NMI-06"));
        
        //----------------------------------------------------------------------
        //---------------------------4NA-6.2.3----------------------------------
        //----------------------------------------------------------------------
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.03-2Failed-02"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.03-2Failed-03"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.03-2Failed-04"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.03-3NMI-01"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.03-3NMI-02"));
        checkResultIsNotApplicable(processPageTest("Rgaa32016.Test.06.02.03-3NMI-03"));

    }

    @Override
    protected void setConsolidate() {
        assertEquals(TestSolution.FAILED,
                consolidate("Rgaa32016.Test.06.02.01-2Failed-01").getValue());
        assertEquals(TestSolution.FAILED,
                consolidate("Rgaa32016.Test.06.02.01-2Failed-02").getValue());
        assertEquals(TestSolution.FAILED,
                consolidate("Rgaa32016.Test.06.02.01-2Failed-03").getValue());
        assertEquals(TestSolution.FAILED,
                consolidate("Rgaa32016.Test.06.02.01-2Failed-04").getValue());
        assertEquals(TestSolution.NEED_MORE_INFO,
                consolidate("Rgaa32016.Test.06.02.01-3NMI-01").getValue());
        assertEquals(TestSolution.NEED_MORE_INFO,
                consolidate("Rgaa32016.Test.06.02.01-3NMI-02").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.01-4NA-01").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.01-4NA-02").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.01-4NA-03").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.01-4NA-04").getValue());
        
        // 06.02.02 testcases : All is Not Applicable
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-2Failed-01").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-2Failed-02").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-2Failed-03").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-2Failed-04").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-3NMI-01").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-3NMI-02").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-3NMI-03").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-3NMI-04").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-3NMI-05").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.02-3NMI-06").getValue());

        // 06.02.03 testcases : All is Not Applicable
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.03-2Failed-02").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.03-2Failed-03").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.03-2Failed-04").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.03-3NMI-01").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.03-3NMI-02").getValue());
        assertEquals(TestSolution.NOT_APPLICABLE,
                consolidate("Rgaa32016.Test.06.02.03-3NMI-03").getValue());

    }
    
}
