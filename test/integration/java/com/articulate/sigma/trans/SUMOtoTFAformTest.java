package com.articulate.sigma.trans;

import com.articulate.sigma.IntegrationTestBase;
import com.articulate.sigma.Formula;
import com.articulate.sigma.KBmanager;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import org.junit.BeforeClass;
import org.junit.Ignore;

//This software is released under the GNU Public License
//<http://www.gnu.org/copyleft/gpl.html>.
// Copyright 2019 Infosys
// adam.pease@infosys.com

public class SUMOtoTFAformTest extends IntegrationTestBase {

    private static SUMOKBtoTFAKB skbtfakb = null;

    /****************************************************************
     */
    @BeforeClass
    public static void init() {

        SUMOtoTFAform.initOnce();
        SUMOtoTFAform.setNumericFunctionInfo();
        skbtfakb = new SUMOKBtoTFAKB();
        skbtfakb.initOnce();
        String filename = KBmanager.getMgr().getPref("kbDir") + File.separator + "SUMO.tff";
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileWriter(filename));
            skbtfakb.writeSorts(pw,filename);
            pw.flush();
            pw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** *************************************************************
     */
    @Test
    public void testExtractSig() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testSorts(): ");
        ArrayList<String> sorts = SUMOtoTFAform.relationExtractSig("ListFn__6Fn__0Ra1Ra2Ra3Ra4Ra5Ra6Ra");
        System.out.println(sorts);
        String expectedRes = "[RationalNumber, RationalNumber, RationalNumber, RationalNumber, RationalNumber, RationalNumber, RationalNumber]";
        String result = sorts.toString();
        if (expectedRes.equals(result))
            System.out.println("testExtractSig(): Success!");
        else
            System.out.println("testExtractSig(): fail");
        assertTrue(sorts.toString().equals("[RationalNumber, RationalNumber, RationalNumber, RationalNumber, RationalNumber, RationalNumber, RationalNumber]"));
    }

    /** *************************************************************
     */
    @Test
    public void testExtractUpdateSig() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testExtractUpdateSig(): ");
        ArrayList<String> sorts = SUMOtoTFAform.relationExtractUpdateSig("ListFn__6Fn__0Ra1Ra2Ra3Ra4Ra5Ra6Ra");
        System.out.println(sorts);
        String expectedRes = "[RationalNumber, RationalNumber, RationalNumber, RationalNumber, RationalNumber, RationalNumber, RationalNumber]";
        String result = sorts.toString();
        if (expectedRes.equals(result))
            System.out.println("testExtractUpdateSig(): Success!");
        else
            System.out.println("testExtractUpdateSig(): fail");
        assertTrue(expectedRes.equals(result));
    }

    /** *************************************************************
     */
    @Test
    public void testSorts() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testSorts(): ");
        System.out.println(skbtfakb.kb.kbCache.getSignature("AbsoluteValueFn__Integer"));
    }

    /** *************************************************************
     */
    @Test
    public void testParents() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testParents(): ");
        Formula f = new Formula("(=> (instance ?X Human) (parents ?X (AdditionFn 1 1)))");
        ArrayList<String> sig = new ArrayList<>();
        sig.add("");
        sig.add("Human");
        sig.add("Integer");
        kb.terms.add("parents");
        kb.kbCache.relations.add("parents");
        kb.kbCache.signatures.put("parents",sig);
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.testParents(): result:   " + result);
        String expectedRes = "! [V__X : $i] : (s__instance(V__X, s__Human) => s__parents__2In(V__X, $sum(1 ,1)))";
        System.out.println("SUMOtoTFAformTest.testParents(): expected: " + expectedRes);
        if (expectedRes.equals(result))
            System.out.println("testParents(): Success!");
        else
            System.out.println("testParents(): fail");
        assertEquals(expectedRes,result);
    }

    /** *************************************************************
     */
    @Test
    public void test1() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.test1(): ");
        Formula f = new Formula("(equal ?X (AdditionFn 1 2))");
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.test1(): " + result);
        String expectedRes = "! [V__X : $real] : (V__X = $sum(1.0 ,2.0))";
        System.out.println("SUMOtoTFAformTest.test1(): expected: " + expectedRes);
        if (expectedRes.equals(result))
            System.out.println("test1(): Success!");
        else
            System.out.println("test1(): fail");
        assertEquals(expectedRes,result);
    }

    /** *************************************************************
     */
    @Test
    public void test1_5() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.test1_5(): ");
        Formula f = new Formula("(equal ?X (SubtractionFn 2 1))");
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.test1_5(): " + result);
        String expectedRes = "! [V__X : $real] : (V__X = $difference(2.0 ,1.0))";
        System.out.println("SUMOtoTFAformTest.test1_5(): expected: " + expectedRes);
        if (expectedRes.equals(result))
            System.out.println("test1_5(): Success!");
        else
            System.out.println("test1_5(): fail");
        assertEquals(expectedRes,result);
    }

    /** *************************************************************
     */
    @Test
    public void test2() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.test2(): ");
        Formula f = new Formula("(=> (and (equal (AbsoluteValueFn ?NUMBER1) ?NUMBER2) " +
                "(instance ?NUMBER1 RealNumber) (instance ?NUMBER2 RealNumber)) " +
                "(or (and (instance ?NUMBER1 NonnegativeRealNumber) (equal ?NUMBER1 ?NUMBER2)) " +
                "(and (instance ?NUMBER1 NegativeRealNumber) (equal ?NUMBER2 (SubtractionFn 0 ?NUMBER1)))))");
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.test2(): " + result);
        String expected = "! [V__NUMBER1 : $real,V__NUMBER2 : $real] : " +
                "(s__AbsoluteValueFn__0Re1ReFn(V__NUMBER1) = V__NUMBER2 => " +
                "(((s__SignumFn__0In1ReFn(V__NUMBER1) = 1 | " +
                "s__SignumFn__0In1ReFn(V__NUMBER1) = 0) & V__NUMBER1 = V__NUMBER2) | " +
                "(s__SignumFn__0In1ReFn(V__NUMBER1) = -1 & V__NUMBER2 = $difference(0.0 ,V__NUMBER1))))";
        System.out.println("SUMOtoTFAformTest.test2(): expected: " + expected);
        if (expected.equals(result))
            System.out.println("test2(): Success!");
        else
            System.out.println("test2(): fail");
        assertEquals(expected,result);
    }

    /** *************************************************************
     */
    @Test
    public void test3() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.test3(): ");
        Formula f = new Formula("(<=> (equal (RemainderFn ?NUMBER1 ?NUMBER2) ?NUMBER) " +
                "(equal (AdditionFn (MultiplicationFn (FloorFn (DivisionFn ?NUMBER1 ?NUMBER2)) ?NUMBER2) ?NUMBER) ?NUMBER1))");
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.test3(): " + result);
        String expected = "! [V__NUMBER1 : $int,V__NUMBER2 : $int,V__NUMBER : $int] : " +
                "((s__RemainderFn__0In1In2InFn(V__NUMBER1, V__NUMBER2) = V__NUMBER => " +
                "$sum($product($to_int($quotient_e(V__NUMBER1 ,V__NUMBER2)) ,V__NUMBER2) ,V__NUMBER) = " +
                "V__NUMBER1) & " +
                "($sum($product($to_int($quotient_e(V__NUMBER1 ,V__NUMBER2)) ,V__NUMBER2) ,V__NUMBER) = " +
                "V__NUMBER1 => s__RemainderFn__0In1In2InFn(V__NUMBER1, V__NUMBER2) = V__NUMBER))";
        System.out.println("SUMOtoTFAformTest.test3(): expected: " + expected);
        if (expected.equals(result))
            System.out.println("test3(): Success!");
        else
            System.out.println("test3(): fail");
        assertEquals(expected,result);
    }

    /** *************************************************************
     */
    @Test
    public void test4() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.test4(): ");
        Formula f = new Formula("(<=> (greaterThanOrEqualTo ?NUMBER1 ?NUMBER2) " +
                "(or (equal ?NUMBER1 ?NUMBER2) (greaterThan ?NUMBER1 ?NUMBER2)))");
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.test4(): result: " + result);
        String expected = "! [V__NUMBER1 : $i,V__NUMBER2 : $i] : " +
                "((greaterThanOrEqualTo(V__NUMBER1 ,V__NUMBER2) => " +
                "(equal(V__NUMBER1 ,V__NUMBER2) | greaterThan(V__NUMBER1 ,V__NUMBER2))) & " +
                "((equal(V__NUMBER1 ,V__NUMBER2) | greaterThan(V__NUMBER1 ,V__NUMBER2)) => " +
                "greaterThanOrEqualTo(V__NUMBER1 ,V__NUMBER2)))";
        System.out.println("SUMOtoTFAformTest.test4(): expected: " + expected);
        if (expected.equals(result))
            System.out.println("test4(): Success!");
        else
            System.out.println("test4(): fail");
        assertEquals(expected,result);
    }

    /** *************************************************************
     */
    @Test
    public void test5() {

        //SUMOtoTFAform.debug = true;
        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.test5(): ");
        Formula f = new Formula("(=>\n" +
                "(measure ?QUAKE\n" +
                "(MeasureFn ?VALUE RichterMagnitude))\n" +
                "(instance ?VALUE PositiveRealNumber))");
        String result = SUMOtoTFAform.modifyTypesToConstraints(f);
        System.out.println("SUMOtoTFAformTest.test5(): result: " + result);
        String expected = "(=>\n" +
                "(measure ?QUAKE\n" +
                "(MeasureFn ?VALUE RichterMagnitude))\n" +
                "(equal (SignumFn ?VALUE) 1))";
        System.out.println("SUMOtoTFAformTest.test5(): expected: " + expected);
        if (expected.equals(result))
            System.out.println("test5(): Success!");
        else
            System.out.println("test5(): fail");
        assertEquals(expected,result);
    }

    /****************************************************************
     */
    @Test
    public void testFloorFn() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testFloorFn(): ");
        Formula f = new Formula("(<=> " +
                "(equal (RemainderFn ?NUMBER1 ?NUMBER2) ?NUMBER) " +
                "(equal (AdditionFn (MultiplicationFn (FloorFn (DivisionFn ?NUMBER1 ?NUMBER2)) ?NUMBER2) ?NUMBER) ?NUMBER1))");
        System.out.println("formula: " + f);
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.testFloorFn(): " + result);
        String expected = "! [V__NUMBER1 : $int,V__NUMBER2 : $int,V__NUMBER : $int] : " +
                "((s__RemainderFn__0In1In2InFn(V__NUMBER1, V__NUMBER2) = V__NUMBER " +
                "=> $sum($product($to_int($quotient_e(V__NUMBER1 ,V__NUMBER2)) ,V__NUMBER2) ,V__NUMBER) = " +
                "V__NUMBER1) & " +
                "($sum($product($to_int($quotient_e(V__NUMBER1 ,V__NUMBER2)) ,V__NUMBER2) ,V__NUMBER) = " +
                "V__NUMBER1 => s__RemainderFn__0In1In2InFn(V__NUMBER1, V__NUMBER2) = V__NUMBER))";
        System.out.println("expect: " + expected);
        System.out.println("equal: " + expected.equals(result));
        if (expected.equals(result))
            System.out.println("testFloorFn(): Success!");
        else
            System.out.println("testFloorFn(): fail");
        assertEquals(expected,result);
    }

    /****************************************************************
     */
    @Test
    public void testNumericSubclass() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testSubclass(): ");
        Formula f = new Formula("(<=> (and (equal (AbsoluteValueFn ?NUMBER1) ?NUMBER2) " +
                "(instance ?NUMBER1 RealNumber) (instance ?NUMBER2 RealNumber)) " +
                "(or (and (instance ?NUMBER1 NonnegativeRealNumber) (equal ?NUMBER1 ?NUMBER2)) " +
                "(and (instance ?NUMBER1 NegativeRealNumber) (equal ?NUMBER2 (SubtractionFn 0 ?NUMBER1)))))");
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.testNumericSubclass(): " + result);
        String expected = "! [V__NUMBER1 : $real,V__NUMBER2 : $real] : " +
                "((s__AbsoluteValueFn__0Re1ReFn(V__NUMBER1) = V__NUMBER2 => " +
                "(((s__SignumFn__0In1ReFn(V__NUMBER1) = 1 | " +
                "s__SignumFn__0In1ReFn(V__NUMBER1) = 0) & V__NUMBER1 = V__NUMBER2) | " +
                "(s__SignumFn__0In1ReFn(V__NUMBER1) = -1 & V__NUMBER2 = " +
                "$difference(0.0 ,V__NUMBER1)))) & ((((s__SignumFn__0In1ReFn(V__NUMBER1) = 1 | " +
                "s__SignumFn__0In1ReFn(V__NUMBER1) = 0) & V__NUMBER1 = V__NUMBER2) | " +
                "(s__SignumFn__0In1ReFn(V__NUMBER1) = -1 & V__NUMBER2 = " +
                "$difference(0.0 ,V__NUMBER1))) => s__AbsoluteValueFn__0Re1ReFn(V__NUMBER1) = V__NUMBER2))";
        System.out.println("expect: " + expected);
        if (expected.equals(result))
            System.out.println("testNumericSubclass(): Success!");
        else
            System.out.println("testNumericSubclass(): fail");
        assertEquals(expected,result);
    }

    /****************************************************************
     */
    @Test
    public void testElimUnitaryLogops() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testElimUnitaryLogops(): ");
        Formula f = new Formula("(<=> (and (equal (AbsoluteValueFn ?NUMBER1) ?NUMBER2)) " +
                "(or (and (instance ?NUMBER1 NonnegativeRealNumber) (equal ?NUMBER1 ?NUMBER2)) " +
                "(and (instance ?NUMBER1 NegativeRealNumber) (equal ?NUMBER2 (SubtractionFn 0 ?NUMBER1)))))");
        String result = SUMOtoTFAform.elimUnitaryLogops(f);
        System.out.println("SUMOtoTFAformTest.testNElimUnitaryLogops(): " + result);
        String expected = "(<=> (equal (AbsoluteValueFn ?NUMBER1) ?NUMBER2) " +
                "(or (and (instance ?NUMBER1 NonnegativeRealNumber) (equal ?NUMBER1 ?NUMBER2)) " +
                "(and (instance ?NUMBER1 NegativeRealNumber) (equal ?NUMBER2 (SubtractionFn 0 ?NUMBER1)))))";
        System.out.println("expect: " + expected);
        if (expected.equals(result))
            System.out.println("testElimUnitaryLogops(): Success!");
        else
            System.out.println("testElimUnitaryLogops(): fail");
        assertEquals(expected,result);
    }

    /****************************************************************
     */
    @Ignore // can't process ListFn
    @Test
    public void testVariableArity() {

        SUMOtoTFAform.debug = true;
        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testVariableArity(): ");
        Formula f = new Formula("(<=> (and (instance ?REL TotalValuedRelation) " +
                "(instance ?REL Predicate)) (exists (?VALENCE) (and (instance ?REL Relation) " +
                "(valence ?REL ?VALENCE) (=> (forall (?NUMBER ?ELEMENT ?CLASS) " +
                "(=> (and (lessThan ?NUMBER ?VALENCE) (domain ?REL ?NUMBER ?CLASS) " +
                "(equal ?ELEMENT (ListOrderFn (ListFn @ROW) ?NUMBER))) " +
                "(instance ?ELEMENT ?CLASS))) (exists (?ITEM) (?REL @ROW ?ITEM))))))");
        System.out.println("formula: " + f);
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.testVariableArity(): result: " + result);
        String expected = "! [V__REL : $i] : (((s__instance(V__REL, s__TotalValuedRelation) & " +
                "s__instance(V__REL, s__Predicate)) => ( ? [V__VALENCE:$int] : " +
                "((s__instance(V__REL, s__Relation) & s__valence__2In(V__REL, V__VALENCE) & " +
                "( ! [V__NUMBER:$int, V__ELEMENT:$i, V__CLASS:$i] : " +
                "(($less(V__NUMBER ,V__VALENCE) & s__domain__2In(V__REL, V__NUMBER, V__CLASS) & " +
                "equal(V__ELEMENT ,s__ListOrderFn__2InFn(s__ListFn(V__ROW), V__NUMBER))) => " +
                "s__instance(V__ELEMENT, V__CLASS))) => ( ? [V__ITEM] : (s__?REL(V__ROW, V__ITEM))))))) & " +
                "(( ? [V__VALENCE:$int] : ((s__instance(V__REL, s__Relation) & " +
                "s__valence__2In(V__REL, V__VALENCE) & ( ! [V__NUMBER:$int, V__ELEMENT:$i, V__CLASS:$i] : " +
                "(($less(V__NUMBER ,V__VALENCE) & s__domain__2In(V__REL, V__NUMBER, V__CLASS) & " +
                "equal(V__ELEMENT ,s__ListOrderFn__2InFn(s__ListFn(V__ROW), V__NUMBER))) => " +
                "s__instance(V__ELEMENT, V__CLASS))) => ( ? [V__ITEM] : (s__?REL(V__ROW, V__ITEM)))))) => " +
                "(s__instance(V__REL, s__TotalValuedRelation) & s__instance(V__REL, s__Predicate))))";
        System.out.println("expect: " + expected);
        System.out.println("equal: " + expected.equals(result));
        if (expected.equals(result))
            System.out.println("testVariableArity(): Success!");
        else
            System.out.println("testVariableArity(): fail");
        assertEquals(expected,result);
    }

    /****************************************************************
     */
    @Ignore // can't process ListFn
    @Test
    public void testVariableArity2() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testVariableArity2(): ");
        Formula f = new Formula("(<=> (and (instance stringLength TotalValuedRelation) " +
                "(instance stringLength Predicate)) (exists (?VALENCE) (and (instance stringLength Relation) " +
                "(valence stringLength ?VALENCE) (=> (forall (?NUMBER ?ELEMENT ?CLASS) " +
                "(=> (and (lessThan ?NUMBER ?VALENCE) (domain stringLength ?NUMBER ?CLASS) " +
                "(equal ?ELEMENT (ListOrderFn (ListFn @ROW) ?NUMBER))) " +
                "(instance ?ELEMENT ?CLASS))) (exists (?ITEM) (stringLength @ROW ?ITEM))))))");
        System.out.println("formula: " + f);
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.testVariableArity2(): actual: " + result);
        String expected = "((s__instance(s__stringLength__m, s__TotalValuedRelation) & " +
                "s__instance(s__stringLength__m, s__Predicate)) => ( ? [V__VALENCE:$int] : " +
                "((s__instance(s__stringLength__m, s__Relation) & " +
                "s__valence__2In(s__stringLength__m, V__VALENCE) & " +
                "( ! [V__NUMBER:$int, V__ELEMENT:$i, V__CLASS:$i] : " +
                "(($less(V__NUMBER ,V__VALENCE) & s__domain__2In(s__stringLength__m, V__NUMBER, V__CLASS) & " +
                "equal(V__ELEMENT ,s__ListOrderFn__2InFn(s__ListFn(V__ROW), V__NUMBER))) => " +
                "s__instance(V__ELEMENT, V__CLASS))) => ( ? [V__ITEM] : " +
                "(s__stringLength(V__ROW, V__ITEM))))))) & (( ? [V__VALENCE:$int] : " +
                "((s__instance(s__stringLength__m, s__Relation) & " +
                "s__valence__2In(s__stringLength__m, V__VALENCE) & " +
                "( ! [V__NUMBER:$int, V__ELEMENT:$i, V__CLASS:$i] : " +
                "(($less(V__NUMBER ,V__VALENCE) & s__domain__2In(s__stringLength__m, V__NUMBER, V__CLASS) & " +
                "equal(V__ELEMENT ,s__ListOrderFn__2InFn(s__ListFn(V__ROW), V__NUMBER))) => " +
                "s__instance(V__ELEMENT, V__CLASS))) => ( ? [V__ITEM] : " +
                "(s__stringLength(V__ROW, V__ITEM)))))) => " +
                "(s__instance(s__stringLength__m, s__TotalValuedRelation) & " +
                "s__instance(s__stringLength__m, s__Predicate)))";
        System.out.println("expect: " + expected);
        System.out.println("equal: " + expected.equals(result));
        if (expected.equals(result))
            System.out.println("testVariableArity2(): Success!");
        else
            System.out.println("testVariableArity2(): fail");
        assertEquals(expected,result);
    }

    /****************************************************************
     */
    @Test
    public void testPredVarArity() {

        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testPredVarArity(): ");
        Formula f = new Formula("(<=>\n" +
                "  (and\n" +
                "    (instance greaterThan__1Ra2Ra TotalValuedRelation)\n" +
                "    (instance greaterThan__1Ra2Ra Predicate))\n" +
                "  (exists (?VALENCE)\n" +
                "    (and\n" +
                "      (greaterThan ?VALENCE 0)\n" +
                "      (and\n" +
                "        (instance greaterThan__1Ra2Ra Relation)\n" +
                "        (valence greaterThan__1Ra2Ra ?VALENCE)\n" +
                "        (=>\n" +
                "          (forall (?NUMBER ?ELEMENT ?CLASS)\n" +
                "            (=>\n" +
                "              (and\n" +
                "                (greaterThan ?NUMBER 0)\n" +
                "                (instance ?CLASS SetOrClass))\n" +
                "              (=>\n" +
                "                (and\n" +
                "                  (lessThan ?NUMBER ?VALENCE)\n" +
                "                  (domain greaterThan__1Ra2Ra ?NUMBER ?CLASS)\n" +
                "                  (equal ?ELEMENT\n" +
                "                    (ListOrderFn\n" +
                "                      (ListFn_1 ?ROW1) ?NUMBER)))\n" +
                "                (instance ?ELEMENT ?CLASS))))\n" +
                "          (exists (?ITEM)\n" +
                "            (and\n" +
                "              (instance ?ITEM RationalNumber)\n" +
                "              (greaterThan__1Ra2Ra ?ROW1 ?ITEM))))))))");
        System.out.println("formula: " + f);
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.testPredVarArity(): actual: " + result);
        String expected = "";
        System.out.println("expect: " + expected);
        System.out.println("equal: " + expected.equals(result));
        if (expected.equals(result))
            System.out.println("testPredVarArity(): Success!");
        else
            System.out.println("testPredVarArity(): fail");
        assertEquals(expected,result);
    }


    /****************************************************************
     */
    @Ignore // can't process ListFn
    @Test
    public void testRemoveNumInst() {

        //SUMOtoTFAform.debug = true;
        System.out.println();
        System.out.println("\n======================== SUMOtoTFAformTest.testRemoveNumInst(): ");
        String sf = "\n" +
                "(<=>\n" +
                "    (and\n" +
                "        (instance initialList__2Ra TotalValuedRelation)\n" +
                "        (instance initialList__2Ra Predicate))\n" +
                "    (exists (?VALENCE)\n" +
                "        (and\n" +
                "            (instance initialList__2Ra Relation)\n" +
                "            (valence initialList__2Ra ?VALENCE)\n" +
                "            (=>\n" +
                "                (forall (?NUMBER ?ELEMENT ?CLASS)\n" +
                "                    (=>\n" +
                "                        (and\n" +
                "                            (lessThan ?NUMBER ?VALENCE)\n" +
                "                            (domain initialList__2Ra ?NUMBER ?CLASS)\n" +
                "                            (equal ?ELEMENT\n" +
                "                                (ListOrderFn\n" +
                "                                    (ListFn @ROW) ?NUMBER)))\n" +
                "                        (instance ?ELEMENT ?CLASS)))\n" +
                "                (exists (?ITEM)\n" +
                "                    (initialList__2Ra @ROW ?ITEM))))))";
        Formula f = new Formula(sf);
        System.out.println("formula: " + f);
        String result = SUMOtoTFAform.process(f);
        System.out.println("SUMOtoTFAformTest.testRemoveNumInst(): actual: " + result);
        String expected = "! [V__ROW : $i] : (((s__instance(s__initialList__2Ra__m, s__TotalValuedRelation) & s__instance(s__initialList__2Ra__m, s__Predicate)) => ( ? [V__VALENCE:$int] : ((s__instance(s__initialList__2Ra__m, s__Relation) & s__valence__2In(s__initialList__2Ra__m, V__VALENCE) & ( ! [V__NUMBER:$int, V__ELEMENT:$i, V__CLASS:$i] : (($less(V__NUMBER ,V__VALENCE) & s__domain__2In(s__initialList__2Ra__m, V__NUMBER, V__CLASS) & equal(V__ELEMENT ,s__ListOrderFn__2InFn(s__ListFn(V__ROW), V__NUMBER))) => s__instance(V__ELEMENT, V__CLASS))) => ( ? [V__ITEM:$rat] : (s__initialList__2Ra(V__ROW, V__ITEM))))))) & (( ? [V__VALENCE:$int] : ((s__instance(s__initialList__2Ra__m, s__Relation) & s__valence__2In(s__initialList__2Ra__m, V__VALENCE) & ( ! [V__NUMBER:$int, V__ELEMENT:$i, V__CLASS:$i] : (($less(V__NUMBER ,V__VALENCE) & s__domain__2In(s__initialList__2Ra__m, V__NUMBER, V__CLASS) & equal(V__ELEMENT ,s__ListOrderFn__2InFn(s__ListFn(V__ROW), V__NUMBER))) => s__instance(V__ELEMENT, V__CLASS))) => ( ? [V__ITEM:$rat] : (s__initialList__2Ra(V__ROW, V__ITEM)))))) => (s__instance(s__initialList__2Ra__m, s__TotalValuedRelation) & s__instance(s__initialList__2Ra__m, s__Predicate))))";
        System.out.println("expect: " + expected);
        System.out.println("equal: " + expected.equals(result));
        if (expected.equals(result))
            System.out.println("testRemoveNumInst(): Success!");
        else
            System.out.println("testRemoveNumInst(): fail");
        assertEquals(expected,result);
    }
}