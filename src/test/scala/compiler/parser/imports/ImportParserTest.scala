/*
 * Cobalt Programming Language Compiler
 * Copyright (C) 2017  Michael Haywood
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package compiler.parser.imports

import compiler.block.Block
import compiler.block.imports.ImportBlock
import compiler.parser.ParserTest
import compiler.tokenizer.TokenizerTest

class ImportParserTest extends ParserTest[ImportBlock] {

  def shouldParse(line: String): Boolean = line.matches("import [a-zA-Z][a-zA-Z0-9]*(\\.[a-zA-Z][a-zA-Z0-9]*)*")

  def parse(superBlock: Block, tokenizer: TokenizerTest): ImportBlock = {

    tokenizer.nextToken // "import"

    var fileLoc: String = tokenizer.nextToken.token
    // Get the string value of the next token.;
    var nextToken: String = tokenizer.nextToken.token
    var fileName: String = nextToken

    while (nextToken != "") {
      {
        if (nextToken == ".") {
          fileLoc += "/"
        }
        else {
          fileLoc += nextToken
        }
        fileName = nextToken
        nextToken = tokenizer.nextToken.token
      }
    }
    val i: Int = fileLoc.lastIndexOf("/")
    fileLoc = if ((i > -1)) fileLoc.substring(0, i)
    else fileLoc
    return new ImportBlock(fileLoc, fileName)
  }
}
