var http = require('http');
var fs = require('fs');
var HashMap = require('hashmap').HashMap;

var server = http.createServer(function(req, res){
    fs.readFile('./index.html', 'utf-8', function(error, content) {
        res.writeHead(200, {"Content-Type": "text/html"});
        res.end(content);
    });
});

map = new HashMap();
follow = new HashMap();

var io = require('socket.io').listen(server);

io.sockets.on('connection', function(socket){
    socket.emit('recept', 'data received');
    socket.on('userInfo', function(message){
	console.log('data : ' + message);
	var response = JSON.parse(message);
	for (var i = 0; i < response.public.length; i++){
	    map.set(response.public[i], socket);
	    if (follow.get(response.public[i]) != null) {
		var client = follow.get(response.public[i]);
		console.log('follow : ' + client);
		for (var i = 0; i < client.length; i++) {
		    client[i].emit('connected', '{ \'user\':' + response.public[i] + ', \'state\':' + true);
		};
	    }
	}
    });
    socket.on('message', function(message){
	var response = JSON.parse(message);
	map.get(response.key).emit('message', message);
    });
    socket.on('isConnected', function(message){
	var response = JSON.parse(message);
	if (map.get(response.key) == null)
	    socket.emit('isConnected', false);
	else
	    socket.emit('isConnected', true);
    });
    socket.on('userFollow', function(message){
	console.log('data : ' + message);
	var response = JSON.parse(message);
	for (var i = 0; i < response.public.length; i++) {
	    if (follow.get(response.public[i]) == null) {
		var array = [];
		array.push(socket);
		follow.set(response.public[i], array);
	    }
	    else
	    {
		follow.get(response.public[i]).push(socket);
		socket.emit('connected', '{ \'user\':' + response.public[i] + ', \'state\':' + true);
	    }
	}
    });
    socket.on('disconnect', function(message) {
	console.log('Disconnected from the server');
	follow.forEach(function(value, key) {
	    console.log("value : "+ value);
	    console.log("socket : "+ socket);
	    if (value === socket){
		console.log(key);
	    	follow.remove(key);
	    }
	});
	var response = JSON.parse(message);
	for (var i = 0; i < response.public.length; i++){
	    if (follow.get(response.public[i]) != null) {
		var client = follow.get(response.public[i]);
		for (var i = 0; i < client.length; i++) {
		    client[i].emit('disconnected', '{ \'user\':' + response.public[i] + ', \'state\':' + true);
		};
	    }
	}
    });
});

server.listen(8080);

console.log('Listen on port 8080');
