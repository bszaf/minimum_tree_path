import scala.io.StdIn

case class Cell (
  upLeft: Option[Cell],
  upRight: Option[Cell],
  cameFrom: Option[Cell],
  value: Int
)

type Triangle = List[Cell]

object Cell {
 def apply(value: Int) = new Cell(None, None, None, value)
 def apply(value: String) = new Cell(None, None, None, value.toInt)
}

def updateCell(cell : Cell, previousRow : List[Cell], index: Int) : Cell = {
  Cell(previousRow.lift(index - 1), previousRow.lift(index), None, cell.value)
}


def maybeUpdateUpCells(currentRow : List[Cell], previousRow : List[Cell]) : List[Cell] =
  previousRow match {
    case Nil => currentRow
    case _ => {
      val (_, newRow) =
        currentRow.foldLeft((0, List[Cell]()))( (acc, elem) => {
          val (i, cellAcc) = acc
          val newCellAcc = updateCell(elem, previousRow, i) :: cellAcc
          (i+1, newCellAcc)
        })
      newRow.reverse
    }
  }

def linesToTriangle(list: List[String], rows: List[Cell] = List()): Triangle = list match {
  case Nil => rows
  case h :: t => {
    val row : List[Cell] = h.split(" ").toList.map(x => Cell(x))
    val newRows = maybeUpdateUpCells(row, rows)
    linesToTriangle(t, newRows)
  }
}

def goUpOnce(row : List[Cell]) : List[Cell] = {
  row match {
    case a :: b :: tail if a.value <= b.value => {
      val up = a.upRight.get
      val newVal = a.value + up.value
      val res = up.copy(value = newVal, cameFrom = Some(a))
      res :: goUpOnce(b :: tail)
    }
    case a :: b :: tail if a.value > b.value => {
      val up = b.upLeft.get
      val newVal = b.value + up.value
      val res = up.copy(value = newVal, cameFrom = Some(b))
      res :: goUpOnce(b :: tail)
    }
    case a :: Nil => Nil
  }
}

def goUp(row : List[Cell]) : Cell = {
  row match {
    case List(cell) => cell
    case list => goUp(goUpOnce(list))
  }
}

def goDown(head : Option[Cell], lastValue : Option[Int] = None, acc : List[Int] = List()) : List[Int] = head match {
  case Some(cell) => {
    lastValue match {
      case Some(v) => {
        goDown(cell.cameFrom, Some(cell.value), (v - cell.value) :: acc)
      }
      case None => goDown(cell.cameFrom, Some(cell.value), acc)
    }
  }
  case None => (lastValue.get :: acc).reverse
}

def readInput(lines : List[String] = List()) : List[String] = {
  val l = StdIn.readLine()
  if (l == null) {
    lines.reverse
  } else {
    readInput(l :: lines)
  }
}

val lines = readInput()

val triangle = linesToTriangle(lines)
val topCell = goUp(triangle)
val result = goDown(Some(topCell))

println(s"Minimal path is: ${result.mkString(" + ")} = ${topCell.value}")
