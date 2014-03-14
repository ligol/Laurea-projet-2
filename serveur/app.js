
var http = require('http');
var fs = require('fs');

var server = http.createServer(function(req, res){
});

var io = require('socket.io').listen(server);

server.listen(8080);