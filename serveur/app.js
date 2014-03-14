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

var io = require('socket.io').listen(server);

io.sockets.on('connection', function(socket){
    socket.emit('recept', 'data received');
    socket.on('userInfo', function(message){
	console.log('data : ' + message);
	var response = JSON.parse(message);
	for (var i = 0; i < response.public.length; i++){
	    map.set(response.public[i], socket);
	}
	socket.on('message', function(message){
	    var response = JSON.parse(message);
	    map.get(response.key).emit('message', message);
	})
	socket.on('isConnected', function(message){
	    var response = JSON.parse(message);
	    if (map.get(response.key) == null)
	    	socket.emit('isConnected', false);
	    else
	    	socket.emit('isConnected', true);
	})

    });
});

server.listen(8080);

console.log('Listen on port 8080');