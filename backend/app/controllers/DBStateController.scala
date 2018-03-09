package controllers

import javax.inject._

import play.api.mvc._
import services.DBConn
import play.api.libs.json._
import java.sql._

/**
 * This controller demonstrates how to use dependency injection to
 * bind a component into a controller class. The class creates an
 * `Action` that shows an incrementing count to users. The [[Counter]]
 * object is injected by the Guice dependency injection system.
 */
@Singleton
class DBStateController @Inject() (cc: ControllerComponents,
                                 connection: DBConn) extends AbstractController(cc) {

  /**
   * Create an action that responds with the [[Counter]]'s current
   * count. The result is plain text. This `Action` is mapped to
   * `GET /count` requests by an entry in the `routes` config file.
   */
  def state = Action { Ok(JsObject(Map("open" -> JsBoolean(!connection.connection().isClosed), "readonly" -> JsBoolean(connection.connection().isReadOnly())))) }
  
  def putCategory = Action { request => 
	val stmt = connection.connection().createStatement()
	stmt.executeUpdate("INSERT INTO `ufys`.`category_type`" +
		"(`category_name`) VALUES" +
		"(" + "'blah'" + ");")
	stmt.close()
	Ok
  }
  
  def taskCategories = Action {
	val stmt = connection.connection().createStatement()
	val rs = stmt.executeQuery("SELECT * FROM ufys.category_type")
	var res = JsObject(Map[String,JsObject]())
	while(rs.next()) {
		val nw = (rs.getString(2)->Json.toJson(rs.getInt(1).toString))
		res = res + nw
	}
	Ok(res)
  }

}
