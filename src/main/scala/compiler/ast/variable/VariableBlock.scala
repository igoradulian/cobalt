/*
 * Cobalt Programming Language Compiler
 * Copyright (C) 2017  Cobalt
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

package compiler.ast.variable

import compiler.ast.Block
import compiler.ast.operators.assignment.AssignmentOpBlock
import compiler.symbol_table.{Row, SymbolTable}
import compiler.utilities.{ReversePolish, Utils}

/**
  * Represents a variable reference
  *
  * @param superBlockInit
  * @param name
  */
class VariableBlock(superBlockInit: Block, name: String) extends Block(superBlockInit, false, true) {

  val row: Row = SymbolTable.getInstance.getValue(Utils.getMethod(this).get, name)

  override def getName: String = name

  override def getValue: String = ""

  override def getType: String = row.getType

  override def getOpeningCode(): String = {
    if (Utils.getMethod(this) != null) {

      // Get assigned blocks in reverse polish notation
      val rpnString: String = if (stack.nonEmpty && stack.head.isInstanceOf[AssignmentOpBlock]) ReversePolish.infixToRPN(stack.drop(1).toList).map(b => b.getOpeningCode).mkString("\n") else ""


      row.getType match {

        // Primitives
        case "char" => "mv.visitVarInsn(ILOAD" + "," + row.getId + ");\n"
        case "byte" => "mv.visitVarInsn(ILOAD" + "," + row.getId + ");\n"
        case "short" => "mv.visitVarInsn(ILOAD" + "," + row.getId + ");\n"
        case "int" => "mv.visitVarInsn(ILOAD" + "," + row.getId + ");\n"
        case "long" => "mv.visitVarInsn(LLOAD" + "," + row.getId + ");\n"
        case "float" => "mv.visitVarInsn(FLOAD" + "," + row.getId + ");\n"
        case "double" => "mv.visitVarInsn(DLOAD" + "," + row.getId + ");\n"

        // Objects
        case "Char" => "mv.visitVarInsn(ALOAD" + "," + row.getId + ");\n"
        case "Byte" => "mv.visitVarInsn(ALOAD" + "," + row.getId + ");\n"
        case "Short" => "mv.visitVarInsn(ALOAD" + "," + row.getId + ");\n"
        case "Int" => "mv.visitVarInsn(ALOAD" + "," + row.getId + ");\n"
        case "Long" => "mv.visitVarInsn(ALOAD" + "," + row.getId + ");\n"
        case "Float" => "mv.visitVarInsn(ALOAD" + "," + row.getId + ");\n"
        case "Double" => "mv.visitVarInsn(ALOAD" + "," + row.getId + ");\n"
        case "String" => "mv.visitVarInsn(ALOAD" + "," + row.getId + ");\n"
        case _ => "mv.visitVarInsn(ALOAD" + "," + row.getId + ");\n" + stack.map(_.getOpeningCode).mkString("\n")
      }
    }
    else {
      ""
    }
  }

  def unwrapCode(): String ={
    // Calls methods to get value from wrapper class
    val convertString: String = {
      if (superBlockInit.isInstanceOf[DefineVariableBlock]) {
        row.getType match {
          case "Char" => "mv.visitMethodInsn(INVOKEVIRTUAL, \"java/lang/Character\", \"charValue\", \"()C\", false);\n"
          case "Byte" => "mv.visitMethodInsn(INVOKEVIRTUAL, \"java/lang/Byte\", \"byteValue\", \"()B\", false);\n"
          case "Short" => "mv.visitMethodInsn(INVOKEVIRTUAL, \"java/lang/Short\", \"shortValue\", \"()S\", false);\n"
          case "Int" => "mv.visitMethodInsn(INVOKEVIRTUAL, \"java/lang/Integer\", \"intValue\", \"()I\", false);\n"
          case "Long" => "mv.visitMethodInsn(INVOKEVIRTUAL, \"java/lang/Long\", \"longValue\", \"()J\", false);\n"
          case "Float" => "mv.visitMethodInsn(INVOKEVIRTUAL, \"java/lang/Float\", \"floatValue\", \"()F\", false);\n"
          case "Double" => "mv.visitMethodInsn(INVOKEVIRTUAL, \"java/lang/Double\", \"doubleValue\", \"()D\", false);\n"
          case _ => ""
        }
      } else {
        ""
      }
    }
    convertString
  }

  override def getClosingCode: String = {
    ""
  }

  override def toString: String = "variable: " + name + " " + stack

}