
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import java.lang._
import java.util._
import scala.collection.JavaConverters._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._
import play.data._
import play.core.j.PlayFormsMagicForJava._

object searchResults extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template7[Form[Search],String,Integer,List[String],Form[Persona],String,String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(searchForm: Form[Search], user: String, bool: Integer, tweets: List[String], personaForm: Form[Persona], img: String, term: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {
/*3.2*/import helper._


Seq[Any](format.raw/*1.134*/("""

"""),format.raw/*4.1*/("""
"""),format.raw/*5.1*/("""<script sync src="https://platform.twitter.com/widgets.js"></script>
<script src="http://code.jquery.com/jquery-2.1.4.js"; type="text/javascript"></script>
<script src=""""),_display_(/*7.15*/routes/*7.21*/.Assets.versioned("javascripts/render.js")),format.raw/*7.63*/(""""; type="text/javascript"></script>

"""),_display_(/*9.2*/main("Search Results")/*9.24*/(searchForm)/*9.36*/(user)/*9.42*/(bool)/*9.48*/(personaForm)/*9.61*/(img)/*9.66*/ {_display_(Seq[Any](format.raw/*9.68*/("""
  """),format.raw/*10.3*/("""<div id="searchResults">
  	<h3>Search Results for """),_display_(/*11.28*/term),format.raw/*11.32*/(""":</h3>
  	<p id="list">
  			"""),_display_(/*13.7*/for(tweet <- tweets) yield /*13.27*/{_display_(Seq[Any](format.raw/*13.28*/("""
  				"""),format.raw/*14.7*/("""<p id=""""),_display_(/*14.15*/tweet),format.raw/*14.20*/(""""><br></p>
  				<script>
					rendering(""""),_display_(/*16.18*/tweet),format.raw/*16.23*/("""");
				</script>
  			""")))}),format.raw/*18.7*/("""
  	"""),format.raw/*19.4*/("""</p>
  </div>
""")))}),format.raw/*21.2*/("""

"""))
      }
    }
  }

  def render(searchForm:Form[Search],user:String,bool:Integer,tweets:List[String],personaForm:Form[Persona],img:String,term:String): play.twirl.api.HtmlFormat.Appendable = apply(searchForm,user,bool,tweets,personaForm,img,term)

  def f:((Form[Search],String,Integer,List[String],Form[Persona],String,String) => play.twirl.api.HtmlFormat.Appendable) = (searchForm,user,bool,tweets,personaForm,img,term) => apply(searchForm,user,bool,tweets,personaForm,img,term)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Thu Nov 30 01:32:40 GMT 2017
                  SOURCE: /home/carly/Documents/Project/NewsTweet/NewsTweet/app/views/searchResults.scala.html
                  HASH: 624f80398c90be3d5fb1c210dc1f81661160f05e
                  MATRIX: 1018->1|1223->136|1269->133|1297->152|1324->153|1520->323|1534->329|1596->371|1659->409|1689->431|1709->443|1723->449|1737->455|1758->468|1771->473|1810->475|1840->478|1919->530|1944->534|2000->564|2036->584|2075->585|2109->592|2144->600|2170->605|2240->648|2266->653|2320->677|2351->681|2396->696
                  LINES: 28->1|31->3|34->1|36->4|37->5|39->7|39->7|39->7|41->9|41->9|41->9|41->9|41->9|41->9|41->9|41->9|42->10|43->11|43->11|45->13|45->13|45->13|46->14|46->14|46->14|48->16|48->16|50->18|51->19|53->21
                  -- GENERATED --
              */
          