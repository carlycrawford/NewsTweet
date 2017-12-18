
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

object tech extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template10[Form[Search],String,Integer,Form[Persona],String,List[String],Form[Interest],List[String],List[String],String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(searchForm: Form[Search], user: String, bool: Integer, personaForm: Form[Persona], img: String, tweets: List[String], interestForm: Form[Interest], personas: List[String], interests: List[String], i: String):play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {
/*3.2*/import helper._


Seq[Any](format.raw/*1.210*/("""

"""),format.raw/*4.1*/("""
"""),format.raw/*5.1*/("""<script sync src="https://platform.twitter.com/widgets.js"></script>
<script src="http://code.jquery.com/jquery-2.1.4.js"; type="text/javascript"></script>
<script src=""""),_display_(/*7.15*/routes/*7.21*/.Assets.versioned("javascripts/render.js")),format.raw/*7.63*/(""""; type="text/javascript"></script>

"""),_display_(/*9.2*/main("NewsTweet")/*9.19*/(searchForm)/*9.31*/(user)/*9.37*/(bool)/*9.43*/(personaForm)/*9.56*/(img)/*9.61*/(interestForm)/*9.75*/(personas)/*9.85*/(interests)/*9.96*/(i)/*9.99*/{_display_(Seq[Any](format.raw/*9.100*/("""
  """),format.raw/*10.3*/("""<div id="categories">
  	<h3>Tech:</h3>
  	<div class="card-columns">
      """),_display_(/*13.8*/for(tweet <- tweets) yield /*13.28*/{_display_(Seq[Any](format.raw/*13.29*/("""
        """),format.raw/*14.9*/("""<div class="card"  id=""""),_display_(/*14.33*/tweet),format.raw/*14.38*/(""""><br></div>
        <script>
          rendering(""""),_display_(/*16.23*/tweet),format.raw/*16.28*/("""");
        </script>
      """)))}),format.raw/*18.8*/("""
    """),format.raw/*19.5*/("""</div>
  </div>
  
""")))}))
      }
    }
  }

  def render(searchForm:Form[Search],user:String,bool:Integer,personaForm:Form[Persona],img:String,tweets:List[String],interestForm:Form[Interest],personas:List[String],interests:List[String],i:String): play.twirl.api.HtmlFormat.Appendable = apply(searchForm,user,bool,personaForm,img,tweets,interestForm,personas,interests,i)

  def f:((Form[Search],String,Integer,Form[Persona],String,List[String],Form[Interest],List[String],List[String],String) => play.twirl.api.HtmlFormat.Appendable) = (searchForm,user,bool,personaForm,img,tweets,interestForm,personas,interests,i) => apply(searchForm,user,bool,personaForm,img,tweets,interestForm,personas,interests,i)

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  DATE: Mon Dec 18 22:04:28 GMT 2017
                  SOURCE: /home/carly/Documents/Project/NewsTweet/NewsTweet/app/views/tech.scala.html
                  HASH: a968c6e6268eafa19b22d80dc8080b9a88e15546
                  MATRIX: 1051->1|1332->212|1378->209|1406->228|1433->229|1629->399|1643->405|1705->447|1768->485|1793->502|1813->514|1827->520|1841->526|1862->539|1875->544|1897->558|1915->568|1934->579|1945->582|1984->583|2014->586|2117->663|2153->683|2192->684|2228->693|2279->717|2305->722|2384->774|2410->779|2469->808|2501->813
                  LINES: 28->1|31->3|34->1|36->4|37->5|39->7|39->7|39->7|41->9|41->9|41->9|41->9|41->9|41->9|41->9|41->9|41->9|41->9|41->9|41->9|42->10|45->13|45->13|45->13|46->14|46->14|46->14|48->16|48->16|50->18|51->19
                  -- GENERATED --
              */
          