package controllers

import models._

import javax.inject._
import play.api._
import play.api.mvc._

import play.api.libs.json._
import play.api.libs.json

import play.api.libs.ws._
import scala.concurrent.ExecutionContext
/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */


@Singleton
class PokedexController @Inject()(ws: WSClient, val controllerComponents: ControllerComponents)(implicit ex: ExecutionContext) extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  val api = "https://pokeapi.co/api/v2/pokemon"

  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.index(Pokemon.listOfPokemonNames.toList, Pokemon.listOfPokemonNamesWithDescription.toList))
  }

  def fetchListOfNames = Action.async { implicit request =>
    Pokemon.listOfPokemonNames.clear()
    Pokemon.listOfPokemonNamesWithDescription.clear()
    val json = ws.url(api+"?limit=20").get.map{
      response => (response.json \\ "results")
    }

    json.map{
      x=> { val xxx = ((x.map(a=>a.\\("name"))))
      val aaa = xxx.map(_.map(_.as[String]))
      aaa.map(_.map(a=>Pokemon.listOfPokemonNames += a))

      val json1 = Pokemon.listOfPokemonNames.map{name =>ws.url(api+ s"/${name}").get.map{
        response => (response.json)
      } }

      json1.map{
        jj => jj.map{
        res => {
          val aaa = (res.asOpt[Pokemon])
          //aaa.map(x=>Pokemon(x.id, x.name, x.abilities))
          Pokemon.listOfPokemonNamesWithDescription += aaa.map(x=>Pokemon(x.id, x.name, x.abilities)).get
          }
        }
      }
      
      println(Pokemon.listOfPokemonNames.size)
      println(Pokemon.listOfPokemonNamesWithDescription)
      Redirect(routes.PokedexController.index())
      }
    }
    
  }


  def getPokemonInfo(name: String) = Action.async { implicit request =>
    val json = ws.url(api+ s"/${name}").get.map{
      response => (response.json)
    }

    json.map{
      res => {
        val aaa = (res.asOpt[Pokemon])
        aaa.map(x=>Pokemon(x.id, x.name, x.abilities))
        Pokemon.listOfPokemonNamesWithDescription += aaa.map(x=>Pokemon(x.id, x.name, x.abilities)).get
      }
      Redirect(routes.PokedexController.index())
      
    }
  }
}
