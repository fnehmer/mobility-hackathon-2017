package Services;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;

public class FavoritenService {

	public MongoClient mongoClient;
	public MongoCollection<Document> favorites;

	public FavoritenService() {
		MongoClientURI connectionString = new MongoClientURI("mongodb://vingu.online:27017");
		mongoClient = new MongoClient(connectionString);
		MongoDatabase database = mongoClient.getDatabase("alexa");
		favorites = database.getCollection("favoriten");

		favorites.createIndex(Indexes.text("name"));
	}

	public void addFavorite(String name, JSONObject geofoxLocationResponse) {
		Document favorite = new Document();
		favorite.append("name", name).append("json", geofoxLocationResponse);
		favorites.insertOne(favorite);
	}
	
	public void removeFavorite(String name) {
		favorites.findOneAndDelete(Filters.text(name));
	}
	
	public static void main(String[] args) {
		FavoritenService fs = new FavoritenService();
		fs.removeFavorite("foobar");
	}
}
