package services

import javax.inject._
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.jdbc.Driver

/**
 * This trait demonstrates how to create a component that is injected
 * into a controller. The trait represents a counter that returns a
 * incremented number each time it is called.
 */
trait DBConn {
  def connection(): Connection
}

/**
 * This class is a concrete implementation of the [[Counter]] trait.
 * It is configured for Guice dependency injection in the [[Module]]
 * class.
 *
 * This class has a `Singleton` annotation because we need to make
 * sure we only use one counter per application. Without this
 * annotation we would get a new instance every time a [[Counter]] is
 * injected.
 */
@Singleton
class SingletonConnection extends DBConn {  
  private val conn = {
	DriverManager.registerDriver(new Driver())
	DriverManager.getConnection("jdbc:mysql://localhost/ufys?" +
		"user=app&password=app")
  }
  override def connection(): Connection = conn
}
