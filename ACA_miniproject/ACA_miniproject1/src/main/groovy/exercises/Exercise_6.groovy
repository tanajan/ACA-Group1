package exercises

import org.bson.Document

import com.mongodb.client.MongoClients

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;


def path = 'src/main/resources/top-rated-movies.json'

def solution = { filePath ->
	// load credentials from src/main/resources/mongodb.properties
	// this file should contain
	//		USN=yourUsername
	//		PWD=yourPassword
	//		SERVER=serverToken
	//		DATABASE=yourDatabaseName
	def properties = new Properties()
	def propertiesFile = new File('src/main/resources/mongodb.properties')
	propertiesFile.withInputStream {
		properties.load(it)
	}
	
	// create connection and upload contents
	def mongoClient = MongoClients.create("mongodb+srv://${properties.USN}:${properties.PWD}@cluster0.${properties.SERVER}.mongodb.net/${properties.DB}?retryWrites=true&w=majority");
	def db = mongoClient.getDatabase(properties.DB);
	
	/* TODO: COMPLETE THE CLOSURE TO SOLVE EXERCISE 6 */
	
	// create connection and upload contents
	def col = db.getCollection("movies-collection")
	
	doc = new Document()
	println(JsonOutput.toJson([id: 3, name: 'Rajesh', role: 'developer', isEmployee: true]))
	doc = Document.parse(JsonOutput.toJson([id: 3, name: 'Rajesh', role: 'developer', isEmployee: true]))
	col.insertOne(doc)
	// reset collection
	
	// get info from file
	
	// add new movie to list
	
	// upload data to MongoDB
	
	// implement query
	
	// return result
}



// desk check your solution twice: both attempts should give the same result
println('first attempt:')
println(solution(path).join(""))
