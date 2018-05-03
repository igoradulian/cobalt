module Parser.ModelParserTest where

import Test.HUnit
import Text.Megaparsec

import TestUtil.ParserTestUtil
import AST.AST
import Parser.ExprParser

testModelParser :: Test
testModelParser = do
    let code = "class Test"
    let test = testParseSuccess code (Model (Name "Test") [] [] Nothing [] [] (BlockStmt [])) modelParser

    let codeInner = unlines [ "class OuterClass"
                            , "    class InnerClass"]
    let testInner = testParseSuccess codeInner (Model {modelName = Name "OuterClass", modelModifiers = [], modelFields = [], modelParent = Nothing, modelParentArguments = [], modelInterfaces = [], modelBody = BlockStmt [ModelDef (Model {modelName = Name "InnerClass", modelModifiers = [], modelFields = [], modelParent = Nothing, modelParentArguments = [], modelInterfaces = [], modelBody = (BlockStmt [])})]}) modelParser

    TestList [ test
             , testInner
             ]
