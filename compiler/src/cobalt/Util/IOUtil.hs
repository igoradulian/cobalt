{-|
Module      : IOUtils
Description : Contains functions used for working with IO.
-}
module Util.IOUtil where

import Control.Monad
import System.Console.GetOpt (getOpt, ArgOrder( Permute ))
import System.Directory
import System.FilePath.Posix

import Util.CommandLineUtil (helpInfo)
import Util.GeneralUtil (endsWith)

printVersion :: IO()
printVersion = putStrLn "cobalt 0.1.x"

printHelpInfo :: IO()
printHelpInfo = putStrLn helpInfo

allFilesIn :: FilePath -> IO [FilePath]
allFilesIn dir = getDirectoryContents dir

cleanDir :: (FilePath -> Bool) -> String -> IO()
cleanDir extensionPredicate directory = do
    createDirectoryIfMissing True directory
    allFilesIn directory >>= mapM (\inputLoc ->
        if (endsWith "" inputLoc)
        then cleanDir extensionPredicate (directory ++ inputLoc ++ "/")
        else if(extensionPredicate inputLoc)
             then removeFile (directory ++ inputLoc)
             else return ()
        )
    return ()

-- | Traverse from 'top' directory and return all the files by
-- filtering out the 'exclude' predicate.
traverseDir :: FilePath -> FilePath -> IO [FilePath]
traverseDir inputDir top = do
    ds <- getDirectoryContents $ inputDir ++ top
    paths <- forM (ds) $ \d -> do
        let path = top </> d
        if endsWith "" (inputDir ++ path)
            then traverseDir (inputDir) path
            else return [path]
    return (concat paths)

generateMissingDirectories :: String -> String -> IO()
generateMissingDirectories inputDir outputDir = do
    allFilesIn inputDir >>= mapM (\inputLoc -> do
        createDirectoryIfMissing True outputDir
        when (endsWith "" inputLoc) $ do
            generateMissingDirectories (inputDir ++ inputLoc ++ "/") (outputDir ++ inputLoc ++ "/")
        )
    return ()

flattenDirectory :: FilePath -> IO ([FilePath])
flattenDirectory dir= do
    contents <- listDirectory formattedDir
    flattened <- mapM recurse contents
    return $ concat flattened
  where --sanity check for empty dir sould not be needed as empty directory cant be passed as argument
      formattedDir = if last dir == '/' then dir else dir ++ "/"
      recurse inputLoc = if (takeExtension inputLoc == ".cobalt")
                           then return $ [formattedDir ++ inputLoc]
                           else do
                               isDirectory <- doesDirectoryExist (formattedDir ++ inputLoc)
                               if isDirectory
                               then flattenDirectory (formattedDir ++ inputLoc)
                               else return $ []
