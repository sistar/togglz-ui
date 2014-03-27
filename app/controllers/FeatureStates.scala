package controllers

import scala.concurrent.ExecutionContext.Implicits.global

import models.FeatureState
import models.FeatureState.featureStateFormat
import models.FeatureState.FeatureStateBSONReader
import models.FeatureState.FeatureStateBSONWriter

import play.api.libs.json.Json
import play.api.mvc.Action
import play.api.mvc.Controller
import play.modules.reactivemongo.MongoController
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentIdentity
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer


object FeatureStates extends Controller with MongoController {
  val collection = db[BSONCollection]("featureStates")

  /** list all featureStates */
  def index = Action.async {
    val cursor = collection.find(
      BSONDocument(), BSONDocument()).cursor[FeatureState] // get all the fields of all the featureStates
    val futureList = cursor.collect[Vector]()
    futureList.map {
      featureStates => Ok(Json.toJson(featureStates))
    } // convert it to a JSON and return it
  }

  /** create a featureState from the given JSON */
  def create() = Action.async(parse.json) {
    request =>

      val name = request.body.\("name").toString().replace("\"", "")
      val enabled = request.body.\("enabled").toString.toBoolean
      val featureState = FeatureState(Option(BSONObjectID.generate), name, enabled) // create the featureState
      collection.insert(featureState).map(
        _ => Ok(Json.toJson(featureState))) // return the created featureState in a JSON

  }

  /** retrieve the featureState for the given id as JSON */
  def show(id: String) = Action.async(parse.empty) {
    request =>

      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
    // get the featureState having this id (there will be 0 or 1 result)
    val futureFeatureState = collection.find(BSONDocument("_id" -> objectID)).one[FeatureState]
      futureFeatureState.map {
        featureState => Ok(Json.toJson(featureState))
      }

  }

  /** update the featureState for the given id from the JSON body */
  def update(id: String) = Action.async(parse.json) {
    request =>

      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
    val name = request.body.\("name").toString().replace("\"", "")
      val enabled = request.body.\("enabled").toString.toBoolean


      val modifier = BSONDocument(// create the modifier featureState
        "$set" -> BSONDocument(
          "name" -> name,
          "enabled" -> enabled))
      collection.update(BSONDocument("_id" -> objectID), modifier).map(
        _ => Ok(Json.toJson(FeatureState(Option(objectID), name, enabled)))) // return the modified featureState in a JSON

  }

  /** delete a featureState for the given id */
  def delete(id: String) = Action.async(parse.empty) {
    request =>

      val objectID = new BSONObjectID(id) // get the corresponding BSONObjectID
      collection.remove(BSONDocument("_id" -> objectID)).map(// remove the featureState
        _ => Ok(Json.obj())).recover {
        case _ => InternalServerError
      } // and return an empty JSON while recovering from errors if any

  }
}
