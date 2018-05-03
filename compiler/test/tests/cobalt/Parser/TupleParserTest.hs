module Parser.TupleParserTest where

import Test.HUnit

import Text.Megaparsec

import TestUtil.ParserTestUtil
import AST.AST
import Parser.ExprParser

testTupleParser :: Test
testTupleParser = do
    let code = "(x, y, z)"
    let test = testParseSuccess code (Tuple (BlockExpr [Identifier (Name "x"),Identifier (Name "y"),Identifier (Name "z")])) tupleParser

    TestList [test]
