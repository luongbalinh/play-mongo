/**
 * This checks whether Play application is started or not before running the Frisby tests.
 * This check is important when integrating with Travis.
 */

var http = require('http');
var counter = 0;

var waitForPlay = function () {
  console.log(++counter, 'waiting for Play');
  http.get("http://127.0.0.1:9000/health", function (res) {
    console.log('Play is ready', res.statusCode);
    process.exit(0);
  }).on("error", function (e) {
    setTimeout(waitForPlay, 1000);
  }).end();
}
waitForPlay();