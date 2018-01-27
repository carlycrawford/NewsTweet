package controllers;

import play.mvc.*;
import models.TwitterUser;
import models.*;
import play.data.Form;
import play.data.FormFactory;
import models.Search;
import javax.inject.Inject;
import views.html.main;
import play.data.Form.*;

import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.DirectMessage;
import twitter4j.User;
import twitter4j.Status;
import twitter4j.MediaEntity;
import twitter4j.TwitterFactory;
import twitter4j.QueryResult;
import twitter4j.Query;
import twitter4j.IDs;
import twitter4j.TwitterException;
import twitter4j.conf.ConfigurationBuilder;
import java.util.List;
import java.util.ArrayList;
import static java.util.Arrays.asList; 
import twitter4j.PagableResponseList; 

import com.aliasi.classify.Classification;
import com.aliasi.classify.Classified;
import com.aliasi.classify.ConfusionMatrix;
import com.aliasi.classify.DynamicLMClassifier;
import com.aliasi.classify.JointClassification;
import com.aliasi.classify.JointClassifier;
import com.aliasi.classify.JointClassifierEvaluator;
import com.aliasi.classify.LMClassifier;

import com.aliasi.lm.NGramProcessLM;

import com.aliasi.util.AbstractExternalizable;

import java.io.File;
import java.io.IOException;
//import org.json.JSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.aliasi.util.Files;


