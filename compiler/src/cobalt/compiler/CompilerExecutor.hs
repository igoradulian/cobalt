{-|
Module      : CompilerExecutor
Description : Calls all of the functions to compile and generate the code.
-}
module CompilerExecutor where

import System.Environment
import Control.Monad

import Compiler
import SymbolTable
import CommandLineUtils (commandLineArgs, CommandLineArgument (..), raiseErrorsException )
import IOUtils
import Utils

execute :: IO ()
execute = do
  args <- getArgs
  let isFileToCompile (FileToCompile _ ) = True
      isFileToCompile _ = False
  let isClassPath (ClassPath _ ) = True
      isClassPath _ = False
  let isDestinationDir (DestinationDir _ ) = True
      isDestinationDir _ = False

  options <- commandLineArgs args

  let helpPresent = elem (Help) options
  let versionPresent = elem (Version) options
  let debugModeOn = elem (DebugMode) options
  let verboseModeOn = elem (VerboseMode) options
  let generateDebugSymbolsOn = elem (GenerateDebugSymbols) options

  let inputFiles = map (\(FileToCompile x) -> x) (filter isFileToCompile options)

  -- if multiple class paths or destination dirs are provided, first is taken instead of throwing error
  -- Note: javac takes the last and does not produce error either
  -- classpath is set to "cobalt_generated_java/" temporarly until we stop compiling to java. Original default was "./"
  let classOutputDir = (\(DestinationDir dd) -> dd) . (defaultHead (DestinationDir "cobalt_generated_classes/")) $ filter isDestinationDir options

  when debugModeOn (putStrLn "Running compiler in debug mode")
  when versionPresent printVersion
  when helpPresent printHelpInfo

  -- in case of using help option the compiler itself is not run, only appropiate message is produced
  if helpPresent
    then return ()
    else
      if (inputFiles == [])
        then do
          _ <- raiseErrorsException ["no source files specified\n"]
          return ()
        else do
          cleanDir (endsWith ".class") classOutputDir
          compile inputFiles classOutputDir
