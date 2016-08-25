var frisby = require('frisby');

frisby.globalSetup({
  timeout: 10000,
  retry: 2
});

console.log('Starting Frisby tests for Application');

var URL = 'http://localhost:9000/health';
frisby.create('health check')
    .get(URL)
    .expectStatus(200)
    .toss();
