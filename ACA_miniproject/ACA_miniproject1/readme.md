<link rel='stylesheet' href='web/swiss.css'/>

# Lab session 3: Accessing MongoDB 


## Tutorial

In this tutorial we are going to use [MongoDb Java Driver](https://mongodb.github.io/mongo-java-driver/) to access a MongoDB available over Infrastructure-as-a-Service (IaaS) on [MongoDB Atlas](https://www.mongodb.com/cloud/atlas/). In the tutorial we will see how the use of domain-specific languages (DSLs) help develop cloud systems, focussing on the persistence layer and on cloud computing. In particular, we are going to use [MongoDB Java Driver](https://mongodb.github.io/mongo-java-driver/) from Groovy. 

### Create an account on MongoDB Atlas

First, we need to set up our MongoDB instance. We are going to use mLab, a cloud database provider built atop Infrastructure-as-a-Service (IaaS) providers, so that we do not need to install and configure the database server locally.

* Sign up and create a new account on [MongoDB Atlas](https://www.mongodb.com/cloud/atlas/).
* Create an organization on MongoDB Atlas. When working in groups, you may want to add several users to the same organization.
* Create a project. When working in groups, you may want to add several users to the same organization.
* Create your first cluster, by choosing a cloud provider with a free-tier on a server that is closest to your geographical location: e.g. Ireland if you are in Leicester. While the cluster is being created, read the rest of the worksheet and documentation.
* Click on `connect` to the cluster and whitelist your IP address (the one shown by default).
* Add a user to your database by providing a `USERNAME` and a `PASSWORD`
* Choose a connection method: `Connect your application`, and select `Java` and `4.1 or later`.
* Copy the connection string in your script for creating the database connection, i.e. something similar to `mongodb+srv://ab373:<password>@cluster0.f2bdm.mongodb.net/<dbname>?retryWrites=true&w=majority`.
* We are all set. Your MongoDB instance is up and running on the cloud!

If working from machines in the lab, you are likely to work from different machines against the same MongoDB cluster so please whitelist the following IP range: `143.210.71.0/24`(in CIDR format): click on the Cluster you created, and then on the pane on the left, click on `Network Access` and add the IP address `143.210.71.0/24` as shown below.

![whitelisting](https://blackboard.le.ac.uk/bbcswebdav/courses/CO4217_2020-21_SEM1/whitelist.png)


#### Connecting to your MongoDB cluster

Do not use **eduroam**, which caps some ports for security purposes and the connection with MongoDB Atlas will timeout:
* If you are working from your computer on campus, use **The Cloud**.
* If you are working from a University machine (wired to internet) or from home, you should not experience any problem.

<!--
##### Wirelessly using eduroam

To connect to `mLab` using `eduroam`, if you are working from your laptop on campus, you will find that most of the ports are blocked. 

To circumvent this problem, create a SSH tunnel to `xanthus.mcscw3.le.ac.uk` which is a wired machine that will work as a local proxy to the Internet using the following command from a Bash terminal:

	ssh -fNg -L $PORT:mlab.com:$PORT $UOL_USERNAME@xanthus.mcscw3.le.ac.uk

Replace `$PORT` with the port used for your database connection and replace `$UOL_USERNAME` with your University username. The password that you have to enter is your departmental Linux password. Once the tunnel is established there is no confirmation message but the connection should work. 
-->


### Getting started with MongoDb

* Download the project `week14_project` from Blackboard.
* Import the Gradle project into the Eclipse using `File>Import>Existing Gradle Project`
  * Choose gradle wrapper
  * Choose to overwrite eclipse settings 
* The Gradle build script imports two dependencies: one for compiling groovy code and the other one for using the MongoDB Java driver.


## File MongoDb

**Demo_MongoDB.groovy** (in src/main/groovy/exercises/Demo_MongodDb.groovy) contains example code showing how to implement CRUD operations using a MongoDB store.

The header is as follows:

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
	def mongoClient = MongoClients.create("mongodb+srv://${properties.USN}:${properties.PWD}@cluster0.f2bdm.mongodb.net/${properties.DB}?retryWrites=true&w=majority")
	
	// GET DATABASE
	def db = mongoClient.getDatabase(properties.DB)


Please configure your credentials in file `src/main/resources/mongodb.properties` by using the credentials of the user registered to your dabatase on MongoDB ATLAS, as explained above.

The following code checks that the connection can be performed and you should be able to see the names of all the collections on your database: 

	// TESTING CONNECTION
	println 'database: ' + db.getName()
	db.listCollectionNames().each{ println it } 

Check the code in the script for:
* creating documents in a database ;
* making queries;
* applying modifications to some documents, the first parameter of the the `update` operation is a filter for finding the document where to apply modifications and the second parameter indicates the type of update to be performed. ;
* making upserts;
* deleting one or many documents, using a filter.

## Exercises 

Solve the following exercises in the file `exercises/Exercises_1_2_3` in the source folder `src/main/groovy`.

### Exercise 1

Using Groovy collections (i.e. using lists and maps), create the following list of documents, where each document is represented as a map and may contain other lists of documents inside.

	[
	      {
	         "id": "X999_Y999",
	         "from": {
	            "name": "Tom Brady", "id": "X12", "age": 32
	         },
	         "message": "Looking forward to 2010!",
	         "actions": [
	            {
	               "name": "Comment",
	               "link": "http://www.facebook.com/X999/posts/Y999"
	            },
	            {
	               "name": "Like",
	               "link": "http://www.facebook.com/X999/posts/Y999"
	            }
	         ],
	         "type": "status",
	         "created_time": "2010-08-02T21:27:44+0000",
	         "updated_time": "2010-08-02T21:27:44+0000"
	      },
	      {
	         "id": "X998_Y998",
	         "from": {
	            "name": "Peyton Manning", "id": "X18", "age": 17
	         },
	         "message": "Where's my contract?",
	         "actions": [
	            {
	               "name": "Comment",
	               "link": "http://www.facebook.com/X998/posts/Y998"
	            },
	            {
	               "name": "Like",
	               "link": "http://www.facebook.com/X998/posts/Y998"
	            }
	         ],
	         "type": "status",
	         "created_time": "2010-08-02T21:27:44+0000",
	         "updated_time": "2010-08-02T21:27:44+0000"
	      }
	]

This is an example of a Facebook JSON file which you might see 
when getting data from the Facebook API. It might also be used to 
contain profile information which can be easily shared across 
your system components using the simple JSON format.
(source: [https://www.sitepoint.com/facebook-json-example/](https://www.sitepoint.com/facebook-json-example/))

Then create a collection 'mongodb-facebook' in a database in your cluster on MongoDB. Use [JSONSlurper](https://groovy-lang.org/json.html) to create JSON format from objects in memory.

### Exercise 2 

Update message field of document with id "X999_Y999" to "Remembering what happened in 2010".

### Exercise 3

Delete the document with id: "id": "X998_Y998"


## Queries in MongoDB

The driver provides several classes that make it easier to use the CRUD API. For example, [query filters](https://mongodb.github.io/mongo-java-driver/3.8/builders/filters/).

<!--
* [Projections](https://mongodb.github.io/mongo-java-driver/latest/builders/projections/): Documentation of the driver’s support for building projections
* [Sorts](https://mongodb.github.io/mongo-java-driver/latest/builders/sorts/): Documentation of the driver’s support for building sort criteria
* [Aggregation](https://mongodb.github.io/mongo-java-driver/latest/builders/aggregation/): Documentation of the driver’s support for building aggregation pipelines
-->

### Exercise 4

Given the JSON file in `src/main/resources/facebook.json`, upload the contents to your MongoDB instance and then obtain those posts that were written by `Tom Brady`.

### Exercise 5

Given the JSON file in `src/main/resources/facebook.json`, upload the contents to your MongoDB instance and then obtains those posts that were written by someone who is not 18 years old yet.


### Exercise 6

Define a closure that, given the path to a JSON file of movies, does the following:
1. Loads the the list of movies from the file using `JSONSlurper` and uploads it to the collection `movies-collection`.
2. Then creates a new movie in your collection with the following fields:

```
title: "David Attenborough: A Life on Our Planet",
year: 2020,
genres: [ "Dobumentary", "Biography" ],
imdbRating: 9,
actors: [ "David Attenborough", "Max Hughes"]
```

3. Then uses the MongoDB Java driver to obtain those movies with an `imdbRating` greater than or equal to `8`.

The following steps can be done in Groovy using collection operations:

4. Then filters those movies that are biographies.
5. Then sort the list by year in **ascending order**, and then by title. 
6. Then, for each movie in the list, it collects the title `YEAR`, `GENRES` and `TITLE` of the movie in the form `YEAR: TITLE (GENRES)`.
7. Then, returns the resulting list of strings.

When using the file `src/main/resources/top-rated-movies.json`, the result printed on the output console should be 
```
1962: Lawrence av Arabien ([Adventure, Biography, Drama])
1980: Tjuren från Bronx ([Biography, Drama, Sport])
1984: Amadeus ([Biography, Drama, History])
1990: Maffiabröder ([Biography, Crime, Drama])
1993: Schindler's List ([Biography, Drama, History])
1995: Braveheart ([Biography, Drama, History])
2002: The Pianist ([Biography, Drama, War])
2004: Undergången - Hitler och Tredje rikets fall ([Biography, Drama, History])
2011: En oväntad vänskap ([Biography, Comedy, Drama])
2016: Dangal ([Action, Biography, Drama])
2020: David Attenborough: A Life on Our Planet ([Dobumentary, Biography])
```


## Sources

* Official API documentation [MongoDb API](https://mongodb.github.io/mongo-java-driver/)
* [Parsing and producing JSON](https://groovy-lang.org/json.html)

***
&copy; Artur Boronat, 2015-21
