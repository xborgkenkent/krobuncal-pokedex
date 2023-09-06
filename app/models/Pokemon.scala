package models

import play.api.libs.ws._

import scala.collection.mutable.ListBuffer
import play.api.libs.json._
import play.api.libs.json
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._



case class Pokemon(id: Int, name: String, abilities: Option[List[Abilities]])

case class Abilities(ability: Option[AbilityItem], slot: Int, is_hidden: Boolean)

case class AbilityItem(name: String, url: String)

// case class Moves(move: List[MoveItem])

// case class MoveItem(name: String)

// case class Types(`type`: List[TypesItem])

// case class TypesItem(name: String)




object Pokemon {
    val listOfPokemonNames: ListBuffer[String] = ListBuffer.empty
    

    val listOfPokemonNamesWithDescription: ListBuffer[Pokemon] = ListBuffer.empty


    implicit val pokemonReads: Reads[Pokemon] = (
        (JsPath \ "id").read[Int] and
        (JsPath \ "name").read[String] and
        (JsPath \ "abilities").readNullable[List[Abilities]]
    )(Pokemon.apply _)

  implicit val pokemonWrites: Writes[Pokemon] = (
        (JsPath \ "id").write[Int] and
        (JsPath \ "name").write[String] and
        (JsPath \ "abilities").writeNullable[List[Abilities]]
    )(unlift(Pokemon.unapply))
}

object Abilities {
    
    implicit val abilitiesReads: Reads[Abilities] = (
        (JsPath \ "ability").readNullable[AbilityItem] and
        (JsPath \ "slot").read[Int] and
        (JsPath \ "is_hidden").read[Boolean]

    )(Abilities.apply _)

  implicit val abilitiesWrites: Writes[Abilities] = (
        (JsPath \ "ability").writeNullable[AbilityItem] and
        (JsPath \ "slot").write[Int] and 
        (JsPath \ "is_hidden").write[Boolean]
    )(unlift(Abilities.unapply))
}

object AbilityItem {
    
    implicit val abilityItemReads: Reads[AbilityItem] = (
        (JsPath \ "name").read[String] and
        (JsPath \ "name").read[String]
    )(AbilityItem.apply _)

  implicit val abilityItemWrites: Writes[AbilityItem] = (
        (JsPath \ "name").write[String] and 
        (JsPath \ "url").write[String]
    )(unlift(AbilityItem.unapply))
}
