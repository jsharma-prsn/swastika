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
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.operation.OrderBy;

public class UpdateUpsert {

	public static void main(String[] args) throws Exception {
		System.out.println("Hello mongo world!!!");
		try {
			final MongoClient mongoClient = new MongoClient("localhost", 27017);
			final MongoDatabase myDB = mongoClient.getDatabase("mydb");
			MongoCollection<Document> collection = myDB.getCollection("clickstream");

			ObjectMapper mapper = new ObjectMapper();

			ClickStream clickStream = mapper.readValue(new File("jsonStr.txt"), ClickStream.class);
			System.out.println("Value of id:" + clickStream.getId());

			Bson cond = Filters.eq("_id", clickStream.getId());
			
			
			// update one field
			
				collection.updateOne(cond,new Document("$set",new Document()
						.append("Id", clickStream.getId())
						.append("Name", clickStream.getName()).append("Clasdifier", clickStream.getClasdifier())
						.append("Timeout", clickStream.getTimeout()).append("Url", clickStream.getUrl())
						.append("Enabled", clickStream.getEnabled()).append("Class", clickStream.getCategory())
						.append("Monitorhostname", clickStream.getMonitorhostname())),
						new UpdateOptions().upsert(true));
				
		//delete
		//collection.deleteMany(cond);
		
		//projection
				Bson projection=new Document("Name",0)/*.append("Id", 0)*/;
				Bson projectionB=	Projections.exclude("id","Name");
				Bson projectioneI=Projections.fields(Projections.exclude("_id"),Projections.include("Id","Name"));
		
		// Sort by		
		Bson sort=new Document("Name",1);
		Bson sortBy=Sorts.ascending("Name");
		//Sorts.orderBy(Sorts.ascending("Name"),Sorts.descending("Id"));
		
		//Skip and limit
		
		
		List<Document> docList=(List<Document>)collection.find().projection(projectioneI).sort(sortBy)/*.skip(1).limit(2)*/
				.into(new ArrayList<>());
		for(Document doc: docList) {
			System.out.println(doc);
		}

		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
