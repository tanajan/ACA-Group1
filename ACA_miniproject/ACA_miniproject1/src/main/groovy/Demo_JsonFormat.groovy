package exercises

import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def json = '''
[
      {
         "id": "X999_Y999",
         "from": {
            "name": "Tom Brady", "id": "X12"
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
            "name": "Peyton Manning", "id": "X18"
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
'''

// we parse some text in JSON format
def parser = new JsonSlurper() 
def doc = parser.parseText(json)
println ""

// we can also parse text that is stored in an external file
// this overrides the contents in the doc vble
def file = new File('src/main/resources/facebook.json')
doc = parser.parse(file)

// here we can work with the document that has been parsed
int i = 0
doc.each{
	println ""
	println "object ${i++}: $it"
}

// now we serialize an object in memory (a document) to JSON format
def outputJson = JsonOutput.toJson(doc)
println ''
println outputJson
