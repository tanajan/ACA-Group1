package exercises

import org.bson.Document

import com.mongodb.client.MongoClients

import static com.mongodb.client.model.Filters.*;

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import static com.mongodb.client.model.Filters.*;
// load credentials from src/main/resources/mongodb.properties
// this file should contain 
//		USN=yourUsername
//		PWD=yourPassword
//		DB=yourDatabaseName 
def properties = new Properties()
def propertiesFile = new File('src/main/resources/mongodb.properties')
propertiesFile.withInputStream {
	properties.load(it)
}
def mongoClient = MongoClients.create("mongodb+srv://${properties.USN}:${properties.PWD}@cluster0.${properties.SERVER}.mongodb.net/${properties.DB}?retryWrites=true&w=majority");

def db = mongoClient.getDatabase(properties.DB);

// parse JSON file
def col = db.getCollection("lab2")
def jsonslur = new JsonSlurper()
def inputFile = new File("C:\\Users\\Licacio\\Downloads\\LAB_week14_project\\week14_project\\src\\main\\resources\\facebook.json")
def dtlist = jsonslur.parseText(inputFile.text)


// create connection and upload contents
def filterObject = new Document()
col.deleteMany(filterObject)

def docList = new BasicDBList()
for (obj in dtlist) {
	doc = new Document();
	doc.putAll(obj)
	docList.add(doc);
}
col.insertMany(docList)

print("Data inserted\n")
// parse JSON file


// create connection and upload contents
println "Result from fetching someone whose age is less than 18 years old"
agerestirction = lt("from.age",18)
for (u in col.find(agerestirction)) {
	println u
}
// https://mongodb.github.io/mongo-java-driver/3.8/builders/filters/#comparison

