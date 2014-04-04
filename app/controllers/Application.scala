package controllers

import java.io.File

import play.Play
import play.api.mvc.Action
import play.api.mvc.Controller

object Application extends Controller {
  def index(any: String) = Action {
    Ok(views.html.index())
  }

  def getURI(any: String): String = any match {
    case "main" => "/public/html/main.html"
    case "detail" => "/public/html/detail.html"
    case _ => "error"
  }
  
  def loadPublicHTML(any: String) = Action {
    val projectRoot = Play.application().path()
    var file = new File(projectRoot + getURI(any))
    if (file.exists())
      Ok(scala.io.Source.fromFile(file.getCanonicalPath()).mkString).as("text/html");
    else
      NotFound
  }
}