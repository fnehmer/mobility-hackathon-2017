package Services;

import java.util.ArrayList;
import java.util.Collection;

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

	/**
	 * Initialise the service.
	 */
	public FavoritenService() {
		MongoClientURI connectionString = new MongoClientURI("mongodb://vingu.online:27017");
		mongoClient = new MongoClient(connectionString);
		MongoDatabase database = mongoClient.getDatabase("alexa");
		favorites = database.getCollection("favoriten");

		favorites.createIndex(Indexes.text("name"));
	}

	/**
	 * Create a new favorite (alias).
	 * @param name the alias for the place
	 * @param geofoxLocationResponse the corresponding location given by Geofox.
	 */
	public void addFavorite(String name, JSONObject geofoxLocationResponse) {
		Document favorite = new Document();
		favorite.append("name", name).append("json", geofoxLocationResponse);
		favorites.insertOne(favorite);
	}

	public void removeFavorite(String name) {
		favorites.findOneAndDelete(Filters.text(name));
	}

	/**
	 * 
	 * @param name the name (alias) of the place
	 * @return the {@link JSONObject} that was saved with addFavorite.
	 */
	public JSONObject getFavorite(String name) {
		return new JSONObject((String) favorites.find(Filters.text(name)).first().get("json"));
	}
	
	/**
	 * Get the names of all registered favorites.
	 * @return all names
	 */
	public Collection<String> getNamesOfFavorites() {
		ArrayList<String> namesOfFavorites = new ArrayList<String>();
		for (Document d : favorites.find()) {
			namesOfFavorites.add(d.getString("name"));
		}
		return namesOfFavorites;
	}

	public static void main(String[] args) {
		FavoritenService fs = new FavoritenService();
		System.out.println(fs.getNamesOfFavorites().toString());
		System.out.println(fs.getFavorite("foobar").toString(2));
	}
}
