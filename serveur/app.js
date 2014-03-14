
var http = require('http');
var fs = require('fs');

var server = http.createServer(function(req, res){
	    fs.readFile('./index.html', 'utf-8', function(error, content) {
        res.writeHead(200, {"Content-Type": "text/html"});
        res.end(content);
    });
});

var io = require('socket.io').listen(server);

io.sockets.on('connection', function(socker){
    socket.emit('recept', 'data received');
    socket.on('userInfo', function(message){
	console.log('data : ' + message);
    });
});

server.listen(8080);

console.log('Listen on port 8080');