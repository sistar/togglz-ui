package models

import play.api.libs.json.Json
import play.api.libs.functional.syntax.functionalCanBuildApplicative
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import reactivemongo.bson.BSONDocument
import reactivemongo.bson.BSONDocumentReader
import reactivemongo.bson.BSONDocumentWriter
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONObjectIDIdentity
import reactivemongo.bson.BSONStringHandler
import reactivemongo.bson.Producer.nameValue2Producer
import play.modules.reactivemongo.json.BSONFormats.BSONObjectIDFormat


case class FeatureState(id: Option[BSONObjectID], name: String, enabled: Boolean)

object FeatureState {
  implicit val featureStateFormat = Json.format[FeatureState]

  implicit object FeatureStateBSONWriter extends BSONDocumentWriter[FeatureState] {
    def write(featureState: FeatureState): BSONDocument =
      BSONDocument(
        "_id" -> featureState.id.getOrElse(BSONObjectID.generate),
        "name" -> featureState.name,
        "enabled" -> featureState.enabled
	  )
  }

  implicit object FeatureStateBSONReader extends BSONDocumentReader[FeatureState] {
    def read(doc: BSONDocument): FeatureState =
		FeatureState(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get,
        doc.getAs[Boolean]("enabled").get
        )
  }
}