package com.github.samyadaleh.cltoolbox.common.cfg;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Objects;

import org.junit.Test;

import com.github.samyadaleh.cltoolbox.common.TestGrammarLibrary;
import com.github.samyadaleh.cltoolbox.common.cfg.Cfg;

public class CfgTest {

  @Test public void testBinarization() {
    assertTrue(!Objects.requireNonNull(TestGrammarLibrary.longRhsCfg()).isBinarized());
    Cfg cfgbin = Objects.requireNonNull(TestGrammarLibrary.longRhsCfg()).getBinarizedCfg();
    assertTrue(cfgbin.isBinarized());
  }

  @Test public void testRemoveEpsilon() {
    assertTrue(Objects.requireNonNull(TestGrammarLibrary.epsCfg()).hasEpsilonProductions());
    Cfg epsfree = Objects.requireNonNull(TestGrammarLibrary.epsCfg()).getCfgWithoutEmptyProductions();
    assertTrue(!epsfree.hasEpsilonProductions());
    assertEquals(
      "G = <N, T, S, P>\n" + "N = {S, A, B, C, S1}\n" + "T = {a, b}\n"
        + "S = S1\n"
        + "P = {S -> b A a S b C, A -> a, A -> b B, B -> b, S -> b a S b C,"
        + " S -> b A a b C, S -> b a b C, S1 -> S, S1 -> ε, S -> b A a S b,"
        + " S -> b a S b, S -> b A a b, S -> b a b}\n" + "",
      epsfree.toString());
  }

  @Test public void testReplaceTerminals() {
    Cfg treplaced = Objects.requireNonNull(TestGrammarLibrary.eftCfg())
      .getCfgWithEitherOneTerminalOrNonterminalsOnRhs();
    assertEquals("G = <N, T, S, P>\n"
      + "N = {I, F, T, E, Y1, Y2, Y3, Y4, Y5, Y6, Y7, Y8}\n"
      + "T = {a, b, 0, 1, (, ), *, +}\n" + "S = E\n"
      + "P = {I -> a, I -> b, Y1 -> a, I -> I Y1, Y2 -> b, I -> I Y2, "
      + "Y3 -> 0, I -> I Y3, Y4 -> 1, I -> I Y4, F -> I, Y5 -> (, Y6 -> ), "
      + "F -> Y5 E Y6, T -> F, Y7 -> *, T -> T Y7 F, E -> T, Y8 -> +, "
      + "E -> E Y8 T}\n", treplaced.toString());
  }

  @Test public void testToCnf() {
    Cfg cfgcnf = Objects.requireNonNull(TestGrammarLibrary.eftCfg()).getCfgWithoutEmptyProductions()
      .getCfgWithoutNonGeneratingSymbols().getCfgWithoutNonReachableSymbols()
      .getBinarizedCfg().getCfgWithEitherOneTerminalOrNonterminalsOnRhs()
      .getCfgWithoutChainRules();
    assertTrue(cfgcnf.isInChomskyNormalForm());
    assertEquals("G = <N, T, S, P>\n"
      + "N = {I, F, T, E, X1, X2, X3, Y1, Y2, Y3, Y4, Y5, Y6, Y7, Y8}\n"
      + "T = {a, b, 0, 1, (, ), *, +}\n" + "S = E\n"
      + "P = {I -> a, I -> b, Y1 -> a, I -> I Y1, Y2 -> b, I -> I Y2, "
      + "Y3 -> 0, I -> I Y3, Y4 -> 1, I -> I Y4, Y5 -> (, F -> Y5 X1, "
      + "Y6 -> ), X1 -> E Y6, T -> T X2, Y7 -> *, X2 -> Y7 F, E -> E X3, "
      + "Y8 -> +, X3 -> Y8 T, F -> a, F -> b, F -> I Y1, F -> I Y2, "
      + "F -> I Y3, F -> I Y4, T -> Y5 X1, E -> T X2}\n", cfgcnf.toString());
  }

