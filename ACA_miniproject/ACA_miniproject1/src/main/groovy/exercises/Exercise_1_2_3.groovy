package exercises

import org.bson.Document
import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClients

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
def mongoClient = MongoClients.create("mongodb+srv://${properties.USN}:${properties.PWD}@cluster0.${properties.SERVER}.mongodb.net/${properties.DB}?retryWrites=true&w=majority");

// GET DATABASE
def db = mongoClient.getDatabase(properties.DB);

def filter = { key, val ->
	def filterAux = new BasicDBObject()
	filterAux.append(key, val)
	filterAux
}

def set = { key, val ->
	def updateObject = new BasicDBObject()
	updateObject.append('$set', new BasicDBObject().append(key,val))
	updateObject
}

def col = db.getCollection("lab2")
def jsonslur = new JsonSlurper()
def inputFile = new File("C:\\Users\\Licacio\\Downloads\\LAB_week14_project\\week14_project\\src\\main\\resources\\facebook.json")
def dtlist = jsonslur.parseText(inputFile.text)



println ("\n\n == Exercise 1 == \n\n")
def filterObject = new Document()
col.deleteMany(filterObject)

def docList = new BasicDBList()
for (obj in dtlist) {
	doc = new Document();
	doc.putAll(obj)
	docList.add(doc);
}
col.insertMany(docList)

for (data in db.getCollection("lab2").find()) {
	println data
}


// EXERCISE 1:
// Create a collection 'lab2' and create the following document
// This is an example of a Facebook JSON file which you might see 
// when getting data from the Facebook API. It might also be used to 
// contain profile information which can be easily shared across 
// your system components using the simple JSON format.
// source: https://www.sitepoint.com/facebook-json-example/
//	 [
//	      {
//	         "id": "X999_Y999",
//	         "from": {
//	            "name": "Tom Brady", "id": "X12"
//	         },
//	         "message": "Looking forward to 2010!",
//	         "actions": [
//	            {
//	               "name": "Comment",
//	               "link": "http://www.facebook.com/X999/posts/Y999"
//	            },
//	            {
//	               "name": "Like",
//	               "link": "http://www.facebook.com/X999/posts/Y999"
//	            }
//	         ],
//	         "type": "status",
//	         "created_time": "2010-08-02T21:27:44+0000",
//	         "updated_time": "2010-08-02T21:27:44+0000"
//	      },
//	      {
//	         "id": "X998_Y998",
//	         "from": {
//	            "name": "Peyton Manning", "id": "X18"
//	         },
//	         "message": "Where's my contract?",
//	         "actions": [
//	            {
//	               "name": "Comment",
//	               "link": "http://www.facebook.com/X998/posts/Y998"
//	            },
//	            {
//	               "name": "Like",
//	               "link": "http://www.facebook.com/X998/posts/Y998"
//	            }
//	         ],
//	         "type": "status",
//	         "created_time": "2010-08-02T21:27:44+0000",
//	         "updated_time": "2010-08-02T21:27:44+0000"
//	      }
//	   ]


// EXERCISE 2
// Update message field of document with id "X999_Y999" to "Remembering what happened in 2010"
println ("\n\n == Exercise 2 == \n\n")
def lab2 = db.getCollection("lab2")
filterObject = new BasicDBObject().append('id', 'X999_Y999')
println "before: " + lab2.find(filterObject).first()
lab2.updateOne(filterObject, new BasicDBObject().append('$set', new BasicDBObject().append('message', 'Remembering what happened in 2010')))
println "after update: "
for (u in lab2.find(filterObject)) {
	println u
}


// EXERCISE 3
// Delete the document with id: "id": "X998_Y998"
println ("\n\n == Exercise 3 == \n\n")
lab2.deleteOne(filter('id','X998_Y998'))
assert 0 == lab2.countDocuments(filter('id','X998_Y998'))
print("Deleted successfully ( No AssertionError)")