import play.*;
import play.mvc.*;
import play.data.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class SearchController extends Controller {

    //private Form<Search> searchForm = Form.form(Search.class);
	@Inject FormFactory formFactory;

	public List<Status> tweets = new ArrayList<Status>();
	public List<String> tID = new ArrayList<String>();
	public List<String> mostPopular = new ArrayList<>();
	public List<String> mostRecent = new ArrayList<>();
	public List<String> media = new ArrayList<>();

	private static File TRAINING_DIR
        = new File("/home/carly/Documents/Project/NewsTweet/NewsTweet/app/controllers/POLARITY_DIR/txt_sentoken");

    private static String[] CATEGORIES
        = { "neg", "pos", "neut" };

    private static int NGRAM_SIZE = 8;

    List<String> neg = new ArrayList<>();
    List<String> pos = new ArrayList<>();
    List<String> neut = new ArrayList<>();
    List<Status> sentiment = new ArrayList<>();
    String term = "";
    String searchType="";

	public Result searchResults() {
			tweets.clear();
			tID.clear();
			mostPopular.clear();
			mostRecent.clear();
			sentiment.clear();
			media.clear();

			 ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        		configurationBuilder.setOAuthConsumerKey("AfZgXUsXP3v9F3DYIMVx2q7KH")
                .setOAuthConsumerSecret("NoIVu1Vq4ggGOnJk0zvUoaGBuIBS3AuxN607zoah5D44PNKLgD")
                .setOAuthAccessToken("1561842786-RaR4w59MNxCD9aL9n6qxygJjx90NKhYZZTdJy3n")
                .setOAuthAccessTokenSecret("tFeSn3QIssVrT8OAH42hl7RX8gYpmJX9uj2hMByLjdK8c")
                .setTweetModeExtended(true);

            Form<Message> messageForm = formFactory.form(Message.class).bindFromRequest();
			Form<Search> searchForm = formFactory.form(Search.class).bindFromRequest();
	        term = searchForm.field("searchTerm").value();
	        searchType = searchForm.field("searchType").value();
	        Search newSearch = new Search(term, searchType);
	        searchForm = searchForm.fill(newSearch);

	        Form<Persona> personaForm = formFactory.form(Persona.class).bindFromRequest();
	        String name = personaForm.field("personaName").value();
	        //List interests = personaForm.field("interests").value();

	        Form<Interest> interestForm = formFactory.form(Interest.class).bindFromRequest();
	        String interestName = interestForm.field("interestName").value();

	        Form<Track> trackForm = formFactory.form(Track.class).bindFromRequest();
	        String search = trackForm.field("term").value();
	        String interestTrack = trackForm.field("interest").value();

		    Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
		    
		    Query query = new Query(term);
		    query.setSince("2017-06-01");
		    query.count(30);
		    query.lang("en");

		    if(searchType.compareTo("User")==0){
		    	String newTerm = "from:"+term;
		    	query = new Query(newTerm);
		    }
		    else if(searchType.compareTo("Hashtag")==0){
		    	String newTerm = term.replace(" ", "");
		    	term = "#"+term;
		    	query = new Query(newTerm);
		    }

		    try{
		    	QueryResult result = twitter.search(query);
			    for (Status status : result.getTweets()) {
			    	sentiment.add(status);
			        tID.add(Long.toString(status.getId()));
			    }
			}
			catch (TwitterException e){
				return ok("error");
			}

			try{
				query.setResultType(Query.POPULAR);
		    	QueryResult result = twitter.search(query);
			    for (Status status : result.getTweets()) {
			    	if(!mostPopular.contains(Long.toString(status.getId())) && !status.isRetweet()){
			        	mostPopular.add(Long.toString(status.getId())+ "-" + status.getUser().getScreenName());
			        	if(!media.contains(Long.toString(status.getId())) && !status.isRetweet()){
			        		System.out.println(status.getText());
			        		if(status.getMediaEntities().length>0){
			        			//status.getText().contains(".com") || status.getText().contains(".gif") || status.getText().contains(".jpeg")
			        			media.add(Long.toString(status.getId())+"a" + "-" + status.getUser().getScreenName());
			        		}
			        	}
			    	}
			    }
			}
			catch (TwitterException e){
				return ok("error");
			}

			try{
				query.setResultType(Query.RECENT);
		    	QueryResult result = twitter.search(query);
			    for (Status status : result.getTweets()) {
			    	if(!mostRecent.contains(Long.toString(status.getId())) && !status.isRetweet()){
			        	mostRecent.add(Long.toString(status.getId())+ "-" + status.getUser().getScreenName());
			        	if(!media.contains(Long.toString(status.getId())) && !status.isRetweet()){
			        		System.out.println(status.getText());
			        		if(status.getMediaEntities().length>0){
			        			//status.getText().contains(".com") || status.getText().contains(".gif") || status.getText().contains(".jpeg")
			        			media.add(Long.toString(status.getId())+"a" + "-" + status.getUser().getScreenName());
			        		}
			        	}
			        }
			    }
			}
			catch (TwitterException e){
				return ok("error");
			}

	        //System.out.println(term);
	         String str = session("id");
	         List<String> personaNames = new ArrayList<>();
            List<String> interests = new ArrayList<>();
	        if(str!=null){
		        Long id = Long.parseLong(str);
		        TwitterUser t = TwitterUser.find.byId(id);
		        List<Persona> personas = Persona.find.query().where()
                                        .ilike("twitter_user", Long.toString(id))
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();
            for(Persona p: personas){
                personaNames.add(p.personaName);
                List<Interest> interestsFromDB = Interest.find.query().where()
                                        .ilike("persona_id", Long.toString(p.id))
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();
                for(Interest i: interestsFromDB){
                    interests.add(i.interestName + " - " + p.personaName);
                }
            }
				String s = t.username;
			    return ok(views.html.searchResults.render(searchForm, trackForm, messageForm, s, 1, tID, mostPopular, mostRecent, media, personaForm, t.imgUrl, interestForm, term, personaNames, interests, ""));
			}
		    else{
		        	return ok(views.html.searchResults.render(searchForm, null, messageForm, "", 0, tID, mostPopular, mostRecent, media, personaForm, "", interestForm, term, personaNames, interests, ""));
		        }
    }

    public Result trackSearch() {
			Form<Interest> interestForm = formFactory.form(Interest.class).bindFromRequest();
	        String interestName = interestForm.field("interestName").value();
	        String personaName = interestForm.field("personaName").value();

	        Form<Persona> personaForm = formFactory.form(Persona.class).bindFromRequest();
        	String name = personaForm.field("personaName").value();

	        Form<Track> trackForm = formFactory.form(Track.class).bindFromRequest();
	        String search = trackForm.field("term").value();
	        String interestTrack = trackForm.field("interest").value();
	        String[] strArray = interestTrack.split(" - ");
	        interestTrack = strArray[0];
	        String personaOfInterest = strArray[1];

	        Form<Search> searchForm = formFactory.form(Search.class).bindFromRequest();
	        //String searchType = searchForm.field("searchType").value();
	        Search newSearch = new Search(search, searchType);
	        searchForm = searchForm.fill(newSearch);

	        String str = session("id");
	        List<String> personaNames = new ArrayList<>();
	            List<String> interests = new ArrayList<>();
	        if(str!=null){
		        Long id = Long.parseLong(str);
		        TwitterUser t = TwitterUser.find.byId(id);
		        List<Persona> personas = Persona.find.query().where()
                                        .ilike("twitter_user", Long.toString(id))
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();
	            
	            for(Persona p: personas){
	                personaNames.add(p.personaName);
	                List<Interest> interestsFromDB = Interest.find.query().where()
	                                        .ilike("persona", Long.toString(p.id))
	                                        .setFirstRow(0)
	                                        .setMaxRows(25)
	                                        .findPagedList()
	                                        .getList();
	                for(Interest i: interestsFromDB){
	                    interests.add(i.interestName + " - " + p.personaName);
	                }
	            }

	   			List<Persona> personaSaveInterest = Persona.find.query().where()
                                        .ilike("twitter_user", Long.toString(id))
                                        .ilike("persona_name", personaOfInterest)
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();

                Persona persona = personaSaveInterest.get(0);
                Long pID = persona.id;
                
                List<Interest> trackedInterest = Interest.find.query().where()
                                        .ilike("persona_id", Long.toString(pID))
                                        .ilike("interest_name", interestTrack)
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();

                Interest interestID = trackedInterest.get(0);

                Track track = new Track(search, interestID);
                track.save();

				String s = t.username;
			    return redirect("http://localhost:9000/search?searchTerm="+search+"&searchType="+searchType);
			}
		    else{
		        	return ok(views.html.index.render(searchForm, "", 0, personaForm, "", interestForm, personaNames, interests, ""));
		        }
    }

    public Result sendMessage() {
			Form<Interest> interestForm = formFactory.form(Interest.class).bindFromRequest();
	        String interestName = interestForm.field("interestName").value();
	        String personaName = interestForm.field("personaName").value();

	        Form<Persona> personaForm = formFactory.form(Persona.class).bindFromRequest();
        	String name = personaForm.field("personaName").value();

        	Form<Message> messageForm = formFactory.form(Message.class).bindFromRequest();
	        String recipient = messageForm.field("recipientName").value();
	        String message = messageForm.field("message").value();

	        System.out.println(recipient+"!!!!!");

	        Form<Search> searchForm = formFactory.form(Search.class).bindFromRequest();
	        //String searchType = searchForm.field("searchType").value();
	        Search newSearch = new Search(term, searchType);
	        searchForm = searchForm.fill(newSearch);

	        String str = session("id");
	        List<String> personaNames = new ArrayList<>();
	            List<String> interests = new ArrayList<>();
	        if(str!=null){
		        Long id = Long.parseLong(str);
		        TwitterUser t = TwitterUser.find.byId(id);
		        List<Persona> personas = Persona.find.query().where()
                                        .ilike("twitter_user", Long.toString(id))
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();
	            
	            for(Persona p: personas){
	                personaNames.add(p.personaName);
	                List<Interest> interestsFromDB = Interest.find.query().where()
	                                        .ilike("persona", Long.toString(p.id))
	                                        .setFirstRow(0)
	                                        .setMaxRows(25)
	                                        .findPagedList()
	                                        .getList();
	                for(Interest i: interestsFromDB){
	                    interests.add(i.interestName + " - " + p.personaName);
	                }
	            }

				String s = t.username;

			 ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        		configurationBuilder.setOAuthConsumerKey("AfZgXUsXP3v9F3DYIMVx2q7KH")
                .setOAuthConsumerSecret("NoIVu1Vq4ggGOnJk0zvUoaGBuIBS3AuxN607zoah5D44PNKLgD")
                .setOAuthAccessToken(t.accessToken)
                .setOAuthAccessTokenSecret(t.accessTokenSecret)
                .setTweetModeExtended(true);

				Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();

                try{
					System.out.println(twitter.getScreenName());
				}
				catch (TwitterException e){

                	e.printStackTrace(System.out);
                	return ok("name");
                }

                try{
                	
                	
                	DirectMessage dm = twitter.sendDirectMessage(recipient, message);
                }
                catch (TwitterException e){

                	e.printStackTrace(System.out);
                	return ok("problem sending message");
                }

			    return redirect("http://localhost:9000/search?searchTerm="+term+"&searchType="+searchType);
			}
		    else{
		        	return ok(views.html.index.render(searchForm, "", 0, personaForm, "", interestForm, personaNames, interests, ""));
		        }
    }

    public Result searchAnalytics() throws TwitterException {
    		LoadingFrame frame = new LoadingFrame();
		    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		    frame.setSize(200, 100);
		    frame.setLocationRelativeTo(null);
		    frame.setUndecorated(true);
		    frame.startLoading();
		    frame.setVisible(true);
		    

			 ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        		configurationBuilder.setOAuthConsumerKey("AfZgXUsXP3v9F3DYIMVx2q7KH")
                .setOAuthConsumerSecret("NoIVu1Vq4ggGOnJk0zvUoaGBuIBS3AuxN607zoah5D44PNKLgD")
                .setOAuthAccessToken("1561842786-RaR4w59MNxCD9aL9n6qxygJjx90NKhYZZTdJy3n")
                .setOAuthAccessTokenSecret("tFeSn3QIssVrT8OAH42hl7RX8gYpmJX9uj2hMByLjdK8c");

			Form<Search> searchForm = formFactory.form(Search.class).bindFromRequest();
	        //term = searchForm.field("searchTerm").value();
	        String searchType = searchForm.field("searchType").value();
	        Search newSearch = new Search(term, searchType);
	        searchForm = searchForm.fill(newSearch);

	        Form<Persona> personaForm = formFactory.form(Persona.class).bindFromRequest();
	        String name = personaForm.field("personaName").value();
	        //List interests = personaForm.field("interests").value();

	        Form<Interest> interestForm = formFactory.form(Interest.class).bindFromRequest();
	        String interestName = interestForm.field("interestName").value();

	        Form<Track> trackForm = formFactory.form(Track.class).bindFromRequest();
	        String search = trackForm.field("term").value();
	        String interestTrack = trackForm.field("interest").value();

		    Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();

		     try{
	            classifier(sentiment);
	        } catch(IOException e){
	            return ok("error");
	        } catch (ClassNotFoundException e){
	            return ok("error");
	        }

	        double posSize = ((double) pos.size())/((double) sentiment.size()) *100;
			double negSize = ((double) neg.size())/((double) sentiment.size()) *100;
			double neutSize = ((double) neut.size())/((double) sentiment.size()) *100;

			//force graph data
			ArrayList<User> mostPopularUsers = new ArrayList<>();
			ArrayList<User> users = new ArrayList<>();
			for(Status t: sentiment){
				User user = t.getUser();
				if(!users.contains(user))
					users.add(user);
			}

			mostPopularUsers = maxFollowers(users, mostPopularUsers);

			JSONObject child = new JSONObject();
			ArrayList<JSONObject> JSONlist = new ArrayList<>();

			String user = "";
			for(User popUser: mostPopularUsers){
				// get follower list
				try {
					PagableResponseList<User> followers = twitter.getFollowersList(popUser.getScreenName(),-1);
					ArrayList<User> allFollowers = new ArrayList<>();
					for(User follower: followers){
						allFollowers.add(follower);
					}
					ArrayList<User> popFollowers = new ArrayList<>();
					popFollowers = maxFollowers(allFollowers, popFollowers);
				    //create JSONlist of JSON objects
					ArrayList<JSONObject> listOfFollowers = new ArrayList<>();
					for(User popFoll: popFollowers){
						JSONObject popFollower = new JSONObject();
						popFollower.put("name", popFoll.getScreenName());
						popFollower.put("img", popFoll.getBiggerProfileImageURL());
						popFollower.put("followers", popFoll.getFollowersCount());
						popFollower.put("link", "http://twitter.com/" + popFoll.getScreenName());
						listOfFollowers.add(popFollower);
					}

					//add list to twitteruser object
					JSONObject twitteruser = new JSONObject();
					twitteruser.put("name", popUser.getScreenName());
					twitteruser.put("img", popUser.getBiggerProfileImageURL());
					twitteruser.put("followers", popUser.getFollowersCount());
					twitteruser.put("link", "http://twitter.com/" + popUser.getScreenName());
					twitteruser.put("children", listOfFollowers);
					JSONlist.add(twitteruser);	

				}
				catch (TwitterException e){
					System.out.println("Error retrieving followers.");
				}
			}

			//primary node
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("children", JSONlist);
			jsonObj.put("name", null);
			String json = jsonObj.toString();

			System.out.println(json);

			// JSON format: {"name": "parentnode", "children": [ {"name": "equens"}, {"name": "test"} ]};

			//wordcloud
			String wordcloudData = findFrequency(sentiment);

			// could be abstracted?
	         String str = session("id");
	         List<String> personaNames = new ArrayList<>();
            List<String> interests = new ArrayList<>();
	        if(str!=null){
		        Long id = Long.parseLong(str);
		        TwitterUser t = TwitterUser.find.byId(id);
		        List<Persona> personas = Persona.find.query().where()
                                        .ilike("twitter_user", Long.toString(id))
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();
            for(Persona p: personas){
                personaNames.add(p.personaName);
                List<Interest> interestsFromDB = Interest.find.query().where()
                                        .ilike("persona_id", Long.toString(p.id))
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();
                for(Interest i: interestsFromDB){
                    interests.add(i.interestName + " - " + p.personaName);
                }
            }
				String s = t.username;
				frame.setVisible(false);
			    return ok(views.html.dataAnalytics.render(searchForm, trackForm, s, 1, tID, pos, posSize, neg, negSize, neut, neutSize, personaForm, t.imgUrl, interestForm, term, personaNames, interests, "", json, wordcloudData));
			}
		    else{
		        	return ok(views.html.dataAnalytics.render(searchForm, null, "", 0, tID, pos, posSize, neg, negSize, neut, neutSize, personaForm, "", interestForm, term, personaNames, interests, "", json, wordcloudData));
		        }
    }

    public ArrayList<User> maxFollowers(ArrayList<User> users, ArrayList<User> mostPopular) {
    	int max = 0;
    	User maxUsername = null;
    	for(User user: users){
    		if(user.getFollowersCount() > max){
    			max = user.getFollowersCount();
    			maxUsername = user;
    		}
    	}
    	users.remove(maxUsername);
    	mostPopular.add(maxUsername);
    	if (mostPopular.size()<5){
    		maxFollowers(users, mostPopular);
    	}
    	return mostPopular;
    }

    public Result showSentiment(String sentiment) {
	    ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        		configurationBuilder.setOAuthConsumerKey("AfZgXUsXP3v9F3DYIMVx2q7KH")
                .setOAuthConsumerSecret("NoIVu1Vq4ggGOnJk0zvUoaGBuIBS3AuxN607zoah5D44PNKLgD")
                .setOAuthAccessToken("1561842786-RaR4w59MNxCD9aL9n6qxygJjx90NKhYZZTdJy3n")
                .setOAuthAccessTokenSecret("tFeSn3QIssVrT8OAH42hl7RX8gYpmJX9uj2hMByLjdK8c");

			Form<Search> searchForm = formFactory.form(Search.class).bindFromRequest();
	       // term = searchForm.field("searchTerm").value();
	        String searchType = searchForm.field("searchType").value();
	        Search newSearch = new Search(term, searchType);
	        searchForm = searchForm.fill(newSearch);

	        Form<Persona> personaForm = formFactory.form(Persona.class).bindFromRequest();
	        String name = personaForm.field("personaName").value();
	        //List interests = personaForm.field("interests").value();

	        Form<Interest> interestForm = formFactory.form(Interest.class).bindFromRequest();
	        String interestName = interestForm.field("interestName").value();

	        Form<Track> trackForm = formFactory.form(Track.class).bindFromRequest();
	        String search = trackForm.field("term").value();
	        String interestTrack = trackForm.field("interest").value();

		    Twitter twitter = new TwitterFactory(configurationBuilder.build()).getInstance();
	        
	         String str = session("id");
	         List<String> personaNames = new ArrayList<>();
            List<String> interests = new ArrayList<>();
	        if(str!=null){
		        Long id = Long.parseLong(str);
		        TwitterUser t = TwitterUser.find.byId(id);
		        List<Persona> personas = Persona.find.query().where()
                                        .ilike("twitter_user", Long.toString(id))
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();
            for(Persona p: personas){
                personaNames.add(p.personaName);
                List<Interest> interestsFromDB = Interest.find.query().where()
                                        .ilike("persona_id", Long.toString(p.id))
                                        .setFirstRow(0)
                                        .setMaxRows(25)
                                        .findPagedList()
                                        .getList();
                for(Interest i: interestsFromDB){
                    interests.add(i.interestName + " - " + p.personaName);
                }
            }
				String s = t.username;
				if(sentiment.compareTo("neutral") == 0){
					return ok(views.html.neutralSentiment.render(searchForm, trackForm, s, 1, tID, pos, neg, neut, personaForm, t.imgUrl, interestForm, term, personaNames, interests, ""));
				}
				if(sentiment.compareTo("positive") == 0){
					return ok(views.html.positiveSentiment.render(searchForm, trackForm, s, 1, tID, pos, neg, neut, personaForm, t.imgUrl, interestForm, term, personaNames, interests, ""));
				}
				if(sentiment.compareTo("negative") == 0){
					return ok(views.html.negativeSentiment.render(searchForm, trackForm, s, 1, tID, pos, neg, neut, personaForm, t.imgUrl, interestForm, term, personaNames, interests, ""));
				}
				return ok("error");
				
			}
		    else{
		    	if(sentiment.compareTo("positive") == 0){
					return ok(views.html.positiveSentiment.render(searchForm, null, "", 0, tID, pos, neg, neut, personaForm, "", interestForm, term, personaNames, interests, ""));
				}else if(sentiment.compareTo("negative") == 0){
					return ok(views.html.negativeSentiment.render(searchForm, null, "", 0, tID, pos, neg, neut, personaForm, "", interestForm, term, personaNames, interests, ""));
				}   
				else{
					return ok(views.html.neutralSentiment.render(searchForm, null, "", 0, tID, pos, neg, neut, personaForm, "", interestForm, term, personaNames, interests, ""));
				}
			}
    }

    public String findFrequency(List<Status> tweets) {
    	//stopwords from https://github.com/Yoast/YoastSEO.js/blob/acd077ca44d904632d8ae7fa5699bdcb76741379/js/config/stopwords.js (with altertions)
    	List<String> stopwords = asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and", "any", "are", "around", "as", "at", 
    		"be", "because", "been", "before", "being", "below", "between", "both", "but", "by", "can", "could", "did", "do", "does", "doing", 
    		"down", "during", "each", "few", "followers", "follower", "for", "from", "further", "got", "had", "has", "have", "having", "he", "he'd", "he'll", "he's", 
    		"her", "here", "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", 
    		"if", "in", "into", "is", "it", "it's", "its", "itself", "let's", "me", "more", "most", "my", "myself", "nor", "of", "on", 
    		"once", "only", "or", "other", "ought", "our", "ours", "ourselves", "out", "over", "own", "retweet", "rt", "same", "she", "she'd", "she'll", 
    		"she's", "should", "so", "some", "such", "still", "than", "that", "that's", "the", "their", "theirs", "them", "themselves", "then", 
    		"there", "there's", "these", "they", "they'd", "they'll", "they're", "they've", "this", "those", "through", "to", "too", 
    		"under", "until", "up", "very", "was", "we", "we'd", "we'll", "we're", "we've", "were", "what", "what's", "when", "when's", 
    		"where", "where's", "which", "while", "who", "who's", "whom", "why", "why's", "with", "would", "you", "you'd", "you'll", 
    		"you're", "you've", "your", "yours", "yourself", "yourselves");

    	//parsing
    	ArrayList<String> words = new ArrayList<>();
    	ArrayList<String> singleWords = new ArrayList<>();
    	for(Status tweet: tweets) {
    		String text = tweet.getText();
    		text = text.replaceAll("[^a-zA-Z #@]","");
    		String[] arrayOfWords = text.split(" ");
    		for(String word: arrayOfWords) {
    			word = word.toLowerCase();
    			if(!stopwords.contains(word) && !word.startsWith("http")){
	    			words.add(word);
	    			if(!singleWords.contains(word)) {
	    				singleWords.add(word);
	    			}
    			}
    		}
    	}

    	//calc freq and create string to hold words + frequencies
		String JSON = "[";

		for(String word: singleWords){
			JSONObject wordsWithFrequencies = new JSONObject();
			int count=0;
			for(String otherWord: words) {
				if(word.compareTo(otherWord)==0) {
					count++;
				}
			}
			wordsWithFrequencies.put("size", count);
			wordsWithFrequencies.put("text", word);
			String wwf = wordsWithFrequencies.toString();
			JSON+= wwf + ",";	
		}
		
		JSON = JSON.substring(0,JSON.length() - 1);
		JSON+="]";
		
		return JSON;
    }
    
    public void classifier(List<Status> classify) throws ClassNotFoundException, IOException {
        pos.clear();
        neg.clear();
        neut.clear();

        DynamicLMClassifier<NGramProcessLM> classifier
            = DynamicLMClassifier.createNGramProcess(CATEGORIES,NGRAM_SIZE);

            for(int i=0; i<CATEGORIES.length; ++i) {
            File classDir = new File(TRAINING_DIR,CATEGORIES[i]);
            if (!classDir.isDirectory()) {
                String msg = "Could not find training directory="
                    + classDir
                    + "\nHave you unpacked 4 newsgroups?";
                System.out.println(msg); // in case exception gets lost in shell
                throw new IllegalArgumentException(msg);
            }

                String[] trainingFiles = classDir.list();
                for (int j = 0; j < trainingFiles.length; ++j) {
                    File file = new File(classDir,trainingFiles[j]);
                    String text = Files.readFromFile(file,"ISO-8859-1");
                    System.out.println("Training on " + CATEGORIES[i] + "/" + trainingFiles[j]);
                    Classification classification
                        = new Classification(CATEGORIES[i]);
                    Classified<CharSequence> classified
                        = new Classified<CharSequence>(text,classification);
                    classifier.handle(classified);
                }
            }

        @SuppressWarnings("unchecked") // we created object so know it's safe
        JointClassifier<CharSequence> compiledClassifier
            = (JointClassifier<CharSequence>)
            AbstractExternalizable.compile(classifier);

        boolean storeCategories = true;
        JointClassifierEvaluator<CharSequence> evaluator
            = new JointClassifierEvaluator<CharSequence>(compiledClassifier,
                                                         CATEGORIES,
                                                         storeCategories);

        ArrayList<String> negText = new ArrayList<>();
        ArrayList<String> neutText = new ArrayList<>();
        ArrayList<String> posText = new ArrayList<>();
        for(int i = 0; i < CATEGORIES.length; ++i) {
            //File classDir = new File(TESTING_DIR,CATEGORIES[i]);
            for (int j=0; j<classify.size();  ++j) {
                String text = classify.get(j).getText();
                String tweetID = Long.toString(classify.get(j).getId());
                System.out.print("Testing on " + CATEGORIES[i] + "/" + text + " ");
                Classification classification
                    = new Classification(CATEGORIES[i]);
                Classified<CharSequence> classified
                    = new Classified<CharSequence>(text,classification);
                evaluator.handle(classified);
                JointClassification jc =
                    compiledClassifier.classify(text);
                String bestCategory = jc.bestCategory();
                String details = jc.toString();
                System.out.println("Got best category of: " + bestCategory);
                System.out.println(jc.toString());
                System.out.println("---------------");

                if(bestCategory.compareTo("neg")==0){
                    if(!negText.contains(text)){
                        neg.add(tweetID);
                        negText.add(text);
                    }
                }
                else if(bestCategory.compareTo("pos")==0){
                    if(!posText.contains(text)){
                        pos.add(tweetID);
                        posText.add(text);
                    }
                }
                else if(bestCategory.compareTo("neut")==0){
                	if(!neutText.contains(text)){
                        neut.add(tweetID);
                        neutText.add(text);
                    }
                }
            }
        }
        ConfusionMatrix confMatrix = evaluator.confusionMatrix();
        System.out.println("Total Accuracy: " + confMatrix.totalAccuracy());

        System.out.println("\nFULL EVAL");
        System.out.println(evaluator);
    }
}
