package exercises

import org.bson.Document

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClients
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.UpdateOptions

import groovy.json.JsonOutput
import groovy.json.JsonSlurper

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

// MAKING THE CONNECTION
def mongoClient = MongoClients.create("mongodb+srv://${properties.USN}:${properties.PWD}@cluster0.${properties.SERVER}.mongodb.net/${properties.DB}?retryWrites=true&w=majority")

// GET DATABASE
def db = mongoClient.getDatabase(properties.DB)

// TESTING CONNECTION
println 'database: ' + db.getName()
db.listCollectionNames().each{ println it } 

println 'connected'

def col = db.getCollection("Metorite")

// DELETE PREVIOUS CONTENTS
def filterObject = new Document()
col.deleteMany(filterObject)

// 	CREATING DOCUMENTS
def jsp = new JsonSlurper()
def inputFile = new File('src/main/resources/Project_Meteorite.json')
def dt = jsp.parseText(inputFile.text)
def docList = new BasicDBList()
for (obj in dt) {
	doc = new Document();
	doc.putAll(obj)
	docList.add(doc);
}
col.insertMany(docList)
