package exercises

import org.bson.Document

import com.mongodb.BasicDBList
import com.mongodb.BasicDBObject
import com.mongodb.client.MongoClients
import com.mongodb.client.model.FindOneAndUpdateOptions
import com.mongodb.client.model.UpdateOptions

import groovy.json.JsonOutput

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


def col = db.getCollection("users")

// DELETE PREVIOUS CONTENTS
def filterObject = new Document()
col.deleteMany(filterObject)

// 	CREATING DOCUMENTS
def doc = new Document()
doc.putAll([id: 1, name: 'John', role: 'developer', isEmployee: true])
col.insertOne(doc)

doc = new Document()
doc = Document.parse(JsonOutput.toJson([id: 2, name: 'Luke', role: 'tester', isEmployee: true]))
col.insertOne(doc)

doc = new Document()
println(JsonOutput.toJson([id: 3, name: 'Rajesh', role: 'developer', isEmployee: true]))
doc = Document.parse(JsonOutput.toJson([id: 3, name: 'Rajesh', role: 'developer', isEmployee: true]))
col.insertOne(doc)


def list = [[id: 4, name: 'Yi', role: 'tester', isEmployee: true], [id: 5, name: 'Pau', role: 'developer', isEmployee: true]]
def docList = new BasicDBList()
for (obj in list) {
	doc = new Document();
	doc.putAll(obj)
	docList.add(doc);
}
col.insertMany(docList)

// MAKING QUERIES
// get the first document
println db.getCollection("users").find().first()

// fetch a specific one
filterObject = new Document()
filterObject.putAll([id: 2])

def update = new BasicDBObject()
update.putAll([name: 'Luke E.'])
def delta = new BasicDBObject('$set', update)
def user = db.getCollection("users").findOneAndUpdate(filterObject, delta, (new FindOneAndUpdateOptions()).upsert(true))
assert db.getCollection("users").find(filterObject).first().name == 'Luke E.'
assert db.getCollection("users").find(filterObject).first().name != 'Luke'

// fetching all the elements of a collection 
for (u in db.getCollection("users").find()) {
	println u.name
}

// fetching all elements that meet a condition: e.g. all developers
db.getCollection("users").find(new BasicDBObject().append('role', 'developer')).each{ println it }

// Counting the number of documents in the collection
def users = db.getCollection("users")
println "counting: " + users.countDocuments(new BasicDBObject().append('role', 'developer'))

// UPDATE: applying modifications to some documents, the first parameter of the the `update` operation 
// is a filter for finding the document where to apply modifications and the second parameter indicates 
// the type of update to be performed. 
filterObject = new BasicDBObject().append('name', 'John')
println "before: " + users.find(filterObject).first()
users.updateOne(filterObject, new BasicDBObject().append('$set', new BasicDBObject().append('role', 'tester')))
println "after update: " 
for (u in users.find(filterObject)) {
	println u
}



// let's groovy the syntax for defining filters and updates
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


// UPSERT: The upsert operation inserts a document if it does not exist yet so duplicates 
// can be avoided (notice the last parameter of the update operation)
println "before: "  + users.find(filter('name','John')).first()

users.updateOne(
	filter('name','John'), 
	set('role', 'tester'), 
	new UpdateOptions().upsert(true))
println "after: " +  users.find(filter('name','John')).first()

println "before deletion: " + users.countDocuments()

// DELETE
// Removing some documents
//users.deleteOne(filter('name','John'))
//assert 0 == users.countDocuments(filter('name','John'))

// Removing all documents
//users.deleteMany(new BasicDBObject())
//assert 0 == users.countDocuments()


	