  @Test public void testToC2f() {
    assertTrue(Objects.requireNonNull(TestGrammarLibrary.eftCfg()).getCfgWithoutEmptyProductions()
      .getCfgWithoutNonGeneratingSymbols().getCfgWithoutNonReachableSymbols()
      .getBinarizedCfg().getCfgWithEitherOneTerminalOrNonterminalsOnRhs()
      .isInCanonicalTwoForm());
  }

  @Test public void testRemoveDirectLeftRecursion() {
    Cfg cfgwlr = Objects
        .requireNonNull(TestGrammarLibrary.directLeftRecursionCfg())
      .getCfgWithoutDirectLeftRecursion();
    assertEquals(
      "G = <N, T, S, P>\n" + "N = {S, S1}\n" + "T = {a, b, c, d}\n" + "S = S\n"
        + "P = {S1 -> ε, S -> d S1, S -> c S1, S1 -> b S1, S1 -> a S1}\n",
      cfgwlr.toString());
  }

  @Test public void testRemoveDirectLeftRecursion2() throws ParseException {
    Cfg cfgwlr =
      Objects.requireNonNull(TestGrammarLibrary.directLeftRecursionCfg()).getCfgWithoutLeftRecursion();
    assertEquals(
      "G = <N, T, S, P>\n" + "N = {S, S1}\n" + "T = {a, b, c, d}\n" + "S = S\n"
        + "P = {S1 -> ε, S -> d S1, S -> c S1, S1 -> b S1, S1 -> a S1}\n",
      cfgwlr.toString());
  }

  @Test public void testRemoveIndirectLeftRecursion() throws ParseException {
    Cfg cfgwlr = Objects
        .requireNonNull(TestGrammarLibrary.indirectLeftRecursionCfg())
      .getCfgWithoutEmptyProductions().getCfgWithoutNonGeneratingSymbols()
      .getCfgWithoutNonReachableSymbols().getCfgWithoutLeftRecursion();
    assertEquals("G = <N, T, S, P>\n" + 
      "N = {S, A, A1}\n" + 
      "T = {a, b}\n" + 
      "S = S\n" + 
      "P = {S -> A a, S -> b, A1 -> ε, A -> b a A1, A1 -> a a A1}\n" + 
      "", cfgwlr.toString());
  }

  @Test public void testRemoveLeftRecursionNoTermination()
    throws ParseException {
    Cfg cfgwlr = Objects
        .requireNonNull(TestGrammarLibrary.leftRecursionNoTerminationCfg())
      .getCfgWithoutLeftRecursion();
    assertNull(cfgwlr);
  }

  @Test public void testRemoveNotReachableSymbols() {
    Cfg after = Objects
        .requireNonNull(TestGrammarLibrary.nonReachableSymbolsCfg())
      .getCfgWithoutNonReachableSymbols();
    assertEquals("G = <N, T, S, P>\n" + "N = {S}\n" + "T = {a}\n" + "S = S\n"
      + "P = {S -> a}\n", after.toString());
  }

  @Test public void testRemoveNonGeneratingSymbols() {
    Cfg after = Objects
        .requireNonNull(TestGrammarLibrary.nonGeneratingSymbolsCfg())
      .getCfgWithoutNonGeneratingSymbols();
    assertEquals("G = <N, T, S, P>\n" + "N = {S}\n" + "T = {a}\n" + "S = S\n"
      + "P = {S -> a}\n", after.toString());

    assertNull(
        Objects.requireNonNull(TestGrammarLibrary.noUsefulNonterminalCfg())
      .getCfgWithoutNonGeneratingSymbols());
  }

  @Test public void testCreateCfgFromPcfg() {
    Cfg cfg = new Cfg(TestGrammarLibrary.banPcfg());
    assertEquals("G = <N, T, S, P>\n" + "N = {S, A, B}\n" + "T = {a, b}\n"
      + "S = S\n" + "P = {S -> A B, A -> b, A -> a, B -> B B, B -> a}\n",
      cfg.toString());
  }
}
