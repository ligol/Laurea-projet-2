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
messages = new HashMap();

var io = require('socket.io').listen(server);

io.sockets.on('connection', function(socket){
    socket.emit('recept', 'data received');
    socket.on('userInfo', function(message) {
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
		if (messages.get(response.id) != null)
		{
			var listmessage = messages.get(response.id)
			console.log("Saved : "+ listmessage);
			for (var i = 0; i < listmessage.length; i++) {
				map.get(response.id).emit('message', listmessage[i]);
				messages.remove(response.id);
			}
		}

    });

    socket.on('message', function(message){
		console.log('message : ' + message);
		var response = JSON.parse(message);
		if (map.get(response.id) != null)
		{
			map.get(response.id).emit('message', message);
		}
		else
		{
		    console.log("ADD MESSAGE");
		    if (messages.get(response.id) == null)
		    {
			var array = [];
			array.push(message);
			messages.set(response.id, array);
	    	    }
	    	    else
	    	    {
			messages.get(response.id).push(message);
	    	    }
		}
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
	    }
	    if (map.get(response.id[i]) != null)
		socket.emit('connected', '{ \'user\':' + response.id[i] + ', \'state\':' + true + "}");
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
	map.remove(response.id)
	if (follow.get(response.id) != null) {
	    var client = follow.get(response.id);
	    for (var i = 0; i < client.length; i++) {
		client[i].emit('disconnected', '{ \'user\':' + response.id + ', \'state\':' + false + "}");
	    };
	}
    });
});

server.listen(8080);

console.log('Listen on port 8080');
