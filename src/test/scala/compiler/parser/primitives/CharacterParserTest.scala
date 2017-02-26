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

package compiler.parser.primitives

import compiler.block.Block
import compiler.block.primitives.CharacterBlockTest
import compiler.parser.ParserTest
import compiler.tokenizer.TokenizerTest

class CharacterParserTest extends ParserTest[CharacterBlockTest] {

  def shouldParse(line: String): Boolean = line.matches("(val|var)[ ]+[a-zA-Z][a-zA-Z0-9]*[ ]*:[ ]*char[ ]*[=][ ]*'[a-zA-Z0-9]'[ ]*")

  def parse(superBlock: Block, tokenizer: TokenizerTest): CharacterBlockTest = {
    val declaration: Boolean = tokenizer.nextToken.token == "val"
    // "val" or "var"
    val name: String = tokenizer.nextToken.token //get name
    tokenizer.nextToken // skip ":"
    tokenizer.nextToken // skip "char"
    tokenizer.nextToken
    // skip "="
    val value: String = tokenizer.nextToken.token
    new CharacterBlockTest(superBlock, declaration, name, value)
  }
}
