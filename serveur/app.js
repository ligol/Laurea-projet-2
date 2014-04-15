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
	map.set(response.id, socket);
	if (follow.get(response.id) != null) {
	    var client = follow.get(response.id);
	    for (var i = 0; i < client.length; i++) {
		console.log('follow : ' + client);
		client[i].emit('connected', '{ \'user\':' + response.id + ', \'state\':' + true + "}");
	    };
	}
    });
    socket.on('message', function(message){
	var response = JSON.parse(message);
	map.get(response.key).emit('message', message);
    });
    socket.on('userFollow', function(message){
	console.log('data : ' + message);
	var response = JSON.parse(message);
	for (var i = 0; i < response.id.length; i++) {
	    if (follow.get(response.id[i]) == null) {
		var array = [];
		array.push(socket);
		follow.set(response.id[i], array);
	    }
	    else
	    {
		follow.get(response.id[i]).push(socket);
		socket.emit('connected', '{ \'user\':' + response.id[i] + ', \'state\':' + true + "}");
	    }
	}
    });
    socket.on('disconnected', function(message) {
	console.log('Disconnected from the server');
	follow.forEach(function(value, key) {
	    console.log("value : "+ value);
	    console.log("socket : "+ socket);
	    if (value === socket){
		console.log(key);
	    	follow.remove(key);
	    }
	});
console.log("disconnected : " + message);
	var response = JSON.parse(message);
	if (follow.get(response.id) != null) {
	    var client = follow.get(response.id);
	    for (var i = 0; i < client.length; i++) {
		client[i].emit('disconnected', '{ \'user\':' + response.id + ', \'state\':' + true + "}");
	    };
	}
    });
});

server.listen(8080);

console.log('Listen on port 8080');
