package edu.jsharma.training.springdatamongodb.curd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;

public class ConnectionToMongoDB {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello mongo world!!!");
		try {
			final MongoClient mongoClient = new MongoClient("localhost", 27017);
			final MongoDatabase myDB = mongoClient.getDatabase("mydb");
			MongoCollection<Document> collection = myDB.getCollection("clickstream");

			ObjectMapper mapper = new ObjectMapper();

			ClickStream clickStream = mapper.readValue(new File("jsonStr.txt"), ClickStream.class);
			System.out.println("Value of id:" + clickStream.getId());
			// filter query
			Bson filter = new Document("Id", "1")/* .append("Id",new Document("$gt","3")) */;
			Bson filter1 = Filters.eq("Id", "1");
            // and /or cond
			Bson filterAnd = Filters.and(Filters.eq("Id", "1"),Filters.eq("Name", "Mohan"));
			Bson filterOr = Filters.or(Filters.eq("Id", "1"),Filters.eq("Id", "Mojan"));
			
			
			//1
			Bson cond = Filters.eq("Id", clickStream.getId());
			
			List<Document> documents = (List<Document>) collection.find(cond).into(new ArrayList<>());
			System.out.println(documents.size());
			
			

			// update command
			// Here we are replacing the document if exists else insert the document
			if (documents.size() > 0) {
				collection.replaceOne(cond,
						new Document().append("Id", clickStream.getId()).append("Name", clickStream.getName())
								.append("Clasdifier", clickStream.getClasdifier())
								.append("Timeout", clickStream.getTimeout()).append("Url", clickStream.getUrl())
								.append("Enabled", clickStream.getEnabled()).append("Class", clickStream.getCategory())
								.append("Monitorhostname", clickStream.getMonitorhostname()));
			} else {
				collection.insertOne(new Document().append("Id", clickStream.getId())
						.append("Name", clickStream.getName()).append("Clasdifier", clickStream.getClasdifier())
						.append("Timeout", clickStream.getTimeout()).append("Url", clickStream.getUrl())
						.append("Enabled", clickStream.getEnabled()).append("Class", clickStream.getCategory())
						.append("Monitorhostname", clickStream.getMonitorhostname()));
			}
			// update one field
			/*if (documents.size() > 0) {
				collection.updateOne(cond,
						new Document("$set", new Document().append("Name", clickStream.getName())
								.append("Clasdifier", clickStream.getClasdifier())
								.append("Timeout", clickStream.getTimeout()).append("Url", clickStream.getUrl())
								.append("Enabled", clickStream.getEnabled()).append("Class", clickStream.getCategory())
								.append("Monitorhostname", clickStream.getMonitorhostname())),
						new UpdateOptions().upsert(true));
			}
*/
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
