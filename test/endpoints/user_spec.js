var frisby = require('frisby');
var testUser = {"id": -1, "firstName": "TestFirstName", "lastName": "TestSecondName", "age": 20, "active": true};

frisby.globalSetup({
  timeout: 10000,
  retry: 2,
});

console.log('Starting Frisby tests for UserController');

var URL = 'http://localhost:9000';
frisby.create('GET index')
    .get(URL)
    .expectStatus(200)
    .expectHeaderContains('content-type', 'text/html')
    .toss();


frisby.create('GET all users')
    .get(URL + '/users')
    .expectStatus(200)
    .expectHeaderContains('content-type', 'application/json')
    .expectJSONTypes('*', { // '*' indicates to check for all elements of the list
      id: Number,
      firstName: String,
      lastName: String,
      age: Number,
      active: Boolean,
      createdDate: String,
      updatedDate: String
    })
    .toss();

frisby.create('create a new user')
    .post(URL + '/user', testUser, {json: true}, {headers: {'Content-Type': 'application/json'}})
    .expectStatus(200)
    .toss();

frisby.create('find a user by id')
    .get(URL + '/users/-1')
    .expectStatus(200)
    .expectHeaderContains('content-type', 'application/json')
    .expectBodyContains('{"id":-1,"firstName":"TestFirstName","lastName":"TestSecondName","age":20,"active":true,"createdDate":')
    .after(function (json) {
      frisby.create('update a user by id')
          .put(URL + '/users/-1',
          {"id": -1, "firstName": "NewTestFirstName", "lastName": "NewTestSecondName", "age": 230, "active": false},
          {json: true},
          {headers: {'Content-Type': 'application/json'}})
          .expectStatus(200)
          .toss()
    })
    .toss();


// Now you can use 'json' in additional requests
frisby.create('find the updated user')
    .get(URL + '/users/-1')
    .expectStatus(200)
    .after(function (json) {
      frisby.create('delete a user by id')
          .delete(URL + '/users/-1')
          .expectStatus(200)
          .after(function (json) {

          })
          .toss();
    })
    .toss()
