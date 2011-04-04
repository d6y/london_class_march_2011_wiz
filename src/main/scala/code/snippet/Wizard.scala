package code
package snippet

import model.User

import net.liftweb._
import common._
import http._
import wizard._
import util._
import Helpers._

class WizExample extends Wizard {
  object user extends WizardVar(User.currentUser openOr User.create)

  val s1 = new Screen {
    def commonv: List[FilterOrValidate[String]] = List(
      valMinLen(5, "Too short"),
      valMinLen(4, "Too short, REALLY"),
      trim,
      valMaxLen(32, "Gee, you're verbose"),
      toLower)

    val i1 = field("Hello "+randomString(4) , "dog",
                   commonv :_*)

    val b1 = field("Are you hungry?", false)

    override def nextScreen = if (b1) Full(s3) else Full(s2)
  }

  val s2: Screen = new Screen {
    val i1 = field("Hello screen 2", s1.i1 +" Howdy", valMinLen(5, "Too short"))
    val b1 = field("Are you hungry?", false)
    val sel = select("Favorite color", "Red", List("Red", "Blue", "Green"))
    addFields(() => user.firstName)
    addFields(() => user.lastName)
    override def finish() {
      println("****** I just finished screen 2")
      S.notice("I just finished screen 2")
    }
  }

  val s3: Screen = new Screen {
    override def screenTop = <span>Dude... where's my car?</span>
    override def confirmScreen_? = true
  }


  def finish() {
    S.notice("Thank you for playing "+(s1.i1.length * 5))
  }
}
