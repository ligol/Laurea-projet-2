date
forever stop app.js

forever -a -l server.log -o server.log -e server.log  start app.js
